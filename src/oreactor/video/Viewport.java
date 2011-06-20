package oreactor.video;

import java.awt.geom.AffineTransform;
import java.util.LinkedList;
import java.util.List;

import oreactor.core.Logger;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class Viewport {
	public static class Vector {
		double x;
		double y;
		
		public double x() {
			return x;
		}
		
		public double y() {
			return y;
		}
		
		@Override
		public String toString() {
			return "(" + this.x + "," + this.y + ")"; 
		}

		public Vector add(Vector j) {
			Vector ret = new Vector();
			ret.x = this.x + j.x;
			ret.y = this.y + j.y;
			return ret;
		}
	}
	Vector offset;
	/**
	 * First base vector for the view port.
	 */
	Vector i;
	/**
	 * Second base vector for the view port.
	 */
	Vector j;
	
	Logger logger = Logger.getLogger();
	private List<ViewportObserver> observers = new LinkedList<ViewportObserver>();
	
	public Viewport(double screenWidth, double screenHeight) {
		this.offset = new Vector();
		this.offset(0, 0);
		this.i = new Vector();
		this.i(screenWidth, 0);
		this.j = new Vector();
		this.j(0, screenHeight);
	}
	public void i(double x, double y) {
		this.i.x = x;
		this.i.y = y;
		for (ViewportObserver o : this.observers) {
			o.viewportChanged(this);
		}
	}
	
	public Vector i() {
		return this.i;
	}
	
	public void j(double x, double y) { 
		this.j.x = x;
		this.j.y = y;
		for (ViewportObserver o : this.observers) {
			o.viewportChanged(this);
		}
	}
	
	public Vector j() {
		return this.j;
	}
	
	public void offset(double x, double y) {
		this.offset.x = x;
		this.offset.y = y;
		for (ViewportObserver o : this.observers) {
			o.viewportChanged(this);
		}
	}
	
	public Vector offset() {
		return this.offset;
	}
	
	public AffineTransform composeMatrix(double width, double height) throws OpenReactorException {
		double ox = offset.x;
		double oy = offset.y;
		double ix = i.x;
		double iy = i.y;
		double jx = j.x;
		double jy = j.y;
		return composeMatrix(width, height, ox, oy, ix, iy, jx, jy);
	}
	private AffineTransform composeMatrix(double width, double height, double ox, double oy, double ix, double iy, double jx, double jy) throws OpenReactorException {
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
		if (delta == 0) {
			ExceptionThrower.throwViewportStateException("Viewport's determinant is currently 0", this);
		}
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
	@Override
	public String toString() {
		return "offset=<" + this.offset + ">, i=<" + i + ">, j=<" + j + ">";
	}
	
	public void addObserver(ViewportObserver observer) {
		if (!this.observers.contains(observer)) {
			this.observers.add(observer);
		}
	}

	public void removeObserver(ViewportObserver observer) {
		if (this.observers.contains(observer)) {
			this.observers.remove(observer);
		}
	}
	
	public double left() {
		return min(offset.x, offset.x + i.x, offset.x + i.x + j.x, offset.x + j.x);
	}

	public double top() {
		return min(offset.y, offset.y + i.y, offset.y + i.y + j.y, offset.y + j.y);
	}

	public double right() {
		return max(offset.x, offset.x + i.x, offset.x + i.x + j.x, offset.x + j.x);
	}

	public double bottom() {
		return max(offset.y, offset.y + i.y, offset.y + i.y + j.y, offset.y + j.y);
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
}
