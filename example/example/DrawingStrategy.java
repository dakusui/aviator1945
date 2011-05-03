package example;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public abstract class DrawingStrategy {
	static class BufferedImageStrategy extends DrawingStrategy {
		protected BufferedImageStrategy(ExampleFrame f) {
			super(f);
		}

		@Override
		public void flip() {
			((Graphics2D)this.renderableScreen.getGraphics()).drawImage(
					this.offscreenImage, 
					0, 0, 
					0, 0, 
					0, 0, 
					0, 0, 
					//dx2, dy2, 
					//sx1, sy1, 
					//sx2, sy2, 
					null);
		}

		@Override
		protected Image createOffScreenImage() {
			// TODO Auto-generated method stub
			return new BufferedImage(0,0,0);
		}
		
	};
	
	static class VolatileImageStrategy extends DrawingStrategy {
		protected VolatileImageStrategy(ExampleFrame f) {
			super(f);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void flip() {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected Image createOffScreenImage() {
			// TODO
			return new BufferedImage(0,0,0);
		}
		
	};
	
	protected Image offscreenImage = null;
	protected ExampleFrame renderableScreen;
	
	protected DrawingStrategy(ExampleFrame f) {
		this.renderableScreen = f;
		this.offscreenImage = createOffScreenImage();
	}

	abstract protected Image createOffScreenImage();

	public abstract void flip();

	protected void renderOffScreen(Graphics2D gg) {
		this.renderableScreen.render(gg);
	}
}
