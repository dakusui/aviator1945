package avis.spec;

import avis.base.AException;
import avis.sprite.AMessageSprite;
import avis.sprite.ASprite;

public class AMessageSpriteSpec extends ASpriteSpec {

	@Override
	public ASprite createSprite_Protected() {
		return new AMessageSprite();
	}

	@Override
	public void init(Object resource) throws AException {
	}
}
