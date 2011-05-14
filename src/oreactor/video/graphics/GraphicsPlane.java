package oreactor.video.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import oreactor.video.Plane;
import oreactor.video.Viewport;

/**
 * A graphics plane.
 * @author hiroshi
 *
 */
public class GraphicsPlane extends Plane {
	BufferedImage image;
	private Color fgColor;
	private Color bgColor;

	public GraphicsPlane(String name, double width, double height, Viewport viewport) {
		super(name, width, height, viewport);
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
		//scanLineFill((int)x1, (int)y1, c.getRGB(), c, b.getRGB());
	}

	static enum Direction {
		BOTH, UPWARD, DOWNWAWRD;
	};
	private void scanLineFill(int x, int y, Color c, Color b, Direction d, int left, int right) {
		int cc = c.getRGB();
		int bb = b.getRGB();
		if (!shouldPaint(x, y, cc, bb)) {
			return;
		}
		int r;
		for (r = x; r < this.width; r++) {
			if (!shouldPaint(r, y, cc, bb)) {
				break;
			}
		}
		int l;
		for (l = x; l >= 0; l--) {
			if (!shouldPaint(r, y, cc, bb)) {
				break;
			}
		}
		line(l, y, r, y, c);	
		List<Integer> points = new LinkedList<Integer>();
		if (d == Direction.BOTH || d == Direction.UPWARD) {
			points.clear();
			scanLine(points, l, r, y - 1);
			for (int i: points) {
				scanLineFill(i, y - 1, c, b, Direction.UPWARD, l, r);
				Graphics2D g;
			}
		}
		if (d == Direction.BOTH || d == Direction.DOWNWAWRD) {
			points.clear();
			scanLine(points, l, r, y + 1);
		}
	}
	
	private void scanLine(List<Integer> points, int l, int r, int y) {
		// TODO Auto-generated method stub
	}

	boolean shouldPaint(int x, int y, int c, int b) {
		try {
			if (x < 0 || x >= this.image.getWidth() || y < 0 || y >= this.image.getHeight()) {
				return false;
			}
			int tmp = this.image.getRGB(x, y);
			if (tmp == c || tmp == b) {
				return false;
			}
			return true;
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("(x,y)=(" + x + "," + y + ")");
			throw e;
		}
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
		/*
		g.drawImage(
				this.image, 
				0, 0, (int)this.width, (int)this.height, 
				0, 0, (int)this.width, (int)this.height, 
				this.bgColor, null
				);
				*/
	} 
}
