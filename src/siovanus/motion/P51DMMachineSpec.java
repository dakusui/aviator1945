package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class P51DMMachineSpec extends AviatorMMachineSpec {
	public static final String LABEL = "P51D";

	@Override
	public String label() {
		return LABEL;
	}
	
	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		// Per spec parameters
		parameters.acceleration =     0.4;  
		parameters.bankRecovery =     1.0;
		parameters.life =             4;
		parameters.maxV  =            6.4 * vCoefficient;
		parameters.minV  =            4.0 * vCoefficient;
		parameters.rollV =            2.0;
		parameters.yawRecovery =      0.5;
		parameters.yawV =             2.0;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_P51D;
	}
}
