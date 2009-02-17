package siovanus.spec;

import siovanus.sprite.SCreditMessageSprite;
import avis.base.AException;
import avis.sprite.ASprite;

public class SCreditMessageSpriteSpec extends STitleSpriteSpec {
	@Override
	public ASprite createSprite_Protected() {
		return new SCreditMessageSprite();
	}

	@Override
	public void init(Object resource) throws AException {
		super.init(resource);
	}
}
