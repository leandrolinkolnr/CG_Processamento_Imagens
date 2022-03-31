package view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import modelo.Efeitos;
import modelo.Morfologia;
import processarImagem.PanelDaImagem;

public class Tela extends JFrame {

	private static JPanel painelPrincipal;

	public static PanelDaImagem panelDaImagem1 = new PanelDaImagem(); // painel da primeira imagem
	public static PanelDaImagem panelDaImagem2 = new PanelDaImagem(); // painel da segunda imagem
	public static PanelDaImagem panelDaImagem3 = new PanelDaImagem(); // painel da quarta imagem
	public static PanelDaImagem panelDaImagem4 = new PanelDaImagem(); // painel da quarta imagem
	public static JPanel panelMascara = new JPanel(new GridLayout(3, 3));
	public static JLabel lblMenorValor = new JLabel("");
	public static JLabel lblMaiorValor = new JLabel("");
	
	Efeitos panelDaImagem_ef = new Efeitos(); //menu de efeitos (filtros)
	

	Morfologia panelDaImagem_mor = new Morfologia(); // menu operadores morfologicos

	public static JPanel panel_2 = new JPanel();
	public static PanelDaImagem panel_Pontos_1 = new PanelDaImagem();
	public static PanelDaImagem panel_Pontos_2 = new PanelDaImagem();
	public static JPanel panel_Pontos_3 = new JPanel(new GridLayout(9, 8));
	public static JPanel panel_Pontos_4 = new JPanel(new GridLayout(9, 8));
	public static JPanel panel_4 = new JPanel();
	public static JLabel lblResultadoNormalizado;
	public static JLabel lblNewLabel = new JLabel("");
	JLabel lblDica = new JLabel("");
	JLabel lblFiltro = new JLabel("");

	private boolean[][] CROSS = { { false, true, false }, { true, true, true }, { false, true, false } };

