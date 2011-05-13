package oreactor.video;

import java.util.HashMap;
import java.util.Map;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.graphics.GraphicsPlane;
import oreactor.video.pattern.PatternPlane;
import oreactor.video.sprite.SpritePlane;

public class PlaneDesc {
	public static final String PATTERNWIDTH_KEY="patternwidth";
	public static final String PATTERNHEIGHT_KEY="patternheight";
	public static enum Type {
		Graphics {
			@Override
			protected Plane createPlane(PlaneDesc desc, Viewport viewport) {
				return new GraphicsPlane(desc.name(), desc.width(), desc.height(), viewport);
			}
		},
		Pattern {
			@Override
			protected Plane createPlane(PlaneDesc desc, Viewport viewport) {
				double patternwidth = ((Number)desc.get(PATTERNWIDTH_KEY)).doubleValue();
				double patternheight = ((Number)desc.get(PATTERNHEIGHT_KEY)).doubleValue();
				return new PatternPlane(desc.name(), desc.width(), desc.height(), patternwidth, patternheight, 256, viewport);
			}
		},
		Sprite {
			@Override
			protected Plane createPlane(PlaneDesc desc, Viewport viewport) {
				return new SpritePlane(desc.name(), desc.width(), desc.height(), viewport);
			}
		};
		protected abstract Plane createPlane(PlaneDesc desc, Viewport viewport);
	}
	protected String name;	
	protected Type type;
	protected double width;
	protected double height;
	protected Map<String, Object> map = new HashMap<String, Object>();
	
	public PlaneDesc(String name, Type type) throws OpenReactorException {
		if (name == null) {
			ExceptionThrower.throwParameterException("'name' must not be null.");
		}
		if (type == null) {
			ExceptionThrower.throwParameterException("'type' must not be null.");
		}
		this.name = name;
		this.type = type;
	}
	
	public String name() {
		return this.name;
	}
	
	public Type type() {
		return this.type;
	}
	
	public void width(double width) {
		this.width = width;
	}
	
	public double width() {
		return this.width;
	}

	public void height(double height) {
		this.height = height;
	}
	
	public double height() {
		return this.height;
	}
	
	public void put(String key, Object value) {
		this.map.put(key, value);
	}
	
	public Object get(String key) {
		return this.map.get(key);
	}

	public String toString() {
		return "Plane:" + this.name + "(" + this.type() + ")";
	}

	public Plane createPlane(Screen screen, Viewport viewport) {
		return this.type.createPlane(this, viewport);
	}
}
