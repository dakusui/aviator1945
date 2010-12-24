package oreactor.video.sprite;

import java.awt.Graphics2D;

public class Sprite implements Renderable {
	private SpriteSpec spec;
	private double x;
	private double y;
	private double theta;
	private int i;

	public Sprite(SpriteSpec spec) {
		this.spec = spec;
	}

	public void put(double x, double y, double theta, int i) {
		this.x = x;
		this.y = y;
		this.theta = theta;
		this.i = i;
	}
	
	public SpriteSpec getSpec() {
		return this.spec;
	}

	@Override
	public void render(Graphics2D g) {
		this.spec.render(g, x, y, theta, i);
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
	
	public int i() {
		return this.i;
	}
}
