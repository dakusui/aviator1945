package siovanus.spec;

import siovanus.sprite.SBombSprite;
import avis.base.AException;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;

public class SBombSpriteSpec extends ASpriteSpec {

	@Override
	protected ASprite createSprite_Protected() {
		SBombSprite ret = new SBombSprite();
		return ret;
	}

	@Override
	public void init(Object resource) throws AException {
		// TODO Auto-generated method stub
	}

}
