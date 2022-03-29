package modelo;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

import processarImagem.TriangulatedImage;
import view.Tela;

/**
 * 
 * Transformação de uma imagem triangulada em outra.
 * Para a animação, a técnica de buffer doube é aplicada.
 * 
 * Créditos:
 * @author Frank Klawonn Last change 31.05.2005
 *
 */

public class Transformacao extends TimerTask {

	// A janela na qual a transformação é mostrada
	private BufferedImage buffid = Tela.panelDaImagem3.imagemOriginal;

	// As duas imagens a serem transformadas uma na outra serão dimensionadas para
	// este tamanho
	private int width;
	private int height;

	// O número de etapas (quadros) para a transformação
	private int steps;

	// A primeira imagem triangulada
	private TriangulatedImage t1;

	// A segunda imagem triangulada
	private TriangulatedImage t2;

	// Isto é usado para gerar/armazenar as imagens intermediárias
	private BufferedImage mix;

	// Uma variável que é aumentada gradualmente de 0 para 1. É necessário
	// para o cálculo das combinações convexas.
	private double alpha;

	// A mudança de alfa em cada etapa: deltAlpha = 1.0 / steps
	private double deltaAlpha;

	/**
	 * Constructor
	 *
	 * @param bid A janela na qual a transformação é mostrada.
	 */
	public Transformacao() {
		// Largura da janela.
		int width = 256;
		// Altura da janela.
		int height = 256;

		// Especifica (em milissegundos) quando o quadro deve ser atualizado.
		int delay = 50;

		// O BufferedImage a ser desenhado na janela.
		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// A janela na qual tudo é desenhado.
		BufferedImage bid = Tela.panelDaImagem3.imagemOriginal;

		// A TimerTask na qual os cálculos repetidos para o desenho ocorrem.
		Transformacao mcs = new Transformacao(bid);

		Timer t = new Timer();
		t.scheduleAtFixedRate(mcs, 0, delay);

	}

	public Transformacao(BufferedImage bid) {
		buffid = bid;

		width = 256;
		height = 256;

		steps = 100;

		deltaAlpha = 1.0 / steps;

		alpha = 0;

		// Este objeto é usado para carregar as duas imagens.
		Image loadedImage;

		// Gerando a primeira imagem triangulada:
		t1 = new TriangulatedImage();

		// Define o tamanho.
		t1.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// Gerando o objeto Graphics2D.
		Graphics2D g2dt1 = t1.bi.createGraphics();

		loadedImage = new javax.swing.ImageIcon(getClass().getResource("/imagens/matheus_p.jpg")).getImage();

		g2dt1.drawImage(loadedImage, 0, 0, null);

		// Definicao de pontos para a triangulation.
		t1.tPoints = new Point2D[9];
		t1.tPoints[0] = new Point2D.Double(0, 0);
		t1.tPoints[1] = new Point2D.Double(0, 220);
		t1.tPoints[2] = new Point2D.Double(0, 255);
		t1.tPoints[3] = new Point2D.Double(80, 255);
		t1.tPoints[4] = new Point2D.Double(255, 255);
		t1.tPoints[5] = new Point2D.Double(255, 110);
		t1.tPoints[6] = new Point2D.Double(255, 0);
		t1.tPoints[7] = new Point2D.Double(50, 0);
		t1.tPoints[8] = new Point2D.Double(50, 110);

		// Definicao dos triangulos.
		t1.triangles = new int[8][3];

		for (int i = 0; i < 7; i++) {
			t1.triangles[i][0] = i;
			t1.triangles[i][1] = i + 1;
			t1.triangles[i][2] = 8;
		}

		t1.triangles[7][0] = 7;
		t1.triangles[7][1] = 0;
		t1.triangles[7][2] = 8;

		// O mesmo para a segunda imagem.
		t2 = new TriangulatedImage();

		t2.bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2dt2 = t2.bi.createGraphics();

		loadedImage = new javax.swing.ImageIcon(getClass().getResource("/imagens/matheus_a.jpg")).getImage();

		g2dt2.drawImage(loadedImage, 0, 0, null);

		t2.tPoints = new Point2D[9];
		t2.tPoints[0] = new Point2D.Double(0, 0);
		t2.tPoints[1] = new Point2D.Double(0, 50);
		t2.tPoints[2] = new Point2D.Double(0, 185);
		t2.tPoints[3] = new Point2D.Double(40, 255);
		t2.tPoints[4] = new Point2D.Double(255, 255);
		t2.tPoints[5] = new Point2D.Double(255, 120);
		t2.tPoints[6] = new Point2D.Double(255, 0);
		t2.tPoints[7] = new Point2D.Double(55, 0);
		t2.tPoints[8] = new Point2D.Double(55, 40);

		// A indexação dos triângulos deve ser a mesma que na
		// a primeira imagem.
		t2.triangles = t1.triangles;

	}

	public void run() {

		// Como esse método é chamado arbitrariamente com freqüência, a interpolação deve
		// ser eliminado enquanto alfa estiver entre 0 e 1.
		if (alpha >= 0 && alpha <= 1) {
			// Gerando a imagem interpolada
			mix = t1.mixWith(t2, alpha);

			// Desenhando a imagem interpolada no BufferedImage.
			Tela.panelDaImagem3.imagemOriginal = mix;
			Tela.panelDaImagem3.imagemOriginal.createGraphics();
			
			// Chamando o método para atualizar a janela.
			Tela.panelDaImagem3.repaint();
		}

		// Incrementando alpha.
		alpha = alpha + deltaAlpha;
	}
}
