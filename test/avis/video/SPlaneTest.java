package avis.video;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.VolatileImage;
import java.io.InputStream;

import javax.swing.JFrame;

import avis.base.AException;
import avis.base.Avis;
import avis.video.ABackground;
import avis.video.APlane;
import avis.video.AViewport;



public class SPlaneTest extends JFrame {
	private static final long serialVersionUID = 1L;
	APlane plane;
	public SPlaneTest() throws AException {
		super("SPlaneTest");
		InputStream is = Avis.openUrl("image/background/bg00.bmp");
		try {
			Image bgImage = Avis.readImage(is);
			AViewport view = new AViewport(this);
			ABackground bg = new ABackground(createVolatileBackground(bgImage), view);
			plane = new APlane("TEST", view, bg, 400, 200, 0, null);
		} finally {
			Avis.closeStream(is);
		}
	}
	
	VolatileImage createVolatileBackground(Image bgImage) {
		VolatileImage ret = createVolatileImage(bgImage.getWidth(this), bgImage.getHeight(this));
		Graphics2D gg = ret.createGraphics();
		gg.drawImage(bgImage, 0, 0, bgImage.getWidth(this), bgImage.getHeight(this), this);
		return ret;
	}
	
	public static void main(String[] args) throws AException {
		SPlaneTest frame = new SPlaneTest();
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.test();
	}

	public void test() throws AException {
		AViewport viewport = plane.viewport();
		viewport.center(1280, 800, 126);
		viewport.zoom(2);
		repaint();
	}
	
	@Override
	public void paint(Graphics g) {
		g.translate(200, 200);
		plane.paint(g);
		g.translate(-200,-200);
		g.setColor(Color.white);
		g.drawRect(200, 200, 400,200);
	}
}
