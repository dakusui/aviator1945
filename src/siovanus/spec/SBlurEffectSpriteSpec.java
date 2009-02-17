package siovanus.spec;

import siovanus.sprite.SBlurEffectSprite;
import avis.base.AException;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;

public class SBlurEffectSpriteSpec extends ASpriteSpec {
	public int width;
	public int height;

	@Override
	public ASprite createSprite_Protected() {
		return new SBlurEffectSprite();
	}

	@Override
	public void init(Object resource) throws AException {
		int[] r = (int[]) resource;
		width  = r[0];
		height = r[0];
	}
}
