package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.VesselGunDrivant;
import siovanus.drivant.AviatorDrivant.AviatorParameters;
import avis.motion.Drivant;

public class USSNewJerseyGunMMachineSpec extends AviatorMMachineSpec {
	public static final String LABEL = "USS New Jersey(GUN)";

	@Override
	protected Drivant drivant() {
		return new VesselGunDrivant();
	}

	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		parameters.acceleration =     0;  
		parameters.bankRecovery =     0;
		parameters.life =             3;
		parameters.maxV  =            0.0;
		parameters.minV  =            0.0;
		parameters.rollV =            0.0;
		parameters.yawRecovery =      0.0;
		parameters.yawV =             0.2;	
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_USSNEWJERSEY_MAINGUN;
	}

	@Override
	public String label() {
		return LABEL;
	}
}
