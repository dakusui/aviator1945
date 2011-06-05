package mu64.motion;

import mu64.motion.MotionProvider.InteractionMode;
import oreactor.core.BaseEngine;
import oreactor.core.Settings;

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
			provider.putSprites();
			provider.reset();
		}
	}

	public MotionProvider getMotionProvider() {
		return this.provider;
	}
}
