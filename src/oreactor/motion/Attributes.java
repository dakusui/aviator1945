package oreactor.motion;


/**
 * Per application basis
 */
abstract class Attributes implements Cloneable {
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