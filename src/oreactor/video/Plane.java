package oreactor.video;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import oreactor.exceptions.OpenReactorException;

public abstract class Plane implements ViewportObserver, VideoDevice.Observer, Comparable<Plane> {
	static enum Type {
		Graphics,
		Pattern,
		Sprite;
	}
	private boolean enabled = true;
	private int priority = 100;
	protected double height;
	protected String name;
	protected VideoUtil util;
	protected Viewport viewport;
	protected double width;
	private boolean accelerationEnabled;
	protected double screenWidth;
	protected double screenHeight;
	protected AffineTransform matrix = null;
	
	protected Plane(String name, double width, double height, Viewport viewport) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
		this.viewport = viewport;
		this.viewport.addObserver(this);
		this.enableAcceleration();
	}

	@Override
	public int compareTo(Plane another) {
		if (another == null) {
			return 1;
		}
		return another.priority - this.priority;
	}
	
	public void disable() {
		this.enabled = false;
	}

	public void enable() {
		this.enabled = true;
	}
	
	public void finish() {
		
	}

	public double height() {
		return height;
	}
	
	public String name() {
		return this.name;
	}
	
	public void prepare() {
		
	}
	
	public int priority() {
		return this.priority;
	}
	
	public void priority(int priority) {
		this.priority = priority;
	}
	
	public void render(Graphics2D g, double screenWidth, double screenHeight) throws OpenReactorException {
		AffineTransform tx = g.getTransform();
		g.setTransform(this.viewport().composeMatrix(screenWidth, screenHeight));
		try {
			this.render_Protected(g);
		} finally {
			g.setTransform(tx);
		}
	}
	
	public boolean isEnabled() {
		return this.enabled;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + "(" +  System.identityHashCode(this) + ")";
	}
	public Viewport viewport() {
		return this.viewport;
	}
	@Override
	public void viewportChanged(Viewport viewport) {
		this.viewport = viewport;
		updateMatrix();
	}
	
	public double width() {
		return width;
	}
	protected abstract void render_Protected(Graphics2D g) throws OpenReactorException;
	protected VideoUtil util() {
		return this.util;
	}
	
	public void enableAcceleration() {
		this.accelerationEnabled = true;
	}
	public void disableAcceleration() {
		this.accelerationEnabled = false;
	}
	public boolean isAcclerationEnabled() {
		return this.accelerationEnabled;
	}

	@Override
	public void sizeChanged(double screenWidth, double screenHeight) {
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		updateMatrix();
	}

	private void updateMatrix() {
		try {
			this.matrix = viewport.composeMatrix(this.screenWidth, this.screenHeight);
		} catch (OpenReactorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

