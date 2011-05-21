package oreactor.video.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;

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

	public GraphicsPlane(String name, double width, double height,  Viewport viewport) {
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

	static class Interval {
		int left;
		int right;
		public Interval(int left, int right) {
			this.left = Math.min(left, right);
			this.right = Math.max(left, right);
		}
		boolean covers(Interval another) {
			if (left() <= another.left() && right() >= another.right()) {
				return true;
			}
			return false;
		}
		boolean contains(int x) {
			return x >= left() && x <= right();
		}
		int left() {
			return this.left;
		}
		int right() {
			return this.right;
		}
	}

	public void paint(double x1, double y1, Color c, Color b) {
		f((int)x1, (int)y1, c.getRGB(), b.getRGB());
	}

	Interval f(int x, int y, int cc, int bb) {
		Interval interval = scanLine(x, y, cc, bb);
		if (interval != null) {
			fillLine(interval, y, cc);
		g(x, y + 1, interval, cc, bb);
		g(x, y - 1, interval, cc, bb);
		}
		return interval;
	}
	
	void g(int x, int y, Interval limit, int cc, int bb) {
		Interval interval = null;
		int xx;
		xx = x;
		do {
			interval = f(xx, y, cc, bb);
			if (interval != null) {
				xx = interval.right();
			} else {
				xx ++;
			}
		} while (limit.contains(xx));
		xx = x - 1;
		do {
			interval = f(xx, y, cc, bb);
			if (interval != null) {
				xx = interval.left();
			} else {
				xx --;
			}
		} while (limit.contains(xx));
	}
	
	private Interval scanLine(int x, int y, int cc, int bb) {
		if (!shouldPaint(x, y, cc, bb)) {
			Interval ret = null;
			return ret;
		} else {
			int r;
			for (r = x; r < this.width; r++) {
				if (!shouldPaint(r, y, cc, bb)) {
					break;
				}
			}
			int l;
			for (l = x - 1; l >= 0; l--) {
				if (!shouldPaint(l, y, cc, bb)) {
					break;
				}
			}
			return new Interval(l, r);
		}
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
	
	private void fillLine(Interval interval, int y, int cc) {
		line(interval.left(), y, interval.right(), y, new Color(cc));
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
