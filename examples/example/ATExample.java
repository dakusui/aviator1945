package example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import javax.swing.JFrame;

public class ATExample extends JFrame {
	private static final long serialVersionUID = 1L;

	public void init() {
		this.setUndecorated(true);
		this.setSize(640, 480);
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		Graphics2D gg = (Graphics2D) this.getGraphics();
		// offset = (40, 40)
		// i = (600,  40)
		// j = (80,  400) 
		AffineTransform Tx = new AffineTransform(
				640.0/600.0,   -40.0/640.0,
				-120.0/480.0,     480.0/400.0,
				0,        0
				/*-80, -80*/
				);
		gg.transform(Tx);
		gg.setColor(Color.red);
		for (int i = 0; i <= 640; i += 40) {
			gg.drawLine(0, i, 639, i);
			gg.drawLine(i, 0,   i, 639);
			for (int j = 0; j <= 640; j += 40) {
				if (j % 80 == 0 && i % 80 == 0) {
					gg.drawString("(" + i + "," + j + ")", i, j);
				}
			}
		}
		gg.setColor(Color.blue);
		gg.fillRect(100, 100, 100, 100);
	}
	
	
	public static void main(String[] args) {
		ATExample frame = new ATExample();
		frame.init();
	}

}
