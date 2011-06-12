package oreactor.video.sprite;

import java.awt.Graphics2D;

import oreactor.exceptions.OpenReactorException;

public final class Sprite implements Comparable<Sprite> {
	private static final int INITIAL_VALUE = 10240;
	private static int defaultPriority = INITIAL_VALUE;
	private SpriteSpec spec;
	private double x;
	private double y;
	private double theta;
	private SpriteSpec.RenderingParameters parameters;
	private int priority;
	private double zoom = 1.0;
	
	Sprite(SpriteSpec spec, int priority) {
		this.spec = spec;
		this.priority = priority >=0 ? priority : defaultPriority++;
	}

	public void put(double x, double y) {
		put(x, y, 0, 1.0);
	}
	
	public void put(double x, double y, double theta) {
		put(x, y, theta, 1.0);
	}

	public void put(double x, double y, double theta, double zoom) {
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.zoom = zoom;
	}

	public SpriteSpec getSpec() {
		return this.spec;
	}

	public void render(Graphics2D g, boolean accelerationEnabled) throws OpenReactorException {
		this.spec.render(g, this, accelerationEnabled);
	}
	
	public double x() {
		return this.x;
	}
	
	public double y() {
		return this.y;
	}
	
	public double theta() {
		return this.theta;
	}
	
	public double zoom() {
		return this.zoom;
	}
	
	public SpriteSpec.RenderingParameters renderingParameters() {
		return this.parameters;
	}

	void renderingParameters(SpriteSpec.RenderingParameters p) {
		this.parameters = p;
	}

	public double width() {
		return spec.width();
	}
	
	public double height() {
		return spec.height();
	}

	@Override
	public int compareTo(Sprite o) {
		if (o == null) {
			return 1;
		}
		return o.priority - this.priority;
	}

	public static void resetDefaultPriorityCounter() {
		defaultPriority = INITIAL_VALUE;
	}
}
