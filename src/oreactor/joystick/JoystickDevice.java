package oreactor.joystick;

import java.io.IOException;

import oreactor.joystick.JoystickEngine.Stick;
import oreactor.joystick.JoystickEngine.Trigger;

import com.centralnexus.input.Joystick;

abstract class JoystickDevice extends BaseInputDevice {
	Joystick device;
	protected int buttons;
	
	public JoystickDevice(JoystickEngine.Type type) throws IOException {
		super(type);
		device = Joystick.createInstance();
	}
	
	protected enum VDir {
		Up, Down, Neutral
	}
	protected enum HDir {
		Right, Left, Neutral
	}
	
	public Stick stick() {
		Stick ret = Stick.NEUTRAL;
		VDir v = vDirection();
		if (VDir.Up.equals(v)) {
			HDir h = hDirection();
			if (h == HDir.Left) {
				ret = Stick.UP_LEFT;
			} else if (h == HDir.Right) {
				ret = Stick.UP_RIGHT;
			} else {
				ret = Stick.UP;
			}
		} else if (VDir.Down.equals(v)) {
			HDir h = hDirection();
			if (h == HDir.Left) {
				ret = Stick.DOWN_LEFT;
			} else if (h == HDir.Right) {
				ret = Stick.DOWN_RIGHT;
			} else {
				ret = Stick.DOWN;
			} 
		} else {
			HDir h = hDirection();
			if (h == HDir.Left) {
				ret = Stick.LEFT;
			} else if (h == HDir.Right) {
				ret = Stick.RIGHT;
			} 
		}
		return ret;
	}

 	protected abstract VDir vDirection();

	protected abstract HDir hDirection(); 
 	public abstract boolean trigger(Trigger trigger);

	public void poll() {
		device.poll();
		buttons = device.getButtons();
	}

}
