package example;

import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class TestFrame extends JFrame {

	/**
	 * A serial version UID.
	 */
	private static final long serialVersionUID = 1304933505792318409L;

	public static void _main(String[] args) {
		TestFrame f = new TestFrame();
		
		f.setVisible(true);
		System.out.println(f.getGraphics());
	}
	
	public static void main(String args[]) {
		long b = 0;
		System.out.println("before:" + (b = Runtime.getRuntime().freeMemory()));
		Image[] images =  new Image[10];
		for (int i = 0 ; i < images.length; i++) {
			images[i] = new BufferedImage(640, 640, BufferedImage.TYPE_4BYTE_ABGR);
		}
		long a = 0;
		System.out.println("after:" + (a = Runtime.getRuntime().freeMemory()));
		System.out.println("delta:" + (b - a));
	}
}
