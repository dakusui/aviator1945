package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.Image;

import org.json.JSONObject;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;
import oreactor.video.PatternPlane;

public final class Pattern  extends BaseResource {
	Image image;
	PatternPlane parent;
	private PatternRenderer renderer;
	
	public Pattern(String name) {
		super(name);
	}

	public void render(Graphics2D g, double x, double y, double w, double h) {
		g.drawImage(
				this.image, 
				(int)x, (int)y, (int)(x + w) -1, (int)(y + h) - 1, 
				0, 0, (int)this.parent.patternWidth(), (int)this.parent.patternHeight(), 
				null
				);
	}
	@Override
	public void init(JSONObject json) throws OpenReactorException {
		renderer.init(json);
	}
	
	@Override
	public Type type() {
		return Type.Pattern;
	}
	
	@SuppressWarnings("unchecked")
	public void loadRenderer(String className) throws OpenReactorException {
		try {
			Class<? extends PatternRenderer> rendererClass;
			rendererClass = (Class<? extends PatternRenderer>) Class.forName(className);
			this.renderer = rendererClass.newInstance();
		} catch (ClassNotFoundException e) {
			ExceptionThrower.throwPatternLoadFailure("Failed to load pattern:<" + this.name() + ">", e);
		} catch (InstantiationException e) {
			ExceptionThrower.throwPatternLoadFailure("Failed to load pattern:<" + this.name() + ">", e);
		} catch (IllegalAccessException e) {
			ExceptionThrower.throwPatternLoadFailure("Failed to load pattern:<" + this.name() + ">", e);
		}
	}
}
