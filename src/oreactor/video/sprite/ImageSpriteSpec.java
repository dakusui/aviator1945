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
		public void patternno(int i) {
			this.patternno = i;
		}
		public int patternno() {
			return this.patternno;
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
				SpriteSpec spec = sprite.getSpec();
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
					vImage = VideoUtil.createVolatileVersion(gConfig, img);
				}
				int i = 0;
				do {
					gg.rotate(theta, x, y);
					gg.drawImage(
							vImage,
							(int)(x - biasX),            
							(int)(y - biasY), 
							(int)(x + biasX - 1),
							(int)(y + biasY - 1), 
							0,         
							0,
							(int)(spec.width() - 1),
							(int)(spec.height() - 1),
							null
					);
					vImages[patternno] = vImage;
					i ++;
				} while ((vImage = VideoUtil.createVolatileVersionForRetry(gConfig, img, vImage)) != null);
				if (i > 1) {
					System.out.println("--: i = <" + i  + ">");
				}				
			} else {
				SpriteSpec spec = sprite.getSpec();
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
				gg.drawImage(
						this.images[patternno],
						(int)(x - biasX),            
						(int)(y - biasY), 
						(int)(x + biasX - 1),
						(int)(y + biasY - 1), 
						0,         
						0,
						(int)(spec.width() - 1),
						(int)(spec.height() - 1),
						null
				);
			}
		} finally {
			gg.setTransform(backup);
		}
	}

	@Override
	public RenderingParameters createRenderingParameters() {
		return new RenderingParameters();
	}

}
