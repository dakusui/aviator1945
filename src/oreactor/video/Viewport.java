package oreactor.video;

public class Viewport {
	public static class Vector {
		double x;
		double y;
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
}
