package avis.sprite;

import java.awt.Graphics;
import java.awt.image.ImageObserver;

import avis.spec.AImageSpriteSpec;
import avis.spec.ASpriteSpec;


public class AImageSprite extends ASprite {

	protected int displayHeight;
	protected int displayWidth;
	protected AImageSpriteSpec imageSpec = null;

	public AImageSprite() {
		super();
	}

	public void displaySize(int w, int h) {
		displayWidth = w;
		displayHeight = h;
	}

	@Override
	public int height() {
		return displayHeight;
	}
	
	@Override
	protected void init_Protected(ASpriteSpec p) {
		AImageSpriteSpec pp = (AImageSpriteSpec)p;
		displayHeight = pp.defaultDisplayHeight();
		displayWidth  = pp.defaultDisplayWidth();
		this.imageSpec = pp;
	}

	@Override
	public void paint_Protected(Graphics g, ImageObserver observer) {
		imageSpec.paint(g, x, y, pattern, index, displayWidth, displayHeight, observer);
	}

	@Override
	public int width() {
		return displayWidth;
	}
}
