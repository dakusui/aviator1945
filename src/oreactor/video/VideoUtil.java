package oreactor.video;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Map;

public class VideoUtil {
	static Map<Image, VolatileImage> vImages = new HashMap<Image, VolatileImage>();
	
	public static VolatileImage getVolatileVersion(GraphicsConfiguration gConfig, Image img, boolean refresh) {
		VolatileImage ret = null;
		int w, h;
		w = img.getWidth(null);
		h = img.getHeight(null);
		ret = vImages.get(img);
		if (ret == null || refresh) {
			ret = gConfig.createCompatibleVolatileImage(w, h);
			Graphics2D gg = (Graphics2D) ret.getGraphics();
			gg.drawImage(img, 
					0, 0, w, h, 
					0, 0, w, h,
					null);
			gg.dispose();
			ret.setAccelerationPriority((float)(1.0));
			
			vImages.put(img, ret);
		}
		return ret;
	}

	/**
	 * Returns new volatile image if the given volatile image <code>vImage</code>'s
	 * contents is lost. Otherwise, <code>null</code> will be returned and nothing
	 * will be performed.
	 * @param gConfig A graphics configuration on which the new volatile image is created.
	 * @param image An image object from which the new volatile image.
	 * @param vImage An original volatile image to be determined if it is valid or not. 
	 * @return A new volatile image or <code>null</code>.
	 */
	public static VolatileImage getVolatileVersionIfContentsLost(GraphicsConfiguration gConfig, Image img, VolatileImage vImage) {
		VolatileImage ret = vImage.contentsLost() ? getVolatileVersion(gConfig, img, true) : null; 
		return ret;
	}
	
	public static void flush() {
		vImages.clear();
	}

	public static VolatileImage getVolatileVersion(
			GraphicsConfiguration gConfig, Image img) {
		return getVolatileVersion(gConfig, img, false);
	}
}
