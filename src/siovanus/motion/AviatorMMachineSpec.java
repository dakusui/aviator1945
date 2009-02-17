package siovanus.motion;

import siovanus.drivant.GenericAviatorDrivant;
import siovanus.drivant.AviatorDrivant.AviatorParameters;
import siovanus.drivant.aviator.Aviator;
import avis.motion.Drivant;
import avis.motion.MMachineSpec;
import avis.motion.Parameters;


public abstract class AviatorMMachineSpec extends MMachineSpec<Aviator> {
	protected double vCoefficient = 1.6;

	@Override
	protected Drivant drivant() {
		return new GenericAviatorDrivant();
	}

	@Override
	protected void setupParameters(Parameters p) {
		AviatorParameters parameters = (AviatorParameters) p;
		////
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

		// - AviatorDrivant Level parameters
		parameters.aviator =          additionalParameter();
		parameters.yawDirection =     parameters.direction;
		parameters.distanceToTarget = Double.MAX_VALUE; // volatile
		parameters.bank =             0;
		parameters.target =           null;
		////
		// Per spec parameters
		setupAviatorParameters(parameters);
	}
	
	protected abstract void setupAviatorParameters(AviatorParameters parameters);
}
