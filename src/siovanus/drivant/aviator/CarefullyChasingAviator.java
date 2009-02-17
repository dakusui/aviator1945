package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.Parameters;
import avis.motion.Drivant.Product;

public class CarefullyChasingAviator extends ChasingAviator {
	private BiasedDrivant rightDummy;
	private BiasedDrivant leftDummy;

	public CarefullyChasingAviator() {
		this.rightDummy = new BiasedDrivant(-Avis.DIRECTION_STEPS / 3, 400);
		this.leftDummy = new BiasedDrivant( Avis.DIRECTION_STEPS / 3, 400);
	}
	
	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		if (target == null) {
			super.performAction(drivant, target);
			return;
		}
		Product q = target.product(drivant);
		if (q.inner > 0) {
			BiasedDrivant imaginaryTarget;
			if (q.outer > 0) {
				imaginaryTarget = rightDummy;
			} else {
				imaginaryTarget = leftDummy;
			}
			imaginaryTarget.target(target);
			super.performAction(drivant, imaginaryTarget);
		} else {
			super.performAction(drivant, target);
		}
	}
	
	static class BiasedDrivant extends Drivant {
		private Drivant target;
		private int theta;
		private double r;

		BiasedDrivant(int theta, double r) {
			this.theta = theta;
			this.r = r;
		}

		public void target(Drivant target) {
			this.target = target;
		}
		
		@Override 
		public double x() {
			return target.x() + Avis.cos(target.direction() + theta) * r;
		}
		
		@Override 
		public double y() {
			return target.y() + Avis.sin(target.direction() + theta) * r;
		}

		@Override
		protected void copyParameters_Protected(Parameters parameters) {
			// Disabled
		}

		@Override
		public void perform() {
		}

		@Override
		protected Parameters snapshot_Protected() {
			return target.parameters();
		}
		
	}

}
