package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;
import avis.motion.Drivant.Product;

public class SmartAviator extends Aviator {
	private Aviator runnaway;
	private Aviator chase;
	private Aviator current;

	public SmartAviator() {
		runnaway = new RunningAwayAviator();
		chase = new CarefullyChasingAviator();
		current = chase;
	}

	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		if (drivant == null || target == null) {
			return;
		}
		Product p = drivant.product(target);
		Product q = target.product(drivant);
		if (p.distance < 200 && p.inner > 0 || q.inner > 0 && p.inner < 0 && Math.abs(q.tangent) < 1 && p.distance < 500) {
			current = runnaway;
		}
		if (p.distance > 500 || p.inner < 0) {
			current = chase;
		}
		current.performAction(drivant, target);
	}

}
