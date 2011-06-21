package oreactor.video.graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.LinkedList;
import java.util.List;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.Plane;
import oreactor.video.VideoUtil;
import oreactor.video.Viewport;

/**
 * A graphics plane.
 * @author hiroshi
 *
 */
public class GraphicsPlane extends Plane {
	public VolatileImage vImage;

	public static enum Mode {
		Immediate {
			@Override
			public void append(GraphicsPlane gplane, Command command) {
				Graphics2D gg = (Graphics2D) gplane.image.getGraphics();
				try {
					command.run(gg);
				} finally {
					gg.dispose();
				}
				gplane.vImage = null;
			}
			@Override
			public void render(GraphicsPlane plane, Graphics2D g) throws OpenReactorException {
				bitblt(plane, g);
			}
			@Override
			void runCommands(GraphicsPlane gplane, Graphics2D g) {
				// does nothing
			}
			@Override
			public BufferedImage newImage(GraphicsPlane gplane) {
				return newImage_(gplane);
			}
		},
		Sticky {
			@Override
			public void render(GraphicsPlane gplane, Graphics2D g) throws OpenReactorException {
				runCommands(gplane, g);
				bitblt(gplane, g);
			}
			@Override
			void runCommands(GraphicsPlane gplane, Graphics2D g) {
				Graphics2D gg = (Graphics2D) gplane.image.getGraphics();
				try {
					for (Command cmd : gplane.commandQueue) {
						cmd.run(gg);
						gplane.vImage = null;
					}
				} finally {
					gg.dispose();
				}
			}
			@Override
			public BufferedImage newImage(GraphicsPlane gplane) {
				BufferedImage ret =  new BufferedImage((int)gplane.width, (int)gplane.height, BufferedImage.TYPE_INT_ARGB);
				return ret;
			}
		},
		Volatile {
			@Override
			public BufferedImage newImage(GraphicsPlane gplane) {
				return null;
			}
			@Override
			void render(GraphicsPlane gplane, Graphics2D g) {
				runCommands(gplane, g);
			}
			@Override
			void runCommands(GraphicsPlane gplane, Graphics2D g) {
				for (Command cmd : gplane.commandQueue) {
					cmd.run(g);
				}
			}
		};
		abstract BufferedImage newImage(GraphicsPlane gplane);
		void append(GraphicsPlane gplane, Command command) {
			gplane.commandQueue.add(command);
		}
		void bitblt(GraphicsPlane gplane, Graphics2D g) throws OpenReactorException {
			GraphicsConfiguration gConfig = g.getDeviceConfiguration();
			int left = (int) gplane.viewport.left();
			int top = (int) gplane.viewport.top();
			int right = (int) gplane.viewport.right();
			int bottom = (int) gplane.viewport.bottom();
			if (gplane.isAcclerationEnabled()) {
				VolatileImage vTmp = gplane.vImage;
				if (vTmp == null) {
					vTmp = VideoUtil.getVolatileVersion(gConfig, gplane.image, gplane.isAutoUpdateEnabled());
				}
				do {
					g.drawImage(
							vTmp,
							left, top, right, bottom, 
							left, top, right, bottom, 
							gplane.bgColor, null
							);

					gplane.vImage = vTmp;
				} while ((vTmp = VideoUtil.getVolatileVersionIfContentsLost(gConfig, gplane.image, gplane.vImage)) != null);
			} else {
				g.drawImage(
						gplane.image, 
						left, top, right, bottom, 
						left, top, right, bottom, 
						gplane.bgColor, null
				);
			}
		}
		abstract void render(GraphicsPlane gplane, Graphics2D g) throws OpenReactorException;
		abstract void runCommands(GraphicsPlane gplane, Graphics2D g);
		public BufferedImage newImage_(GraphicsPlane gplane) {
			BufferedImage ret =  new BufferedImage((int)gplane.width, (int)gplane.height, BufferedImage.TYPE_INT_ARGB);
			return ret;
		}
	}
	static abstract class Command {
		Color c;
		int x, y, w, h;
		int x1, y1, x2, y2;
		Command(double x, double y, double w, double h, Color c) {
			this.x1 = this.x = (int)x;
			this.y1 = this.y = (int)y;
			this.x2 = this.w = (int)w;
			this.y2 = this.h = (int)h;
			this.c = c;
		}
		abstract void run(Graphics g);
	}
	static class Interval {
		int left;
		int right;
		public Interval(int left, int right) {
			this.left = Math.min(left, right);
			this.right = Math.max(left, right);
		}
		boolean contains(int x) {
			return x >= left() && x <= right();
		}
		boolean covers(Interval another) {
			if (left() <= another.left() && right() >= another.right()) {
				return true;
			}
			return false;
		}
		int left() {
			return this.left;
		}
		int right() {
			return this.right;
		}
	}
	private Color bgColor;
	private List<Command> commandQueue = new LinkedList<Command>();
	private Color fgColor;
	private BufferedImage image;
	private Mode mode;
	private boolean autoUpdateEnabled;
	
