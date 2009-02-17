package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;

import siovanus.drivant.aviator.Aviator;
import siovanus.drivant.aviator.UserAviator;
import siovanus.spec.SGageSpriteSpec;

import avis.spec.ASpriteSpec;

public class SGageSprite extends SShapeSprite {

	private Aviator aviator;

	@Override
	protected void init_Protected(ASpriteSpec p) {
		SGageSpriteSpec spec = (SGageSpriteSpec) p;
		this.aviator = spec.aviator();
	}

	@Override
	protected void paint_Protected(Graphics graphics, ImageObserver observer) {
		Graphics2D gg = (Graphics2D) graphics;
		SGageSpriteSpec sspec = (SGageSpriteSpec) spec;
		Color backupColor = gg.getColor();
		try {
			int w = sspec.width() * aviator.energy() / UserAviator.INITIAL_ENERGY;
			if (aviator.isTwisting()) {
				gg.setColor(Color.cyan);
			} else if (aviator.energy() > UserAviator.TWIST_ENERGY_REQUIRED) {
				gg.setColor(Color.green);
			} else {
				gg.setColor(Color.red);
			}
			gg.fillRect(x, y, w, sspec.height());
			gg.setColor(Color.yellow);
			gg.drawRect(x, y, (sspec.width()), sspec.height());
		} finally {
			gg.setColor(backupColor);
		}

	}

}
