package siovanus.motion;

import avis.motion.Parameters;
import siovanus.Siovanus;
import siovanus.drivant.VolatileDrivant.VolatileParameters;

public class MinorExplosionMMachineSpec extends ExplosionMMachineSpec {
	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_EXP_MINOR;
	}
	@Override
	protected void setupParameters(Parameters p) {
		super.setupParameters(p);
		((VolatileParameters)p).lifeTime = 15;
	}

}
