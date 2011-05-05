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

import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.graphics.GraphicsPlane;
import oreactor.video.pattern.PatternPlane;
import oreactor.video.sprite.SpritePlane;

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

	GraphicsPlane createGraphicsPlane(String name, double width, double height) {
		GraphicsPlane ret = new GraphicsPlane(name, width, height);
		return ret;
	}
	
	SpritePlane createSpritePlane(String name, double width, double height) {
		SpritePlane ret = new SpritePlane(name, width, height);
		return ret;
	}
	
	PatternPlane createPatternPlane(String name, double width, double height, double patternwidth, double patternheight) {
		PatternPlane ret = new PatternPlane(name, width, height, patternwidth, patternheight, 256);
		return ret;
	}

	public void createPlane(PlaneDesc desc) {
		Plane p = desc.createPlane(this);
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
        	p.render(graphics);
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
