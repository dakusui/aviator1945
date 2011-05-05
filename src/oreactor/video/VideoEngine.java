package oreactor.video;

import oreactor.core.BaseEngine;
import oreactor.core.Settings;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class VideoEngine extends BaseEngine {
	private Screen screen;

	public VideoEngine(Settings settings) {
		super(settings);
		this.screen = new Screen(settings);
		this.screen.setVisible(true);
	}
	
	@Override
	public void prepare() throws OpenReactorException {
		screen.prepare();
	}
	
	@Override
	public void run() throws OpenReactorException {
		screen.render();
		if (this.screen.isClosed()) {
			ExceptionThrower.throwWindowClosedException();
		}
	}
	
	@Override
	public void finish() throws OpenReactorException {
		screen.finish();
	}
	
	public Screen screen() {
		return this.screen;
	}
}
