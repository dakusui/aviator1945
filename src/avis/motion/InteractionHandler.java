package avis.motion;

public interface InteractionHandler {
	public void handleInteraction(Drivant d1, Drivant d2, double distance);
	public boolean collides(Drivant d1, Drivant d2, double distance);
	public void handleCollision(Drivant d1, Drivant d2, double distance);
}
