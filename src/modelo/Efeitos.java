package modelo;

import java.awt.image.BufferedImage;
import java.util.Arrays;

import processarImagem.PanelDaImagem;

public class Efeitos extends PanelDaImagem {

	public void transformacao() throws Exception {
		Transformacao teste = new Transformacao();
	}
	


	/**
	 * Aplica o filtro da média em matrizImagem. É feito a soma de cada um dos 9 pixels de onde a mascara
	 * esta posicionada. Logo após, é feito a divisão dessa soma pelo fator de normalização 9, pois cada 
	 * pixel da mascara é 1. Por fim, o pixel central é subtituido pelo resultado da divisao.
	 */
	// Método que aplica o filtro média na imagem
	public void media(int largura, int altura, int[][] matrizImagem) {// recebe como paramêtro a matriz da
																		// imagem para realizar
		int[][] matriz_auxiliar = new int[largura + 1][altura + 1];
		int[][] matrizResultado = new int[largura][altura];

		// copia a imagem recebida em parametro para a matriz da imagem auxiliar, para
		// poder realizar a operação
		for (int i = 1; i < matriz_auxiliar.length; i++) {
			for (int j = 1; j < matriz_auxiliar.length; j++) {
				matriz_auxiliar[j][i] = matrizImagem[j - 1][i - 1];
			}
		}

		// realizando efeito média
		for (int y = 1; y < largura; y++) {
			for (int x = 1; x < altura; x++) {

				// soma todos os 9 pixel correspondente de onde a mascara estar
				float media = matriz_auxiliar[y - 1][x - 1] + matriz_auxiliar[y - 1][x] + matriz_auxiliar[y - 1][x + 1]
						+ matriz_auxiliar[y][x + 1] + matriz_auxiliar[y][x] + matriz_auxiliar[y + 1][x + 1]
						+ matriz_auxiliar[y + 1][x] + matriz_auxiliar[y][x - 1] + matriz_auxiliar[y + 1][x - 1];

				// pega o resultado da soma e faz a média dividindo por 9
				media = media / 9;

				// coloca resultado na imagem de saída
				matrizResultado[y - 1][x - 1] = Math.round(media);
			}
		}

		exibir(matrizResultado);
	}

