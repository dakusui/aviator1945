package example;

import java.awt.BufferCapabilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class BSExample extends JFrame {

	BSExample(GraphicsConfiguration gc) {
		super(gc);
		// TODO Auto-generated constructor stub
	}

	public BSExample() {
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	public static void _main(String[] args) {
		// Check the capabilities of the GraphicsConfiguration
		GraphicsConfiguration gc = null;
		// Create our component
		Window w = new BSExample();
		gc = w.getGraphicsConfiguration();
		gc.getBufferCapabilities();
		System.out.println(gc.getImageCapabilities().isAccelerated());
		System.out.println(gc.getImageCapabilities().isTrueVolatile());
		
		
		// Show our window
		w.setVisible(true);
		w.setSize(600, 400);
		
		// Create a general double-buffering strategy
		w.createBufferStrategy(2);
		BufferStrategy strategy = w.getBufferStrategy();

		BufferCapabilities capabilities = strategy.getCapabilities(); 
		System.out.println("back:accelarated:" + capabilities.getBackBufferCapabilities().isAccelerated() );
		System.out.println("back:volatile:" + capabilities.getBackBufferCapabilities().isTrueVolatile() );
		System.out.println("front:accelarated:" + capabilities.getFrontBufferCapabilities().isAccelerated() );
		System.out.println("front:volatile:" + capabilities.getFrontBufferCapabilities().isTrueVolatile() );
		System.out.println("flipcontents:" + capabilities.getFlipContents() );
		System.out.println("flipping:" + capabilities.isPageFlipping() );
		System.out.println("multi buffer:" + capabilities.isMultiBufferAvailable() );
		System.out.println("full screen required:" + capabilities.isFullScreenRequired() );
		boolean done = false;
		// Main loop
		int i = 0;
		while (!done) {
		    // Prepare for rendering the next frame
		    // ...
		    // Render single frame
		    do {
		        // The following loop ensures that the contents of the drawing buffer
		        // are consistent in case the underlying surface was recreated
		        do {
		            // Get a new graphics context every time through the loop
		            // to make sure the strategy is validated
		            Graphics graphics = strategy.getDrawGraphics();
		    
		            // Render to graphics
		            graphics.setColor(Color.black);
		            graphics.fillRect(0, 0, 600, 400);
		            graphics.setColor(Color.white);
					graphics.drawString("<" + i + ">", 200, 200);
		          	i++;
		          	
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

		// Dispose the window
		w.setVisible(false);
		w.dispose();
	}
	public static void main(String[] args) {
		// Check the capabilities of the GraphicsConfiguration
		GraphicsConfiguration gc = null;
		// Create our component
		Window w = new BSExample();
		gc = w.getGraphicsConfiguration();
		gc.getBufferCapabilities();
		
		
		// Show our window
		w.setVisible(true);
		w.setSize(600, 400);
		
		// Create a general double-buffering strategy
		w.createBufferStrategy(2);
		BufferStrategy strategy = w.getBufferStrategy();

		boolean done = false;
		// Main loop
		int i = 0;
		while (!done) {
			/////
			// <prepare>
		    // Prepare for rendering the next frame
		    // ...
			
			/////
			// <render>:screen level
		    // Render single frame
		    do {
		        // The following loop ensures that the contents of the drawing buffer
		        // are consistent in case the underlying surface was recreated
		        do {
		            // Get a new graphics context every time through the loop
		            // to make sure the strategy is validated
		            Graphics graphics = strategy.getDrawGraphics();
		    
					/////
		            // <render>:plane level
		            // Render to graphics
		            graphics.setColor(Color.black);
		            graphics.fillRect(0, 0, 600, 400);
		            graphics.setColor(Color.white);
					graphics.drawString("<" + i + ">", 200, 200);
		          	i++;
		          	
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

		// Dispose the window
		w.setVisible(false);
		w.dispose();
	}
		
}
