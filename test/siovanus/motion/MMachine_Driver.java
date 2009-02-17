package siovanus.motion;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import avis.motion.Drivant;
import avis.motion.MMachine;
import avis.motion.Parameters;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;

public class MMachine_Driver {
	public boolean drivant1CollisionDetected = false;
	public boolean drivant2CollisionDetected = false;
	public int drivant1TimesCollisionDetected = 0;
	public int drivant2TimesCollisionDetected = 0;
	
	private static ASprite sprite = new ASprite() {
		@Override
		protected void init_Protected(ASpriteSpec p) {
		}

		@Override
		protected void paint_Protected(Graphics graphics, ImageObserver observer) {
		}
		
	};
	Drivant drivant_01 = new Drivant() {
		@Override
		protected void copyParameters_Protected(Parameters parameters) {
		}

		@Override
		public void perform() {
		}

		@Override
		protected Parameters snapshot_Protected() {
			Parameters ret = new Parameters(); 
			return ret;
		}
	};
	Drivant drivant_02 = new Drivant() {
		@Override
		protected void copyParameters_Protected(Parameters parameters) {
		}

		@Override
		public void perform() {
		}

		@Override
		protected Parameters snapshot_Protected() {
			Parameters ret = new Parameters(); 
			return ret;
		}
	};
	public MMachine_Driver() {
		drivant_01.init(drivant_01.createParameters());
		drivant_02.init(drivant_02.createParameters());
	}
	
	public MMachine createTestMMachine_01() {
		MMachine ret = new MMachine(drivant_01, sprite) {
			
		};
		return ret; 
	}

	public MMachine createTestMMachine_02() {
		MMachine ret = new MMachine(drivant_02, sprite) {
			
		};
		return ret; 
	}

}
