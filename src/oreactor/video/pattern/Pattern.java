package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.Image;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;

import org.json.JSONException;
import org.json.JSONObject;

public final class Pattern {
	Image image;
	private int num;
	
	public Pattern(int i) {
		this.num = i;
	}

	public int num() {
		return this.num;
	}
	
	public void render(Graphics2D g, double x, double y, double w, double h) {
		g.drawImage(
				this.image, 
				(int)x, (int)y, (int)w, (int)h, 
				null
				);
	}
	
	public void init(JSONObject json, ResourceLoader loader) throws OpenReactorException {
		try {
			String imgresource = json.getString("image");
			this.image = loader.getImage(imgresource).image();
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException(e.getMessage(), e);
		}
	}
	
}
