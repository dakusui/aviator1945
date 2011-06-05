package n45;

import mu64.Mu64Reactor;
import mu64.motion.Attributes;
import mu64.motion.Drivant;
import mu64.motion.Group;
import mu64.motion.MMachine;
import mu64.motion.MMachineSpec;
import mu64.motion.Motion;
import mu64.motion.MotionProvider;
import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.JoystickEngine.Stick;
import oreactor.joystick.JoystickEngine.Trigger;
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
			return !this.equals(another);
		}

		@Override
		public boolean doesInteract(Group another) {
			return false;
		}
	}

	static class N45Attrs extends Attributes {
		private static final long serialVersionUID = 2767667589491228395L;
		double x, y;
		private double direction;

		@Override
		protected void applyMotion(Motion b) {
			x += ((N45Motion) b).dx();
			y += ((N45Motion) b).dy();
		}
		public double x() {
			return this.x;
		}
		public double y() {
			return this.y;
		}
		public double direction() {
			return this.direction;
		}
		public void x(double x) {
			this.x = x;
		}
		public void y(double y) {
			this.y = y;
		}
		public void direction(double dir) {
			this.direction = dir;
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
	abstract class N45MMachineSpec extends MMachineSpec {
		protected Sprite createSprite(String spriteSpecName) {
			return spriteplane().createSprite(spritespec(spriteSpecName));
		}

		@Override
		protected void fillInAttributes(Attributes attrs, MMachine parent) {
			fillInAttrs_((N45Attrs)attrs, parent);
		}
		
		public abstract void fillInAttrs_(N45Attrs attrs, MMachine parent);

		@Override
		protected void releaseSprite(Sprite s) {
			spriteplane().removeSprite(s);
		}		
	}
	abstract static class N45BaseDrivant extends Drivant {
		@Override
		protected final void perform(Motion motion, MMachine owner,
				MotionProvider provider) throws OpenReactorException {
			this.perform_((N45Motion) motion, owner, provider);
		}
		@ExtensionPoint
		protected void performCollisionWith(Motion motion, MMachine owner, MMachine another, double distance) {
			motion.destroy();
		}
		@Override
		protected final void performInteractionWith(Motion motion,
				MMachine owner, MMachine another, double distance) {
		}
		protected abstract void perform_(N45Motion motion, MMachine owner,
				MotionProvider provider) throws OpenReactorException;
	}

	@Override
	public MotionProvider newMotionProvider() throws OpenReactorException {
		MotionProvider ret = new MotionProvider() {
			@Override
			protected double calculateDistance(MMachine m1, MMachine m2) {
				N45Attrs m1a = (N45Attrs) m1.attributes();
				N45Attrs m2a = (N45Attrs) m2.attributes();
				double ret = 0;
				double dx = m2a.x - m1a.x;
				double dy = m2a.y - m1a.y;
				ret = Math.sqrt(dx*dx + dy * dy);
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
			protected boolean isAcceptable(Attributes attrs) {
				return attrs instanceof N45Attrs;
			}
			@Override
			protected boolean isAcceptable(Group group) {
				return true;
			}
			@Override
			protected void putStrite(Sprite sprite, Attributes attributes) {
				N45Attrs attrs = (N45Attrs) attributes;
				sprite.put(attrs.x(), attrs.y(), attrs.direction());
			}
			@Override
			protected boolean touches(MMachine m, MMachine n, double d) {
				if (d < 32) {
					return true;
				}
				return false;
			}
		};
		ret.addGroup(N45Group.FRIEND);
		ret.addGroup(N45Group.ENEMY);
		return ret;
	}
	MMachineSpec myshotSpec = new N45MMachineSpec() {
		@Override
		protected Drivant createDrivant() {
			return new N45BaseDrivant() {
				int life = 20;
				@Override
				public void perform_(N45Motion motion, MMachine owner,
						MotionProvider provider) throws OpenReactorException {
					motion.dy(-8);
					life--;
					if (life <=0) {
						motion.destroy();
					}
				}
			};
		}
		@Override
		protected Group getGroup() {
			return N45Group.FRIEND;
		}
		@Override
		public void fillInAttrs_(N45Attrs attrs, MMachine parent) {
			N45Attrs pattrs = (N45Attrs) parent.attributes();
			attrs.x(pattrs.x());
			attrs.y(pattrs.y());
		}
		@Override
		protected Sprite createSprite() {
			return createSprite("shot");
		}
	};
	MMachineSpec myplaneSpec =new N45MMachineSpec() {
		@Override
		protected Drivant createDrivant() {
			return new N45BaseDrivant() {
				private int counter;
				
				@Override
				protected void perform_(N45Motion motion,
						MMachine owner, MotionProvider provider) throws OpenReactorException {
					Stick s = stick();
					if (s != null) {
						motion.dx(s.x() *2);
						motion.dy(s.y() *2);
					}					
					if (trigger(Trigger.SQUARE)) {
						if ( counter <= 0 ) {
							motion.addEmission(myshotSpec);
							counter = 5;
						}
					}
					if (counter > 0) {
						counter--;
					}
				}
			};
		}
		@Override
		protected Group getGroup() {
			return N45Group.FRIEND;
		}
		@Override
		protected Sprite createSprite() {
			return createSprite("zero");
		}
		@Override
		public void fillInAttrs_(N45Attrs attrs, MMachine parent) {
			attrs.x(512.0);
			attrs.y(384.0);
		}
	};

	MMachineSpec enemyplaneSpec =new N45MMachineSpec() {
		@Override
		protected Drivant createDrivant() {
			return new N45BaseDrivant() {
				double dx = Math.random() * 2 - 1;
				double dy =  4;
				
				@Override
				protected void perform_(N45Motion motion,
						MMachine owner, MotionProvider provider) throws OpenReactorException {
					motion.dx(dx);
					motion.dy(dy);
					if (((N45Attrs)owner.attributes()).y() > 768) {
						motion.destroy();
					}
				}
			};
		}
		@Override
		protected Group getGroup() {
			return N45Group.ENEMY;
		}
		@Override
		protected Sprite createSprite() {
			return createSprite("p38");
		}
		
		@Override
		public void fillInAttrs_(N45Attrs attrs, MMachine parent) {
			attrs.x(Math.random() * 1024.0);
			attrs.y(0.0);
			attrs.direction(Math.PI);
		}
	};

	@Override
	public void run() throws OpenReactorException {
		if (isFirstTime()) {
			loadConfig("n45/config.json");
			this.motionProvider().buildMMachine(myplaneSpec);
		} else {
			if (Math.random() * 100 > 97) {
				this.motionProvider().buildMMachine(enemyplaneSpec);
			}
		}
	}
}
