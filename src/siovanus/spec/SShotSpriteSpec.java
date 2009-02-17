package siovanus.spec;

import avis.base.AException;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;
import siovanus.sprite.SShotSprite;

public class SShotSpriteSpec extends ASpriteSpec {

	@Override
	public ASprite createSprite_Protected() {
		SShotSprite ret = new SShotSprite();
		return ret;
	}

	@Override
	public void init(Object resource) throws AException {
		// TODO Auto-generated method stub
	}
}
