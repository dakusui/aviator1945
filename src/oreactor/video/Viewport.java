package oreactor.video;

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
	
	public Viewport(double width, double height) {
		this.i = new Vector();
		this.i(width, 0);
		this.j = new Vector();
		this.i(0, height);
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
}
