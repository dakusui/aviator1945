package example;


import java.awt.Color;

import openreactor.nu64.Nu64Reactor;
import openreactor.nu64.Nu64ResourceLoader;
import oreactor.core.Context;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.JoystickEngine.Stick;
import oreactor.video.graphics.GraphicsPlane;
import oreactor.video.pattern.Pattern;
import oreactor.video.pattern.PatternPlane;
import oreactor.video.sprite.Sprite;
import oreactor.video.sprite.SpritePlane;

public class ExampleReactor extends Nu64Reactor {

	protected Sprite ss;
	protected Sprite tt;
	protected Sprite uu;
	protected double x = 0;
	protected double y = 0;

	@Override
	public Action action() {
		return new Action() {
			public void perform(Context c) throws OpenReactorException {
				Nu64ResourceLoader loader = (Nu64ResourceLoader) c.getResourceLoader();
				loader.loadConfigFromUrl("example/config.json");
				SpritePlane s = spriteplane(c);
				ss = s.createSprite(spriteSpecs.get("spr00"));
				tt = s.createSprite(spriteSpecs.get("spr00"));
				uu = s.createSprite(spriteSpecs.get("spr00"));
				
				PatternPlane p= patternplane(c);
				for (Pattern pt : patterns.values()) {
					p.bind(pt);
				}
				p.viewport().offset(100, 100);
				//p.viewport().i((1024-32)/1, (32)/1);
				//p.viewport().j((-32)/1, (768-32)/1);
			}
			public Action next() {
				return new Action() {
					private int counter;

					@Override
					public void perform(Context c) throws OpenReactorException {
						GraphicsPlane g = graphicsplane(c);
						g.box(100 + x, 100, 80, 100, Color.blue);
						//g.paint(105, 90, Color.pink, Color.blue);
						g.line(10, 600 + x, 1000, 50, Color.red);

						/*
						PatternPlane p = patternplane(c);
						for (int i = 0; i < p.columns(); i++) {
							for (int j = 0; j < p.rows(); j++) {
								int no = Math.abs((int)(Math.random() * 100));
								if (no < 10) {
									p.put(i, j, no);
								} else {
									p.reset(i, j);
								}
							}
						}
						*/

						tt.put(x, 200 + y, 0);
						ss.put(x + 200, x+22 - y, (x*Math.PI/200));
						uu.put(-x + 1200 + y, -x+600, -(x*Math.PI/200));
						Stick s = stick(c);
						if (s != null) {
							int xsig = s.x();
							int ysig = s.y();
							x = x + 2 * xsig;
							y = y + 2 * ysig;
						}
						if (trigger(c)) {
							if ( counter <= 0 ) {
								playwave(c, "spell");
								counter = 5;
							}
						}
						if (counter > 0) {
							counter--;
						}
						g.print("hello world", 10 + x, 400 - y, Color.green);
					}
				};
			}
		};
	}
}
