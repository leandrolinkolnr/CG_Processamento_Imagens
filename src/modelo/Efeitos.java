package modelo;

import processarImagem.PanelDaImagem;

public class Efeitos extends PanelDaImagem {

	public void transformacao() throws Exception {
		
	}

	/**
	 * Aplica a transforma��o Gamma em r_matrizImagem atrav�s da f�rmula: S = cr^y.
	 * 
	 * @param c = 1
	 * @param r = n�vel de cinza
	 * @param 0 <= y <= 1
	 * 
	 */
	
	public void gamma(int largura, int altura, int[][] r_matrizImagem, double gamma) {	
		int[][] matrizImagemNegativa = new int[largura][altura];
		int c = 1;
		// percorre toda imagem pixel a pixel para realizar efeito 		
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {
				matrizImagemNegativa[x][y] = (int) Math.round(c * Math.pow(r_matrizImagem[x][y], gamma));
			}
		}
		 exibir(matrizImagemNegativa);
	}

	
	
	/**
	 * Aplica a transforma��o Logaritmo atrav�s da f�rmula: S = alog(r+1).
	 * 
	 * @param constante fator = 20
	 * @param r = n�vel de cinza
	 * 
	 */
	
	public void logaritmo(int largura, int altura, int[][] r_matrizImagem, double fator) {
		// Criar uma imagem de Buffer para receber manipula��es
		int[][] matrizImagemNegativa = new int[largura][altura];
		// percorre toda imagem pixel a pixel para realizar a transformacao logaritmica
		for (int y = 0; y < largura; y++) {
			for (int x = 0; x < altura; x++) {

				double log = Math.round(fator * Math.log(r_matrizImagem[x][y] + 1));
				matrizImagemNegativa[x][y] = (int) log;
				
			}
		}

		exibir(matrizImagemNegativa);
	}

	/**
	 * Aplica a transfer�ncia de intensidade geral  atrav�s da fun��o:
	 * S(r) = 255 * (1/(1+e^-((r-w)/sigma)))
	 * 
	 * @param sigma = largura da janela
	 * @param r = n�vel de cinza
	 * @param w = centro dos valores de cinza (255/2)
	 * 
	 */
	public void intensidade_geral(int largura, int altura, int[][] r_matrizImagem, int w, int sigma) {

		// Criar uma imagem de Buffer para receber manipula��es
		
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
	 * Aplica a transfer�ncia de faixa din�mica: f' = ((f - fmin) / (fmax - fmin))*w .
	 * 
	 * f = valor do pixel a ser transformado
	 * @param fmin = pixel menor valor
	 * @param fmax = pixel maior valor
	 * @param w = faixa din�mica
	 * 
	 */
	public void faixa_dinamica(int largura, int altura, int[][] r_matrizImagem, int w) {
		
		// Criar uma imagem de Buffer para receber manipula��es
		
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
	 * Aplica a transfer�ncia linear 
	 * 
	 * @param contraste = usuario pode setar
	 * @param brilho = usuario pode setar
	 * 
	 */
	public void transformacao_linear(int largura, int altura, int[][] r_matrizImagem, int contraste, int brilho) {
		
		// Criar uma imagem de Buffer para receber manipula��es
		
		int[][] matrizImagemNegativa = new int[largura][altura];

		// percorre toda imagem pixel a pixel para realizar a transfer�ncia linear
		
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