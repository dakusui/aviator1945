package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;
import avis.motion.Drivant.Product;

public class FixedDirectionAviator extends Aviator {
	protected FixedDirectionAviator() {
		super();
	}
	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		Product p = drivant.product(target);
		if (p.outer> 0) {
			drivant.rollLeft(true);
		} else if (p.outer< 0) {
			drivant.rollRight(true);
		}
		drivant.recoverYawHalf();
		if (p.inner> 0) {
			drivant.accelerate();
		} else if (p.inner< 0){
			drivant.decelerate();
		}
	}


}
