package oreactor.video;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.VolatileImage;
import java.util.HashMap;
import java.util.Map;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class VideoUtil {
	static Map<Image, VolatileImage> vImages = new HashMap<Image, VolatileImage>();
	private static Color transparentColor = new Color(0,0,0,0);
	
	public static VolatileImage getVolatileVersion(GraphicsConfiguration gConfig, Image img, boolean refresh) throws OpenReactorException {
		VolatileImage ret = null;
		int w, h;
		w = img.getWidth(null);
		h = img.getHeight(null);
		ret = vImages.get(img);
		try {
			if (ret == null || refresh) {
				if (ret != null) {
					ret.flush();
					ret = null;
				}
				ret = gConfig.createCompatibleVolatileImage(w, h, Transparency.TRANSLUCENT);
				Graphics2D gg = (Graphics2D) ret.getGraphics();
				try {
					gg.setBackground(transparentColor);
					gg.clearRect(0, 0, w, h);
					gg.drawImage(img, 
							0, 0, w, h, 
							0, 0, w, h,
							null);
				} finally {
					gg.dispose();
				}
				ret.setAccelerationPriority((float)(1.0));
				
				vImages.put(img, ret);
			}
		} catch (Exception e) {
			ExceptionThrower.throwVideoException("Failed to create a volatile image:" + e.getMessage(), e);
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
	 * @throws OpenReactorException 
	 */
	public static VolatileImage getVolatileVersionIfContentsLost(GraphicsConfiguration gConfig, Image img, VolatileImage vImage) throws OpenReactorException {
		VolatileImage ret = vImage.contentsLost() ? getVolatileVersion(gConfig, img, true) : null; 
		return ret;
	}
	
	public static void flush() {
		vImages.clear();
	}

	public static VolatileImage getVolatileVersion(
			GraphicsConfiguration gConfig, Image img) throws OpenReactorException {
		return getVolatileVersion(gConfig, img, false);
	}
}