	JPanel painel3; // painel das imagens resultado
	// condições booleanas para ativar e desativar a visibilidade do painel3(de resultados)
	boolean logico = false;
	boolean aritmeticos = false;
	public static boolean efeitos1 = false;
	boolean his = false;
	public static boolean mor = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tela frame = new Tela();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tela() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1323, 865);
		setTitle("Processamento de Imagens");

		JMenuBar menuBar = new JMenuBar(); // barra de menu
		setJMenuBar(menuBar);

		painelPrincipal = new JPanel();
		painelPrincipal.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(painelPrincipal);
		painelPrincipal.setLayout(null);

		JPanel panel_3 = new JPanel();// painel resultados
		panel_3.setLayout(null);
		panel_3.setBounds(681, 122, 256, 256);
		panel_3.setBorder(BorderFactory.createEtchedBorder());
		painelPrincipal.add(panel_3);


		
		
		JMenu transformacoes = new JMenu("Transformações"); // menu efeito de transformações
		menuBar.add(transformacoes);

		JMenuItem gamma = new JMenuItem("Gamma"); // botao transformacao gamma
		transformacoes.add(gamma);
		gamma.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				efeitos1 = true;
				panelDaImagem_ef.setBounds(0, 0, 256, 256);
				panelDaImagem_ef.setVisible(efeitos1);
				panel_3.add(panelDaImagem_ef);
				double gamma = 1.05;
				gamma = Double.parseDouble(JOptionPane.showInputDialog("Gamma: ", gamma));

				panelDaImagem_ef.gamma(panelDaImagem1.largura, panelDaImagem1.altura,
						panelDaImagem1.matrizImagem, gamma);

				lblFiltro.setText("Gamma");
			}

		});

		JMenuItem logaritmica = new JMenuItem("Logarítmica"); // botao transformacao logaritmica
		transformacoes.add(logaritmica);
		logaritmica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				efeitos1 = true;
				panelDaImagem_ef.setBounds(0, 0, 256, 256);
				panelDaImagem_ef.setVisible(efeitos1);
				panel_3.add(panelDaImagem_ef);
				double a = 20;
				//a eh constante
				a = Double.parseDouble(JOptionPane.showInputDialog("Constante: ", a));
				panelDaImagem_ef.logaritmo(panelDaImagem1.largura, panelDaImagem1.altura,
						panelDaImagem1.matrizImagem, a);

				lblFiltro.setText("Logarítmica");
			}

		});

		JMenuItem itf_sigmoide = new JMenuItem("Intensidade Geral"); // botao da  função de transferência de intensidade geral
		transformacoes.add(itf_sigmoide);
		itf_sigmoide.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				efeitos1 = true;
				panelDaImagem_ef.setBounds(0, 0, 256, 256);
				panelDaImagem_ef.setVisible(efeitos1);
				panel_3.add(panelDaImagem_ef);
				// centro dos valores de cinza
				int w = Integer.parseInt(JOptionPane.showInputDialog("Preencha com o valor w", "127"));
				// e 𝜎 a largura da janela
				int sigma = Integer.parseInt(JOptionPane.showInputDialog("Preencha com o tamanho da janela", "25"));
				panelDaImagem_ef.intensidade_geral(panelDaImagem1.largura, panelDaImagem1.altura,
						panelDaImagem1.matrizImagem,w,sigma);

				lblFiltro.setText(" Transferência de Intensidade Geral");
			}

		});

		JMenuItem faixa_dinamica = new JMenuItem("Faixa Dinamica"); // botao da função de transferência faixa dinâmica
		transformacoes.add(faixa_dinamica);
		faixa_dinamica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				efeitos1 = true;
				panelDaImagem_ef.setBounds(0, 0, 256, 256);
				panelDaImagem_ef.setVisible(efeitos1);
				panel_3.add(panelDaImagem_ef);
				int w = 255;
				w = Integer.parseInt(JOptionPane.showInputDialog("w_target: ", w));
				panelDaImagem_ef.faixa_dinamica(panelDaImagem1.largura, panelDaImagem1.altura,
						panelDaImagem1.matrizImagem, w);

				lblFiltro.setText("Transferência de Faixa Dinâmica");
			}

		});

		JMenuItem transformacao_linear = new JMenuItem("Transferência Linear"); //botao da função de tranferencia linear
		transformacoes.add(transformacao_linear);
		transformacao_linear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				efeitos1 = true;
				panelDaImagem_ef.setBounds(0, 0, 256, 256);
				panelDaImagem_ef.setVisible(efeitos1);
				panel_3.add(panelDaImagem_ef);
				int contraste = 2;
				int brilho = 2;
				contraste = Integer
						.parseInt(JOptionPane.showInputDialog("Adicione um valor para o contraste: ", contraste));
				brilho = Integer.parseInt(JOptionPane.showInputDialog("Adicione um valor para o brilho: ", brilho));
				panelDaImagem_ef.transformacao_linear(panelDaImagem1.largura, panelDaImagem1.altura,
						panelDaImagem1.matrizImagem, contraste, brilho);

				lblFiltro.setText("Transferência Linear");
			}
		});

		JMenu morfologia = new JMenu("Morfologia");
		menuBar.add(morfologia);
		
		JMenu mmnNC = new JMenu("Nível de Cinza"); //Menu das Operaçoes morf em imagem NC
		morfologia.add(mmnNC);
		
		
		JMenuItem dilatar = new JMenuItem("Dilatação");
		mmnNC.add(dilatar);
		dilatar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					String times = JOptionPane.showInputDialog("Preencha com o número de vezes que deseja aplicar", "2");
					int timesInt = Integer.parseInt(times);
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					parcial = panelDaImagem_mor.dilate(parcial, timesInt, CROSS);
					
					String[] mascara = {"0", "1", "0", "1", "1", "1", "0", "1", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Dilatar");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "O valor de entrada deve ser um inteiro!");
				}
			}
		});
		
		JMenuItem erodir = new JMenuItem("Erosão");
		mmnNC.add(erodir);
		erodir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setVisible(mor);
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panel_3.add(panelDaImagem_mor);

				try {
					String times = JOptionPane.showInputDialog("Preencha com o número de vezes que deseja aplicar", "2");
					int timesInt = Integer.parseInt(times);
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					parcial = panelDaImagem_mor.erode(parcial, timesInt, CROSS);
					
					String[] mascara = {"0", "1", "0", "1", "1", "1", "0", "1", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Erodir");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "O valor de entrada deve ser um inteiro!");
				}
			}
		});
		
		JMenuItem gradienteMor = new JMenuItem("Gradiente Morfológico");
		mmnNC.add(gradienteMor);
		gradienteMor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();
				mor = true;
				panelDaImagem_mor.setVisible(mor);
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panel_3.add(panelDaImagem_mor);

				try {
					BufferedImage erosao = converterBuffered(panelDaImagem1.matrizImagem);
					erosao = panelDaImagem_mor.erodir(erosao, CROSS);
					BufferedImage dilatacao = converterBuffered(panelDaImagem1.matrizImagem);
					dilatacao = panelDaImagem_mor.dilatar(dilatacao, CROSS);
					
					panelDaImagem_mor.subtract(dilatacao, erosao);
					
					lblFiltro.setText("Gradiente Morfologico");
				
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Erro inesperado!");
				}
			}
		});
		
		JMenuItem abertura = new JMenuItem("Abertura");
		mmnNC.add(abertura);
		abertura.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setVisible(mor);
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panel_3.add(panelDaImagem_mor);

				try {
					String times = JOptionPane.showInputDialog("Preencha com o número de vezes que deseja aplicar", "2");
					int timesInt = Integer.parseInt(times);
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					parcial = panelDaImagem_mor.abertura(parcial, timesInt, CROSS);
					
					lblFiltro.setText("Abertura");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "O valor de entrada deve ser um inteiro!");
				}
			}
		});
		
		JMenuItem fechamento = new JMenuItem("Fechamento");
		mmnNC.add(fechamento);
		fechamento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setVisible(mor);
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panel_3.add(panelDaImagem_mor);

				try {
					String times = JOptionPane.showInputDialog("Preencha com o número de vezes que deseja aplicar", "2");
					int timesInt = Integer.parseInt(times);
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					parcial = panelDaImagem_mor.fechamento(parcial,timesInt,CROSS);
	
					lblFiltro.setText("Fechamento");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "O valor de entrada deve ser um inteiro!");
				}
			}
		});
		
		JMenuItem topHat = new JMenuItem("Top-Hat"); // Botao top hat
		mmnNC.add(topHat);
		topHat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setVisible(mor);
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panel_3.add(panelDaImagem_mor);

				try {
					BufferedImage imagem = converterBuffered(panelDaImagem1.matrizImagem);
					BufferedImage abertura = converterBuffered(panelDaImagem1.matrizImagem);

					String x = JOptionPane.showInputDialog("Preencha com o número de vezes que deseja aplicar", "20");
					int xInt = Integer.parseInt(x);
					
					//abertura primeiro
					abertura = panelDaImagem_mor.abertura(abertura, xInt, CROSS);
					panelDaImagem_mor.subtract(imagem, abertura);
					
					lblFiltro.setText("Top-Hat");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "O valor de entrada deve ser um inteiro!");
				}
			}
		});
		
		JMenuItem bottomHat = new JMenuItem("Bottom-Hat");
		mmnNC.add(bottomHat);
		bottomHat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setVisible(mor);
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panel_3.add(panelDaImagem_mor);

				try {
					BufferedImage imagem = converterBuffered(panelDaImagem1.matrizImagem);
					BufferedImage fechamento = converterBuffered(panelDaImagem1.matrizImagem);
					
					String times = JOptionPane.showInputDialog("Preencha com o número de vezes que deseja aplicar", "20");
					int timesInt = Integer.parseInt(times);
					
					//fechamento primeiro
					fechamento = panelDaImagem_mor.fechamento(fechamento, timesInt, CROSS);
					panelDaImagem_mor.subtract(fechamento, imagem);
					
					lblFiltro.setText("Bottom-Hat");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "O valor de entrada deve ser um inteiro!");
				}
			}
		});
		
		JMenu mmnBinario = new JMenu("Binária"); //Menu das Operaçoes morf em imagem binaria
		morfologia.add(mmnBinario);
		
		JMenuItem dilatarMascara = new JMenuItem("Dilatação");
		mmnBinario.add(dilatarMascara);
		dilatarMascara.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para dilatar
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.dilatarBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Dilatação");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		JMenuItem erodirMascara = new JMenuItem("Erosão");
		mmnBinario.add(erodirMascara);
		erodirMascara.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para erodir
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.erodirBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Erosão");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		JMenuItem fechamentoB = new JMenuItem("Fechamento");//Botão para aplicar fechamento na imagem binaria
		mmnBinario.add(fechamentoB);
		fechamentoB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para fechamento
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.fechamentoBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Fechamento");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		JMenuItem aberturaB = new JMenuItem("Abertura");//Botão para aplicar abertura na imagem binaria
		mmnBinario.add(aberturaB);
		aberturaB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para abertura
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.aberturaBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Abertura");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		
		JMenuItem hitormissB = new JMenuItem("Hit-or-Miss");//Botão para aplicar hitormiss na imagem binaria
		mmnBinario.add(hitormissB);
		hitormissB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para hitormiss
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.hitormissBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Hit-Or-Miss");
				
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		
		JMenuItem contExtB = new JMenuItem("Contorno Externo");//Botão para aplicar contorno externo na imagem binaria
		mmnBinario.add(contExtB);
		contExtB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para contorno externo
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.contExtBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Contorno Externo");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		JMenuItem contIntB = new JMenuItem("Contorno Interno");//Botão para aplicar contorno interno na imagem binaria
		mmnBinario.add(contIntB);
		contIntB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para contorno interno
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.contIntBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Contorno Interno");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		JMenuItem gradienteB = new JMenuItem("Gradiente Morfológico");//Botão para aplicar gradiente Morfológico na imagem binaria
		mmnBinario.add(gradienteB);
		gradienteB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				mor = true;
				panelDaImagem_mor.setBounds(0, 0, 256, 256);
				panelDaImagem_mor.setVisible(mor);
				panel_3.add(panelDaImagem_mor);


				try {
					//Elemento estruturante para o gradiente
					int[][] elementoEstruturante = new int[3][2];
					elementoEstruturante[0][0] = 0;
					elementoEstruturante[0][1] = -1;
					elementoEstruturante[1][0] = 0;
					elementoEstruturante[1][1] = 0;
					elementoEstruturante[2][0] = 0;
					elementoEstruturante[2][1] = 1;
					
					BufferedImage parcial = converterBuffered(panelDaImagem1.matrizImagem);
					panelDaImagem_mor.gradMorfBin(panelDaImagem1.largura, panelDaImagem1.altura, panelDaImagem1.matrizImagem, elementoEstruturante);
					
					String[] mascara = { "0", "0", "0", "1", "X", "1", "0", "0", "0"};
					plotaMascara(mascara);
					
					lblFiltro.setText("Gradiente Morfológico");
					
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Estouro no array!");
				}
			}
		});
		
		JMenu mnAjuda = new JMenu("Sobre");
		menuBar.add(mnAjuda);

		JMenuItem mntmEquippe = new JMenuItem("Grupo");
		mnAjuda.add(mntmEquippe);
		mntmEquippe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				limpaInformacoes();

				String team = "Leandro Lincoln Rodrigues \n"
						+ "Pedro Henrique Ferreira Ramos \n"
						+ "Rafael de Medeiros Silva";
				JOptionPane.showMessageDialog(null, team);
			}
		});

		JPanel painel1 = new JPanel();
		painelPrincipal.add(painel1);
		painel1.setLayout(null);

		panelDaImagem1.setBounds(30, 30, 256, 256);
		panelDaImagem1.setVisible(true);
		painel1.add(panelDaImagem1);

		JButton btAbrirImagem1 = new JButton("Abrir Imagem");// Botão abrir imagem
		btAbrirImagem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {

					// Instanciacao de fileChooser e alteracao do diretorio para buscar a imagem
					final JFileChooser fileChooser = new JFileChooser();
					fileChooser.setCurrentDirectory(new File("src/"));

					// Verificacao do fileChooser
					if (fileChooser.showOpenDialog(btAbrirImagem1) == JFileChooser.APPROVE_OPTION) {

						// Cria um file onde eh armazenada a imagem
						File file = fileChooser.getSelectedFile();

						apresenta2Imagem();
						panelDaImagem1.colocaImagemNoPainel(file.getPath());
						panelDaImagem1.plotaPixels(panel_Pontos_1);
					}

				} catch (Exception erro) {
					JOptionPane.showMessageDialog(null, "Não foi possivel carregar a imagem.");

				}
			}
		});

		btAbrirImagem1.setBounds(150, 403, 131, 33);
		painelPrincipal.add(btAbrirImagem1);

		panel_2.setBorder(new LineBorder(new Color(0, 0, 0), 3));
		panel_2.setBounds(384, 122, 256, 256);
		painelPrincipal.add(panel_2);
		panel_2.setLayout(null);

		panelDaImagem2.setBounds(0, 0, 256, 256);
		panelDaImagem2.setVisible(true);
		panel_2.add(panelDaImagem2);

		panelDaImagem3.setBounds(0, 0, 256, 256);
		panelDaImagem3.setVisible(true);
		panel_3.add(panelDaImagem3);

		panelDaImagem4.setBounds(0, 0, 256, 256);
		panelDaImagem4.setVisible(true);
		panel_4.add(panelDaImagem4);


		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(743, 406, 163, 24);
		painelPrincipal.add(lblNewLabel);
		panelDaImagem1.setBounds(86, 122, 256, 256);

		panel_Pontos_1.setBounds(87, 456, 261, 261);
		panel_Pontos_2.setBounds(384, 456, 261, 261);
		panel_Pontos_3.setBounds(681, 456, 261, 261);
		painelPrincipal.add(panel_Pontos_1);
		painelPrincipal.add(panel_Pontos_2);
		painelPrincipal.add(panel_Pontos_3);

		panel_4.setLayout(null);
		panel_4.setBorder(BorderFactory.createEtchedBorder());
		panel_4.setBounds(973, 122, 256, 256);
		panel_4.setVisible(false);
		painelPrincipal.add(panel_4);

		lblResultadoNormalizado = new JLabel("Resultado Normalizado");
		lblResultadoNormalizado.setHorizontalAlignment(SwingConstants.CENTER);
		lblResultadoNormalizado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblResultadoNormalizado.setBounds(1024, 406, 163, 24);
		lblResultadoNormalizado.setVisible(false);
		painelPrincipal.add(lblResultadoNormalizado);

		panel_Pontos_4.setBounds(973, 456, 261, 261);
		panel_Pontos_4.setVisible(false);
		painelPrincipal.add(panel_Pontos_4);
		lblDica.setHorizontalAlignment(SwingConstants.CENTER);

		lblDica.setBounds(384, 51, 845, 46);
		painelPrincipal.add(lblDica);

		panelMascara.setBounds(168, 28, 74, 80);
		painelPrincipal.add(panelMascara);
		lblMenorValor.setHorizontalAlignment(SwingConstants.CENTER);

		lblMenorValor.setBounds(973, 730, 261, 16);
		painelPrincipal.add(lblMenorValor);
		lblMaiorValor.setHorizontalAlignment(SwingConstants.CENTER);

		lblMaiorValor.setBounds(973, 755, 261, 16);
		painelPrincipal.add(lblMaiorValor);
		
		lblFiltro.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblFiltro.setHorizontalAlignment(SwingConstants.CENTER);
		lblFiltro.setBounds(549, 13, 482, 25);
		painelPrincipal.add(lblFiltro);
		
		painelPrincipal.add(panelDaImagem1);
		panelDaImagem1.setVisible(true);
	}

	/**
	 * Remove a 2 imagem e/ou a parte da imagem normalizada
	 */
	public static void removeElementos() {
		if (efeitos1 || mor) {
			panel_Pontos_2.setVisible(false);
			panel_2.setVisible(false);
			panelDaImagem2.setVisible(false);
			
		} else {
			apresenta2Imagem();
		}
	}

	public static BufferedImage converterBuffered(int[][] mtzImg) {
		// matriz que vc passa como parâmetro definindo a largura da imagem
		int largura = mtzImg.length,
				// matriz que vc passa como parâmetro definindo a altura da imagem
				altura = mtzImg[0].length;

		// criando uma objeto BufferedImage a partir das dimensões da imagem
		// representada pela matriz
		BufferedImage image = new BufferedImage(largura, altura, BufferedImage.TYPE_INT_RGB);

		WritableRaster raster = image.getRaster();
		for (int h = 0; h < largura; h++) {
			for (int w = 0; w < altura; w++) {
				raster.setSample(h, w, 0, mtzImg[h][w]);
			}
		}

		return image;
	}

	public int corPixel(int corRGB) {
		Color cor = new Color(corRGB, corRGB, corRGB);
		return cor.getRGB();
	}

	/**
	 * Oculta o painel da imagem/pontos normalizados 
	 * 
	 * @param tf - True para apresentar. False para ocultar
	 */
	public static void removeDaTelaNormalizado(boolean tf) {
		lblResultadoNormalizado.setText("Resultado Normalizado");
		panel_4.setVisible(tf);
		lblResultadoNormalizado.setVisible(tf);
		panel_Pontos_4.setVisible(tf);
	}

	/**
	 * Apresenta a segunda imagem Não apresenta para operações que utilizam apenas 1
	 * imagem, como os efeitos
	 */
	public static void apresenta2Imagem() {
		panel_2.setVisible(true);
		panelDaImagem2.setVisible(true);
		panel_Pontos_2.setVisible(true);
	}

	private void limpaInformacoes() {
		lblDica.setText("");
		lblFiltro.setText("");
		panelMascara.setVisible(false);
		lblNewLabel.setText("Imagem Resultado");
		

		logico = false;
		aritmeticos = false;
		efeitos1 = false;
		his = false;
		mor = false;
		panelDaImagem_ef.setVisible(false);
		panelDaImagem_mor.setVisible(false);
	}

	public void plotaMascara(String[] mascara) {
		removeElementos();
		panelMascara.removeAll();
		panelMascara.setLayout(new GridLayout(3, 3));
		panelMascara.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		panelMascara.setVisible(true);

		for (int i = 0; i < mascara.length; i++) {
			String text = mascara[i];
			JLabel label = new JLabel(text, SwingConstants.CENTER);
			label.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
			panelMascara.add(label);
		}
		panelMascara.validate();
	}
}
