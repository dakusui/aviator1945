package oreactor.video.pattern;

import java.awt.Graphics2D;

import oreactor.exceptions.OpenReactorException;
import oreactor.video.sprite.Sprite;

import org.json.JSONObject;

public abstract class PatternRenderer {
	public PatternRenderer() {
	}

	public abstract void render(Graphics2D gg, Sprite sprite);
	
	public abstract void init(JSONObject params) throws OpenReactorException;
}
