package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;
import avis.motion.Drivant.Product;

public class MobAviator extends Aviator {
	private Aviator activated;
	private Aviator current;

	public MobAviator() {
		activated = new FixedDirectionAviator();
		current = null;
	}
	
	public MobAviator(Aviator activated) {
		this.activated = activated;
		current = null;
	}
	
	@Override
	protected void performAction(AviatorDrivant drivant, Drivant target) {
		if (drivant == null || target == null) {
			return;
		}
		Product p = drivant.product(target);
		Product q = target.product(drivant);
		if (p.distance < 300 && p.inner > 0 || q.inner > 0 && p.inner < 0 && Math.abs(q.tangent) < 1 && p.distance < 500) {
			current = activated;
		}
		if (current != null) {
			current.performAction(drivant, target);
		} else {
			if (drivant.parameters().velocity < drivant.parameters().maxV * 0.60) {
				drivant.accelerate();
			}
		}
	}

}
