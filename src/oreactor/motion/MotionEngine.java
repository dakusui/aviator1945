package oreactor.motion;

import oreactor.motion.MotionProvider.InteractionMode;

public class MotionEngine {
	MotionProvider provider = null;
	public void run() {
		provider.prepareAction();
		provider.performScavenging();
		provider.performAction();
		provider.performInteraction(InteractionMode.Collision);
		provider.performInteraction(InteractionMode.Generic);
		provider.commit();
		provider.reset();
	}
}
