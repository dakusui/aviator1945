package oreactor.motion;

/**
 * Per entity basis
 * @author hiroshi
 *
 */
public abstract class MMachineSpec {
	protected MMachineSpec() {
	}
	
	public MMachine composeMmachine() {
		MMachine ret = new MMachine(this);
		ret.setAttributes(createAttributes());
		return ret;
	}
	protected abstract Attributes createAttributes();
	protected abstract Motion createMotionObject();
	protected abstract Drivant createDrivant();
}
