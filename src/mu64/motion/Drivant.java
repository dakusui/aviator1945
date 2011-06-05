package mu64.motion;

import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.OpenReactorException;

public abstract class Drivant {
	protected abstract void perform(Motion motion, MMachine owner, MotionProvider provider) throws OpenReactorException;
	@ExtensionPoint
	protected void performCollisionWith(Motion motion, MMachine owner, MMachine another, double distance) {
		motion.addCollision(another);
	}
	@ExtensionPoint
	protected void performInteractionWith(Motion motion, MMachine owner, MMachine another, double distance) {
	}
}
