package oreactor.video;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.VolatileImage;

public class VideoUtil {
	public static VolatileImage createVolatileVersion(GraphicsConfiguration gConfig, Image img, int w, int h) {
		VolatileImage ret = null;
		ret = gConfig.createCompatibleVolatileImage(w, h);
		ret.setAccelerationPriority((float)(1.0));
		Graphics2D gg = (Graphics2D) ret.getGraphics();
		gg.drawImage(img, 
				0, 0, w, h, 
				0, 0, img.getWidth(null), img.getHeight(null), 
				null);
		gg.dispose();
		System.out.print("*");
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
	public static VolatileImage createVolatileVersionForRetry(
			GraphicsConfiguration gConfig, Image image, VolatileImage vImage, int w, int h) {
		VolatileImage ret = null;
		if (vImage.contentsLost()) {
			ret = createVolatileVersion(gConfig, image, w, h);
		}
		return ret;
	}
}
