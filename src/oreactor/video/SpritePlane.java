package oreactor.video;

import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oreactor.video.sprite.Sprite;
import oreactor.video.sprite.SpriteSpec;

public class SpritePlane extends Plane {
	protected List<Sprite> sprites;

	protected SpritePlane(String name, double width, double height) {
		super(name, width, height);
		this.sprites = new LinkedList<Sprite>();
	}

	public Sprite createSprite(SpriteSpec spec) {
		Sprite ret = new Sprite(spec);
		this.sprites.add(ret);
		return ret;
	}
	
	public boolean removeSprite(Sprite s) {
		return this.sprites.remove(s);
	}

	@Override
	protected void renderEngine(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
	
	public Iterator<Sprite> iterator() {
		return this.sprites.iterator();
	}
}
