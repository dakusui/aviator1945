package oreactor.video.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

import oreactor.video.Plane;

/**
 * A graphics plane.
 * @author hiroshi
 *
 */
public class GraphicsPlane extends Plane {
	BufferedImage image;
	private Color fgColor;
	private Color bgColor;

	public GraphicsPlane(String name, double width, double height) {
		super(name, width, height);
		this.image = new BufferedImage((int)width, (int)height, ColorSpace.TYPE_RGB); 
	}

	public void line(double x1, double y1, double x2, double y2, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}

	public void box(double x, double y, double w, double h, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}

	public void boxfill(double x, double y, double w, double h, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		g.fillRect((int)x, (int)y, (int)w, (int)h);
	}

	public void oval(double x, double y, double w, double h, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		g.drawOval((int)x, (int)y, (int)w, (int)h);
	}

	public void filloval(double x, double y, double w, double h, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		g.drawOval((int)x, (int)y, (int)w, (int)h);
	}

	public void pset(double x, double y, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		this.image.setRGB((int)x, (int)y, c.getRGB());
	}

	public void put(Image image, double x, double y, double hratio,
			double vratio) {
		Graphics g = this.image.getGraphics();
		g.drawImage(image, (int)x, (int)y, null);
	}

	public Color point(double x, double y) {
		return new Color(this.image.getRGB((int)x, (int)y));
	}

	public void print(String msg, double x, double y, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(figureOutColor(c));
		g.drawString(msg, (int)x, (int)y);
	}

	public void clear() {
		Graphics g = this.image.getGraphics();
		g.clearRect(0, 0, this.image.getWidth(), this.image.getHeight());
	}

	public void paint(double x1, double y1, Color c, Color b) {
		//this.image.getRGB(x1, y1);
		// TODO
	}

	public void color(Color foreground, Color background) {
		this.fgColor = foreground;
		this.bgColor = background;
	}

	private Color figureOutColor(Color c) {
		Color ret = c;
		if (ret == null) {
			ret = this.fgColor;
		}
		return ret;
	}
	
	@Override
	protected void render_Protected(Graphics2D g) {
		g.drawImage(
				this.image, 
				0, 0, (int)this.width, (int)this.height, 
				0, 0, (int)this.width, (int)this.height, 
				this.bgColor, null
				);
	} 
}
