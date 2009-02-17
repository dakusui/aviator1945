package siovanus.motion;

import siovanus.SGroup;
import siovanus.Siovanus;
import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.SEmittableDrivant.SEmittableParameters;
import avis.base.Avis;
import avis.motion.Group;

public class EnemyShotMMachineSpec extends EmittableMMachineSpec {
	double vCoefficient = 1.0; 
	@Override
	protected void setupEmittableParameters(SEmittableParameters parameters) {
		AviatorDrivant parent = additionalParameter();
		parameters.groupId = parent.groupId();
		parameters.acceleration = 1.8;
		parameters.x = parent.x() + parent.gunDeltaX() ;
		parameters.y = parent.y() + parent.gunDeltaY() ;
		parameters.direction = (parent.viewDirection() + parent.gunBias()) % Avis.DIRECTION_STEPS;
		parameters.maxV = 48 * vCoefficient;
		parameters.minV = 24 * vCoefficient;

//		parameters.lifeTime = 5;
		parameters.lifeTime = 12;
		Group parentGroupId =  parent.groupId();
		if (parentGroupId  == SGroup.Player_Aerial || parentGroupId == SGroup.Player_Surface) {
			parameters.groupId = SGroup.Player_Aerial;
		} else if (parentGroupId == SGroup.Enemy_Aerial || parentGroupId == SGroup.Enemy_Surface) {
			parameters.groupId = SGroup.Enemy_Aerial;
		}
	}
	

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_SHOT;
	}
}
