package mu64.motion;

import oreactor.annotations.ExtensionPoint;

public abstract class Drivant {
	protected abstract void perform(Motion motion, MMachine owner, MotionProvider provider);
	@ExtensionPoint
	protected void performCollisionWith(Motion motion, MMachine owner, MMachine another, double distance) {
		motion.addCollision(another);
	}
	@ExtensionPoint
	protected void performInteractionWith(Motion motion, MMachine owner, MMachine another, double distance) {
	}
}
