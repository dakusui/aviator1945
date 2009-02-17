/**
 * 
 */
package avis.video;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.io.InputStream;

import javax.swing.JComponent;

import avis.base.AException;
import avis.base.Avis;

public class ABackground extends JComponent {
	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1008157887766723036L;

	protected Image image;
	protected double imageHeight;
	protected double imageWidth;
	protected ImageObserver observer;

	private AViewport viewport;

	protected ABackground(Image background, AViewport viewport) {
		if (background != null) {
			image(background);
		}
		this.viewport = viewport;
	}

	public void draw(Graphics2D gg, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2) {
		if (image != null) {
			double ratio = viewport.ratio();
			gg.drawImage(
					this.image, 
					dx1, 
					dy1, 
					dx2, 
					dy2, 
					(int) (sx1 * ratio), 
					(int) (sy1 * ratio), 
					(int) (sx2 * ratio), 
					(int) (sy2 * ratio), 
					null);
		}
	}

	public void image(Image background) {
		if (this.image != null) {
			this.image.flush();
			this.image = null;
		}
		imageWidth = background.getWidth(observer);
		imageHeight = background.getHeight(observer);
		this.image = background;
		Avis.logger().info("Setup:background=<" + background + ">");
	}
	
	public Image image() {
		return this.image;
	}

	public double imageWidth() {
		return imageWidth * viewport.zoom();
	}

	public double imageHeight() {
		return imageHeight * viewport.zoom();
	}
	
	public void load(String resource) throws AException {
    	InputStream is = Avis.openUrl(resource);
 		Image image = null;
		if (this.image != null) {
			this.image.flush();
			this.image = null;
		}
 		try {
 			image = Avis.readImage(is);
 		} finally {
 			Avis.closeStream(is);
 		}
 		image(image);
	}
}