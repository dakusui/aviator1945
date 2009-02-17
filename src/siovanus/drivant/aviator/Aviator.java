package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;

public abstract class Aviator {
	protected Aviator() {
	}

	public void perform(AviatorDrivant drivant, Drivant target) {
		performAction(drivant, target);
	}

	abstract protected void performAction(AviatorDrivant drivant, Drivant target);

	public boolean isTwisting() {
		return false;
	}

	public int energy() {
		return 0;
	}

}
