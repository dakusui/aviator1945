package splash;

import oreactor.exceptions.OpenReactorException;
import oreactor.video.sprite.ImageSpriteSpec;
import oreactor.video.sprite.ImageSpriteSpec.RenderingParameters;
import oreactor.video.sprite.Sprite;
import openreactor.mu64.Mu64Reactor;

public class TestReactor extends Mu64Reactor {
	Sprite s = null;
	public void run() throws OpenReactorException {
		if (isFirstTime()) {
			loadConfig("splash/config.json");
			s = spriteplane().createSprite(spritespec("splash"));
		} else {
			ImageSpriteSpec.RenderingParameters p = (RenderingParameters) s.renderingParameters();
			p.enableCropping(0, 0, 180, 120);
			s.put(500,300, 0);
		}
	}
}
