package avis.motion;

import avis.video.APlane;


public interface MMachineBuilder {

	public abstract MMachine buildMMachine(
			Drivant drivant,
			Parameters drivantParameters, 
			String spriteSpecName, 
			int priority);
	
	public APlane plane();

}