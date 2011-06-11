package oreactor.video.sprite;

import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;

import org.json.JSONObject;

public abstract class SpriteSpec {
	static class RenderingParameters {
	}
	public abstract RenderingParameters createRenderingParameters();

	private double width;
	private int height;
	private String name;
	
	public SpriteSpec(String name) {
		this.name = name;
	}

	public abstract void render(Graphics2D gg, Sprite sprite);
	

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
	public static SpriteSpec loadSpec(String name, String className) {
		SpriteSpec ret = null;
		try {
			Class<? extends SpriteSpec> specClass;
			specClass = (Class<? extends SpriteSpec>) Class.forName(className);
			ret = specClass.getConstructor(String.class).newInstance(name);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
		
	}
	
	public Sprite createSprite(int priority) {
		Sprite ret = new Sprite(this, priority);
		RenderingParameters p = this.createRenderingParameters();
		ret.renderingParameters(p);
		return ret;
	}
	
	public String name() {
		return this.name;
	}

	public abstract void init(JSONObject params, ResourceLoader loader) throws OpenReactorException ;
}
