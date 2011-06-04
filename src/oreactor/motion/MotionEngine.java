package oreactor.motion;

import java.util.List;

import oreactor.motion.MotionProvider.InteractionMode;

public class MotionEngine {
	MotionProvider provider = null;
	public void run() {
		provider.performInteraction(InteractionMode.Collision);
		provider.performInteraction(InteractionMode.Generic);
		List<MMachine> mmachines = this.provider.machines();
		
		for (MMachine m : mmachines) {
			m.performAction(this.provider);
		}

		for (MMachine m : mmachines) {
			m.commit();
		}
		
		provider.reset();
}
}
