/**
 * 
 */
package mu64.motion.shootergame;

import mu64.motion.Drivant;
import mu64.motion.MMachine;
import mu64.motion.Motion;
import mu64.motion.MotionProvider;
import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.OpenReactorException;

public abstract class SGBaseDrivant extends Drivant {
	@Override
	protected final void perform(Motion motion, MMachine owner,
			MotionProvider provider) throws OpenReactorException {
		this.perform_((SGMotion) motion, owner, provider);
	}
	@ExtensionPoint
	protected void performCollisionWith(Motion motion, MMachine owner, MMachine another, double distance) {
		motion.destroy();
	}
	@Override
	protected final void performInteractionWith(Motion motion,
			MMachine owner, MMachine another, double distance) {
	}
	protected abstract void perform_(SGMotion motion, MMachine owner,
			MotionProvider provider) throws OpenReactorException;
}