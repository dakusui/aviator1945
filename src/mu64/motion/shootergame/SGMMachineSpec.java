/**
 * 
 */
package mu64.motion.shootergame;

import mu64.Mu64Reactor;
import mu64.motion.Attributes;
import mu64.motion.MMachine;
import mu64.motion.MMachineSpec;
import oreactor.video.sprite.Sprite;

public abstract class SGMMachineSpec extends MMachineSpec {
	private Mu64Reactor mu64reactor;

	protected SGMMachineSpec(Mu64Reactor mu64reactor) {
		this.mu64reactor = mu64reactor;
	}
	protected Sprite createSprite(String spriteSpecName) {
		return createSprite(spriteSpecName, -1);
	}

	protected Sprite createSprite(String spriteSpecName, int priority) {
		return mu64reactor.spriteplane().createSprite(mu64reactor.spritespec(spriteSpecName), priority);
	}

	@Override
	protected void fillInAttributes(Attributes attrs, MMachine parent) {
		fillInAttrs_((SGAttrs)attrs, parent);
	}
	
	public abstract void fillInAttrs_(SGAttrs attrs, MMachine parent);

	@Override
	protected void releaseSprite(Sprite s) {
		mu64reactor.spriteplane().removeSprite(s);
	}		
}