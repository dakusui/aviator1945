package siovanus.motion;

import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.SEmittableDrivant;
import siovanus.drivant.SEmittableDrivant.SEmittableParameters;
import avis.motion.Drivant;
import avis.motion.MMachineSpec;
import avis.motion.Parameters;

public abstract class EmittableMMachineSpec extends MMachineSpec<AviatorDrivant> {
	@Override
	protected Drivant drivant() {
		return new SEmittableDrivant();
	}
	
	@Override
	protected void setupParameters(Parameters p) {
		SEmittableParameters parameters = (SEmittableParameters) p;
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
		////
		// Per application parameters
		// - Drivant Level parameters
		setupEmittableParameters(parameters);
	}
	
	protected abstract void setupEmittableParameters(SEmittableParameters parameters) ;

	@Override
	public String label() {
		return null;
	}
}
