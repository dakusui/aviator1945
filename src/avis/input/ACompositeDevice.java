package avis.input;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ACompositeDevice implements AInputDevice {
	private List<AInputDevice> devices;

	public ACompositeDevice() {
		devices = new LinkedList<AInputDevice>();
	}
	public void add(AInputDevice device) {
		devices.add(device);
	}
	
	public void poll() {
		Iterator<AInputDevice> iDevices = devices.iterator();
		while (iDevices.hasNext()) {
			iDevices.next().poll();
		}
	}

	public Stick cursor() {
		Iterator<AInputDevice> iDevices = devices.iterator();
		while (iDevices.hasNext()) {
			Stick ret = iDevices.next().cursor(); 
			if (ret != Stick.NEUTRAL) {
				return ret;
			}
		}
		return Stick.NEUTRAL;
	}

	public boolean trigger(Trigger trigger) {
		Iterator<AInputDevice> iDevices = devices.iterator();
		while (iDevices.hasNext()) {
			if (iDevices.next().trigger(trigger)) {
				return true;
			}
		}
		return false;
	}

}
