package oreactor.video;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JApplet;
import javax.swing.JFrame;

import oreactor.core.Logger;
import oreactor.core.Reactor;
import oreactor.core.Settings.VideoMode;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public abstract class VideoDevice {
	static final Logger logger = Logger.getLogger();

	public static VideoDevice createJAppletBsedVideoDevice(Reactor reactor,
			final JApplet applet) throws OpenReactorException {
		VideoDevice ret = new VideoDevice(reactor) {
			JApplet applet_ = applet;
			@Override
			protected BufferStrategy _createBufferStrategy() {
				// does nothing
				return null;
			}

			@Override
			protected void _initialize() throws OpenReactorException {
				this.applet_.setSize(reactor.screenWidth(), reactor.screenHeight());
				//this.applet_.getRootPane().setSize(reactor.screenWidth(), reactor.screenHeight());
				disableBufferStrategy();
			}

			@Override
			public void addKeyListener(KeyListener d) {
				this.applet_.addKeyListener(d);
			}

			@Override
			protected VolatileImage createVolatileImage(int width, int height) {
				return this.applet_.createVolatileImage(width, height);
			}

			@Override
			protected Graphics2D getGraphics2D() {
				return (Graphics2D) this.applet_.getGraphics();
			}

			@Override
			protected void terminate() {
				// does nothing;
			}

		};
		return ret;
	}

	public static VideoDevice createJFrameBasedVideoDevice(Reactor reactor)
			throws OpenReactorException {
		VideoDevice ret = new VideoDevice(reactor) {
			JFrame frame = new JFrame();

			GraphicsDevice gd;

			DisplayMode originalDisplayMode = null;

			@Override
			public void _initialize() throws OpenReactorException {
				this.frame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						close();
					}
				});
				this.frame.setSize(reactor.screenWidth(), reactor
						.screenHeight());
				GraphicsEnvironment ge = GraphicsEnvironment
						.getLocalGraphicsEnvironment();
				VideoMode vm = reactor.settings().videoMode();
				if (VideoMode.FULL.equals(vm)
						|| VideoMode.FULL_FALLBACK.equals(vm)) {
					boolean succeeded = false;
					this.originalDisplayMode = null;
					this.gd = ge.getDefaultScreenDevice();
					DisplayMode dm = new DisplayMode(reactor.screenWidth(),
							reactor.screenHeight(), reactor.screenColorDepth(),
							reactor.screenRefreshRate());
					try {
						this.originalDisplayMode = gd.getDisplayMode();
						if (gd.isFullScreenSupported()) {
							gd.setFullScreenWindow(this.frame);
							if (gd.isFullScreenSupported()) {
								if (!dm.equals(gd.getDisplayMode())) {
									if (gd.isDisplayChangeSupported()) {
										gd.setDisplayMode(dm);
										this.frame.setUndecorated(true);
										succeeded = true;
									} else {
										throw new UnsupportedOperationException();
									}
								}
							}
						}
					} catch (UnsupportedOperationException e) {
						handleException(vm, e);
					} catch (RuntimeException e) {
						handleException(vm, e);
					} finally {
						if (!succeeded) {
							gd.setFullScreenWindow(null);
							// iff. this object succeeds to go to full screen
							// mode,
							// this.originalDisplayMode is set to non-null.
							this.originalDisplayMode = null;
							this.gd = null;
						}
					}
				} else {
					logger.debug("Normal screen mode is selected.");
				}
				this.frame.setVisible(true);
				enableBufferStrategy();
			}

			@Override
			public void addKeyListener(KeyListener d) {
				this.frame.addKeyListener(d);
			}

			@Override
			public VolatileImage createVolatileImage(int width, int height) {
				return frame.createVolatileImage(width, height);
			}

			@Override
			public Graphics2D getGraphics2D() {
				return (Graphics2D) this.frame.getGraphics();
			}

			public void terminate() {
				// iff. this object succeeds to go to full screen mode,
				// this.originalDisplayMode is set to non-null.
				if (originalDisplayMode != null) {
					gd.setDisplayMode(originalDisplayMode);
					gd.setFullScreenWindow(null);
					frame.setUndecorated(false);
					this.originalDisplayMode = null;
					this.gd = null;
				}
			}

			@Override
			protected BufferStrategy _createBufferStrategy() {
				this.frame.createBufferStrategy(2);
				return this.frame.getBufferStrategy();
			}

			void handleException(VideoMode vm, Exception e)
					throws OpenReactorException {
				if (VideoMode.FULL.equals(vm)) {
					String msg = "Failed to go to full screen mode: Quitting.";
					logger.info(msg);
					logger.debug(msg, e);
					ExceptionThrower.throwVideoException(msg, e);
				} else if (VideoMode.FULL_FALLBACK.equals(vm)) {
					String msg = "Failed to go to full screen mode: Falling back.";
					logger.info(msg);
					logger.debug(msg, e);
				} else {
				}
			}
		};
		return ret;
	}

	private boolean bsEnabled = true;

	private boolean closed;

	private int height;

	private BufferStrategy strategy;

	private int width;

	List<Plane> planes;

	Reactor reactor;

	public VideoDevice(Reactor reactor) throws OpenReactorException {
		this.reactor = reactor;
		this.planes = new LinkedList<Plane>();
		this.width = reactor.screenWidth();
		this.height = reactor.screenHeight();
		this.closed = false;
	}

	/*
	 * @Override public void paint(Graphics g) { try {
	 * this.renderPlanes((Graphics2D) g); } catch (OpenReactorException e) {
	 * logger.debug("Rendering failed:" + e.getMessage()); for
	 * (StackTraceElement st : e.getStackTrace()) logger.debug("---- " + st); }
	 * }
	 */

	public abstract void addKeyListener(KeyListener d);

	public void close() {
		this.closed = true;
	}

	public void createPlane(PlaneDesc desc) {
		Plane p = desc.createPlane(new Viewport(this.width, this.height));
		logger.debug("Created plane is:<" + p + ">");
		this.planes.add(p);
	}

	public void enableBufferStrategy() {
		this.bsEnabled = true;
	}
	
	public void disableBufferStrategy() {
		this.bsEnabled = false;
	}
	
	public void finish() {
		List<Plane> rev = new LinkedList<Plane>();
		rev.addAll(this.planes);
		Collections.reverse(rev);
		for (Plane p : rev) {
			if (p.isEnabled()) {
				p.finish();
			}
		}
	}

	public void initialize() throws OpenReactorException {
		_initialize();
		if (isBufferStrategyEnabled()) {
			strategy = _createBufferStrategy();
		}
	}

	public boolean isBufferStrategyEnabled() {
		return this.bsEnabled;
	}
	public boolean isClosed() {
		return this.closed;
	}

	public List<Plane> planes() {
		return this.planes;
	}

	public void prepare() {
		Collections.sort(this.planes);
		for (Plane p : this.planes) {
			if (p.isEnabled()) {
				p.prepare();
			}
		}
	}

	public void render() throws OpenReactorException {
		// ///
		// Render single frame
		if (bsEnabled) {
			do {
				// The following loop ensures that the contents of the drawing
				// buffer
				// are consistent in case the underlying surface was recreated
				do {
					// Get a new graphics context every time through the loop
					// to make sure the strategy is validated
					Graphics2D graphics = (Graphics2D) strategy
							.getDrawGraphics();

					// ///
					// Render all the planes.
					this.renderPlanes(graphics);

					// Dispose the graphics
					graphics.dispose();
					// Repeat the rendering if the drawing buffer contents
					// were restored
				} while (strategy.contentsRestored());

				// Display the buffer
				strategy.show();

				// Repeat the rendering if the drawing buffer was lost
			} while (strategy.contentsLost());
		} else {
			VolatileImage vImg = createVolatileImage((int) width, (int) height);
			Graphics2D graphics = vImg.createGraphics();
			do {
				this.renderPlanes(graphics);
				graphics.dispose();
			} while (vImg.contentsLost());
			Graphics2D gg = this.getGraphics2D();
			gg.drawImage(vImg, 0, 0, (int) width, (int) height, 0, 0,
					(int) width, (int) height, null);
			gg.dispose();
		}
	}

	protected abstract BufferStrategy _createBufferStrategy();

	protected abstract void _initialize() throws OpenReactorException;

	protected abstract VolatileImage createVolatileImage(int width, int height);

	protected abstract Graphics2D getGraphics2D();

	protected void renderPlanes(Graphics2D graphics)
			throws OpenReactorException {
		for (Plane p : this.planes()) {
			if (p.isEnabled()) {
				p.render(graphics, width, height);
			}
		}
	}

	protected abstract void terminate();
}
