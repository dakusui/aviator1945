package oreactor.video.sprite;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import oreactor.io.BaseResource;

public class SpriteSpec extends BaseResource {
	protected BufferedImage[] images;

	protected SpriteSpec(String name) {
		super(name);
	}

	public void render(Graphics2D g, double x, double y, double theta, int i) {
		
	}
	
	@Override
	public Type type() {
		return Type.SpriteSpec;
	}
}
