package oreactor.video;

import java.awt.image.BufferedImage;
import java.util.List;

public interface Drawable {
	public BufferedImage offscreenBuffer();
	public void flipped();
	public List<Plane> planes();
	public BufferedImage onscreenBuffer();
	public int width();
	public int height();
}
