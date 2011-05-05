package oreactor.video;

import java.awt.Graphics2D;

import oreactor.exceptions.OpenReactorException;

public abstract class Plane {
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

	public void prepare() {
		
	}
	
	public void render(Graphics2D g) throws OpenReactorException {
		this.render_Protected(g);
	}

	public void finish() {
		
	}
	
	public Viewport viewport() {
		return this.viewport;
	}

	public String name() {
		return this.name;
	}
	
	protected abstract void render_Protected(Graphics2D g) throws OpenReactorException;
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" +  System.identityHashCode(this) + ")";
	}
}

