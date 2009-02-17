package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class AvengerMMachineSpec extends AviatorMMachineSpec {
	
	public static final String LABEL = "Avenger";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		// Per spec parameters
		parameters.acceleration =     0.4;  
		parameters.bankRecovery =     1.0;
		parameters.life =             1;
		parameters.maxV  =            5.0 * vCoefficient;
		parameters.minV  =            4.0 * vCoefficient;
		parameters.rollV =            1.0;
		parameters.yawRecovery =      0.5;
		parameters.yawV =             1.0;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_AVG;
	}

	@Override
	public String label() {
		return LABEL;
	}
}
