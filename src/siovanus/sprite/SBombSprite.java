package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import avis.base.Avis;
import avis.spec.ASpriteSpec;

public class SBombSprite extends SShapeSprite {

	public SBombSprite() {
		super();
	}

	private boolean white;

	@Override
	public void paint_Protected(Graphics g, ImageObserver observer) {
		if (!white) {
			g.setColor(Color.RED);
		} else {
			g.setColor(Color.WHITE);
		}
		white = !white;
		double dx = Avis.cos(index) * 2, dy = Avis.sin(index) * 2;
		g.fillRect((int)(x - dx), (int)(y - dy), (int)(x + dx), (int)(y + dy));
	}
	
	@Override
	protected void init_Protected(ASpriteSpec p) {
	}
	@Override
	public int width() {
		return 4;
	}
	@Override
	public int height() {
		return 4;
	}}