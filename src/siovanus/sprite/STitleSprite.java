package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import avis.sprite.AMessageSprite;

public class STitleSprite extends AMessageSprite {
	int effectDuration = 120;
	int remainingFrames = effectDuration;
	Color originalColor;
	int originalLifeTime;
	protected float originalSize;

	public STitleSprite() {
		super();
	}

	@Override
	public void color(Color c) {
		originalColor = c;
		super.color(c);
	}

	@Override
	public void lifetime(int lifetime) {
		originalLifeTime = lifetime;
		super.lifetime(lifetime);
	}
	
	@Override
	public void size(float size) {
		this.originalSize = size;
		super.size(size);
	}
	
	@Override
	public void paint_Protected(Graphics graphics, ImageObserver observer) {
		if (remainingFrames > 0) {
			super.size(originalSize * (1 +  1 * ((float)remainingFrames/(float)effectDuration)));
			double r = ((double)(effectDuration-remainingFrames))/(double)(effectDuration);
			super.color(new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), (int)(Math.min(255, 32 + 256 * r)))) ;
			super.lifetime(originalLifeTime);
			remainingFrames--;
			this.x = -1;
		}
		super.paint_Protected(graphics, observer);
	}

}
