package siovanus.motion;

import siovanus.SGroup;
import siovanus.Siovanus;
import siovanus.drivant.SEmittableDrivant.SEmittableParameters;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.Group;

public class BombMMachineSpec extends EmittableMMachineSpec {

	@Override
	protected void setupEmittableParameters(SEmittableParameters parameters) {
		Drivant parent = additionalParameter();
		parameters.groupId = parent.groupId();
		parameters.acceleration = 0.2;
		parameters.x = parent.x();
		parameters.y = parent.y();
		parameters.velocity = parent.velocity() * 1.4;
		parameters.direction = parent.viewDirection() % Avis.DIRECTION_STEPS;
		parameters.maxV = parent.velocity() * 1.4;
		parameters.minV = parent.velocity() * 0.5;
		
		parameters.lifeTime = 30;
		Group parentGroupId =  parent.groupId();
		if (parentGroupId  == SGroup.Player_Aerial || parentGroupId == SGroup.Player_Surface) {
			parameters.groupId = SGroup.Player_Surface;
		} else if (parentGroupId == SGroup.Enemy_Aerial || parentGroupId == SGroup.Enemy_Surface) {
			parameters.groupId = SGroup.Enemy_Surface;
		}
	}
	

	@Override
	protected String spriteSpecName() {
		return Siovanus.SPRITESPEC_BOMB;
	}


	@Override
	public String label() {
		return null ;
	}
}
