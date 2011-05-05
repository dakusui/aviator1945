package example;

import hobbbystation.HobbyStationReactor;

import java.awt.Color;

import oreactor.core.Context;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.video.graphics.GraphicsPlane;
import oreactor.video.pattern.Pattern;
import oreactor.video.pattern.PatternPlane;
import oreactor.video.sprite.Sprite;
import oreactor.video.sprite.SpritePlane;

public class ExampleReactor extends HobbyStationReactor {

	protected Sprite ss;
	protected Sprite tt;
	protected Sprite uu;
	protected double x = 0;

	@Override
	public Action action() {
		return new Action() {
			public void perform(Context c) throws OpenReactorException {
				SpritePlane s = spriteplane(c);
				ss = s.createSprite(spriteSpecs.get("spr00"));
				tt = s.createSprite(spriteSpecs.get("spr00"));
				uu = s.createSprite(spriteSpecs.get("spr00"));
				
				PatternPlane p= patternplane(c);
				for (Pattern pt : patterns.values()) {
					p.bind(pt);
				}
			}
			public Action next() {
				return new Action() {
					@Override
					public void perform(Context c) throws OpenReactorException {
						GraphicsPlane g = graphicsplane(c);
						g.box(100 + x, 100, 80, 100, Color.blue);
						g.line(10, 600 + x, 1000, 50, Color.red);

						PatternPlane p = patternplane(c);
						for (int i = 0; i < p.columns(); i++) {
							for (int j = 0; j < p.rows(); j++) {
								int no = Math.abs((int)(Math.random() * 40));
								if (no < 10) {
									p.put(i, j, no);
								} else {
									p.reset(i, j);
								}
							}
						}

						tt.put(x, 200, 0);
						ss.put(x + 200, x+22, (x*Math.PI/200));
						uu.put(-x + 1200, -x+600, -(x*Math.PI/200));
						x = x + 2;
						
						if (x > 512) {
							next = null;
						}
					}
				};
			}
		};
	}
	
	@Override
	protected void loadConfig(ResourceLoader loader) throws OpenReactorException {
		loader.loadConfigFromUrl("example/config.json");
	}

}
