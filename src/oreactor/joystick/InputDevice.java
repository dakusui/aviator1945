package oreactor.joystick;

import oreactor.joystick.JoystickEngine.Stick;
import oreactor.joystick.JoystickEngine.Trigger;

public interface InputDevice {
    public abstract void poll();

	public abstract Stick stick();

	public abstract boolean trigger(Trigger trigger);
}