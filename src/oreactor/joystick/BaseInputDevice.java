package oreactor.joystick;

abstract class BaseInputDevice implements InputDevice {

	protected JoystickEngine.Type type;

	protected BaseInputDevice(JoystickEngine.Type type) {
		this.type = type;
	}
}
