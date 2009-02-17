package avis.video;

import java.awt.GraphicsEnvironment;

public class FontTest {
	public static void main(String[] args) throws Exception {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		String []fontFamilies = ge.getAvailableFontFamilyNames();
		for (int i = 0; i < fontFamilies.length; i++) {
			System.out.println(fontFamilies[i]);
		}
	}
}
