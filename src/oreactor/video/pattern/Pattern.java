package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.Image;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;
import oreactor.io.ResourceLoader;

import org.json.JSONException;
import org.json.JSONObject;

public final class Pattern  extends BaseResource {
	Image image;
	private int id;
	
	public Pattern(int i) {
		super(Integer.toString(i));
		this.id = i;
	}

	public int id() {
		return this.id;
	}
	
	public void render(Graphics2D g, double x, double y, double w, double h) {
		g.drawImage(
				this.image, 
				(int)x, (int)y, (int)w, (int)h, 
				null
				);
	}
	
	@Override
	public void init(JSONObject json, ResourceLoader loader) throws OpenReactorException {
		try {
			String imgresource = json.getString("image");
			this.image = ResourceLoader.getResourceLoader().loadImage(imgresource);
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException(e.getMessage(), e);
		}
	}
	
	@Override
	public Type type() {
		return Type.Pattern;
	}
}
