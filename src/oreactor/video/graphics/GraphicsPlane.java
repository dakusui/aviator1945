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

	public void paint(double x1, double y1, Color c, Color b) {
		//scanLineFill((int)x1, (int)y1, c.getRGB(), c, b.getRGB());
	}

	static enum Direction {
		BOTH, UPWARD, DOWNWAWRD;
	};
	
	static abstract class Interval {
		abstract int left();
		abstract int right();
		boolean covers(Interval another) {
			if (left() <= another.left() && right() >= another.right()) {
				return true;
			}
			return false;
		}
		Interval protrusion(Interval another) {
			Interval ret = null;
			if ((ret = rightProtrusion(another)) != null) {
				return ret;
			}
			return leftProtrusion(another);
		}
		private Interval rightProtrusion(Interval another) {
			return null;
		}
		private Interval leftProtrusion(Interval another) {
			return null;
		}
	}

	static class CompositeInterval extends Interval {
		List<Interval> children = new LinkedList<Interval>();
		void add(Interval i) {
			children.add(i);
		}
		@Override
		int left() {
			int ret = Integer.MAX_VALUE;
			for (Interval i : children) {
				ret = Math.min(i.left(), ret);
			}
			return ret;
		}
		@Override
		int right() {
			int ret = 0;
			for (Interval i : children) {
				ret = Math.max(i.right(), ret);
			}
			return ret;
		}
	}
	
	static class SimpleInterval extends Interval {
		int left;
		int right;
		public SimpleInterval(int left, int right) {
			this.left = Math.min(left, right);
			this.right = Math.max(left, right);
		}
		@Override
		int left() {
			return this.left;
		}
		@Override
		int right() {
			return this.right;
		}
	}
	Interval scanLineFill(int x, int y, Interval from, Color c, Color b) {
		int cc = c.getRGB();
		int bb = b.getRGB();
		Interval interval = null;
		interval = scanLine(x, y, cc, bb);
		if (interval == null) {
			return null;
		}
		fillLine(interval, c);

		Interval newinterval;
		{
			int s = x;
			do {
				if (shouldPaint(s, y -1, cc, bb)) {
					newinterval = scanLineFill(s, y-1, interval, c, b);
					if (newinterval != null) {
						s = newinterval.right() + 1;
					} else {
						s = s + 1;
					}
				} else {
					s = s + 1;
				}
			} while (s <= interval.right());
			s = x - 1 ;
			do {
				if (shouldPaint(s, y -1, cc, bb)) {
					newinterval = scanLineFill(s, y-1, interval, c, b);
					if (newinterval != null) {
						s = newinterval.left() - 1;
					} else {
						s = s - 1;
					}
				} else {
					s = s - 1;
				}
			} while (s <= interval.right());
		}
		return interval;
	}
	
	private Interval scanLine(int x, int y, int cc, int bb) {
		if (!shouldPaint(x, y, cc, bb)) {
			return null;
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
		return null;//new Interval(l, r, y);
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
	
	private void fillLine(Interval interval, Color c) {
		//line(interval.left, interval.y, interval.right, interval.y, c);
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
