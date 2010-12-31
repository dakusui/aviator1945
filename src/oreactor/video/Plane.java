package oreactor.video;

import java.awt.Graphics2D;

import oreactor.core.BaseGear;
import oreactor.video.sprite.Renderable;

public abstract class Plane  extends BaseGear implements Renderable {
	static enum Type {
		Graphics,
		Sprite,
		Pattern;
	}
	
	final protected Region logicalRegion;
	final protected Region physicalRegion;
	final protected String name;

	protected Plane(String name, Region physical, Region logical) {
		super();
		this.name = name;
		this.physicalRegion = physical;
		this.logicalRegion = logical;
	}

	@Override
	public void render(Graphics2D g) {
		this.assertRenderingState();
		this.renderEngine(g);
	}

	protected abstract void renderEngine(Graphics2D g);
}

