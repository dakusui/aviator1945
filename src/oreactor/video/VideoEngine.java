package oreactor.video;

import oreactor.core.BaseEngine;
import oreactor.core.Settings;

public class VideoEngine extends BaseEngine {
	private Screen screen;

	public VideoEngine(Settings settings) {
		super(settings);
		this.screen = new Screen(settings);
	}

	@Override
	public void run() {
		screen.render();
	}
}
