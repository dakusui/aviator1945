package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class F4UMMachineSpec extends AviatorMMachineSpec {

	public static final String LABEL = "F4U";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		// Per spec parameters
		parameters.acceleration =     0.4;  
		parameters.bankRecovery =     0.5;
		parameters.life =             4;
		parameters.maxV  =            6.0 * vCoefficient;
		parameters.minV  =            4.0 * vCoefficient;
		parameters.rollV =            1.0;
		parameters.yawRecovery =      0.5;
		parameters.yawV =             2.0;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_F4U;
	}

	@Override
	public String label() {
		return LABEL;
	}
}
