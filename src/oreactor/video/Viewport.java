package oreactor.video;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

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
	private boolean firstTime = true;
	
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
	}
	
	public Vector i() {
		return this.i;
	}
	
	public void j(double x, double y) { 
		this.j.x = x;
		this.j.y = y;
	}
	
	public Vector j() {
		return this.j;
	}
	
	public void offset(double x, double y) {
		this.offset.x = x;
		this.offset.y = y;
	}
	
	public Vector offset() {
		return this.offset;
	}
	
	public AffineTransform affineTransform(double screenWidth, double screenHeight) throws OpenReactorException {
		double delta = this.i.x*this.j.y -  this.i.y * this.j.x;
		if (delta == 0) {
			ExceptionThrower.throwViewportStateException("Viewport's determinant is currently 0", this);
		}
		//  m00 m01   i.x i.y   w  0
		//          *         =     
		//  m10 m11   i.y j.y   0  h
		//
		//  m00 m01    w     0     j.y/delta  -i.y/delta      1      w*j.y  -w*i.y
		//           =          *                         = ----- * 
		//  m10 m11    0     h    -j.x/delta   i.x/delta    delta   -h*j.x   h*i.x 
		double m00 =  screenWidth*j.y/delta;
		double m01 = -screenHeight*j.x/delta;
		double m10 = -screenWidth*i.y/delta;
		double m11 = screenHeight*i.x/delta;

		//  i.x i.y   m00 m01   w  0
		//          *         =    
		//  i.y j.y   m10 m11   0  h
		//
		//  m00 m01     j.y/delta  -i.y/delta       w     0          1      j.y*w   -i.y*h
		//           =                         *                 = ----- * 
		//  m10 m11    -j.x/delta   i.x/delta       0     h        delta   -j.x*w    i.x*h
		//double m00 =   screenWidth*j.y/delta;
		//double m01 = -screenHeight*i.y/delta;
		//double m10 =  -screenWidth*j.x/delta;
		//double m11 =  screenHeight*i.x/delta;
		AffineTransform ret = new AffineTransform(
				m00,                    m10,
				m01,                    m11, 
				-this.offset.x/*m02*/, -this.offset.y/*m12*/
				);
		//try {
		//ret.invert();
		//} catch (NoninvertibleTransformException e) {
			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//}
		if (firstTime) {
			System.out.println(ret);
			System.out.println(this);
			firstTime  = false;
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return "offset=<" + this.offset + ">, i=<" + i + ">, j=<" + j + ">";
	}
}
