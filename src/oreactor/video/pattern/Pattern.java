package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.VolatileImage;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.video.VideoUtil;

import org.json.JSONException;
import org.json.JSONObject;

public final class Pattern {
	Image image;
	VolatileImage vImage;

	private int num;
	private int h;
	private int w;
	
	public Pattern(int i,double w, double h) {
		this.num = i;
		this.w = (int) w;
		this.h = (int) h;
	}

	public int num() {
		return this.num;
	}
	
	
	public void render(Graphics2D g, double x, double y) {
		boolean isVCacheEnabled = true;
		GraphicsConfiguration gConfig = g.getDeviceConfiguration();
		if (isVCacheEnabled) {
			VolatileImage vTmp = this.vImage;
			if (vTmp == null) {
				vTmp = VideoUtil.createVolatileVersion(gConfig, this.image);
			}
			int i = 0;
			do {
				g.drawImage(
						vTmp,
						(int)x, (int)y, (int)(x + w), (int)(y + h), 
						0,      0,      (int)w,       (int)h, 
						null);
				i++;
				this.vImage = vTmp;
			} while ((vTmp = VideoUtil.createVolatileVersionForRetry(gConfig, this.image, vTmp)) != null);
			if (i > 1) {
				System.out.println("--- i = <" + i  + ">");
			}
		} else {
			g.drawImage(
					this.image, 
					(int)x, (int)y, (int)(x + w), (int)(y + h), 
					0,      0,      (int)w,       (int)h, 
					null);
		}
	}
	
	public void init(JSONObject json, ResourceLoader loader) throws OpenReactorException {
		try {
			String imgresource = json.getString("image");
			this.image = loader.getImage(imgresource).image();
			this.image.setAccelerationPriority((float)0.0);
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException(e.getMessage(), e);
		}
	}
	
}
