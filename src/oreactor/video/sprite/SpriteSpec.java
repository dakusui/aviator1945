package oreactor.video.sprite;

import java.awt.Graphics2D;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;
import oreactor.io.ResourceLoader;
import oreactor.video.sprite.SpriteRenderer.RenderingParameters;

import org.json.JSONObject;

public final class SpriteSpec extends BaseResource {
	private double width;
	private int height;
	private SpriteRenderer renderer = null;
	
	public SpriteSpec(String name) {
		super(name);
	}

	public void render(Graphics2D gg, Sprite sprite) {
		this.renderer.render(gg, sprite);
	}
	
	@Override
	public Type type() {
		return Type.SpriteSpec;
	}

	@Override
	public void init(JSONObject params, ResourceLoader loader) throws OpenReactorException {
		this.renderer.init(params, loader);
	}

	public void width(double w) {
		this.width = w;
	}

	public double width() {
		return this.width;
	}
	
	public void height(int h) {
		this.height = h;
	}
	
	public double height() {
		return this.height;
	}

	@SuppressWarnings("unchecked")
	public void loadRenderer(String className) {
		try {
			Class<? extends SpriteRenderer> rendererClass;
			rendererClass = (Class<? extends SpriteRenderer>) Class.forName(className);
			this.renderer = rendererClass.newInstance();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Sprite createSprite() {
		Sprite ret = new Sprite(this);
		RenderingParameters p = this.renderer.createRenderingParameters();
		ret.renderingParameters(p);
		return ret;
	}
}
