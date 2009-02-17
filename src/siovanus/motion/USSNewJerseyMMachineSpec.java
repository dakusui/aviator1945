package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;

public class USSNewJerseyMMachineSpec extends AviatorMMachineSpec {
	public static final String LABEL = "USS New Jersey";

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		parameters.acceleration =     1.0;  
		parameters.bankRecovery =     1.0;
		parameters.life =             20;
		parameters.maxV  =            0.5;
		parameters.minV  =            0.1;
		parameters.rollV =            0.0;
		parameters.yawRecovery =      0.0;
		parameters.yawV =             0.0;	
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_USSNEWJERSEY;
	}

	@Override
	public String label() {
		return LABEL;
	}

}
