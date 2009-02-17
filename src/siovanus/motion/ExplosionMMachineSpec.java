package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.ExplosionDrivant;
import siovanus.drivant.VolatileDrivant.VolatileParameters;
import avis.motion.Drivant;
import avis.motion.MMachineSpec;
import avis.motion.Parameters;

public class ExplosionMMachineSpec extends MMachineSpec<AviatorDrivant> {
	@Override
	protected Drivant drivant() {
		return new ExplosionDrivant();
	}

	@Override
	protected void setupParameters(Parameters p) {
		VolatileParameters parameters = (VolatileParameters) p;
		// Per instance parameters
		// - Drivant Level parameters
		//   These parameters are set by framework automatically.
		//   You don't need to take care of.
		//     direction =         (set to value which is given by a application programmer);        
		//     velocity =          (set to value which is given by a application programmer);        
		//     x =                 (set to value which is given by a application programmer);        
		//     y =                 (set to value which is given by a application programmer);        
		//     alive =             true (fiexed);
		//     height =            20 (fiexed);
		//     width =             20 (fiexed);

		// - SEmittableDrivant Level parameters
		parameters.parent = additionalParameter();
		parameters.groupId = null;
		parameters.acceleration = 0.1;
		parameters.velocity = 1;
		parameters.alive = true;
		if (parameters.parent != null) {
			parameters.direction = 
			parameters.maxV = parameters.parent.velocity();
			parameters.minV = 0;
		}
		
		parameters.lifeTime = 30;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_EXPLOSION;
	}

	@Override
	public String label() {
		return null;
	}

}
