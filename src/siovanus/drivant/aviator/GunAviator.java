package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.AviatorDrivant.AviatorParameters;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.Parameters;
import avis.motion.Drivant.Product;

public class GunAviator extends Aviator {
	protected AdjusterDrivant adjuster;
	private double rightEndX;
	private double rightEndY;
	private double leftEndX;
	private double leftEndY;

	public GunAviator(Drivant parent, double r, double theta, int rightEnd, int leftEnd) {
		super();
		this.adjuster = new AdjusterDrivant(parent, r, theta);
		this.rightEndX =  Avis.cos(rightEnd);
		this.rightEndY =  Avis.sin(rightEnd);
		this.leftEndX = Avis.cos(leftEnd);
		this.leftEndY = Avis.sin(leftEnd);
	}
	
	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		AviatorParameters param = (AviatorParameters) drivant.parameters();
		int direction = (int) param.direction;
		param.direction = param.yawDirection;
		Product p = drivant.product(target);
		double outerProduct = p.outer;
		if (outerProduct > 0) {
			
			double outerProductWithRightEnd = rightEndX * Avis.sin(direction) - rightEndY * Avis.cos(direction);
			if (outerProductWithRightEnd > 0) {
				drivant.yawRight();
			}
		} else if (outerProduct < 0) {
			double outerProductWithLeftEnd = leftEndX * Avis.sin(direction) - leftEndY * Avis.cos(direction);
			if (outerProductWithLeftEnd < 0) {
				drivant.yawLeft();
			}
		}
		double tan = p.tangent;
		if (p.inner > 0 && Math.abs(tan) < 0.25 && target.groupId() != drivant.groupId()) {
			drivant.shot();
		}
		adjuster.adjust(drivant);
	}
	/*
	 * dirty trick to access to protected fields
	 */
	protected static class AdjusterDrivant extends Drivant {
		private double r;
		private double theta;
		private Drivant parent;
		protected AdjusterDrivant(Drivant parent, double r, double theta) {
			this.parent = parent;
			this.r = r;
			this.theta = theta;
		}
		protected void adjust(Drivant owner) {
			AviatorParameters pOwner = (AviatorParameters) owner.parameters();
			AviatorParameters pParent = (AviatorParameters) parent.parameters();
			pOwner.x = pParent.x + Avis.cos( (int)(theta + pParent.direction) ) * r;
			pOwner.y = pParent.y + Avis.sin( (int)(theta + pParent.direction) ) * r;
		}
		@Override
		protected void copyParameters_Protected(Parameters parameters) {
		}
		@Override
		public void perform() {
		}
		@Override
		protected Parameters snapshot_Protected() {
			return null;
		}
		protected void collision(Drivant another, double distance) {
		}
	}
}
