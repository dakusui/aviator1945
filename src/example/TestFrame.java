package example;

import javax.swing.JFrame;

public class TestFrame extends JFrame {

	/**
	 * A serial version UID.
	 */
	private static final long serialVersionUID = 1304933505792318409L;

	public static void main(String[] args) {
		TestFrame f = new TestFrame();
		
		f.setVisible(true);
		System.out.println(f.getGraphics());
	}
	
}
