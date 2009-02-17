package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class B29MMachineSpec extends AviatorMMachineSpec {

	public static final String LABEL = "B29";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		// Per spec parameters
		parameters.acceleration =     1.0;  
		parameters.bankRecovery =     1.0;
		parameters.life =             12;
		parameters.maxV  =            2.4 * vCoefficient;
		parameters.minV  =            1.5 * vCoefficient;
		parameters.rollV =            0.2;
		parameters.yawRecovery =      0.1;
		parameters.yawV =             0.0;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_B29;
	}

	@Override
	public String label() {
		return LABEL;
	}

}
