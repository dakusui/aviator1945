package oreactor.video.sprite;

import java.awt.Graphics2D;

public final class Sprite {
	private SpriteSpec spec;
	private double x;
	private double y;
	private double theta;
	private SpriteSpec.RenderingParameters parameters;

	Sprite(SpriteSpec spec) {
		this.spec = spec;
	}

	public void put(double x, double y, double theta) {
		this.x = x;
		this.y = y;
		this.theta = theta;
	}
	
	public SpriteSpec getSpec() {
		return this.spec;
	}

	public void render(Graphics2D g) {
		this.spec.render(g, this);
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

}
