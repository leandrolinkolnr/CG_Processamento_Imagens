package view;

import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Tela_Resultado {
	
	public Tela_Resultado(BufferedImage imagem, String titulo){
		
		JFrame j = new JFrame();
		j.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				j.setDefaultCloseOperation( j.DISPOSE_ON_CLOSE );
			}
		});
		j.setSize(imagem.getWidth(), imagem.getHeight());
		j.setVisible(true);
		j.setTitle(titulo);
		JPanel p = new JPanel();
		j.getContentPane().add(p);
		p.setSize(300, 300);
		JLabel l = new JLabel();
		p.add(l);
		l.setSize(300, 300);
		l.setIcon(new ImageIcon(imagem));
		
	}

}
