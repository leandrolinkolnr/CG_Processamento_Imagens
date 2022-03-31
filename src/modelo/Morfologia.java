package modelo;

import java.awt.Color;
import java.awt.image.BufferedImage;

import processarImagem.PanelDaImagem;

public class Morfologia extends PanelDaImagem {

	/**
     * Subtrai duas imagens do mesmo tamanho. Considera a imagem em tons de cinza.
     */
	
	
    /**
     * Aplica a dilata��o x vezes.
     */
    
    public BufferedImage dilate(BufferedImage img, int x, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < x; i++) {
            out = dilatar(out, kernel);
        }
        exibir(out.getRaster());
        return out;
    }
    
    
    /**
     * Opera��o de dilata��o morfol�gica.
     * Nesta opera��o buscamos entre o pixel e seus vizinhos aqueles com o tom de cinza mais claro (de maior valor).
     * Os pixels considerados na busca s�o aqueles marcados com true no kernel (pixels ativos).
     */
    
    public BufferedImage dilatar(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de sa�da
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //A dilata��o busca pelo pixel de maior valor
                int max = 0;
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 9 vizinhos
                //Os vizinhos que ser�o considerados est�o marcados como true no kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // pr�ximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }
                        //Obtem o tom de cinza do pixel e se for mais claro que o maior j� encontrado, substitui
                        int tone = new Color(img.getRGB(px, py)).getRed();
                        if (kernel[kx][ky] && tone > max) {
                            max = tone;
                        }
                    }
                }
                //Define essa cor na imagem de sa�da.
                out.setRGB(x, y, new Color(max, max, max).getRGB());
            }
        }
        return out;
    }
    
    
    
    /**
     * Aplica a erosao x vezes.
     */
    
    public BufferedImage erode(BufferedImage img, int x, boolean[][] kernel) {
        BufferedImage out = img;
        for (int i = 0; i < x; i++) {
            out = erodir(out, kernel);
        }
        exibir(out.getRaster());
        return out;
    }
    
    
    
    /**
     * Opera��o de eros�o morfol�gica.
     * Nesta opera��o buscamos entre o pixel e seus vizinhos aqueles com o tom de cinza mais escuro (de menor valor).
     * Os pixels considerados na busca s�o aqueles marcados com true no kernel.
     */
    public BufferedImage erodir(BufferedImage img, boolean[][] kernel) {
        //Cria a imagem de sa�da
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //A eros�o busca pelo pixel de menor valor
                int min = 255;
                //Para cada pixel percorrido na imagem, precisamos percorrer os seus 8 vizinhos
                //Os vizinhos que ser�o considerados est�o marcados como true no kernel
                for (int ky = 0; ky < 3; ky++) {
                    for (int kx = 0; kx < 3; kx++) {
                        //Observe que os �ndices de kx e ky variam de 0 at� 2. J� os vizinhos de x seriam
                        //x+(-1), x+0 + x+1. Por isso, subtra�mos 1 de kx e ky para chegar no vizinho.
                        int px = x + (kx-1);
                        int py = y + (ky-1);

                        //Nas bordas, px ou py podem acabar caindo fora da imagem. Quando isso ocorre, pulamos para o
                        // pr�ximo pixel.
                        if (px < 0 || px >= img.getWidth() || py < 0 || py >= img.getHeight()) {
                            continue;
                        }

                        //Obtem o tom de cinza do pixel
                        int tone = new Color(img.getRGB(px, py)).getRed();

                        //Se ele for mais escuro que o menor j� encontrado, substitui
                        if (kernel[kx][ky] && tone < min) {
                            min = tone;
                        }
                    }
                }

                //Define essa cor na imagem de sa�da.
                out.setRGB(x, y, new Color(min, min, min).getRGB());
            }
        }
        return out;
    }
	
	
    /**
     * Opera��o de gradiente morfol�gico binario.
     */
    
    public int[][] gradMorfBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
    	int p0 = 0;
      	int p255 = 0;
      	int pNovo = 0;
      	int pNovo2 = 255;
      	// M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      	// Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      	// Pun (limiar 51), Kapur (limiar 162).
      	//percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      				p255++;
      			}else {
      				matrizImagem[x][y] = 0;
      				p0++;
      			}
      		}
      	}
      	if (p0 > p255) {
      		pNovo = 255;
      		pNovo2 = 0;
      	}
      	int[][] parcial = dilatarBin(largura, altura, matrizImagem, elementoEstruturante);
      	int[][] parcial2 = erodirBin(largura, altura, matrizImagem, elementoEstruturante);
      	
      	// retirando os pixels ativos que pertencem a imagem parcial2 da parcial
      	for (int i = 0; i < matrizImagem.length; i++) {
      		for (int j = 0; j < matrizImagem.length; j++) {
				if (parcial2[i][j] == pNovo && parcial[i][j] == pNovo) {
					parcial[i][j] = pNovo2;
				}
			}
		}
      	exibirBin(parcial, matrizImagem);
        return parcial;
    }
    
    
    /**
     * Abertura morfol�gica.
     * Trata-se de v�rias eros�es seguidas do mesmo n�mero de dilata��es. Isso faz com que �reas pequenas da imagem
     * tendam a desaparecer, e estruturas maiores sejam mantidas.
     */
    
    public BufferedImage abertura(BufferedImage img, int x, boolean[][] kernel) {
        BufferedImage c = dilate(erode(img, x, kernel), x, kernel);
        exibir(c.getRaster());
    	return c;
    }
    
    
    
    /**
     * Fechamento morfol�gico.
     * Trata-se de v�rias dilata��es seguidas do mesmo n�mero de eros�es. Isso faz com que "buracos" pequenos na imagem
     * tendam a desaparecer.
     */
    
    public BufferedImage fechamento(BufferedImage img, int times, boolean[][] kernel) {
    	BufferedImage c = erode(dilate(img, times, kernel), times, kernel);
    	exibir(c.getRaster());
    	return c;
    }
    
   
    public BufferedImage subtract(BufferedImage img1, BufferedImage img2) {
        //Cria a imagem de sa�da
        BufferedImage out = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
            	
                //Le o pixel
                Color p1 = new Color(img1.getRGB(x, y));
                Color p2 = new Color(img2.getRGB(x, y));

                //Faz a subtra��o, canal a canal
                int tone1 = p1.getRed() - p2.getRed();
                int tone2 = p1.getGreen() - p2.getGreen();
                int tone3 = p1.getBlue() - p2.getBlue();

                //Define a cor de sa�da na imagem de sa�da (out).
                out.setRGB(x, y, toColor(tone1, tone2, tone3));
            }
        }
        exibir(out.getRaster());
        return out;
    }
    
    

    
    /**
     * Opera��o de dilata��o morfol�gica binaria.
     */
    public int[][] dilatarBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
        //Cria a imagem de sa�da
        BufferedImage out = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

        //matriz auxiliar
      	int[][] matrizImagemBinaria = new int[largura][altura];
      		
      	int xPosicao = 0;
      	int yPosicao = 0;
      	int p0 = 0;
      	int p255 = 0;
      	int pNovo = 0;
      	int pNovo2 = 255;
      	
      //percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      				p255++;
      			}else {
      				matrizImagem[x][y] = 0;
      				p0++;
      			}
      		}
      	}
      	
      	if (p0 > p255) {
      		pNovo = 255;
      		pNovo2 = 0;
      	}
      	
      	
      	//percorre toda imagem pixel a pixel para realizar dilatacao
      	for (int y = 0; y < matrizImagem.length - 1; y++) {
      		for (int x = 0; x < matrizImagem[y].length; x++) {
      			// so aplica em pixels ativos
      			if (matrizImagem[x][y] == pNovo) {
  					// soma a posicao do pixel pelo elemento estruturante
    				for (int xEE = 0; xEE < elementoEstruturante.length; xEE++) {
      					for (int yEE = 0; yEE < elementoEstruturante[xEE].length - 1; yEE++) {
      						// somando a posicao de x, y na imagem com a posicao de x, y do elemento estruturante
      						xPosicao = x + elementoEstruturante[xEE][yEE];
      						yPosicao = y + elementoEstruturante[xEE][yEE + 1];
      						// se essa posicao jah esta com pixel ativo, nao muda nada
      						if (xPosicao >= 0 && yPosicao >= 0 && xPosicao <= 256 && yPosicao <= 256 && matrizImagemBinaria[xPosicao][yPosicao] != pNovo) {
      							matrizImagemBinaria[xPosicao][yPosicao] = pNovo;
      							
          			      		//atribui a "out" pixel a pixel o resultado obtido em "matrizImagemBinaria[x][y]"				
          		      			out.setRGB(xPosicao, yPosicao, toColor(matrizImagemBinaria[xPosicao][yPosicao],matrizImagemBinaria[xPosicao][yPosicao], matrizImagemBinaria[xPosicao][yPosicao]));
      						}
      					}
      					
      				}
          			System.out.println(matrizImagemBinaria[x][y]);    				
      			}else {
      				matrizImagemBinaria[x][y] = pNovo2;
      			}
      			
       		}
    	}
      	
      	exibirBin(matrizImagemBinaria, matrizImagem);
        return matrizImagemBinaria;
    }
    
    
    /**
     * Opera��o de eros�o morfol�gica binaria.
     */
    public int[][] erodirBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
       //matriz auxiliar
      	int[][] matrizImagemBinaria = new int[largura][altura];
      		
      	int xPosicao = 0;
      	int yPosicao = 0;
      	int p0 = 0;
      	int p255 = 0;
      	int pNovo = 0;
      	int pNovo2 = 255;
      	
      // M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      // Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      // Pun (limiar 51), Kapur (limiar 162).
      //percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      				p255++;
      			}else {
      				matrizImagem[x][y] = 0;
      				p0++;
      			}
      		}
      	}
      	
      	if (p0 > p255) {
      		pNovo = 255;
      		pNovo2 = 0;
      	}
      	
      	
      	//percorre toda imagem pixel a pixel para realizar dilatacao
      	for (int y = 0; y < matrizImagem.length - 1; y++) {
      		for (int x = 0; x < matrizImagem[y].length; x++) {
      			// so aplica em pixels ativos
      			if (matrizImagem[x][y] == pNovo) {
  					// soma a posicao do pixel pelo elemento estruturante
    				for (int xEE = 0; xEE < elementoEstruturante.length; xEE++) {
      					for (int yEE = 0; yEE < elementoEstruturante[xEE].length - 1; yEE++) {
      						// somando a posicao de x, y na imagem com a posicao de x, y do elemento estruturante
      						xPosicao = x - elementoEstruturante[xEE][yEE];
      						yPosicao = y - elementoEstruturante[xEE][yEE + 1];
      						// se essa posicao � pixel ativo, se torna inativo
      						if (xPosicao >= 0 && yPosicao >= 0 && xPosicao <= 255 && yPosicao <= 255 && matrizImagem[xPosicao][yPosicao] == pNovo) {
      							matrizImagemBinaria[xPosicao][yPosicao] = pNovo;
      							
          			      	}else {
      							//desativando pixel
      							matrizImagemBinaria[x][y] = pNovo2;
      						}
      					}
      					
      				}
          			System.out.println(matrizImagemBinaria[x][y]);    				
      			}else {
      				matrizImagemBinaria[x][y] = pNovo2;
      			}
      			
       		}
    	}
      	exibirBin(matrizImagemBinaria, matrizImagem);
        return matrizImagemBinaria;
    }
    
    /**
     * Opera��o de fechamento morfol�gico binario.
     */
    public int[][] fechamentoBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
        //matriz auxiliar
      	int[][] matrizImagemBinaria = new int[largura][altura];

      // M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      // Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      // Pun (limiar 51), Kapur (limiar 162).
      //percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      			}else {
      				matrizImagem[x][y] = 0;
      			}
      		}
      	}
      	
      	int[][] parcial = dilatarBin(largura, altura, matrizImagem, elementoEstruturante);
      	matrizImagemBinaria = erodirBin(largura, altura, parcial, elementoEstruturante);
      	
      	exibirBin(matrizImagemBinaria, matrizImagem);
        return matrizImagemBinaria;
    }
    
    
    /**
     * Opera��o de abertura morfol�gica binaria.
     */
    public int[][] aberturaBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
        //matriz auxiliar
      	int[][] matrizImagemBinaria = new int[largura][altura];

      // M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      // Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      // Pun (limiar 51), Kapur (limiar 162).
      //percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      			}else {
      				matrizImagem[x][y] = 0;
      			}
      		}
      	}
      	
      	int[][] parcial = erodirBin(largura, altura, matrizImagem, elementoEstruturante);
      	matrizImagemBinaria = dilatarBin(largura, altura, parcial, elementoEstruturante);
      	
      	exibirBin(matrizImagemBinaria, matrizImagem);
        return matrizImagemBinaria;
    }
    
    /**
     * Opera��o de hit-or-miss binaria.
     */
    public int[][] hitormissBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
    	
      	int p0 = 0;
      	int p255 = 0;
      	int pNovo = 0;
      	int pNovo2 = 255;
      	
    	//matrizes auxiliares
      	int[][] matrizImagemBinaria = new int[largura][altura];
      	int[][] matrizAux = new int[largura][altura];
      	
      	int[][] parcial = erodirBin(largura, altura, matrizImagem, elementoEstruturante);
      	
      	//elemento estruturante que engloba o inicial
      	int[][] newElementoEstruturante = new int[11][2];
      	newElementoEstruturante[0][0] = -1;
      	newElementoEstruturante[0][1] = -2;
      	newElementoEstruturante[1][0] = -1;
      	newElementoEstruturante[1][1] = -1;
      	newElementoEstruturante[2][0] = -1;
      	newElementoEstruturante[2][1] = 0;
      	newElementoEstruturante[3][0] = -1;
      	newElementoEstruturante[3][1] = 1;
      	newElementoEstruturante[4][0] = -1;
      	newElementoEstruturante[4][1] = 2;
      	newElementoEstruturante[5][0] = 0;
      	newElementoEstruturante[5][1] = 2;
      	newElementoEstruturante[6][0] = 1;
      	newElementoEstruturante[6][1] = -2;
      	newElementoEstruturante[7][0] = 1;
      	newElementoEstruturante[7][1] = -1;
      	newElementoEstruturante[8][0] = 1;
      	newElementoEstruturante[8][1] = 0;
      	newElementoEstruturante[9][0] = 1;
      	newElementoEstruturante[9][1] = 1;
      	newElementoEstruturante[10][0] = 1;
      	newElementoEstruturante[10][1] = 2;

      	
      // M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      // Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      // Pun (limiar 51), Kapur (limiar 162).
      //percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      				p255++;
      			}else {
      				matrizImagem[x][y] = 0;
      				p0++;
      			}
      		}
      	}
      	
      	if (p0 > p255) {
      		pNovo = 255;
      		pNovo2 = 0;
      	}
      	
      	// fazer a imagem complemento da original binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] == pNovo) {
      				matrizAux[x][y] = pNovo2;
      				p255++;
      			}else {
      				matrizAux[x][y] = pNovo;
      				p0++;
      			}
      		}
      	}
      	
      	int[][] parcial2 = erodirBin(largura, altura, matrizAux, newElementoEstruturante);
      	
      	for (int i = 0; i < parcial2.length; i++) {
			for (int j = 0; j < parcial2.length; j++) {
				if (parcial[i][j] == pNovo && parcial2[i][j] == pNovo) {
					matrizImagemBinaria[i][j] = pNovo;
				}else {
					matrizImagemBinaria[i][j] = pNovo2;
				}
			}
		}
      	exibirBin(matrizImagemBinaria, matrizImagem);
        return matrizImagemBinaria;
    }
    
    /**
     * Opera��o de contorno externo binario.
     */
    public int[][] contExtBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
    	int p0 = 0;
      	int p255 = 0;
      	int pNovo = 0;
      	int pNovo2 = 255;
    
      	// M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      	// Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      	// Pun (limiar 51), Kapur (limiar 162).
      	//percorre toda imagem pixel a pixel para transformar em binaria
      	
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      				p255++;
      			}else {
      				matrizImagem[x][y] = 0;
      				p0++;

      			}
      		}
      	}
      	
      	if (p0 > p255) {
      		pNovo = 255;
      		pNovo2 = 0;
      	}
      	
      	int[][] parcial = dilatarBin(largura, altura, matrizImagem, elementoEstruturante);
      	
      	// retirando os pixels que pertencem a imagem original da parcial
      	for (int i = 0; i < matrizImagem.length; i++) {
      		for (int j = 0; j < matrizImagem.length; j++) {
				if (matrizImagem[i][j] == pNovo && parcial[i][j] == pNovo) {
					parcial[i][j] = pNovo2;
				}
			}
		}
      	      	
      	exibirBin(parcial, matrizImagem);
        return parcial;
    }
    
    
    /**
     * Opera��o de contorno interno binario.
     */
    
    public int[][] contIntBin(int largura, int altura, int[][] matrizImagem, int[][] elementoEstruturante) {
    	int p0 = 0;
      	int p255 = 0;
      	int pNovo = 0;
      	int pNovo2 = 255;
      	
    	//matriz auxiliar
      	int[][] matrizImagemBinaria = new int[largura][altura];
    
      	// M�dia Padr�o (limiar 127), N�vel M�dio de Cinza (limiar 94), 10% de Preto (limiar 28),
      	// Sele��o Iterativa (limiar 82), Otsu (limiar 83), Dois Picos (limiar 109), Borda (limiar 127), 
      	// Pun (limiar 51), Kapur (limiar 162).
      	//percorre toda imagem pixel a pixel para transformar em binaria
      	for (int y = 0; y < largura; y++) {
      		for (int x = 0; x < altura; x++) {
      			if (matrizImagem[x][y] >= 127) {
      				matrizImagem[x][y] = 255;
      				matrizImagemBinaria[x][y] = 255;
      				p255++;
      			}else {
      				matrizImagem[x][y] = 0;
      				matrizImagemBinaria[x][y] = 0;
      				p0++;

      			}
      		}
      	}
      	
      	if (p0 > p255) {
      		pNovo = 255;
      		pNovo2 = 0;
      	}
      	
      	int[][] parcial = erodirBin(largura, altura, matrizImagem, elementoEstruturante);
      	
      	// retirando os pixels ativos que pertencem a imagem parcial da original
      	for (int i = 0; i < matrizImagem.length; i++) {
      		for (int j = 0; j < matrizImagem.length; j++) {
				if (matrizImagem[i][j] == pNovo && parcial[i][j] == pNovo) {
					matrizImagemBinaria[i][j] = pNovo2;
				}
			}
		}
      	      	
      	exibirBin(matrizImagemBinaria, matrizImagem);
        return matrizImagemBinaria;
    }
    
   



    



    

    
    


    

	 /**
     * Garante que o valor do pixel estar� entre 0 e 255.
     */
    private int clamp(float value) {
        int v = (int)value;
        return v > 255 ? 255 : (v < 0 ? 0 : v);
    }

    /**
     * Converte os valores de r, g e b para o inteiro da cor.
     * Os valores podem estar fora do intervalo de 0 at� 255, pois
     * a fun��o ajusta chamando a fun��o clamp (acima).
     */
    private int toColor(float r, float g, float b) {
        return new Color(clamp(r), clamp(g), clamp(b)).getRGB();
    }

}