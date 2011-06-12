package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.video.VideoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Pattern {
	private int h;
	private int num;

	private int w;
	Image image;
	VolatileImage vImage;
	
	public Pattern(int i,double w, double h) {
		this.num = i;
		this.w = (int) w;
		this.h = (int) h;
	}

	public void init(JSONObject json, ResourceLoader loader) throws OpenReactorException {
		String url = null;
		try {
			JSONArray imageparams = json.getJSONArray("image");
			try {
				url = imageparams.getString(0);
				int ox = imageparams.getInt(1);
				int oy = imageparams.getInt(2);
				int w = imageparams.getInt(3);
				int h = imageparams.getInt(4);
				Image im = new BufferedImage(this.w, this.h, BufferedImage.TYPE_INT_ARGB); 
				Graphics2D gg = ((Graphics2D)im.getGraphics()); 
				gg.drawImage(loader.getImage(url).image(), 0, 0, this.w, this.h, ox, oy, ox + w, oy + h, null);
				gg.dispose();
				this.image = im;
			} catch (JSONException e) {
				String msg = "Malformed configuration item : <" + url + ">";
				ExceptionThrower.throwMalformedConfigurationException(msg, e);
			}
		} catch (JSONException e) {
			String imgresource;
			try {
				imgresource = json.getString("image");
				this.image = loader.getImage(imgresource).image();
			} catch (JSONException e1) {
				ExceptionThrower.throwMalformatJsonException(e.getMessage(), e);
			}
		}
	}
	
	
	public int num() {
		return this.num;
	}
	
	public void render(Graphics2D g, double x, double y, boolean accelerationEnabled) throws OpenReactorException {
		GraphicsConfiguration gConfig = g.getDeviceConfiguration();
		if (accelerationEnabled) {
			VolatileImage vTmp = this.vImage;
			if (vTmp == null) {
				vTmp = VideoUtil.getVolatileVersion(gConfig, this.image);
			}
			do {
				g.drawImage(
						vTmp,
						(int)x, (int)y, (int)(x + w), (int)(y + h), 
						0,      0,      (int)w,       (int)h, 
						null);
				this.vImage = vTmp;
			} while ((vTmp = VideoUtil.getVolatileVersionIfContentsLost(gConfig, this.image, vImage)) != null);
		} else {
			g.drawImage(
					this.image, 
					(int)x, (int)y, (int)(x + w), (int)(y + h), 
					0,      0,      (int)w,       (int)h, 
					null);
		}
	}
}
