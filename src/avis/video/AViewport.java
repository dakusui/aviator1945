/**
 * 
 */
package avis.video;

import java.awt.image.ImageObserver;

public class AViewport {
	protected double theta;
	protected double ratio;
	protected double x; // ���S���W(X)
	protected double y; // ���S���W(Y)
	protected ImageObserver observer;
	private double zoom;

	public AViewport(ImageObserver observer) {
		this.observer = observer;
	}

	public void center(double x, double y, double theta) {
		this.x = x;	this.y = y;
		this.theta = theta;
	}

	public void zoom(double zoom) {
		this.ratio = 1/zoom;
		this.zoom = zoom;
	}
	
	/**
	 * <code>zoom</code>�Őݒ肳�ꂽ�l�̋t����ԋp����B
	 * @return <code>zoom</code>�̋t��
	 */
	public double ratio() {
		return this.ratio;
	}
	
	public double theta() {
		return this.theta;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double zoom() {
		return zoom;
	}
}