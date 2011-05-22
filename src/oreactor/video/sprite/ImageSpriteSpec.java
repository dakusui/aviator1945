package oreactor.video.sprite;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.VolatileImage;
import java.util.LinkedList;
import java.util.List;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.video.VideoUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImageSpriteSpec extends SpriteSpec {
	public static class RenderingParameters extends SpriteSpec.RenderingParameters {
		int patternno = 0;
		int x;
		int y;
		int w;
		int h;
		
		RenderingParameters(ImageSpriteSpec spec) {
			this.crop(0, 0, (int)spec.width(), (int)spec.height());
		}
		
		void patternno(int i) {
			this.patternno = i;
		}
		public int patternno() {
			return this.patternno;
		}
		public void crop(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}

	protected Image images[] = null;
	protected VolatileImage vImages[] = null;

	public ImageSpriteSpec(String name) {
		super(name);
	}

	@Override
	public void init(JSONObject params, ResourceLoader loader) throws OpenReactorException {
		try {
			JSONArray resources = params.getJSONArray("images");
			int len = resources.length();
			List<Image> imageList = new LinkedList<Image>();
			for (int i = 0; i < len; i ++) {
				String cur;
				cur = resources.getString(i);
				Image im = loader.getImage(cur).image();
				imageList.add(im);
			}
			images = new Image[0];
			images = imageList.toArray(images);
			vImages = new VolatileImage[images.length];
		} catch (JSONException e) {
			System.out.println("--->" + params.toString());
			ExceptionThrower.throwMalformedConfigurationException(e.getMessage(), e);
		}
	}
	
	@Override
	public void render(Graphics2D gg, Sprite sprite) {
		boolean isVCacheEnabled = true;
		GraphicsConfiguration gConfig = gg.getDeviceConfiguration();
		AffineTransform backup = gg.getTransform();
		try {
			if (isVCacheEnabled) {
				double x = sprite.x();
				double y = sprite.y();
				double biasX = sprite.width() / 2;
				double biasY = sprite.height() / 2;
				double theta = sprite.theta();
				RenderingParameters p = (RenderingParameters) sprite.renderingParameters();
				int patternno = 0;
				if (p != null) {
					patternno = p.patternno();
				}
				Image img = images[patternno];
				VolatileImage vImage = vImages[patternno];
				if (vImage == null) {
					vImage = VideoUtil.createVolatileVersion(gConfig, img, (int)width(), (int)height());
				}
				int i = 0;
				do {
					gg.rotate(theta, x, y);
					drawImage(gg, vImage, x, y, biasX, biasY, p);
					vImages[patternno] = vImage;
					i ++;
				} while ((vImage = VideoUtil.createVolatileVersionForRetry(gConfig, img, vImage, (int)width(), (int)height())) != null);
				if (i > 1) {
					System.out.println("--: i = <" + i  + ">");
				}				
			} else {
				double x = sprite.x();
				double y = sprite.y();
				double biasX = sprite.width() / 2;
				double biasY = sprite.height() / 2;
				double theta = sprite.theta();
				RenderingParameters p = (RenderingParameters) sprite.renderingParameters();
				int patternno = 0;
				if (p != null) {
					patternno = p.patternno();
				}
				gg.rotate(theta, x, y);
				drawImage(gg, this.images[patternno], x, y, biasX, biasY, p);
			}
		} finally {
			gg.setTransform(backup);
		}
	}

	private void drawImage(
			Graphics2D gg, Image image, 
			double x, double y, double biasX, double biasY,
			RenderingParameters p) {
		gg.drawImage(
				image,
				(int)(x - biasX),            
				(int)(y - biasY), 
				(int)(x + biasX),
				(int)(y + biasY), 
				0,         
				0,
				(int)(p.w),
				(int)(p.h),
				null
		);
		
	}

	@Override
	public RenderingParameters createRenderingParameters() {
		return new RenderingParameters(this);
	}

}
