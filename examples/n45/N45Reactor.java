package n45;

import mu64.Mu64Reactor;
import mu64.motion.Attributes;
import mu64.motion.Drivant;
import mu64.motion.Group;
import mu64.motion.MMachine;
import mu64.motion.MMachineSpec;
import mu64.motion.Motion;
import mu64.motion.MotionProvider;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.JoystickEngine.Stick;
import oreactor.video.sprite.Sprite;

public class N45Reactor extends Mu64Reactor {
	static class N45Group extends Group {
		public static final N45Group FRIEND = new N45Group("Friend");
		public static final N45Group ENEMY = new N45Group("Enemy");
		private N45Group(String name) {
			super(name);
		}

		@Override
		public boolean doesCollide(Group another) {
			return this.equals(another);
		}

		@Override
		public boolean doesInteract(Group another) {
			return false;
		}
	}

	static class N45Attrs extends Attributes {
		private static final long serialVersionUID = 2767667589491228395L;
		double x, y;
		int life = 100;

		@Override
		protected void applyMotion(Motion b) {
			x += ((N45Motion) b).dx();
			y += ((N45Motion) b).dy();
			life--;
		}

		@Override
		protected boolean touches(Attributes another, double distance) {
			return false;
		}

		public double x() {
			return this.x;
		}

		public double y() {
			return this.y;
		}

		public int life() {
			return this.life;
		}

		public void x(double x) {
			this.x = x;
		}

		public void y(double y) {
			this.y = y;
		}
	}

	static class N45Motion extends Motion {
		double dx, dy;

		double dx() {
			return this.dx;
		}

		double dy() {
			return this.dy;
		}

		public void dx(double dx) {
			this.dx = dx;
		}

		public void dy(double dy) {
			this.dy = dy;
		}
	}

	abstract static class N45BaseDrivant extends Drivant {
		@Override
		protected final void perform(Motion motion, MMachine owner,
				MotionProvider provider) {
			this.perform_((N45Motion) motion, owner, provider);
		}

		@Override
		protected final void performInteractionWith(Motion motion,
				MMachine owner, MMachine another, double distance) {
		}

		protected abstract void perform_(N45Motion motion, MMachine owner,
				MotionProvider provider);
	}

	@Override
	public MotionProvider newMotionProvider() {
		return new MotionProvider() {
			@Override
			protected double calculateDistance(MMachine m1, MMachine m2) {
				N45Attrs m1a = (N45Attrs) m1.attributes();
				N45Attrs m2a = (N45Attrs) m2.attributes();
				double ret = 0;
				double dx = m2a.x - m1a.x;
				double dy = m2a.y - m1a.y;
				ret = Math.sqrt(dx*dx - dy * dy);
				return ret;
			}

			@Override
			protected Attributes createAttributes() {
				return new N45Attrs();
			}

			@Override
			protected Motion createMotionObject() {
				return new N45Motion();
			}

			@Override
			protected boolean isAcceptable(Drivant drivant) {
				return drivant instanceof N45BaseDrivant;
			}

			@Override
			protected boolean isAcceptable(Attributes attr) {
				return attr instanceof N45Attrs;
			}

			@Override
			protected boolean isAcceptable(Group group) {
				return true;
			}

			@Override
			protected void putStrite(Sprite sprite, Attributes attributes) {
				N45Attrs attr = (N45Attrs) attributes;
				sprite.put(attr.x(), attr.y(), 0);
			}
		};
	}

	MMachineSpec myplaneSpec =new MMachineSpec() {
		@Override
		protected Drivant createDrivant() {
			return new N45BaseDrivant() {
				@Override
				protected void perform_(N45Motion motion,
						MMachine owner, MotionProvider provider) {
					Stick s = stick();
					if (s != null) {
						motion.dx(s.x() *2);
						motion.dy(s.y() *2);
					}					
				}
			};
		}
		@Override
		protected void fillInAttributes(Attributes attr,
				MMachine parent) {
			N45Attrs a = (N45Attrs) attr;
			a.x(512.0);
			a.y(384.0);
		}
		@Override
		protected Group getGroup() {
			return N45Group.FRIEND;
		}
		@Override
		protected Sprite createSprite() {
			return spriteplane().createSprite(spritespec("zero"));
		}
		@Override
		protected void releaseSprite(Sprite s) {
			spriteplane().removeSprite(s);
		}
	};

		
	@Override
	public void run() throws OpenReactorException {
		if (isFirstTime()) {
			loadConfig("n45/config.json");
			this.motionProvider().buildMMachine(myplaneSpec);
		} else {
		}
	}
}
