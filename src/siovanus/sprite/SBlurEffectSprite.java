package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import siovanus.spec.SBlurEffectSpriteSpec;
import avis.spec.ASpriteSpec;
import avis.video.APlane;

public class SBlurEffectSprite extends SShapeSprite {
	private int width;
	private int height;
	private APlane targetPlane;
	
	@Override
	public void paint_Protected(Graphics graphics, ImageObserver observer) {
		Color c = new Color(64,64,64,96);
		Color backupColor = graphics.getColor();
		graphics.setColor(c);
		graphics.fillRect(0, 0, width, height);
		graphics.setColor(backupColor);
	}

	@Override
	protected void init_Protected(ASpriteSpec p) {
		SBlurEffectSpriteSpec pp = (SBlurEffectSpriteSpec) p;
		this.width = (int) pp.width;
		this.height = (int) pp.height;
	}

	public APlane targetPlane() {
		return targetPlane;
	}
	
	public void targetPlane(APlane plane) {
		this.targetPlane = plane;
	}
}
