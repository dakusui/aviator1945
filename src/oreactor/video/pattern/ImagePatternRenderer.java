package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.Image;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.video.sprite.Sprite;

import org.json.JSONException;
import org.json.JSONObject;

public class ImagePatternRenderer extends PatternRenderer {
	Image image = null;

	@Override
	public void init(JSONObject params) throws OpenReactorException {
		try {
			String imgresource = params.getString("image");
			this.image = ResourceLoader.getResourceLoader().loadImage(imgresource);
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException(e.getMessage(), e);
		}
	}

	@Override
	public void render(Graphics2D gg, Sprite sprite) {
		// TODO Auto-generated method stub

	}

}
