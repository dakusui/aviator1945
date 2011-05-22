package splash;

import java.awt.Color;

import openreactor.mu64.Mu64Reactor;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.sprite.ImageSpriteSpec;
import oreactor.video.sprite.ImageSpriteSpec.RenderingParameters;
import oreactor.video.sprite.Sprite;
import oreactor.video.sprite.SpritePlane;

public class SplashReactor extends Mu64Reactor {
	Sprite[] lines = new Sprite[120];
	double[] factors = new double[lines.length];
	double frames = 64;
	@Override
	public void run() throws OpenReactorException {
		SpritePlane spriteplane = spriteplane();
		if (isFirstTime()) {
			graphicsplane().boxfill(0, 0, 1024, 768, Color.BLUE);
			loadConfig("splash/config.json");
			for (int i = 0; i < lines.length; i ++) {
				lines[i] = spriteplane.createSprite(spritespec("splash"));
				ImageSpriteSpec.RenderingParameters param =(RenderingParameters) lines[i].renderingParameters();
				param.enableCropping(0, i, 320, 2);
				factors[i] = Math.random() * 10 + 1;
			}
		}
		if (ticks() <= frames) {
			for (int i = 0; i < lines.length; i ++) {
				int sig = i % 2 == 1 ? 1 : -1;
				lines[i].put(512 + (frames - ticks()) * 8 * sig * factors[i], 308 + i, 0);
			}
			double th = ((frames - ticks())/frames) * Math.PI /4;
			double c = 3;
			double o = 128; 
			double cos = Math.cos(th);
			double sin = Math.sin(th);
			double ox = ticks() * o / frames;
			double oy = ticks() * o / frames;
			spriteplane.viewport().offset(ox, oy);
			spriteplane.viewport().i( cos*(1024 - ox*c), -sin*(768 -oy*c));
			spriteplane.viewport().j( sin*(768 - oy*c), cos*(1024 - ox*c));
		} else {
			graphicsplane().print("Mu=64: Handy programming platform", 384, 400, Color.white);
			exit();
		}
	}
}
