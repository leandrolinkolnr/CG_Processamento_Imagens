package processarImagem;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import modelo.Transformacao;



/**
* Esta classe é para definir imagens trianguladas e para interpolação
* entre duas imagens trianguladas com triangulações correspondentes.
*  
* Créditos:
* @author Frank Klawonn
* Last change 31.05.2005
* 
* @see Transformacao
*/
public class TriangulatedImage
{

  // a imagem inicial
  public BufferedImage bi;

  //Os pontos necessários para a triangulação
  public Point2D[] tPoints;


 //Os triângulos que definem a triangulação. Cada linha das matrizes contém
 // índices de três pontos na matriz tPoints. O i-ésimo triângulo d
 // triangulação é definida pelos três pontos no array tPoints com os índices
 // triângulos [i][0], triângulos [i][1], triângulos [i] [2].
  public int[][] triangles;



  /**
  * 
  * Um método que calcula para um ponto v e três pontos não colineares adicionais
  * uma representação de v como uma combinação linear dos três pontos. Os três coeficientes
  * para a representação de v são retornados.
  *
  * @param v aponta para ser representado.
  * @param triangle um array contém três pontos não-colineares.
  *
  * @revolve os três coeficientes para a representação de v.
  */
  public static double[] triangleCoordinates(Point2D v, Point2D[] triangle)
  {
    //O array para os três coeficientes.
  	double[] result = new double[3];

  	//Alguns valores que são necessários para resolver o sistema de equações lineares para
    // determinação dos coeficientes.
    double d13x = triangle[0].getX() - triangle[2].getX();
    double d23x = triangle[1].getX() - triangle[2].getX();
    double dx3 =  v.getX()           - triangle[2].getX();
    double d13y = triangle[0].getY() - triangle[2].getY();
    double d23y = triangle[1].getY() - triangle[2].getY();
    double dy3 =  v.getY()           - triangle[2].getY();

    double delta = d13x*d23y - d23x*d13y;

    // Quando os três pontos são (quase) colineares, a divisão por zero é evitada e
    // Coeficientes são retornados, que pelo menos não representam uma combinação convexa.
    // Caso contrário, os coeficientes não podem ser calculados.
    if (Math.abs(delta)<0.00000001)
    {
      result[0] = 10;
    }
    else
    {
      result[0] = (dx3*d23y - d23x*dy3)/delta;
      result[1] = (d13x*dy3 - dx3*d13y)/delta;
    }


    result[2] = 1 - result[0] - result[1];

    return(result);
  }



