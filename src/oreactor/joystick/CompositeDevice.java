package oreactor.joystick;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oreactor.joystick.JoystickEngine.Stick;
import oreactor.joystick.JoystickEngine.Trigger;

class CompositeDevice implements InputDevice {
	private List<InputDevice> devices;

	public CompositeDevice() {
		devices = new LinkedList<InputDevice>();
	}
	public void add(InputDevice device) {
		devices.add(device);
	}
	
	public void poll() {
		Iterator<InputDevice> iDevices = devices.iterator();
		while (iDevices.hasNext()) {
			iDevices.next().poll();
		}
	}

	public Stick stick() {
		Iterator<InputDevice> iDevices = devices.iterator();
		while (iDevices.hasNext()) {
			Stick ret = iDevices.next().stick(); 
			if (ret != Stick.NEUTRAL) {
				return ret;
			}
		}
		return Stick.NEUTRAL;
	}

	public boolean trigger(Trigger trigger) {
		Iterator<InputDevice> iDevices = devices.iterator();
		while (iDevices.hasNext()) {
			if (iDevices.next().trigger(trigger)) {
				return true;
			}
		}
		return false;
	}

}