	/**
	 * Aplica o filtro da mediana em matrizImagem. Primeiramente, todos o pixel e sua vizinhança são 
	 * postos em um array. Logo em seguida são ordenado de forma crescente. O pixel localizado no meio
	 * é o valor que será atribuído ao pixel central.
	 */
	public void mediana(int largura, int altura, int[][] matrizImagem) {
		int matriz_imagem_auxiliar[][] = new int[largura + 1][altura + 1];
		int matrizResultado[][] = new int[largura][altura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matriz_imagem_auxiliar.length; i++) {
			for (int j = 1; j < matriz_imagem_auxiliar.length; j++) {

				matriz_imagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		for (int y = 1; y < altura; y++) {
			for (int x = 1; x < largura; x++) {

				// coloca em um array todos os valores dos pixel da mascara
				int[] mascara = { matriz_imagem_auxiliar[y - 1][x - 1], matriz_imagem_auxiliar[y - 1][x],
						matriz_imagem_auxiliar[y - 1][x + 1], matriz_imagem_auxiliar[y][x + 1],
						matriz_imagem_auxiliar[y][x], matriz_imagem_auxiliar[y + 1][x + 1],
						matriz_imagem_auxiliar[y + 1][x], matriz_imagem_auxiliar[y][x - 1],
						matriz_imagem_auxiliar[y + 1][x - 1] };
				// ordena
				Arrays.sort(mascara);
				// atribui a variavel mediana a posição do meio do array, obtendo assim a
				// mediana(4 pq o primeiro indice eh 0)
				int mediana = mascara[4];

				// Atribui a imagem de saída o resultado final
				matrizResultado[y - 1][x - 1] = mediana;
			}
		}

		exibir(matrizResultado);
	}

	/**
	 * Realce de Imagens:
	 * Aplica o filtro Passa Alta básico em matrizImagem. É considerada uma máscara com o elemento central 8,
	 * e todo restante -1. A soma de todos os pixels dessa máscara é 0 (em regiões homogêneas o resultado 
	 * é 0 ou próximo de 0). É feita a soma de toda a multiplicação dos pixels da imagem sob a mascara com
	 * os pixels da máscara. O valor do pixel central é substituído pelo valor da soma.
	 */
	public void passa_alta(int largura, int altura, int[][] matrizImagem) {
		altura = altura + 2;
		largura = largura + 2;
		int[][] matriz_Resultado = new int[altura][largura];
		double w = 8;

		BufferedImage imagem_auxiliar = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);
		int matriz_auxiliar[][] = new int[largura][altura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < imagem_auxiliar.getHeight() - 1; i++) {
			for (int j = 1; j < imagem_auxiliar.getWidth() - 1; j++) {
				
				matriz_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}


		for (int y = 1; y < imagem_auxiliar.getHeight() - 1; y++) {
			for (int x = 1; x < imagem_auxiliar.getWidth() - 1; x++) {

				double passa_alta = -matriz_auxiliar[y - 1][x - 1] - matriz_auxiliar[y - 1][x]
						- matriz_auxiliar[y - 1][x + 1] - matriz_auxiliar[y][x + 1] + (w * matriz_auxiliar[y][x])
						- matriz_auxiliar[y + 1][x + 1] - matriz_auxiliar[y + 1][x] - matriz_auxiliar[y][x - 1]
						- matriz_auxiliar[y + 1][x - 1];
				int passa_alta_aux = (int) Math.round(passa_alta);

				matriz_Resultado[y - 1][x - 1] = matrizImagem[y - 1][x - 1] - passa_alta_aux;

			}
		}

		exibir(matriz_Resultado);
	}

	/**
	 * Aplica o alto reforço em matrizImagem. Nesta implementação é aplicada a técnica a partir do valor
	 * de w. Após encontrar o valor de w, aplica o processo de passa alta. Se w=8, caracteriza-se como o
	 * passa alta comum.
	 */
	public void alto_reforco(int largura, int altura, int[][] matrizImagem, double a) {
		largura = largura + 2;
		altura = altura + 2;
		int[][] matriz_Resultado = new int[altura][largura];
		int matriz_auxiliar[][] = new int[largura][altura];
		double w = (9 * a) - 1;

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < altura - 1; i++) {
			for (int j = 1; j < largura - 1; j++) {
				matriz_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		for (int y = 1; y < altura - 1; y++) {
			for (int x = 1; x < largura - 1; x++) {

				double alto_reforco = -matriz_auxiliar[y - 1][x - 1] - matriz_auxiliar[y - 1][x]
						- matriz_auxiliar[y - 1][x + 1] - matriz_auxiliar[y][x + 1] + (w * matriz_auxiliar[y][x])
						- matriz_auxiliar[y + 1][x + 1] - matriz_auxiliar[y + 1][x] - matriz_auxiliar[y][x - 1]
						- matriz_auxiliar[y + 1][x - 1];
				int alto_reforco_aux = (int) Math.round(alto_reforco);
				matriz_Resultado[y - 1][x - 1] = alto_reforco_aux;
			}
		}

		exibir(matriz_Resultado);
	}

	/**
	 * Aplica o filtro de Prewitt em matrizImagem. Primeiramente é obtido o valor do pixel central resultante
	 * da aplicação da máscara em x (primeira linha horizontal de -1, segunda linha de 0 e terceira linha de
	 * 1). Depois é obtido o valor do pixel central resultante da aplicação da máscara em y (primeira linha
	 * vertical de -1, segunda linha de 0 e terceira linha de 1). Todos esses resultados em módulo. Logo
	 * após é feito a soma do resultado em x com o resultado em y.
	 * 
	 * @param resultado = |(Z7+Z8+Z9) - (Z1+Z2+Z3)| + |(Z3+Z6+Z9) - (Z1+Z4+Z7)| 
	 */
	public void prewitt(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura][largura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		
		int resultadoX, resultadoY, resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				//aplicando mascara em x
				resultadoX = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-1) * matrizImagem_auxiliar[x-1][y]
						+ (-1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				//aplicando mascara em y
				resultadoY = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-1) * matrizImagem_auxiliar[x][y-1]
						+ (-1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				//obtendo a magnitude final
				resultado = Math.abs(resultadoX) + Math.abs(resultadoY);
				matrizImagem_auxiliar[x - 1][y - 1] = resultado;
			}
		}
		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica o filtro de Prewitt em X em matrizImagem. É obtido o valor do pixel central em modulo
	 * resultante da aplicação da máscara em x (primeira linha horizontal de -1, segunda linha de 0 
	 * e terceira linha de 1).
	 * 
	 * @param resultado = |(Z7+Z8+Z9) - (Z1+Z2+Z3)|
	 */
	public void prewitt_em_x(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura][largura];
		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		
		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				// aplicacao da mascara em x
				resultado = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-1) * matrizImagem_auxiliar[x-1][y]
						+ (-1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica o filtro de Prewitt em matrizImagem. É obtido o valor do pixel central em módulo 
	 * resultante da aplicação da máscara em y (primeira linha vertical de -1, segunda linha de 0
	 * e terceira linha de 1). 
	 * 
	 * @param resultado = |(Z3+Z6+Z9) - (Z1+Z4+Z7)| 
	 */
	public void prewitt_em_y(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura][largura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		
		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				// aplicando a mascara em y
				resultado = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-1) * matrizImagem_auxiliar[x][y-1]
						+ (-1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica o filtro de Sobel em X na matrizImagem. É obtido o valor do pixel central resultante em
	 * módulo da aplicação da máscara em x (primeira linha horizontal -1-2-1, segunda linha de 0 e 
	 * terceira linha de 121).
	 * 
	 * @param resultado = |(Z7+2Z8+Z9) - (Z1+2Z2+Z3)|
	 */
	public void sobelEmX(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura][largura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		
		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				// aplicando mascara em x
				resultado = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-1) * matrizImagem_auxiliar[x-1][y]
						+ (-1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica o filtro de Sobel em matrizImagem. É obtido o valor do pixel central em módulo resultante
	 * da aplicação da máscara em y (primeira linha vertical de -1-2-1, segunda linha de 0 e terceira 
	 * linha de 121). 
	 * 
	 * @param resultado = |(Z3+2Z6+Z9) - (Z1+2Z4+Z7)| 
	 */
	public void sobelEmY(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura][largura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				//aplicando mascara em y
				resultado = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-1) * matrizImagem_auxiliar[x][y-1]
						+ (-1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);

			}
		}

		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica o filtro de Sobel em matrizImagem. Primeiramente é obtido o valor do pixel central resultante
	 * da aplicação da máscara em x (primeira linha horizontal -1-2-1, segunda linha de 0 e terceira linha
	 * de 121). Depois é obtido o valor do pixel central resultante da aplicação da máscara em y (primeira linha
	 * vertical de -1-2-1, segunda linha de 0 e terceira linha de 121). Todos esses resultados em módulo. 
	 * Logo após é feito a soma do resultado em x com o resultado em y.
	 * 
	 * @param resultado = |(Z7+2Z8+Z9) - (Z1+2Z2+Z3)| + |(Z3+2Z6+Z9) - (Z1+2Z4+Z7)| 
	 */
	public void sobel(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura][largura];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		
		int resultadoX, resultadoY, resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				// aplicando mascara em x
				resultadoX = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-2) * matrizImagem_auxiliar[x-1][y]
						+ (-1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (2) * matrizImagem_auxiliar[x + 1][y]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				// aplicando mascara em y
				resultadoY = (-1) * matrizImagem_auxiliar[x - 1][y - 1]
						+ (-2) * matrizImagem_auxiliar[x][y-1]
						+ (-1) * matrizImagem_auxiliar[x + 1][y - 1]
						+ (1) * matrizImagem_auxiliar[x - 1][y + 1]
						+ (2) * matrizImagem_auxiliar[x][y + 1]
						+ (1) * matrizImagem_auxiliar[x + 1][y + 1];
				// obtendo a magniitude final
				resultado = Math.abs(resultadoX) + Math.abs(resultadoY);
				matrizImagem_auxiliar[x - 1][y - 1] = resultado;
			}
		}

