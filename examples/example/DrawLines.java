package example;

import java.awt.Color;

import oreactor.exceptions.OpenReactorException;
import oreactor.video.graphics.GraphicsPlane;
import oreactor.video.graphics.GraphicsPlane.Mode;
import oreactor.video.pattern.PatternPlane;
import mu64.Mu64Reactor;

public class DrawLines extends Mu64Reactor {
	private GraphicsPlane gg;

	@Override
	public void run() throws OpenReactorException {
		if (isFirstTime()) {
			loadConfig("example/config.json");
			gg = graphicsplane();
			gg.mode(Mode.Sticky);
			gg.priority(5);
			PatternPlane p = patternplane();
			for (int i = 0; i < p.columns(); i++) {
				for (int j = 0; j < p.rows(); j++) {
					int no = Math.abs((int)(Math.random() * 10));
					if (no < 20) {
						p.put(i, j, no);
					} else {
						p.reset(i, j);
					}
				}
			}
		} else {
			if (ticks() % 30 == 0) {
				gg.enableAutoUpdate();
			} else {
				gg.disableAutoUpdate();
			}
			double x1 = Math.random() * gg.width();
			double y1 = Math.random() * gg.height();
			double x2 = Math.random() * gg.width();
			double y2 = Math.random() * gg.height();
			int r = (int) (Math.random() * 256);
			int g = (int) (Math.random() * 256);
			int b = (int) (Math.random() * 256);
			Color c = new Color(r, g, b); 
			gg.line(x1, y1, x2, y2, c);
			c = null;
		}
		
	}
}
