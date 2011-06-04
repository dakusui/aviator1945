package oreactor.motion;

import java.io.Serializable;


/**
 * Per application basis
 */
abstract class Attributes implements Cloneable, Serializable {
	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -7845726884165594278L;
	Attributes cloneState() {
		try {
			return (Attributes) this.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	public abstract void apply(Motion b);
	protected abstract boolean touches(Attributes another, double distance);
}