package oreactor.video;

import java.awt.Graphics2D;

import oreactor.core.BaseGear;

public abstract class Plane  extends BaseGear implements Renderable {
	static enum Type {
		Graphics,
		Sprite,
		Pattern;
	}
	protected Viewport viewport;
	protected String name;
	protected double width;
	protected double height;

	protected Plane(String name, double width, double height) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
		this.viewport = new Viewport(this.width, this.height);
	}

	@Override
	public void render(Graphics2D g) {
		this.renderEngine(g);
	}

	public Viewport viewport() {
		return this.viewport;
	}

	public String name() {
		return this.name;
	}
	protected abstract void renderEngine(Graphics2D g);
}

