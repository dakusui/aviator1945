package siovanus.spec;

import siovanus.sprite.STitleSprite;
import avis.base.AException;
import avis.spec.AMessageSpriteSpec;
import avis.sprite.ASprite;

public class STitleSpriteSpec extends AMessageSpriteSpec {

	@Override
	public ASprite createSprite_Protected() {
		return new STitleSprite();
	}

	@Override
	public void init(Object resource) throws AException {
		super.init(resource);
	}

}
