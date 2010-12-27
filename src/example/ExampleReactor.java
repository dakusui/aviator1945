package example;

import java.util.Iterator;

import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.video.Plane;
import oreactor.video.Screen;
import oreactor.video.VideoEngine;

public class ExampleReactor extends Reactor {

	@Override
	protected void action(Context c) {
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		Iterator<Plane> i = s.planes();
		while (i.hasNext()) {
			
		}
	}
}
