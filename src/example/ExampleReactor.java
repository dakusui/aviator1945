package example;

import java.util.Iterator;

import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.video.Plane;
import oreactor.video.Screen;
import oreactor.video.Screen.PlaneType;
import oreactor.video.VideoEngine;

public class ExampleReactor extends Reactor {
	public ExampleReactor() {
		
	}
	
	@Override
	protected Settings customize(Settings settings) {
		settings.addPlaneType(PlaneType.GRAPHICS);
		settings.addPlaneType(PlaneType.PATTTERN);
		settings.addPlaneType(PlaneType.SPRITE);
		return settings;
	}
	
	@Override
	protected void action(Context c) {
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		Iterator<Plane> i = s.planes();
		while (i.hasNext()) {
			
		}
	}
}
