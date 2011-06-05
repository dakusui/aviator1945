package mu64.motion;

public abstract class Drivant {
	protected abstract void perform(Motion motion, MMachine owner, MotionProvider provider);
	protected void performCollisionWith(Motion motion, MMachine owner, MMachine another, double distance) {
		motion.addCollision(another);
	}
	protected abstract void performInteractionWith(Motion motion, MMachine owner, MMachine another, double distance);
}
