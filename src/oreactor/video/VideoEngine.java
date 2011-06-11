package oreactor.video;

import java.awt.event.KeyListener;
import java.util.List;

import oreactor.core.BaseEngine;
import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.InputDevice;
import oreactor.video.sprite.Sprite;

public class VideoEngine extends BaseEngine {
	private Screen screen;

	public VideoEngine(Reactor reactor) {
		super(reactor);
	}
	
	@Override 
	public void initialize(Context c) throws OpenReactorException {
		super.initialize(c);
		this.screen = new Screen(this.reactor());
		this.screen.setVisible(true);
		for (InputDevice d : c.getJoystickEngine().devices()) {
			if (d instanceof KeyListener) {
				this.screen.addKeyListener((KeyListener) d);
			}
		}
		List<PlaneDesc> planeDescs = reactor().planeInfoItems();
		logger().debug("List of plane info items:<" + planeDescs + ">");
		for (PlaneDesc desc : planeDescs) {
			this.screen.createPlane(desc);
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

	public void terminate(Context c) throws OpenReactorException {
		screen.terminate();
		VideoUtil.flush();
		Sprite.resetDefaultPriorityCounter();
		this.screen = null;
	}

	public Screen screen() {
		return this.screen;
	}
}
