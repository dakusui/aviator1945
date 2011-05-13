package oreactor.video.sprite;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oreactor.video.Plane;
import oreactor.video.Viewport;

public class SpritePlane extends Plane {
	protected List<Sprite> sprites;

	public SpritePlane(String name, double width, double height, Viewport viewport) {
		super(name, width, height, viewport);
		this.sprites = new LinkedList<Sprite>();
	}

	public Sprite createSprite(SpriteSpec spec) {
		Sprite ret = spec.createSprite();
		this.sprites.add(ret);
		return ret;
	}
	
	public boolean removeSprite(Sprite s) {
		return this.sprites.remove(s);
	}

	@Override
	protected void render_Protected(Graphics2D g) {
		for (Sprite s : sprites) {
			s.render(g);
		}
	}	
	
	public Iterator<Sprite> iterator() {
		return this.sprites.iterator();
	}
}
