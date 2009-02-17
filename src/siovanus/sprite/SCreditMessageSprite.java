package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.ImageObserver;

public class SCreditMessageSprite extends STitleSprite {
	int remnantEffectDuration = 50;
	int remnantDuration = -1;
	@Override
	public void paint_Protected(Graphics graphics, ImageObserver observer) {
		if (remnantDuration != -1 && remnantDuration > 0) {
			size((float) (originalSize * 1.01));
			double r = ((double)(remnantDuration))/(double)(remnantEffectDuration);
			super.color(new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), (int)(Math.min(255, 256 * r)))) ;
			super.lifetime(originalLifeTime);
			this.x = -1;
			remnantDuration--;
		} else if (remnantDuration == 0) {
			super.dispose();
		}
		this.y--;
		super.paint_Protected(graphics, observer);
	}
	
	@Override
	public void dispose() {
		remnantDuration = remnantEffectDuration;
	}
}
