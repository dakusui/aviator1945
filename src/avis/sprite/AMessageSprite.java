package avis.sprite;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import java.awt.image.ImageObserver;

import siovanus.sprite.SShapeSprite;
import avis.base.Configuration;
import avis.spec.ASpriteSpec;
import avis.video.APlane;


public class AMessageSprite extends SShapeSprite {
	protected int lifetime = -1;
	protected Font font;
	
	public AMessageSprite() {
		super();
		this.font = Configuration.defaultFont;
	}

	protected String message = "";
	private GlyphVector glyphVector;
	private APlane plane;
	private Color color;
	
	@Override
	protected void init_Protected(ASpriteSpec p) {
	}

	public Font font() {
		return this.font;
	}
	
	public void font(Font font) {
		this.glyphVector = null;
		this.font = font;
	}
	
	public APlane plane() {
		return this.plane;
	}
	
	public void plane(APlane plane) {
		this.plane = plane;
	}
	
	public void color(Color color) {
		this.color = color;
	}
	
	public Color color() {
		return color;
	}
	
	public void size(float size) {
		this.glyphVector = null;
		this.font = this.font.deriveFont(size);
	}
	
	public int size() {
		return this.font.getSize();
	}
	
	public void lifetime(int lifetime) {
		this.lifetime = lifetime;
	}

	public int lifetime() {
		return this.lifetime;
	}

	@Override
	public void paint_Protected(Graphics graphics, ImageObserver observer) {
		if (lifetime == 0) {
			dispose();
			return;
		}
		if (lifetime > 0) {
			lifetime--;
		}
		Graphics2D graphics2d = (Graphics2D) graphics;
		if (glyphVector == null) {
			FontRenderContext frc = graphics2d.getFontRenderContext();
			glyphVector = font().createGlyphVector(frc, message);
			Point2D first  = glyphVector.getGlyphPosition(0);
			Point2D last = glyphVector.getGlyphPosition(glyphVector.getNumGlyphs());
			if (this.x == -1) {
				int width = (int) (plane() != null ? plane().width()
                                                    : 0);
				this.x = (int) (width/2 - (last.getX() - first.getX()) / 2);
			}
			if (this.y == -1) {
				int height = (int) (plane() != null ? plane().height()
                                                     : 0);
				this.y = (int) (height / 2);
			}
		}
		Color backupColor = graphics.getColor();
		try {
			graphics2d.setColor(color());
			graphics2d.drawGlyphVector(glyphVector, this.x, this.y);
		} finally {
			graphics.setColor(backupColor);
		}
	}

	public void message(String message) {
		this.message = message;
		this.glyphVector = null;
	}
	
	public String message() {
		return this.message;
	}
	
	@Override
	public boolean touches(Rectangle rect) {
		return super.touches(rect) ? true
								    : this.x == -1 || this.y == -1;
	}
}
