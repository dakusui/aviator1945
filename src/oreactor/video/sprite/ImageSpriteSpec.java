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
		private boolean cropEnabled;
		
		public void patternno(int i) {
			this.patternno = i;
		}
		public int patternno() {
			return this.patternno;
		}
		public void enableCropping(int x, int y, int w, int h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.cropEnabled = true;
		}
		public void diableCropping() {
			this.cropEnabled = false;
		}
		public boolean isCroppingEnabled() {
			return this.cropEnabled;
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
			ExceptionThrower.throwMalformedConfigurationException(
					e.getMessage() + "<json:" + params + ">",
					e
			);
		}
	}
	
	@Override
	public void render(Graphics2D gg, Sprite sprite) {
		boolean isVCacheEnabled = false;
		GraphicsConfiguration gConfig = gg.getDeviceConfiguration();
		AffineTransform backup = gg.getTransform();
		try {
			int ox, oy, ow, oh;
			RenderingParameters p = (RenderingParameters) sprite.renderingParameters();
			int patternno = 0;
			patternno = p.patternno();
			Image img = images[patternno];
			if (p.isCroppingEnabled()) {
				ox = p.x;
				oy = p.y;
				ow = p.w;
				oh = p.h;
				//System.out.print("p.x=" + p.x + ",p.y="+p.y+",p.w=" + p.w + ",p.h=" + p.h);
			} else {
				ox = 0;
				oy = 0;
				ow = img.getWidth(null);
				oh = img.getHeight(null);
			}
			if (isVCacheEnabled) {
				double x = sprite.x();
				double y = sprite.y();
				double biasX = sprite.width() / 2;
				double biasY = sprite.height() / 2;
				double theta = sprite.theta();
				VolatileImage vImage = vImages[patternno];
				if (vImage == null) {
					vImage = VideoUtil.createVolatileVersion(gConfig, img, (int)width(), (int)height());
				}
				do {
					gg.rotate(theta, x, y);
					drawImage(gg, vImage, x, y, biasX, biasY, ox, oy, ow, oh);
					vImages[patternno] = vImage;
				} while (
					(vImage = VideoUtil.createVolatileVersionForRetry(gConfig, img, vImage, (int)width(), (int)height())) != null
					 );
			} else {
				double x = sprite.x();
				double y = sprite.y();
				double biasX = sprite.width() / 2;
				double biasY = sprite.height() / 2;
				double theta = sprite.theta();
				gg.rotate(theta, x, y);
				drawImage(gg, this.images[patternno], x, y, biasX, biasY, ox, oy, ow, oh);
			}
		} finally {
			gg.setTransform(backup);
		}
	}

	private void drawImage(
			Graphics2D gg, Image image, 
			double x, double y, double biasX, double biasY,
			int ox, int oy, int ow, int oh
			) {
		gg.drawImage(
				image,
				(int)(x - biasX),            
				(int)(y - biasY), 
				(int)(x + biasX),
				(int)(y + biasY),
				ox,
				oy,
				ox + ow,
				oy + oh,
				null
		);
		
	}

	@Override
	public RenderingParameters createRenderingParameters() {
		return new RenderingParameters();
	}

}
