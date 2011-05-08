package oreactor.video;

import java.awt.event.KeyListener;

import oreactor.core.BaseEngine;
import oreactor.core.Context;
import oreactor.core.Settings;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.InputDevice;

public class VideoEngine extends BaseEngine {
	private Screen screen;

	public VideoEngine(Settings settings) {
		super(settings);
		this.screen = new Screen(settings);
		this.screen.setVisible(true);
	}
	
	@Override 
	public void initialize(Context c) throws OpenReactorException {
		super.initialize(c);
		for (InputDevice d : c.getJoystickEngine().devices()) {
			if (d instanceof KeyListener) {
				this.screen.addKeyListener((KeyListener) d);
			}
		}
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
