/**
 * 
 */
package mu64.motion.shootergame;

import mu64.motion.Motion;

public class SGMotion extends Motion {
	double dx, dy;

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
}