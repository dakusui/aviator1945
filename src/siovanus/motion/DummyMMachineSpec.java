package siovanus.motion;

import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant.AviatorParameters;


public class DummyMMachineSpec extends AviatorMMachineSpec {
	@Override
	protected void setupAviatorParameters(AviatorParameters parameters) {
		parameters.maxV = 0;
		parameters.minV = 0;
		parameters.rollV = 0;
		parameters.yawDirection = 0;
		parameters.yawV = 0;
		parameters.acceleration = 0;
	}

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_ZERO;
	}

	@Override
	public String label() {
		return null;
	}
}
