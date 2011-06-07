package example;

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
import oreactor.video.sprite.Sprite;

public class ExampleReactor2  extends Mu64Reactor {
	static class Attrs extends Attributes {
		private static final long serialVersionUID = 2767667589491228395L;
		double x, y;
		int life = 100;
		@Override
		protected void applyMotion(Motion b) {
			x += ((Mtn)b).dx();
			y += ((Mtn)b).dy();
			life--;
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
	static class Mtn extends Motion {
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
	@Override
	public MotionProvider newMotionProvider() {
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
			@Override
			protected void putStrite(Sprite sprite, Attributes attributes) {
				Attrs attr = (Attrs) attributes;
				sprite.put(attr.x(), attr.y(), 0);
			}
			@Override
			protected boolean touches(MMachine m, MMachine n, double d) {
				return false;
			}
		};
	}
	
	@Override
	public void run() throws OpenReactorException {
		if (isFirstTime()) {
			loadConfig("example/config.json");
		} else {
			if (Math.random() * 100 > 95) {
				this.motionProvider().buildMMachine(
						new MMachineSpec() {
							@Override
							protected Drivant createDrivant() {
								return new Drivant() {
									private double dx = Math.random() * 10 - 5;
									private double dy = Math.random() * 10 - 5;
									@Override
									protected void perform(Motion motion,
											MMachine owner,
											MotionProvider provider) {
										Mtn m = (Mtn) motion;
										m.dx(this.dx);
										m.dy(this.dy);
										if (((Attrs)owner.attributes()).life() <=0 ) {
											m.destroy();
										}
									}
									@Override
									protected void performInteractionWith(
											Motion motion, MMachine owner,
											MMachine another, double distance) {
									}
								};
							}
							@Override
							protected void fillInAttributes(Attributes attr,
									MMachine parent) {
								Attrs a = (Attrs) attr;
								a.x(512.0);
								a.y(384.0);
							}
							@Override
							protected Group getGroup() {
								return null;
							}
							@Override
							protected Sprite createSprite() {
								return spriteplane().createSprite(spritespec("spr00"));
							}
							@Override
							protected void releaseSprite(Sprite s) {
								spriteplane().removeSprite(s);
							}
						}
				);
			}
		}
	}
	@Override
	public boolean fullScreenEnabled() {
		return false;
	}
	@Override
	public ScreenSize screenSize() {
		return ScreenSize.XGA;
	}
}

