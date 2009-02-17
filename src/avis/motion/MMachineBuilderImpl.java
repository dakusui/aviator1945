package avis.motion;

import avis.spec.ASpriteSpec;
import avis.spec.ASpriteSpecManager;
import avis.sprite.ASprite;
import avis.video.APlane;

public class MMachineBuilderImpl implements MMachineBuilder {
	protected APlane plane;
	protected ASpriteSpecManager spriteSpecManager;

	protected MMachineBuilderImpl(ASpriteSpecManager manager, APlane plane) {
		this.plane = plane;
		this.spriteSpecManager = manager;
	}
	
	public synchronized static MMachineBuilder getInstance(ASpriteSpecManager manager, APlane plane) {
		return new MMachineBuilderImpl(manager, plane);
	}
	
	/* (non-Javadoc)
	 * @see avis.motion.IMMachineBuilder#buildMMachine(avis.motion.Drivant, avis.motion.Drivant.Parameters, avis.spec.ASpriteSpec, int)
	 */
	public MMachine buildMMachine(Drivant drivant, Parameters drivantParameters, String spriteSpecName, int priority) {
		drivant.init(drivantParameters);
		ASpriteSpec spriteSpec = spriteSpecManager.getSpriteSpec(spriteSpecName);
		ASprite sprite = spriteSpec.createSprite(priority);
		plane.attach(sprite);
			
		if (sprite != null) {
			sprite.visible(true);
		}
		
		MMachine ret = new MMachine(drivant, sprite);
		return ret;
	}

	public APlane plane() {
		return plane;
	}
	
	
}
