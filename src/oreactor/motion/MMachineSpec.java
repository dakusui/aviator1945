package oreactor.motion;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

/**
 * Per entity basis
 * 
 * @author hiroshi
 * 
 */
public abstract class MMachineSpec {
	protected MMachineSpec() {
	}

	public final MMachine buildMMachine(MotionProvider provider, MMachine parent) throws OpenReactorException {
		MMachine ret = new MMachine(provider);

		Attributes attr = provider.createAttributes();
		if (!provider.isAcceptable(attr)) {
			ExceptionThrower.throwException("Given object is not valid for this application:<" + attr + ">");
		}
		fillInAttributes(attr, parent);
		ret.setAttributes(attr);

		Drivant drivant = createDrivant();
		if (!provider.isAcceptable(drivant)) {
			ExceptionThrower.throwException("Given object is not valid for this application:<" + drivant + ">");
		}
		ret.setDrivant(drivant);

		ret.bind(this.getGroup());

		return ret;
	}

	protected abstract void fillInAttributes(Attributes attr, MMachine parent);

	protected abstract Drivant createDrivant();

	protected abstract Group getGroup();
}
