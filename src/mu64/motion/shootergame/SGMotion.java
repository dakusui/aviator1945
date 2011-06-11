/**
 * 
 */
package mu64.motion.shootergame;

import mu64.motion.Motion;

public class SGMotion extends Motion {
	private double dx, dy;
	private int pattern;
	private double zoom = 1.0;
	
	double dx() {
		return this.dx;
	}
	double dy() {
		return this.dy;
	}
	public void dx(double dx) {
		this.dx = dx;
	}
	public void dy(double dy) {
		this.dy = dy;
	}
	public int pattern() {
		return pattern;
	}
	public void pattern(int pattern) {
		this.pattern = pattern;
	}
	public double zoom() {
		return this.zoom;
	}
	public void zoom(double zoom) {
		this.zoom = zoom;
	}
}