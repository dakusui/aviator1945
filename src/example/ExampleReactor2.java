package example;

import openreactor.mu64.Mu64Reactor;
import oreactor.motion.Attributes;
import oreactor.motion.Drivant;
import oreactor.motion.Group;
import oreactor.motion.MMachine;
import oreactor.motion.Motion;
import oreactor.motion.MotionProvider;

public class ExampleReactor2  extends Mu64Reactor {
	static class Attrs extends Attributes {
		private static final long serialVersionUID = 2767667589491228395L;
		double x = 512, y = 384;
		int life = 100;
		@Override
		protected void applyMotion(Motion b) {
			x += ((Mtn)b).dx();
			y += ((Mtn)b).dy();
		}
		@Override
		protected boolean touches(Attributes another, double distance) {
			return false;
		}
	}
	static class Mtn extends Motion {
		double dx, dy;
		double dx() {
			return this.dx;
		}
		double dy() {
			return this.dy;
		}
	}
	@Override
	public int patternWidth() {
		return 64;
	}
	@Override
	public int patternHeight() {
		return 64;
	}
	@Override
	public MotionProvider motionProvider() {
		return new MotionProvider() {
			@Override
			protected double calculateDistance(MMachine m1, MMachine m2) {
				return 0;
			}
			@Override
			protected Attributes createAttributes() {
				return new Attrs();
			}
			@Override
			protected Motion createMotionObject() {
				return new Mtn();
			}
			@Override
			protected boolean isAcceptable(Drivant drivant) {
				return true;
			}
			@Override
			protected boolean isAcceptable(Attributes attr) {
				return true;
			}
			@Override
			protected boolean isAcceptable(Group group) {
				return true;
			}
		};
	}
}

