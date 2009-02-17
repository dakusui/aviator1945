/**
 * 
 */
package avis.video;

import java.awt.image.ImageObserver;

public class AViewport {
	protected double theta;
	protected double ratio;
	protected double x; // 中心座標(X)
	protected double y; // 中心座標(Y)
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
	 * <code>zoom</code>で設定された値の逆数を返却する。
	 * @return <code>zoom</code>の逆数
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