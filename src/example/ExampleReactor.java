package example;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.io.ResourceMonitor;
import oreactor.video.GraphicsPlane;
import oreactor.video.PatternPlane;
import oreactor.video.Plane;
import oreactor.video.Screen;
import oreactor.video.Screen.PlaneType;
import oreactor.video.SpritePlane;
import oreactor.video.VideoEngine;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.Sprite;
import oreactor.video.sprite.SpriteSpec;

public class ExampleReactor extends Reactor implements ResourceMonitor {
	Map<String, SpriteSpec> spriteSpecs = new HashMap<String, SpriteSpec>();
	Map<String, Pattern> patterns = new HashMap<String, Pattern>();
	private Sprite ss;
	
	@Override
	protected Settings loadSettings() throws OpenReactorException {
		System.out.println("--- *** --- *** ---");
		Settings settings = super.loadSettings();
		settings.addPlaneInfo("graphics", PlaneType.GRAPHICS);
		settings.addPlaneInfo("pattern", PlaneType.PATTTERN);
		settings.addPlaneInfo("sprite", PlaneType.SPRITE);
		ResourceLoader loader = ResourceLoader.getResourceLoader();
		loader.addMonitor(this);
		loader.loadConfig("example/config.json");
		return settings;
	}
	@Override 
	protected Context initialize(Settings settings) {
		Context ret = super.initialize(settings);
		return ret;
	}
	@Override
	protected void action(Context c) {
		GraphicsPlane g = graphicsplane(c);
		g.box(100, 100, 80, 100, Color.blue);
		g.line(10, 600, 1000, 50, Color.red);
		
		PatternPlane p = patternplane(c);
		for (int i = 0; i < p.columns(); i++) {
			int no = Math.abs((int)(Math.random() * 10));
			for (int j = 0; j < p.rows(); j++) {
				p.put(i, j, no);
			}
		}
		
		SpritePlane s = spriteplane(c);
		if (ss == null) {
			ss = s.createSprite(spriteSpecs.get("spr00"));
		}
		ss.put(800, 200, 0);
	}
	
	protected GraphicsPlane graphicsplane(Context c) {
		return graphicsplane(c, "graphics");
	}
	
	protected GraphicsPlane graphicsplane(Context c, String name) {
		GraphicsPlane ret = null; 
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof GraphicsPlane && p.name().equals(name)) {
				ret = (GraphicsPlane)p;
			}
		}
		return ret;
	}
	protected PatternPlane patternplane(Context c) {
		return patternplane(c, "pattern");
	}
	
	protected SpritePlane spriteplane(Context c, String name) {
		SpritePlane ret = null; 
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof SpritePlane && p.name().equals(name)) {
				ret = (SpritePlane)p;
			}
		}
		return ret;
	}

	protected SpritePlane spriteplane(Context c) {
		return spriteplane(c, "sprite");
	}
	
	protected PatternPlane patternplane(Context c, String name) {
		PatternPlane ret = null; 
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof PatternPlane && p.name().equals(name)) {
				ret = (PatternPlane)p;
			}
		}
		return ret;
	}
	
	@Override
	public void numPatterns(int numPatterns) {
		// does nothing
		System.err.println("Loading " + numPatterns + " patterns.");
	}
	
	@Override
	public void patternLoaded(Pattern pattern) {
		patterns.put(pattern.name(), pattern);
		System.err.println("  Pattern:<" + pattern.name() + "> is loaded.");
	}
	
	@Override
	public void numSpriteSpecs(int numSpriteSpecs) {
		// does nothing
		System.err.println("Loading " + numSpriteSpecs + " sprite specs.");
	}
	@Override
	public void spriteSpecLoaded(SpriteSpec spriteSpec) {
		spriteSpecs.put(spriteSpec.name(), spriteSpec);
		System.err.println("  SpriteSpec:<" + spriteSpec.name() + "> is loaded.");
	}
}
