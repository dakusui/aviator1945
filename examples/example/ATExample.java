package example;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class ATExample extends JFrame {
	private static final long serialVersionUID = 1L;
	private static Image img;
	
	public void init() {
		this.setUndecorated(true);
		this.setSize(1024, 768);
		this.setVisible(true);
	}
	
	public void paint(Graphics g) {
		Graphics2D gg = (Graphics2D) this.getGraphics();
		double width = 640;
		double height = 480;
		double ix = 320.0, iy =  20.0;
		double jx =  20.0,  jy = 240.0;
		double ox =   80.0,   oy =  80.0;
		AffineTransform Tx = composeMatrix(width, height, ox, oy, ix, iy, jx, jy);
		gg.setColor(Color.MAGENTA);
		gg.drawRect(0, 0, 640, 480);
		gg.transform(Tx);
		int x1 = (int) min(ox, ox + ix, ox + jx, ox + ix + jx);
		int y1 = (int) min(oy, oy + iy, oy + jy, oy + iy + jy);
		int x2 = (int) max(ox, ox + ix, ox + jx, ox + ix + jx);
		int y2 = (int) max(oy, oy + iy, oy + jy, oy + iy + jy);
		
		gg.drawImage(img, x1, y1, x2, y2, x1, y1, x2, y2, null);
		System.out.println("x1=" + x1);
		System.out.println("y1=" + y1);
		System.out.println("x2=" + x2);
		System.out.println("y2=" + y2);
		//gg.drawImage(img, 80, 80, 80+320 + 20, 80+ 240 + 20, 80, 80, 80 + 320 + 20, 80 + 240 + 20, null);
		gg.dispose();
	}

	private static double min(double... x) {
		double ret = Double.MAX_VALUE;
		for (double i : x) {
			if ( i < ret) {
				ret = i;
			}
		}
		return ret;
	}
	
	private static double max(double... x) {
		double ret = Double.MIN_VALUE;
		for (double i : x) {
			if (i > ret) {
				ret = i;
			}
		}
		return ret;
	}
	
	private static void drawGrid(Graphics2D gg) {
		for (int i = 0; i <= 640; i += 20) {
			if (i % 80 == 0) {
				gg.setColor(Color.red);
			} else if (i % 40 == 0) {
				gg.setColor(Color.green);
			} else {
				gg.setColor(Color.blue);
			}
			gg.drawLine(0, i, 639, i);
			gg.drawLine(i, 0,   i, 639);
			for (int j = 0; j <= 640; j += 20) {
				if (j % 80 == 0 && i % 80 == 0) {
					gg.drawString("(" + i + "," + j + ")", i, j);
				}
			}
		}
	}

	private static AffineTransform composeMatrix(double width, double height, double ox, double oy, double ix, double iy, double jx, double jy) {
		// offset = (40, 40)
		// i = (600,  40)
		// j = (80,  400) 
		// [ a b ][ ix  jx] -> [640   0]
		// [ c d ][ iy  jy]    [  0 480]
		
		// [ a b ]    [640    0]1/(ixjy-jxiy)[ jy  -jx]
		// [ c d ]    [  0  480]             [-iy   ix]
		//            (1/ixjy-jxiy)[ 640jy  640jx]
		//                         [-480iy  480ix]
		double delta = (ix*jy - jx*iy);
		double a =   (width * jy) / delta,  b = -(width * jx)/ delta;
		double c = - (height * iy) / delta, d =  (height * ix)/ delta;
		double e = - (a * ox + b * oy), f = -(c * ox + d * oy);
		AffineTransform ret = new AffineTransform(
				a,    c,
				b,    d,
				e,    f
				);
		return ret;
	}
	
	
	public static void main(String[] args) {
		ATExample frame = new ATExample();
		img = new BufferedImage(640, 480, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D gg = (Graphics2D) img.getGraphics();
		drawGrid(gg);
		gg.dispose();
		
		
		
		
		frame.setLocation(1800, 100);
		frame.init();
	}

}
