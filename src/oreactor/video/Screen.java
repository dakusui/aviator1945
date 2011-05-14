package oreactor.video;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import oreactor.core.Logger;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;

public class Screen extends JFrame {
	/**
	 * A serial version UID.
	 */
	private static final long serialVersionUID = -7060260135859428693L;

    protected Graphics2D graphics2d;
    
    protected Image    bImage;

	protected Settings settings;

	private List<Plane> planes;

	private boolean closed;
	
	double width;
	
	double height;

	private BufferStrategy strategy;
	
	Logger logger = Logger.getLogger(); 
	
	public Screen(Settings settings) {
    	this.settings = settings;
		this.planes = new LinkedList<Plane>();
		this.width = settings.screenSize().width();
		this.height = settings.screenSize().height();
		this.closed = false;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closed = true;
			}
		});
		this.setSize(settings.screenSize().width(), settings.screenSize().height());
		this.setVisible(true);
    	this.createBufferStrategy(2);
    	this.strategy = getBufferStrategy();
    	logger.debug("starategy-=<" + strategy + ">");
    	logger.debug("backbuffer:");
    	logger.debug("    accelerateg:" + strategy.getCapabilities().getBackBufferCapabilities().isAccelerated());
    	logger.debug("    volatile:" + strategy.getCapabilities().getBackBufferCapabilities().isTrueVolatile());
    	logger.debug("frontbuffer:");
    	logger.debug("    accelerated:" + strategy.getCapabilities().getFrontBufferCapabilities().isAccelerated());
    	logger.debug("    volatile:" + strategy.getCapabilities().getFrontBufferCapabilities().isTrueVolatile());
    	logger.debug("pageflipping:" + strategy.getCapabilities().isPageFlipping());
    	
    }
    
	@Override
	public void paint(Graphics g) {
		try {
			this.renderPlanes((Graphics2D) g);
		} catch (OpenReactorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

	public void createPlane(PlaneDesc desc) {
		Plane p = desc.createPlane(this, new Viewport(this.width, this.height));
		System.err.println("Created plane is:" + p);
		this.planes.add(p);
	}
	
	public void prepare() {
		for (Plane p: this.planes) {
			p.prepare();
		}		
	}
	
	protected void renderPlanes(Graphics2D graphics) throws OpenReactorException {
        for (Plane p: this.planes()) {
        	p.render(graphics, width, height);
        }
	}

    public void render() throws OpenReactorException {
		/////
	    // Render single frame
	    do {
	        // The following loop ensures that the contents of the drawing buffer
	        // are consistent in case the underlying surface was recreated
	        do {
	            // Get a new graphics context every time through the loop
	            // to make sure the strategy is validated
	            Graphics2D graphics = (Graphics2D) strategy.getDrawGraphics();
	    
				/////
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
    }

    public void finish() {
		List<Plane> rev = new LinkedList<Plane>();
		rev.addAll(this.planes);
		Collections.reverse(rev);
		for (Plane p: rev) {
			p.finish();
		}	
	}

	public boolean isClosed() {
		return this.closed;
	}

	public List<Plane> planes() {
		return this.planes;
	}

}
