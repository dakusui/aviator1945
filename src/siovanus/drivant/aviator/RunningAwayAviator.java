package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;
import avis.motion.Drivant.Product;

public class RunningAwayAviator extends Aviator {

	public RunningAwayAviator() {
		super();
	}

	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		Product p = drivant.product(target);
		
		double outerProduct = p.outer;
		if (outerProduct > 0) {
			drivant.rollLeft();
			drivant.yawLeft();
		} else if (outerProduct <= 0) {
			drivant.rollRight();
			drivant.yawRight();
		}
		drivant.recoverYawHalf();
		double innerProduct = p.inner;
		if (innerProduct > 0) {
			drivant.decelerate();
			if (Math.abs(p.tangent) < 0.5 && p.distance < 400) {
				drivant.shot();
			}
		} else if (innerProduct < 0){
			drivant.accelerate();
		}
	}

}
