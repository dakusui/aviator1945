package mu64.motion;

public interface MotionObserver {
	public void registered(MMachine m);
	public void unregistered(MMachine m);
}
