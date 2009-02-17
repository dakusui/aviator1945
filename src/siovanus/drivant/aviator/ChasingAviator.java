package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;
import avis.motion.Drivant.Product;

public class ChasingAviator extends Aviator {

	public ChasingAviator() {
		super();
	}

	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		Product p = drivant.product(target);
	
		double outerProduct = p.outer;
		if (outerProduct > 0) {
			drivant.rollRight();
			drivant.yawRight();
		} else if (outerProduct < 0) {
			drivant.yawLeft();
			drivant.rollLeft();
		}
		drivant.recoverYawHalf();
		double innerProduct = p.inner;
		if (innerProduct > 0) {
			drivant.accelerate();
		} else if (innerProduct < 0){
			drivant.decelerate();
		}
		if (innerProduct > 0 && Math.abs(p.tangent) < 0.25 && target.groupId() != drivant.groupId()) {
			if (drivant.distance(target) < 800) {
				drivant.shot();
			}
		}
	}
}
