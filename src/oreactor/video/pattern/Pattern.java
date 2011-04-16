package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.Image;

import oreactor.video.PatternPlane;

public class Pattern {
	Image image;
	PatternPlane parent;
	public void render(Graphics2D g, double x, double y, double w, double h) {
		g.drawImage(
				this.image, 
				(int)x, (int)y, (int)(x + w) -1, (int)(y + h) - 1, 
				0, 0, (int)this.parent.patternWidth(), (int)this.parent.patternHeight(), 
				null
				);
	}
}
