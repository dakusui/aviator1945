package oreactor.motion;

import oreactor.motion.MotionProvider.InteractionMode;

public class MotionEngine {
	MotionProvider provider = null;
	public void run() {
		provider.prepareActions();
		provider.performScavenging();
		provider.performActions();
		provider.performInteractions(InteractionMode.Collision);
		provider.performInteractions(InteractionMode.Generic);
		provider.commit();
		provider.reset();
	}
}
