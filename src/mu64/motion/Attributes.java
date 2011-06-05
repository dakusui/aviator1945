package mu64.motion;

import java.io.Serializable;


/**
 * Per application basis
 */
public abstract class Attributes implements Cloneable, Serializable {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -7845726884165594278L;
	private boolean destroyed = false;

	protected abstract void applyMotion(Motion b);

	protected abstract boolean touches(Attributes another, double distance);
	
	Attributes cloneState() {
		try {
			return (Attributes) this.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public void apply(Motion b) {
		applyMotion(b);
		if (b.isDestroyed()) {
			this.destroyed = true;
		}
	}
	
	public boolean isDestroyed() {
		return this.destroyed;
	}
}