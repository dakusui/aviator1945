package n45;

import mu64.Mu64Reactor;
import mu64.motion.Drivant;
import mu64.motion.Group;
import mu64.motion.MMachine;
import mu64.motion.MMachineSpec;
import mu64.motion.MotionProvider;
import mu64.motion.shootergame.SGAttrs;
import mu64.motion.shootergame.SGBaseDrivant;
import mu64.motion.shootergame.SGGroup;
import mu64.motion.shootergame.SGMMachineSpec;
import mu64.motion.shootergame.SGMotion;
import mu64.motion.shootergame.SGMotionProvider;
import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.JoystickEngine.Stick;
import oreactor.joystick.JoystickEngine.Trigger;
import oreactor.video.Viewport;
import oreactor.video.sprite.Sprite;

public class N45Reactor extends Mu64Reactor {
	private int s = patternplaneHeight() - screenHeight();
	MMachineSpec enemyplaneSpec =new SGMMachineSpec(this) {
		@Override
		public void fillInAttrs_(SGAttrs attrs, MMachine parent) {
			attrs.x(Math.random() * 1024.0);
			attrs.y(0.0);
			attrs.direction(0);
		}
		@Override
		protected Drivant createDrivant() {
			return new SGBaseDrivant() {
				double dx = Math.random() * 2 - 1;
				double dy =  4;
				@Override
				protected void perform_(SGMotion motion,
						MMachine owner, MotionProvider provider) throws OpenReactorException {
					motion.dx(dx);
					motion.dy(dy);
					if (((SGAttrs)owner.attributes()).y() > 768) {
						motion.destroy();
					}
					motion.pattern(ticks() % 3);
				}
			};
		}
		@Override
		protected Sprite createSprite() {
			return createSprite("zero", 11);
		}
		@Override
		protected Group getGroup() {
			return SGGroup.ENEMY;
		}
	};
	MMachineSpec myplaneSpec =new SGMMachineSpec(this) {
		@Override
		public void fillInAttrs_(SGAttrs attrs, MMachine parent) {
			attrs.x(512.0);
			attrs.y(384.0);
		}
		@Override
		protected Drivant createDrivant() {
			return new SGBaseDrivant() {
				private int counter;
				@Override
				protected void perform_(SGMotion motion,
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
					motion.pattern(ticks() % 3);
				}
			};
		}
		@Override
		protected Sprite createSprite() {
			return createSprite("p38", 10);
		}
		@Override
		protected Group getGroup() {
			return SGGroup.FRIEND;
		}
	};
	MMachineSpec myshotSpec = new SGMMachineSpec(this) {
		@Override
		public void fillInAttrs_(SGAttrs attrs, MMachine parent) {
			SGAttrs pattrs = (SGAttrs) parent.attributes();
			attrs.x(pattrs.x());
			attrs.y(pattrs.y());
		}
		@Override
		protected Drivant createDrivant() {
			return new SGBaseDrivant() {
				int life = 40;
				@Override
				public void perform_(SGMotion motion, MMachine owner,
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
		protected Sprite createSprite() {
			return createSprite("shot", 100);
		}
		@Override
		protected Group getGroup() {
			return SGGroup.FRIEND;
		}
	};
	@Override
	public MotionProvider newMotionProvider() throws OpenReactorException {
		MotionProvider ret = new SGMotionProvider();
		return ret;
	}
	@Override
	public void run() throws OpenReactorException {
		if (isFirstTime()) {
			loadConfig("n45/config2.json");
			this.motionProvider().buildMMachine(myplaneSpec);
			for (int i = 0; i < this.patternplane().columns(); i++) {
				for (int j = 0; j < this.patternplane().rows(); j++) {
					int patternno = 0;
					if (Math.random() > 0.95) {
						patternno = 1;
					}
					this.patternplane().put(i, j, patternno);
				}
			}
			s = 768 * (4-1);
		} else {
			if (Math.random() * 100 > 97) {
				this.motionProvider().buildMMachine(enemyplaneSpec);
			}
			Viewport viewport = patternplane().viewport();
			viewport.offset(0, s);
			if (s-- < 0) s = patternplaneHeight() - screenHeight();
		}
	}
	@Override
	protected int patternplaneHeight() {
		return 768 * 4;
	}
}
