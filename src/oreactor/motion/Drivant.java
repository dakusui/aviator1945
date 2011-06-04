package oreactor.motion;

public abstract class Drivant {
	protected abstract void perform(Motion motion, MMachine owner, MotionProvider provider);
	protected abstract void performCollisionWith(Motion motion, MMachine owner, MMachine another, double distance);
	protected abstract void performInteractionWith(Motion motion, MMachine owner, MMachine another, double distance);
}