	public GraphicsPlane(String name, double width, double height,  Viewport viewport) {
		super(name, width, height, viewport);
		this.mode(Mode.Immediate);
		this.disableAcceleration();
		this.enableAutoUpdate();
	}

	public void box(double x, double y, double w, double h, Color c) {
		Command comm = new Command(x, y, w, h, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.drawRect((int)x, (int)y, (int)w, (int)h);
			}
		};
		mode.append(this, comm);
		comm = null;
	}
	
	public void boxfill(double x, double y, double w, double h, Color c) {
		Command comm = new Command(x, y, w, h, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.fillRect((int)x, (int)y, (int)w, (int)h);
			}
		};
		mode.append(this, comm);
		comm = null;
	}
	
	public void clear() {
		Command comm = new Command(0, 0, this.image.getWidth(), this.image.getHeight(), null) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.clearRect(x1, y1, x2, y2);
			}
		};
		mode.append(this, comm);
		comm = null;
	}
	public void color(Color foreground, Color background) {
		this.fgColor = foreground;
		this.bgColor = background;
	}

	public void filloval(double x, double y, double w, double h, Color c) {
		Command comm = new Command(x, y, w, h, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.fillOval(x, y, w, h);
			}
		};
		mode.append(this, comm);
		comm = null;
	}

	public void line(double x1, double y1, double x2, double y2, Color c) {
		Command comm = new Command(x1, y1, x2, y2, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.drawLine(x1, y1, x2, y2);
			}
		};
		mode.append(this, comm);
		comm = null;
	}

	public Mode mode() {
		return this.mode;
	}

	public void mode(Mode mode) {
		if (this.image != null) {
			this.image.flush();
		}
		this.image = mode.newImage(this);
		this.mode = mode;
	}

	public void oval(double x, double y, double w, double h, Color c) {
		Command comm = new Command(x, y, w, h, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.drawOval(x, y, w, h);
			}
		};
		mode.append(this, comm);
		comm = null;
	}

	public void paint(double x, double y, Color c, final Color b) throws OpenReactorException {
		if (!Mode.Immediate.equals(this.mode)) {
			ExceptionThrower.throwException("GraphicsPlane.paint method works only when the plane is set to " + Mode.Immediate + " mode. Current mode is " + this.mode);
		}
		f((int)x, (int)y, c.getRGB(), b.getRGB());
	}

	public Color point(double x, double y) {
		return new Color(this.image.getRGB((int)x, (int)y));
	}
	
	@Override
	public void finish() {
		this.commandQueue.clear();
	}
	
	public void print(final String msg, double x, double y, Color c) {
		Command comm = new Command(x, y, 0, 0, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.drawString(msg, (int)x, (int)y);
			}
		};
		mode.append(this, comm);
		comm = null;
	}

	public void pset(double x, double y, Color c) {
		Command comm = new Command(x, y, 0, 0, figureOutColor(c)) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.drawLine(x, y, x, y);
				//this.image.setRGB((int)x, (int)y, c.getRGB());
			}
		};
		mode.append(this, comm);
		comm = null;
	}

	public void put(final Image image, double x, double y, double hratio,
			double vratio) {
		Command comm = new Command(x, y, 0, 0, null) {
			@Override
			void run(Graphics g) {
				g.setColor(c);
				g.drawImage(image, (int)x, (int)y, null);
			}
		};
		mode.append(this, comm);
		comm = null;
	}
	
	private Color figureOutColor(Color c) {
		Color ret = c;
		if (ret == null) {
			ret = this.fgColor;
		}
		return ret;
	}
	
	private void fillLine_(Interval interval, int y, int cc) {
		line_(interval.left(), y, interval.right(), y, new Color(cc));
	}

	/*
	 * This method is used only by paint method.
	 */
	private void line_(int x1, int y1, int x2, int y2, Color c) {
		Graphics g = this.image.getGraphics();
		g.setColor(c);
		g.drawLine(x1, y1, x2, y2);
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
	
	@Override
	protected void render_Protected(Graphics2D g) throws OpenReactorException {
		this.mode.render(this, g);
	}
	
	Interval f(int x, int y, int cc, int bb) {
		Interval interval = scanLine(x, y, cc, bb);
		if (interval != null) {
			fillLine_(interval, y, cc);
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
	public void enableAutoUpdate() {
		this.autoUpdateEnabled = true;
	}
	public void disableAutoUpdate() {
		this.autoUpdateEnabled = false;
	}
	public boolean isAutoUpdateEnabled() {
		return this.autoUpdateEnabled;
	}
}
