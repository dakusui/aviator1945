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

public class Screen extends JFrame {
	public static enum PlaneType {
		GRAPHICS {
			@Override
			public Plane createPlane(Screen screen, String name, double width, double height) {
				return screen.createGraphicsPlane(name, width, height);
			}
		}, 
		PATTTERN {
			@Override
			public Plane createPlane(Screen screen, String name, double width, double height) {
				return screen.createPatternPlane(name, width, height);
			}
		}, 
		SPRITE {
			@Override
			public Plane createPlane(Screen screen, String name, double width, double height) {
				return screen.createSpritePlane(name, width, height);
			}
		};
		public abstract Plane createPlane(Screen screen, String name, double width, double height);
	}
	
	public static class PlaneInfo {
		private String name;
		private PlaneType type;
		double width, height;
		
		public PlaneInfo(String name, PlaneType type, double width, double height) {
			this.name = name;
			this.type = type;
			this.width = width;
			this.height = height;
		}
		
		public String name() {
			return this.name;
		}
		
		public PlaneType type() {
			return this.type;
		}
		
		public String toString() {
			return "Plane:" + this.name + "(" + this.type() + ")";
		}
	}
	
	
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
		this.renderPlanes((Graphics2D) g);
    }

	GraphicsPlane createGraphicsPlane(String name, double width, double height) {
		GraphicsPlane ret = new GraphicsPlane(name, width, height);
		return ret;
	}
	
	SpritePlane createSpritePlane(String name, double width, double height) {
		SpritePlane ret = new SpritePlane(name, width, height);
		return ret;
	}
	
	PatternPlane createPatternPlane(String name, double width, double height) {
		PatternPlane ret = new PatternPlane(name, width, height, 32, 32, 256);
		return ret;
	}

	public void createPlane(PlaneInfo info) {
		System.out.println("Creating a plane:" + info);
		Plane p = info.type().createPlane(this, info.name(), info.width, info.height);
		System.out.println("Created plane is:" + p);
		this.planes.add(p);
		System.out.println("Registered planes:" + this.planes);
	}
	
	public void prepare() {
		for (Plane p: this.planes) {
			p.prepare();
		}		
	}
	
	protected void renderPlanes(Graphics2D graphics) {
        for (Plane p: this.planes()) {
        	p.render(graphics);
        }
	}

    public void render() {
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
