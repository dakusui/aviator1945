package avis.base;

import java.awt.geom.Point2D;

public final class APoint extends Point2D.Double {
	/**
	 * Serial version id
	 */
	private static final long serialVersionUID = 2949237185811663757L;
	public APoint() {
		super();
	}
	public APoint(double x, double y) {
		super(x, y);
	}
	public void set(double x, double y) {
		setLocation(x, y);
	}
	public double x() {
		return getX();
	}
	public double y() {
		return getY();
	}
}
