package siovanus.drivant.aviator;

import siovanus.drivant.AviatorDrivant;
import avis.input.AInputDevice;
import avis.input.AInputDevice.Stick;
import avis.input.AInputDevice.Trigger;
import avis.motion.Drivant;

public class UserAviator extends Aviator {
	protected AInputDevice inputDevice;
	protected static enum TwistActionState {
		Neutral, RightTwist, LeftTwist
	}
	protected static enum TurningDirection {
		Left, Right, Neutral
	}
	protected TwistActionState twistActionState = TwistActionState.Neutral;
	protected int remainingStateDuration = 0;
	public static final int INITIAL_ENERGY=4800;
	private int _twistEnergy = INITIAL_ENERGY;
	public static final int TWIST_ENERGY_REQUIRED = 2400;
	
	public UserAviator(AInputDevice inputDevice) {
		super();
		this.inputDevice = inputDevice;
	}

	/**
	 * @param target will be always <code>null</code>
	 */
	@Override
	public void performAction(AviatorDrivant drivant, Drivant target) {
		// ユーザコード
		AInputDevice.Stick s = inputDevice.cursor();
        if (s == Stick.UP || s == Stick.UP_LEFT ||s == Stick.UP_RIGHT) {
        	drivant.accelerate();
        } else if (s == Stick.DOWN || s == Stick.DOWN_LEFT || s == Stick.DOWN_RIGHT) {
        	drivant.decelerate();
        }
        boolean rollOnly = false;
        TurningDirection turningDirection = TurningDirection.Neutral;
        if (s == Stick.RIGHT || s == Stick.UP_RIGHT || s == Stick.DOWN_RIGHT) {
        	if (inputDevice.trigger(Trigger.L1) || !inputDevice.trigger(Trigger.R1)) {
        		drivant.yawRight();
            	drivant.recoverYawHalf();
        	}
        	if ((rollOnly = inputDevice.trigger(Trigger.R1)) || !inputDevice.trigger(Trigger.L1)) {
        		drivant.rollRight(rollOnly);
        	}
        	turningDirection = TurningDirection.Right;
        } else if (s == Stick.LEFT || s == Stick.UP_LEFT || s == Stick.DOWN_LEFT) {
        	if (inputDevice.trigger(Trigger.L1) || !inputDevice.trigger(Trigger.R1)) {
        		drivant.yawLeft();
        		drivant.recoverYawHalf();
        	}
        	if ((rollOnly = inputDevice.trigger(Trigger.R1)) || !inputDevice.trigger(Trigger.L1)) {
        		drivant.rollLeft(rollOnly);
        	}
        	turningDirection = TurningDirection.Left;
        } else {
        	drivant.recoverYaw();
        	drivant.recoverBank();
        }
        if (inputDevice.trigger(Trigger.SQUARE)) {
        	drivant.shot();
        }
        if (inputDevice.trigger(Trigger.CIRCLE)) {
        	drivant.bomb();
        }
        if (remainingStateDuration > 0) {
        	remainingStateDuration--;
        	if (twistActionState == TwistActionState.LeftTwist || twistActionState == TwistActionState.RightTwist) {
        		drivant.disableCollisionDetect();
        		twist(drivant);
        	}
        } else {
        	drivant.enableCollisionDetect();
        	twistActionState = TwistActionState.Neutral;
        }
        _twistEnergy = Math.min(INITIAL_ENERGY, _twistEnergy + 1);
        if (twistActionState == TwistActionState.Neutral) {
        	if (_twistEnergy > TWIST_ENERGY_REQUIRED) {
        		if (turningDirection == TurningDirection.Left && inputDevice.trigger(Trigger.L1)) {
        			twistActionState = TwistActionState.LeftTwist;
        			// set remaining duration not to make a state transition to "neutral"
        			_twistEnergy = _twistEnergy - TWIST_ENERGY_REQUIRED;
        			remainingStateDuration = 96;
        		} else if (turningDirection == TurningDirection.Right && inputDevice.trigger(Trigger.L1)) {
        			twistActionState = TwistActionState.RightTwist;
        			// set remaining duration not to make a state transition to "neutral"
        			_twistEnergy = _twistEnergy - TWIST_ENERGY_REQUIRED;
        			remainingStateDuration = 96;
        		}
        	}
        } else if (twistActionState == TwistActionState.RightTwist) {
        	// does nothing
        } else if (twistActionState == TwistActionState.LeftTwist) {
        	// does nothing
        } 
	}
	
	protected void twist(AviatorDrivant drivant) {
		if (twistActionState == TwistActionState.RightTwist) {
			drivant.rollRight(2.0, false);
		} else if (twistActionState == TwistActionState.LeftTwist) {
			drivant.rollLeft(2.0, false);
		}
	}
	public boolean isTwisting() {
		return twistActionState == TwistActionState.LeftTwist || twistActionState == TwistActionState.RightTwist;
	}
	
	public int energy() {
		return _twistEnergy;
	}
}
