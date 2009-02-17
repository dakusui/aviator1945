package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.motion.Drivant;

public class NullAviator extends Aviator {

	protected NullAviator() {
		super();
	}

	@Override
	protected void performAction(AviatorDrivant drivant,
			Drivant target) {
		// Does nothing
	}

}
