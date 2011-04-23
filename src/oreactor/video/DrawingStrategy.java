package oreactor.video;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Iterator;
import java.util.List;

public enum DrawingStrategy {
	Buffered {

		@Override
		public void draw(Drawable d) {
			Image offscreenBuffer = d.offscreenBuffer();
			Graphics2D gg = (Graphics2D) offscreenBuffer.getGraphics();
			List<Plane> planes = d.planes();
			Iterator<Plane> i = planes.iterator();
			while (i.hasNext()) {
				Plane cur = i.next();
				cur.render(gg);
			}
		}

		@Override
		protected void flipEngine(Drawable d) {
			//Graphics2D gg = d.onscreenBuffer();
			//gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
			
		}
		
	},
	Volatile {
		@Override
		public void draw(Drawable d) {
			// Graphics2D gg = d.drawableBuffer();
			// gg.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, observer);
		}

		@Override
		protected void flipEngine(Drawable d) {
		}
	};
	
	public abstract void draw(Drawable d);
	protected abstract void flipEngine(Drawable d);
	public void flip(Drawable d) {
		flipEngine(d);
		d.flipped();
	}
}
