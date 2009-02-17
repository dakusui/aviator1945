package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class Ki84MMachineSpec extends AviatorMMachineSpec {
	
	public static final String LABEL = "Ki84";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {

		// Per spec parameters
		parameters.acceleration =     0.7;  
		parameters.bankRecovery =     1.1;
		parameters.life =             12;
		parameters.maxV  =            6.89 * vCoefficient;
		parameters.minV  =            2.20 * vCoefficient;
		parameters.rollV =            2.4;
		parameters.yawRecovery =      0.5;
		parameters.yawV =             2.4;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_KI84;
	}

	@Override
	public String label() {
		return LABEL;
	}
}
