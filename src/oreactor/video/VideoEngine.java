package oreactor.video;

import oreactor.core.BaseEngine;
import oreactor.core.Settings;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorWindowClosedException;

public class VideoEngine extends BaseEngine {
	private Screen screen;

	public VideoEngine(Settings settings) {
		super(settings);
		this.screen = new Screen(settings);
		this.screen.setSize(settings.screenSize().width(), settings.screenSize().height());
		this.screen.setVisible(true);
	}

	@Override
	public void prepare() {
		super.prepare();
	}
	
	@Override
	public void run() throws OpenReactorWindowClosedException {
		screen.render();
		if (this.screen.isClosed()) {
			ExceptionThrower.throwWindowClosedException();
		}
	}
	
	public Screen screen() {
		return this.screen;
	}
}
