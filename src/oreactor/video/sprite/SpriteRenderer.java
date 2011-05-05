package oreactor.video.sprite;

import java.awt.Graphics2D;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;

import org.json.JSONObject;

public abstract class SpriteRenderer {
	static class RenderingParameters {
	}
	public SpriteRenderer() {
	}

	public abstract void render(Graphics2D gg, Sprite sprite);
	
	public abstract RenderingParameters createRenderingParameters();

	public abstract void init(JSONObject params, ResourceLoader loader) throws OpenReactorException;
}
