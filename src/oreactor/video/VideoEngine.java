package oreactor.video;

import java.awt.event.KeyListener;
import java.util.List;

import javax.swing.JApplet;

import oreactor.core.BaseEngine;
import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.core.Settings.WindowMode;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.InputDevice;
import oreactor.video.sprite.Sprite;

public class VideoEngine extends BaseEngine {
	private VideoDevice device;

	public VideoEngine(Reactor reactor) {
		super(reactor);
	}
	
	@Override 
	public void initialize(Context c) throws OpenReactorException {
		super.initialize(c);
		Reactor reactor = reactor();
		Settings settings = reactor.settings();
		JApplet applet = settings.applet();
		if (WindowMode.Applet.equals(settings.windowMode()) && applet != null) {
			this.device = VideoDevice.createJAppletBsedVideoDevice(reactor(), applet);
		} else if (WindowMode.Frame.equals(settings.windowMode())) {
			this.device = VideoDevice.createJFrameBasedVideoDevice(reactor());
		}
		this.device.initialize();
		for (InputDevice d : c.getJoystickEngine().devices()) {
			if (d instanceof KeyListener) {
				this.device.addKeyListener((KeyListener) d);
			}
		}
		List<PlaneDesc> planeDescs = reactor().planeInfoItems();
		logger().debug("List of plane info items:<" + planeDescs + ">");
		for (PlaneDesc desc : planeDescs) {
			this.device.createPlane(desc);
		}
		////
		// To guarantee the affine transform matrices are updated, 
		// issue setSize method on the device object.
		this.device.setSize(this.device.width(), this.device.height());
	}

	@Override
	public void prepare() throws OpenReactorException {
		device.prepare();
	}
	
	@Override
	public void run() throws OpenReactorException {
		device.render();
		if (this.device.isClosed()) {
			ExceptionThrower.throwWindowClosedException();
		}
	}
	
	@Override
	public void finish() throws OpenReactorException {
		device.finish();
	}

	public void terminate(Context c) throws OpenReactorException {
		device.terminate();
		VideoUtil.flush();
		Sprite.resetDefaultPriorityCounter();
		this.device = null;
	}

	public VideoDevice device() {
		return this.device;
	}
}
