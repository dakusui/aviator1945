package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class ZeroMMachineSpec extends AviatorMMachineSpec {
	public static final String LABEL = "ZERO";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		// Per spec parameters
		parameters.acceleration =     0.6;  
		parameters.bankRecovery =     1.0;
		parameters.life =             2;
		parameters.maxV  =            6.00 * vCoefficient;
		parameters.minV  =            2.0 * vCoefficient;
		parameters.rollV =            2.4;
		parameters.yawRecovery =      0.5;
		parameters.yawV =             2.4;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_ZERO;
	}

	@Override
	public String label() {
		return LABEL;
	}
}
