package oreactor.video.sprite;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
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
				String url = null;
				Image im = null;
				try {
					JSONArray imageparams = resources.getJSONArray(i);
					try {
						url = imageparams.getString(0);
						int ox = imageparams.getInt(1);
						int oy = imageparams.getInt(2);
						int w = imageparams.getInt(3);
						int h = imageparams.getInt(4);
						im = new BufferedImage(w, h, ColorSpace.TYPE_RGB); 
						Graphics2D gg = ((Graphics2D)im.getGraphics()); 
						gg.drawImage(loader.getImage(url).image(), 0, 0, w, h, ox, oy, ox + w, oy + h, null);
						gg.dispose();
					} catch (JSONException e) {
						String msg = "Malformed configuration item : <" + url + ">";
						ExceptionThrower.throwMalformedConfigurationException(msg, e);
					}
				} catch (JSONException e) {
					url = resources.getString(i);
					im = loader.getImage(url).image();
				}
				imageList.add(im);
			}
			images = imageList.toArray(new Image[0]);
		} catch (JSONException e) {
			ExceptionThrower.throwMalformedConfigurationException(
					e.getMessage() + "<json:" + params + ">",
					e
			);
		}
	}
	
	@Override
	public void render(Graphics2D gg, Sprite sprite) {
		boolean isVCacheEnabled = true;
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
				VolatileImage vImage = null;
				vImage = VideoUtil.getVolatileVersion(gConfig, img);
				do {
					gg.rotate(theta, x, y);
					drawImage(gg, vImage, x, y, biasX, biasY, ox, oy, ow, oh);
				} while (
					(vImage = VideoUtil.getVolatileVersionIfContentsLost(gConfig, img, vImage)) != null);
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
