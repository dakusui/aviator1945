package oreactor.video;

import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import oreactor.core.Logger;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.core.Settings.VideoMode;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class Screen extends JFrame {
	/**
	 * A serial version UID.
	 */
	private static final long serialVersionUID = -7060260135859428693L;

	protected Graphics2D graphics2d;

	protected Image bImage;

	protected Settings settings;

	private List<Plane> planes;

	private boolean closed;

	int width;

	int height;

	private BufferStrategy strategy;

	Logger logger = Logger.getLogger();

	private boolean bsEnabled = true;

	private DisplayMode originalDisplayMode = null;

	private GraphicsDevice gd;

	public Screen(Reactor reactor) throws OpenReactorException {
		this.settings = reactor.settings();
		this.planes = new LinkedList<Plane>();
		this.width = reactor.screenWidth();
		this.height = reactor.screenHeight();
		this.closed = false;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closed = true;
			}
		});
		this.setSize(reactor.screenWidth(), reactor.screenHeight());
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		VideoMode vm = reactor.settings().videoMode();
		if (VideoMode.FULL.equals(vm) || VideoMode.FULL_FALLBACK.equals(vm)) {
			boolean succeeded = false;
			this.originalDisplayMode = null;
			this.gd = ge.getDefaultScreenDevice();
			try {
				this.originalDisplayMode = gd.getDisplayMode();
				gd.setFullScreenWindow(this);
				gd.setDisplayMode(new DisplayMode(reactor.screenWidth(), reactor.screenHeight(), reactor.screenColorDepth(), reactor.screenRefreshRate()));
				this.setUndecorated(true);
				succeeded = true;
			} catch (UnsupportedOperationException e) {
				handleException(vm, e);
			} catch (RuntimeException e) {
				handleException(vm, e);
			} finally {
				if (!succeeded) {
					gd.setFullScreenWindow(null);
					// iff. this object succeeds to go to full screen mode, 
					// this.originalDisplayMode is set to non-null.
					this.originalDisplayMode = null;
					this.gd = null;
				}
			}
		} else {
			logger.debug("Normal screen mode is selected.");
		}
		this.setVisible(true);
		this.createBufferStrategy(2);
		this.strategy = getBufferStrategy();
		logger.debug("starategy-=<" + strategy + ">");
		logger.debug("backbuffer:");
		logger.debug("    accelerated:"
				+ strategy.getCapabilities().getBackBufferCapabilities()
						.isAccelerated());
		logger.debug("    volatile:"
				+ strategy.getCapabilities().getBackBufferCapabilities()
						.isTrueVolatile());
		logger.debug("frontbuffer:");
		logger.debug("    accelerated:"
				+ strategy.getCapabilities().getFrontBufferCapabilities()
						.isAccelerated());
		logger.debug("    volatile:"
				+ strategy.getCapabilities().getFrontBufferCapabilities()
						.isTrueVolatile());
		logger.debug("pageflipping:"
				+ strategy.getCapabilities().isPageFlipping());

		for (GraphicsDevice gd : ge.getScreenDevices()) {
			boolean isDefault = false;
			if (gd == ge.getDefaultScreenDevice()) {
				isDefault = true;
			}

			logger.debug("====");
			String s = "";
			if (isDefault) {
				s = "(default)";
			}
			
			logger.debug("ID:" + gd.getIDstring() + s);
			logger.debug("----");
			logger.debug("Full Screen Supported:" + gd.isFullScreenSupported());
			logger.debug("Display Change Supported:" + gd.isDisplayChangeSupported());
			logger.debug("Accelerated Memory:" + gd.getAvailableAcceleratedMemory());
			logger.debug("Modes:");
			for (DisplayMode dm : gd.getDisplayModes()) {
				logger.debug("    (w,h,d,rr)=(" + dm.getWidth() + "," + dm.getHeight() + "," + dm.getBitDepth() + "," + dm.getRefreshRate() + ")");
			}
		}
	}

	private void handleException(VideoMode vm, Exception e)
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
	
	@Override
	public void paint(Graphics g) {
		try {
			this.renderPlanes((Graphics2D) g);
		} catch (OpenReactorException e) {
			logger.debug("Rendering failed:" + e.getMessage());
			for (StackTraceElement st : e.getStackTrace())
			logger.debug("---- " + st);
		}
	}

	public void createPlane(PlaneDesc desc) {
		Plane p = desc.createPlane(this, new Viewport(this.width, this.height));
		logger.debug("Created plane is:<" + p + ">");
		this.planes.add(p);
	}

	public void prepare() {
		Collections.sort(this.planes);
		for (Plane p : this.planes) {
			if (p.isEnabled()) {
				p.prepare();
			}
		}
	}

	protected void renderPlanes(Graphics2D graphics)
			throws OpenReactorException {
		for (Plane p : this.planes()) {
			if (p.isEnabled()) {
				p.render(graphics, width, height);
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
			Graphics2D gg = (Graphics2D) this.getGraphics();
			gg.drawImage(vImg, 0, 0, (int)width, (int)height, 0, 0, (int)width, (int)height, null);
			gg.dispose();
		}
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

	public boolean isClosed() {
		return this.closed;
	}

	public List<Plane> planes() {
		return this.planes;
	}

	public void terminate() {
		// iff. this object succeeds to go to full screen mode, 
		// this.originalDisplayMode is set to non-null.
		if (originalDisplayMode != null) {
			gd.setDisplayMode(originalDisplayMode);
			gd.setFullScreenWindow(null);
			this.setUndecorated(false);
			this.originalDisplayMode = null;
			this.gd = null;
		}
	}

}
