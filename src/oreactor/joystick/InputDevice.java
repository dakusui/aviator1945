package oreactor.joystick;

public interface InputDevice {
    public static enum Trigger {
        TRIANGLE, 
        SQUARE, 
        CIRCLE, 
        CROSS, 
        R1,
        R2,
        L1,
        L2,
        START,
        SELECT,
        ANY, 
        HELP
    }
    
    public static enum Stick {
        NEUTRAL,
        UP,
        UP_RIGHT,
        RIGHT,
        DOWN_RIGHT,
        DOWN,
        DOWN_LEFT,
        LEFT,
        UP_LEFT,
        INVALID
    }
    public abstract void poll();

	public abstract InputDevice.Stick stick();

	public abstract boolean trigger(Trigger trigger);

}