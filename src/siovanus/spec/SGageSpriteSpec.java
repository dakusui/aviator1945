package siovanus.spec;

import siovanus.drivant.aviator.Aviator;
import siovanus.sprite.SGageSprite;
import avis.base.AException;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;

public class SGageSpriteSpec extends ASpriteSpec {
	private Aviator aviator;

	public SGageSpriteSpec(Aviator aviator) {
		this.aviator = aviator;
	}
	
	public Aviator aviator() {
		return this.aviator;
	}
	
	public int width() {
		return 256;
	}

	public int height() {
		return 8;
	}

	@Override
	protected ASprite createSprite_Protected() {
		return new SGageSprite();
	}

	@Override
	public void init(Object resource) throws AException {
	}

}
