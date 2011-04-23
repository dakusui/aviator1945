package oreactor.video;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public interface Drawable {
	public BufferedImage offscreenBuffer();
	public void flipped();
	public List<Plane> planes();
	public Graphics2D onscreenBuffer();
}
