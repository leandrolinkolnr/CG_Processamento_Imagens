package processarImagem;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


import view.Tela;


public class PanelDaImagem extends JPanel {

	private static final String COMENTARIO = "#";
	private static final String P2 = "P2";

	public int[][] matrizImagem;
	public static int[][] matrizPontosImagem = new int[10][9];
	public int altura;
	public int largura;
	public BufferedReader imagem;
	public BufferedImage imagemOriginal;
	public static BufferedImage resultado;
	public static BufferedImage resultado2;

	public PanelDaImagem() {

		setBounds(new Rectangle(0, 0, 256, 256));

	}

	public void colocaImagemNoPainel(String caminhoDaImagem) {
		try {
			imagem = new BufferedReader(new FileReader(caminhoDaImagem));
			geraImagem();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao tentar abrir a imagem.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void geraImagem() throws Exception {
		String type = imagem.readLine();// Ler a primeira linha da mensagem e ignora a descricao do tipo da imagem

		if (type == null || !type.equals(P2)) {
			throw new Exception("Formato invalido - Tipo P2 requerido");
		}

		// Ignora as linhas com comentários
		String linha = null;

		do {
			linha = imagem.readLine();
		} while (linha != null && linha.startsWith(COMENTARIO));

		// Le o número de linhas e colunas
		// linha possuirá a resolucao da imagem ex: 256 256
		String[] dimensao = linha.split(" ");
		altura = Integer.parseInt(dimensao[0]);
		largura = Integer.parseInt(dimensao[1]);
		matrizImagem = new int[altura][largura];

		String[] pixels;
		int line = 0;// coluna onde o pixel se localiza

		/* A linha onde refere-se ao valor maximo que o pixel pode ter eh ignorado */
		linha = imagem.readLine();
		linha = imagem.readLine();
		while (linha != null) {

			pixels = linha.split(" ");
			for (int i = 0; i < pixels.length; i++) {
				matrizImagem[line][i] = Integer.parseInt(pixels[i]);

				if (line < 10 && i < 9) {
					matrizPontosImagem[line][i] = Integer.parseInt(pixels[i]);
				}

			}
			line++;
			linha = imagem.readLine();
		}

		imagemOriginal = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < altura; i++) {
			for (int j = 0; j < largura; j++) {
				imagemOriginal.setRGB(j, i, corPixel(matrizImagem[i][j]));
				repaint();
			}
		}
	}

	public int corPixel(int corRGB) {
		Color cor = new Color(corRGB, corRGB, corRGB);
		return cor.getRGB();
	}

	public void exibirResultado(BufferedImage resultado) {
		imagemOriginal = resultado;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage((Image) imagemOriginal, 0, 0, null);
	}

	public static void exibir(int[][] matriz) {
		try {
			Tela.panelDaImagem3.exibirTruncada(matriz);
			Tela.panelDaImagem4.exibirNormalizada(matriz, 255);
			Tela.removeElementos();
		} catch (Exception IllegalArgumentException) {
			JOptionPane.showMessageDialog(null, "É necessário adicionar imagem.");
		}
	}
	
	public static void exibirBin(int[][] matriz, int[][] matriz2) {
		try {
			Tela.lblNewLabel.setText("Imagem Binária");
			Tela.panelDaImagem3.exibirMatrizBin(matriz2, 255);
			Tela.panelDaImagem4.exibirNormalizada(matriz, 255);
			Tela.removeElementos();
			Tela.lblResultadoNormalizado.setText("Imagem Resultado");
		} catch (Exception IllegalArgumentException) {
			JOptionPane.showMessageDialog(null, "É necessário adicionar imagem.");
		}
	}

	public void exibirMatrizBin(int[][] matriz, int w) {
		altura = matriz.length;
		largura = matriz.length;
		resultado = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);
		int[][] matriz_normalizada = new int[altura][largura];
		double maior_pixel = 0;
		double menor_pixel = 65025; // 255 * 255 = 65025

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {

				int valor_pixel = matriz[i][j];
				if (valor_pixel > maior_pixel) {
					maior_pixel = valor_pixel;
				}

				if (valor_pixel < menor_pixel) {
					menor_pixel = valor_pixel;
				}
			}
		}
		for (int i = 0; i < matriz_normalizada.length; i++) {
			for (int j = 0; j < matriz_normalizada.length; j++) {

				double fun1 = (w / (maior_pixel - menor_pixel));
				double fun2 = (matriz[i][j] - menor_pixel);
				double fun3 = Math.round(fun1 * fun2);
				matriz_normalizada[i][j] = (int) (fun3);

				// Matriz para apresentar na Tela - Pixels
				if (i < 10 && j < 9) {
					matrizPontosImagem[i][j] = (int) (fun3);
				}
				resultado.setRGB(j, i, corPixel(matriz_normalizada[i][j]));
			}
		}
		plotaPixels(Tela.panel_Pontos_3);
		exibirResultado(resultado);
		repaint();
		validate();
	}
	
	public static void exibir(int[][] matriz, int w) {
		Tela.panelDaImagem3.exibirTruncada(matriz);
		Tela.panelDaImagem4.exibirNormalizada(matriz, w);
		Tela.removeElementos();
	}	

	public static void exibir(Raster matriz) {
		int[][] matrizResultado = convertToMatriz(matriz);
		exibir(matrizResultado);
	}
	
	private static int[][] convertToMatriz(Raster matriz) {
		int[][] novaMatriz = new int[matriz.getWidth()][matriz.getHeight()];
		for (int i = 0; i < novaMatriz.length; i++) {
			for (int j = 0; j < novaMatriz.length; j++) {
				novaMatriz[i][j] = matriz.getSample(i, j, 0);
			}
		}
		return novaMatriz;
	}

	public void exibirTruncada(int[][] matriz) {
		altura = matriz.length;
		largura = matriz.length;
		resultado = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);
		int[][] matriz_truncada = new int[altura][largura];
		for (int i = 0; i < matriz_truncada.length; i++) {
			for (int j = 0; j < matriz_truncada.length; j++) {

				if (matriz[i][j] > 255) {
					matriz_truncada[i][j] = 255;
				} else if (matriz[i][j] < 0) {
					matriz_truncada[i][j] = 0;
				} else {
					matriz_truncada[i][j] = matriz[i][j];
				}
				// Matriz para apresentar na Tela - Pixels
				if (i < 10 && j < 9) {
					matrizPontosImagem[i][j] = matriz_truncada[i][j];
				}
				resultado.setRGB(j, i, corPixel(matriz_truncada[i][j]));
			}
		}
		plotaPixels(Tela.panel_Pontos_3);  
		exibirResultado(resultado);
		repaint();
		validate();
	}

	// Método para normalizar matrizes
	public void exibirNormalizada(int[][] matriz, int w) {
		altura = matriz.length;
		largura = matriz.length;
		resultado = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);
		int[][] matriz_normalizada = new int[altura][largura];
		double maior_pixel = 0;
		double menor_pixel = 65025; // 255 * 255 = 65025

		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {

				int valor_pixel = matriz[i][j];
				if (valor_pixel > maior_pixel) {
					maior_pixel = valor_pixel;
				}

				if (valor_pixel < menor_pixel) {
					menor_pixel = valor_pixel;
				}
			}
		}
		for (int i = 0; i < matriz_normalizada.length; i++) {
			for (int j = 0; j < matriz_normalizada.length; j++) {

				double fun1 = (w / (maior_pixel - menor_pixel));
				double fun2 = (matriz[i][j] - menor_pixel);
				double fun3 = Math.round(fun1 * fun2);
				matriz_normalizada[i][j] = (int) (fun3);

				// Matriz para apresentar na Tela - Pixels
				if (i < 10 && j < 9) {
					matrizPontosImagem[i][j] = (int) (fun3);
				}
				resultado.setRGB(j, i, corPixel(matriz_normalizada[i][j]));
			}
		}

		Tela.lblMenorValor.setText("Menor valor: " + (int) menor_pixel);
		Tela.lblMaiorValor.setText("Maior valor: " + (int) maior_pixel);
		plotaPixels(Tela.panel_Pontos_4);
		Tela.removeDaTelaNormalizado(true);
		exibirResultado(resultado);
		repaint();
		validate();
	}

	
	public static void plotaPixels(JPanel painel) {
		/*
		Tela.removeElementos();
		painel.removeAll();
		painel.setLayout(new GridLayout(9, 8));
		painel.setBorder(BorderFactory.createLineBorder(Color.RED));

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 8; j++) {
				String text = Integer.toString(matrizPontosImagem[i][j]);
				JLabel label = new JLabel(text, SwingConstants.CENTER);
				label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
				painel.add(label);
			}
		}
		painel.setVisible(true);
		painel.validate(); */
	}

	public int[] defineAlturaLargura(int altura1, int largura1, int altura2, int largura2) {
		int[] resultado = new int[2];
		if (altura1 <= altura2) {
			altura = altura1;
		} else {
			altura = altura2;
		}

		if (largura1 <= largura2) {
			largura = largura1;
		} else {
			largura = largura2;
		}
		resultado[0] = altura;
		resultado[1] = largura;
		return resultado;
	}

	public void exibirCandS(int[][] matriz1, int[][] matriz2) {
		altura = matriz1.length;
		largura = matriz1.length;
		resultado = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);
		resultado2 = new BufferedImage(altura, largura, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < matriz1.length; i++) {
			for (int j = 0; j < matriz1.length; j++) {
				resultado.setRGB(j, i, corPixel(matriz1[i][j]));
				resultado2.setRGB(j, i, corPixel(matriz2[i][j]));
			}
		}
	}


}