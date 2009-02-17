package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

import avis.base.Avis;
import avis.spec.ASpriteSpec;


public class SShotSprite extends SShapeSprite {

	public SShotSprite() {
		super();
	}
	
	private Color[] color1 = new Color[]{
			new Color(255, 255, 128, 255),
			new Color(255, 192, 64, 255),
			new Color(192, 32, 32, 192),
			new Color(128, 16, 16, 128),
	};
	private Color[] color2 = new Color[]{
			new Color(255, 156, 32, 128),
			new Color(255, 32, 32, 128),
			new Color(64, 16, 16, 128),
			new Color(32, 0, 0, 96),
	};
	protected int length = 4;

	@Override
	public void paint_Protected(Graphics g, ImageObserver observer) {
		double dx = 0, dy = 0; 
		for (int i = color1.length - 1; i >= 0; i--) {
			g.setColor(color1[i].brighter());      
			dx = - Avis.sin(index) * i;
			dy =   Avis.cos(index) * i;
			double ex = - length * Avis.cos(index) / 3;
			double ey = length * Avis.sin(index) /3;
			g.setColor(color1[i]);
			g.drawLine(
					(int)(x      + dx), (int)(y      + dy), 
					(int)(x + ex + dx), (int)(y + ey + dy)
			);
			g.drawLine(
					(int)(x       -dx), (int)(y      - dy), 
					(int)(x + ex  -dx), (int)(y + ey - dy)
			);
			g.setColor(color2[i]);
			g.drawLine(
					(int)(x + ex     + dx), (int)(y + ey     + dy), 
					(int)(x + ex * 4 + dx), (int)(y + ey * 4 + dy)
			);
			g.drawLine(
					(int)(x + ex     - dx), (int)(y + ey     - dy), 
					(int)(x + ex * 4 - dx), (int)(y + ey * 4 - dy)
			);
		}
		
		length += 2;
	}
	
	@Override
	protected void init_Protected(ASpriteSpec p) {
	}

	@Override
	public int width() {
		return length;
	}
	@Override
	public int height() {
		return length;
	}
}
