package oreactor.motion;

public interface MotionObserver {
	public void registered(MMachine m);
	public void unregistered(MMachine m);
}
