package oreactor.video;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.image.VolatileImage;

public class VideoUtil {
	public static VolatileImage createVolatileVersion(GraphicsConfiguration gConfig, Image img) {
		VolatileImage ret = null;
		int w = img.getWidth(null);
		int h = img.getHeight(null);
		//int w = 32, h = 32;
		ret = gConfig.createCompatibleVolatileImage(w, h);
		ret.setAccelerationPriority((float)(1.0));
		Graphics2D gg = (Graphics2D) ret.getGraphics();
		gg.drawImage(img, 
				0, 0, w, h, 
				0, 0, w, h, 
				null);
		gg.dispose();
		System.out.print("*");
		return ret;
	}

	public static VolatileImage createVolatileVersionForRetry(
			GraphicsConfiguration gConfig, Image image, VolatileImage vImage) {
		VolatileImage ret = null;
		if (vImage.contentsLost()) {
			ret = createVolatileVersion(gConfig, image);
		}
		return ret;
	}
}
