package oreactor.motion;

import oreactor.core.BaseEngine;
import oreactor.core.Settings;
import oreactor.motion.MotionProvider.InteractionMode;

public class MotionEngine extends BaseEngine {
	MotionProvider provider = null;

	public MotionEngine(Settings settings) {
		super(settings);
	}

	public void setProvider(MotionProvider provider) {
		this.provider = provider;
	}
	
	public void run() {
		if (provider != null) {
			provider.prepareActions();
			provider.performScavenging();
			provider.performActions();
			provider.performInteractions(InteractionMode.Collision);
			provider.performInteractions(InteractionMode.Generic);
			provider.commit();
			provider.reset();
		}
	}
}
