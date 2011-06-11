/**
 * 
 */
package mu64.motion.shootergame;

import mu64.motion.Attributes;
import mu64.motion.Motion;

public class SGAttrs extends Attributes {
	private static final long serialVersionUID = 2767667589491228395L;
	private double x, y;
	private double direction;
	private int pattern = 0;
	private double zoom = 1.0;

	@Override
	protected void applyMotion(Motion b) {
		x += ((SGMotion) b).dx();
		y += ((SGMotion) b).dy();
		pattern = ((SGMotion) b).pattern();
		zoom = ((SGMotion) b).zoom();
	}
	public double x() {
		return this.x;
	}
	public double y() {
		return this.y;
	}
	public double direction() {
		return this.direction;
	}
	public void x(double x) {
		this.x = x;
	}
	public void y(double y) {
		this.y = y;
	}
	public void direction(double dir) {
		this.direction = dir;
	}
	public int pattern() {
		return pattern;
	}
	public void zoom(double zoom) {
		this.zoom = zoom;
	}
	public double zoom() {
		return zoom ;
	}
}