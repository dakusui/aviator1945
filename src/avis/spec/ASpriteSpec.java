package avis.spec;

import avis.base.AException;
import avis.base.Avis;
import avis.sprite.ASprite;

public abstract class ASpriteSpec {

	protected String name;

	final public ASprite createSprite(int priority) {
		ASprite ret = createSprite_Protected();
		ret.init(this);
		ret.priority(priority);
		return ret;
	}

	protected abstract ASprite createSprite_Protected();

	public abstract void init(Object resource) throws AException;

	public int patternDenominator() {
		return Avis.BANK_STEPS;
	}

	public void name(String name) {
		this.name = name;
	}

	public String name() {
		return this.name;
	}

	public void dispose() {
	}
}