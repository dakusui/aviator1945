package oreactor.video.sprite;

import java.awt.Graphics2D;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oreactor.exceptions.OpenReactorException;
import oreactor.video.Plane;
import oreactor.video.Viewport;

public class SpritePlane extends Plane {
	protected List<Sprite> sprites;

	public SpritePlane(String name, double width, double height, Viewport viewport) {
		super(name, width, height, viewport);
		this.sprites = new LinkedList<Sprite>();
	}

	public Sprite createSprite(SpriteSpec spec) {
		return createSprite(spec, -1);
	}
	public Sprite createSprite(SpriteSpec spec, int priority) {
		Sprite ret = spec.createSprite(priority);
		this.sprites.add(ret);
		return ret;
	}
	@Override
	public void prepare() {
		Collections.sort(sprites);
	}

	public boolean removeSprite(Sprite s) {
		return this.sprites.remove(s);
	}

	@Override
	protected void render_Protected(Graphics2D g) throws OpenReactorException {
		for (Sprite s : sprites) {
			s.render(g, isAcclerationEnabled());
		}
	}	
	
	public Iterator<Sprite> iterator() {
		return this.sprites.iterator();
	}
	
}