		exibir(matrizImagem_auxiliar);
	}
	
	/**
	 * Aplica o filtro de Roberts/Gradiente em matrizImagem. É obtido o valor do pixel central em módulo
	 * resultante da aplicação da máscara em x (primeira linha horizontal de 0, segunda linha 010 e 
	 * terceira linha 0-10).
	 * 
	 * @param resultado = |Z5-Z8|
	 */
	public void gradienteEmX(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura + 2][largura + 2];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}
		
		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				//aplicando mascara em x
				resultado = (1) * matrizImagem_auxiliar[x][y]
						+ (-1) * matrizImagem_auxiliar[x+1][y];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);

	}
	/**
	 * Aplica o filtro de Roberts/Gradiente em matrizImagem. É obtido o valor do pixel central em módulo
	 * resultante da aplicação da máscara em y (primeira linha horizontal de 0, segunda linha 01-1 e
	 * terceira linha de 0).
	 * 
	 * @param resultado = |Z5-Z6| 
	 */
	public void gradienteEmY(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura + 2][largura + 2];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {

				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				// aplicando mascara em y
				resultado = (1) * matrizImagem_auxiliar[x][y]
						+ (-1) * matrizImagem_auxiliar[x][y+1];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);
	}
	
	/**
	 * Aplica o filtro de Roberts/Gradiente em matrizImagem. Primeiramente é obtido o valor do pixel central
	 * resultante da aplicação da máscara em x (primeira linha horizontal de 0, segunda linha 010 e 
	 * terceira linha 0-10). Depois é obtido o valor do pixel central resultante da aplicação da máscara
	 * em y (primeira linha horizontal de 0, segunda linha 01-1 e terceira linha de 0). Todos esses 
	 * resultados em módulo. Logo após é feito a soma do resultado em x com o resultado em y.
	 * 
	 * @param resultado = |Z5-Z8| + |Z5-Z6| 
	 */
	public void gradiente(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura + 2][largura + 2];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		int resultadoX, resultadoY, resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				//aplicando mascara em x
				resultadoX = (1) * matrizImagem_auxiliar[x][y]
						+ (-1) * matrizImagem_auxiliar[x+1][y];
				// aplicando mascara em y
				resultadoY = (1) * matrizImagem_auxiliar[x][y]
						+ (-1) * matrizImagem_auxiliar[x][y+1];
				// obtendo magnitude final
				resultado = Math.abs(resultadoX) + Math.abs(resultadoY);
				matrizImagem_auxiliar[x - 1][y - 1] = resultado;
			}
		}

		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica o filtro de Roberts Cruzado/Gradiente Cruzado em matrizImagem. É obtido o valor do pixel
	 * central em módulo resultante da aplicação da máscara em x (primeira linha horizontal de 0, segunda
	 * linha 010 e terceira linha 00-1).
	 * 
	 * @param resultado = |Z5-Z9| 
	 */
	public void gradienteCruzadoEmX(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura + 2][largura + 2];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				//aplicando mascara em x
				resultado = (1) * matrizImagem_auxiliar[x][y]
						+ (-1) * matrizImagem_auxiliar[x+1][y+1];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);

	}
	
	/**
	 * Aplica o filtro de Roberts Cruzado/Gradiente Cruzado em matrizImagem. É obtido o valor do pixel 
	 * central em módulo resultante da aplicação da máscara em y (primeira linha horizontal de 0, segunda
	 * linha 001 e terceira linha 0-10).
	 * 
	 * @param resultado = |Z6-Z8| 
	 */
	public void gradienteCruzadoEmY(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura + 2][largura + 2];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		int resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {
				//aplicacao da mascara em y
				resultado = (1) * matrizImagem_auxiliar[x][y+1]
						+ (-1) * matrizImagem_auxiliar[x+1][y];
				
				matrizImagem_auxiliar[x - 1][y - 1] = Math.abs(resultado);
			}
		}

		exibir(matrizImagem_auxiliar);
	}
	
	/**
	 * Aplica o filtro de Roberts Cruzado/Gradiente Cruzado em matrizImagem. Primeiramente é obtido 
	 * o valor do pixel central resultante da aplicação da máscara em x (primeira linha horizontal de 0,
	 * segunda linha 010 e terceira linha 00-1). Depois é obtido o valor do pixel central resultante da
	 * aplicação da máscara em y (primeira linha horizontal de 0, segunda linha 001 e terceira linha 0-10).
	 * Todos esses resultados em módulo. Logo após é feito a soma do resultado em x com o resultado em y.
	 * 
	 * @param resultado = |Z5-Z9| + |Z6-Z8| 
	 */
	public void gradienteCruzado(int largura, int altura, int[][] matrizImagem) {
		int matrizImagem_auxiliar[][] = new int[altura + 2][largura + 2];

		// inclui em uma imagem auxilar mais uma linha de "zeros" em todas as laterais
		for (int i = 1; i < matrizImagem_auxiliar.length - 1; i++) {
			for (int j = 1; j < matrizImagem_auxiliar.length - 1; j++) {
				matrizImagem_auxiliar[i][j] = matrizImagem[i - 1][j - 1];
			}
		}

		int resultadoX, resultadoY, resultado;
		for (int x = 1; x < matrizImagem_auxiliar.length - 1; x++) {
			for (int y = 1; y < matrizImagem_auxiliar.length - 1; y++) {

				resultadoX = (1) * matrizImagem_auxiliar[x][y]
						+ (-1) * matrizImagem_auxiliar[x+1][y+1];
				
				resultadoY = (1) * matrizImagem_auxiliar[x][y+1]
						+ (-1) * matrizImagem_auxiliar[x+1][y];
				
				resultado = Math.abs(resultadoX) + Math.abs(resultadoY);
				matrizImagem_auxiliar[x - 1][y - 1] = resultado;
			}
		}

		exibir(matrizImagem_auxiliar);
	}

	/**
	 * Aplica a transformação Gamma em r_matrizImagem através da fórmula: S = cr^y.
	 * 
	 * @param c = 1
	 * @param r = nível de cinza
	 * @param 0 <= y <= 1
	 * 
	 */
	public void gamma(int largura, int altura, int[][] r_matrizImagem, double gamma) {
		// Criar uma imagem de Buffer para receber manipulações
		int[][] matrizImagemNegativa = new int[largura][altura];
		int c = 1;
		// percorre toda imagem pixel a pixel para realizar efeito (negativo)
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {

				// pega o nivel de cor de um pixel e aplica a formula
				matrizImagemNegativa[x][y] = (int) Math.round(c * Math.pow(r_matrizImagem[x][y], gamma));
			}
		}

		 exibir(matrizImagemNegativa);
	}

	/**
	 * Aplica a transformação Logaritmo através da fórmula: S = alog(r+1).
	 * 
	 * @param constante fator = 20
	 * @param r = nível de cinza
	 * 
	 */
	
	public void logaritmo(int largura, int altura, int[][] r_matrizImagem, double fator) {

		// Criar uma imagem de Buffer para receber manipulações
		int[][] matrizImagemNegativa = new int[largura][altura];

		// percorre toda imagem pixel a pixel para realizar a transformacao logaritmica
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {

				// pega o nivel de cor de um pixel
				double log = Math.round(fator * Math.log(r_matrizImagem[x][y] + 1));
				matrizImagemNegativa[x][y] = (int) log;
				
			}
		}

		exibir(matrizImagemNegativa);
	}

	/**
	 * Aplica a transferência de intensidade geral  através da função:
	 * S(r) = 255 * (1/(1+e^-((r-w)/sigma)))
	 * 
	 * @param sigma = largura da janela
	 * @param r = nível de cinza
	 * @param w = centro dos valores de cinza (255/2)
	 * 
	 */
	public void intensidade_geral(int largura, int altura, int[][] r_matrizImagem, int w, int sigma) {

		// Criar uma imagem de Buffer para receber manipulações
		int[][] matrizImagemNegativa = new int[largura][altura];
		
		// percorre toda imagem pixel a pixel
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {

				double s = Math.round(255 * (1 / (1 + Math.pow(Math.E, - ((r_matrizImagem[x][y] - w) / sigma)))));
				matrizImagemNegativa[x][y] = (int) s;
			}
		}

		exibir(matrizImagemNegativa);
	}

	/**
	 * Aplica a transferência de faixa dinâmica: f' = ((f - fmin) / (fmax - fmin))*w .
	 * 
	 * f = valor do pixel a ser transformado
	 * @param fmin = pixel menor valor
	 * @param fmax = pixel maior valor
	 * @param w = faixa dinâmica
	 * 
	 */
	public void faixa_dinamica(int largura, int altura, int[][] r_matrizImagem, int w) {
		// Criar uma imagem de Buffer para receber manipulações
		int[][] matrizImagemNegativa = new int[largura][altura];
		int fmin = fMin(r_matrizImagem, largura, altura);
		int fmax = fMax(r_matrizImagem, largura, altura);

		// percorre toda imagem pixel a pixel para realizar a transformacao
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {
				// pega o nivel de cor de um pixel
				double dividendo = r_matrizImagem[y][x]-fmin;
				double divisor = fmax- fmin;
				double divisao = dividendo / divisor;

				matrizImagemNegativa[y][x] = (int) Math.round(divisao*w);
			}
		}

		exibir(matrizImagemNegativa);
	}

	/**
	 * Aplica a transferência linear 
	 * 
	 * @param contraste = usuario pode setar
	 * @param brilho = usuario pode setar
	 * 
	 */
	public void transformacao_linear(int largura, int altura, int[][] r_matrizImagem, int contraste, int brilho) {
		// Criar uma imagem de Buffer para receber manipulações
		int[][] matrizImagemNegativa = new int[largura][altura];

		// percorre toda imagem pixel a pixel para realizar a transferência linear
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {

				// pega o nivel de cor de um pixel
				matrizImagemNegativa[y][x] = r_matrizImagem[y][x] * contraste + brilho;
			}
		}

		exibir(matrizImagemNegativa);
	}
	
	/**
	 * Encontra o pixel de maior valor
	 * 
	 */
	private int fMax(int[][] matrizImagem, int largura, int altura) {
		int max = matrizImagem[0][0];
				
				for (int y = 0; y < largura; y++) {
					for (int x = 0; x < altura; x++) {
						if (max < matrizImagem[y][x]) {
							max = matrizImagem[y][x];
						}
					}
				}

		return max;
	}

	/**
	 * Encontra o pixel de menor valor 
	 * 
	 */
	private int fMin(int[][] matrizImagem, int largura, int altura) {
		int min = matrizImagem[0][0];
		
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {
				if (min > matrizImagem[y][x] && matrizImagem[y][x] != 0) {
					min = matrizImagem[y][x];
				}
			}
		}

		return min;
	}
}