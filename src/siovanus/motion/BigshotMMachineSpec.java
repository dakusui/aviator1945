package siovanus.motion;

import siovanus.drivant.SBigshotDrivant;
import avis.motion.Drivant;

public class BigshotMMachineSpec extends ShotMMachineSpec {
	@Override
	protected Drivant drivant() {
		return new SBigshotDrivant();
	}
	

}