  /**
  * Teste, se uma determinada matriz de coeficientes representa uma combinação convexa,
  * isto é, se todos os elementos da matriz estão entre 0 e 1
  * e se somam 1.
  *
  * @param t array com os coeficientes.
  *
  * @retorne true se e somente se os coeficientes representam uma combinação convexa.
  */
  public static boolean isConvexCombination(double[] t)
  {
    boolean result;
    double sum;
    // De acordo com os erros de arredondamento, os coeficientes quase nunca somam exatamente 1.
    // O valor a seguir especifica quanto de desvio do valor exato 1 ainda é considerado
    // como uma combinação convexa.
    double tolerance = 0.000001;

    result = true;
    sum = 0;
    for (int i=0; i<t.length; i++)
    {
      if (t[i]<0 || t[i]>1) {result = false;}
      sum = sum + t[i];
    }
    
    if (Math.abs(sum-1)>tolerance) {result = false;}
    
    return(result);
  }

  
  /**
  * Gera e interpola imagem de duas imagens trianguladas (this e ti).
  * ti contribuiu para a imagem interpolada por alfa * 100%. As duas imagens devem ter o
  * mesmo tamanho, o mesmo número de pontos para a triangulação e o número de triângulos.
  * Além disso, os triângulos para ambas as imagens devem consistir dos pontos com o mesmo
  * índices. Apenas a posição dos pontos precisa coincidir para as duas imagens.
  *
  * @param ti segunda imagem triangulada.
  * @param proporção alfa que ti deve contribuir para a imagem interpolada.
  *
  * @revolve a imagem interpolada.
  */
  public BufferedImage mixWith(TriangulatedImage ti, double alpha)
  {
    TriangulatedImage mix = new TriangulatedImage();

    // Aqui a imagem interpolada será armazenada.
    mix.bi = new BufferedImage(this.bi.getWidth(),this.bi.getHeight(),
                                            BufferedImage.TYPE_INT_RGB);

    

    // Defina os pontos para os pontos de triangulação na imagem interpolada como
    // combinações convexas dos pontos correspondentes nas imagens a serem
    // interpoladas.
    mix.tPoints = new Point2D[this.tPoints.length];
    for (int i=0; i<mix.tPoints.length; i++)
    {
      mix.tPoints[i] = new Point2D.Double((1-alpha)*this.tPoints[i].getX()
                                          + alpha*ti.tPoints[i].getX(),
                                          (1-alpha)*this.tPoints[i].getY()
                                          + alpha*ti.tPoints[i].getY());
    }

    // Os triângulos para a triangulação devem ser definidos usando os mesmos pontos (índices)
    // como nas imagens originais.
    mix.triangles = this.triangles;



    int rgbValueThis;
    int rgbValueTi;
    int rgbValueMix;
    Color thisColour;
    Color tiColour;
    Color pixelColour;
    int rMix;
    int gMix;
    int bMix;
    int xInt;
    int yInt;
    double[] t = new double[3];
    double aux;
    Point2D[] threePoints = new Point2D[3];;
    Point2D.Double pixel = new Point2D.Double();
    int tNo;
    boolean notFound;

    
    // Determine para cada pixel na imagem interpolada a cor como uma
    // combinação convexa dos valores RGB dos pixels correspondentes nas
    // duas imagens a serem interpoladas.
    for (int i=0; i<mix.bi.getWidth(); i++)
    {
      for (int j=0; j<mix.bi.getHeight(); j++)
      {
    	// Para este pixel, a cor deve ser determinada.
        pixel.setLocation(i,j);

        // tNo é usado para calcular o índice do triângulo em que o
        // considera pixel lies.
        tNo = 0;

        // Quando o triângulo for encontrado, notFound muda para false.
        notFound = true;
        while(tNo<mix.triangles.length && notFound)
        {
        	
          // Determine os três pontos do triângulo no. tNo.
          for(int k=0; k<3; k++)
          {
            threePoints[k] = mix.tPoints[mix.triangles[tNo][k]];
          }

          // Determine as coordenadas dos pixels w.r.t. para o triângulo no. tNo.
          t = triangleCoordinates(pixel,threePoints);

          // Verifique se o pixel está dentro do triângulo.
          if (isConvexCombination(t))
          {
            notFound = false;
          }
          else
          {
            tNo++;
          }
        }// endWhile
        // O loop while termina quando um índice tNo de um triângulo que contém o
        // pixel foi encontrado ou quando nenhum triângulo contendo o pixel foi encontrado.
        
        // Se um triângulo contendo o pixel for encontrado, a cor do pixel
        // pode ser calculado.
        if (!notFound)
        {
          // Determine a coordenada x do pixel na primeira imagem (this) como
          // uma combinação convexa dos vértices do triângulo correspondente.
          aux = 0;
          for (int k=0; k<3; k++)
          {
            aux = aux + t[k]*this.tPoints[this.triangles[tNo][k]].getX();
          }
          xInt = (int) Math.round(aux);
          
          // Temos que ter certeza de que os erros de arredondamento não levarão a um pixel
          // fora da imagem.
          xInt = Math.max(0,xInt);
          xInt = Math.min(this.bi.getWidth()-1,xInt);


          // O mesmo para a coordenada y.
          aux = 0;
          for (int k=0; k<3; k++)
          {
            aux = aux + t[k]*this.tPoints[this.triangles[tNo][k]].getY();
          }
          yInt = (int) Math.round(aux);
          yInt = Math.max(0,yInt);
          yInt = Math.min(this.bi.getHeight()-1,yInt);


          // Determine a cor do pixel na primeira imagem.
          rgbValueThis = this.bi.getRGB(xInt,yInt);
          thisColour = new Color(rgbValueThis);



          // Faça o mesmo que acima para a segunda imagem ti.
          aux = 0;
          for (int k=0; k<3; k++)
          {
            aux = aux + t[k]*ti.tPoints[ti.triangles[tNo][k]].getX();
          }
          xInt = (int) Math.round(aux);

          aux = 0;
          for (int k=0; k<3; k++)
          {
            aux = aux + t[k]*ti.tPoints[ti.triangles[tNo][k]].getY();
          }
          yInt = (int) Math.round(aux);

          rgbValueTi = ti.bi.getRGB(xInt,yInt);
          tiColour = new Color(rgbValueTi);



          // Agora que temos as duas cores, podemos calcular suas
          // Combimação convexa para os valores R, G e B.
          // Problemas causados ​​por erros de arredondamento são bastante improváveis ​​aqui.
          rMix = (int) Math.round((1-alpha)*thisColour.getRed()+
                                        alpha*tiColour.getRed());

          gMix = (int) Math.round((1-alpha)*thisColour.getGreen()+
                                        alpha*tiColour.getGreen());

          bMix = (int) Math.round((1-alpha)*thisColour.getBlue()+
                                        alpha*tiColour.getBlue());


          // Gere a cor interpolada como uma combinação convexa.
          pixelColour = new Color(rMix, gMix, bMix);

          // Use a cor interpolada para desenhar o pixel na imagem interpolada
          mix.bi.setRGB(i,j,pixelColour.getRGB());

        }//endif (!naoEncontrado)

      }//endfor j
    }//endfor i

    return(mix.bi);
  }

}

