package example;

import java.awt.Color;
import java.util.List;

import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.GraphicsPlane;
import oreactor.video.Plane;
import oreactor.video.Screen;
import oreactor.video.Screen.PlaneType;
import oreactor.video.VideoEngine;

public class ExampleReactor extends Reactor {

	@Override
	protected Settings loadSettings() throws OpenReactorException {
		System.out.println("--- *** --- *** ---");
		Settings settings = super.loadSettings();
		settings.addPlaneInfo("graphics", PlaneType.GRAPHICS);
		settings.addPlaneInfo("pattern", PlaneType.PATTTERN);
		settings.addPlaneInfo("sprite", PlaneType.SPRITE);
		return settings;
	}
	
	@Override
	protected void action(Context c) {
		GraphicsPlane g = graphicsplane(c);
		g.box(100, 100, 80, 100, Color.blue);
		g.line(10, 600, 1000, 50, Color.red);
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
}
