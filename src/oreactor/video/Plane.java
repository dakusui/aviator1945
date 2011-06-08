package oreactor.video;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import oreactor.exceptions.OpenReactorException;

public abstract class Plane implements ViewportObserver {
	static enum Type {
		Graphics,
		Sprite,
		Pattern;
	}
	protected Viewport viewport;
	protected String name;
	protected double width;
	protected double height;
	protected VideoUtil util;
	private boolean enabled = true;
	
	protected Plane(String name, double width, double height, Viewport viewport) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
		this.viewport = viewport;
		this.viewport.addObserver(this);
	}

	public void prepare() {
		
	}
	
	public void render(Graphics2D g, double screenWidth, double screenHeight) throws OpenReactorException {
		if (this.enabled) {
			AffineTransform tx = g.getTransform();
			g.setTransform(this.viewport().affineTransform(screenWidth, screenHeight));
			try {
				this.render_Protected(g);
			} finally {
				g.setTransform(tx);
			}
		}
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
	
	protected VideoUtil util() {
		return this.util;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" +  System.identityHashCode(this) + ")";
	}
	
	public void enable() {
		this.enabled = true;
	}
	
	public void disable() {
		this.enabled = false;
	}
	
	@Override
	public void viewportChanged(Viewport viewport) {
	}
}

