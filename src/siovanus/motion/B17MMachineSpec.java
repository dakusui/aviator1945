package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class B17MMachineSpec extends AviatorMMachineSpec {

	public static final String LABEL = "B17";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		// Per spec parameters
		parameters.acceleration =     1.0;  
		parameters.bankRecovery =     1.0;
		parameters.life =             10;
		parameters.maxV  =            2.0 * vCoefficient;
		parameters.minV  =            1.5 * vCoefficient;
		parameters.rollV =            0.3;
		parameters.yawRecovery =      0.1;
		parameters.yawV =             0.0;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_B17;
	}

	@Override
	public String label() {
		return LABEL;
	}

}
