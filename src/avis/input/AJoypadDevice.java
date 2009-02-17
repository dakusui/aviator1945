package avis.input;

import java.io.IOException;

import com.centralnexus.input.Joystick;

public class AJoypadDevice extends AJoystickDevice {
	protected static final float CURSOR_THRESHOLD = (float) 0.5;
	
	public AJoypadDevice() throws IOException {
		super();
	}

	@Override
	protected HDir hDirection() {
		HDir ret = HDir.Neutral;
		float hState = device.getZ() + device.getX(); //workaround a joypad behaves differently on linux and win
		if (hState < -CURSOR_THRESHOLD) {
			ret = HDir.Left;
		} else if (hState > CURSOR_THRESHOLD){
			ret = HDir.Right;
		}
		return ret;
	}

	@Override
	public boolean trigger(Trigger trigger) {
        if (trigger == Trigger.SQUARE) {            // SQUARE
            return (buttons & Joystick.BUTTON4) != 0;
        } else if (trigger == Trigger.CIRCLE) {     // CIRCLE
            return (buttons & Joystick.BUTTON2) != 0;
        } else if (trigger == Trigger.R1) {    // R1
        	return (buttons & Joystick.BUTTON8) != 0;
        } else if (trigger == Trigger.L1) {     // L1
        	return (buttons & Joystick.BUTTON7) != 0;
        } else if (trigger == Trigger.SELECT) {    // SELECT
        	return (buttons & Joystick.BUTTON9) != 0;
        } else if (trigger == Trigger.START) {   // START
        	return (buttons & Joystick.BUTTON10) != 0;
        } else if (trigger == Trigger.ANY) {
    		return buttons != 0;
    	} else {
    		assert false;
    		return false;
        }
	}

	@Override
	protected VDir vDirection() {
		VDir ret = VDir.Neutral;
		float vState = device.getR() + device.getY(); //workaround a joypad behaves differently on linux and win 
		if (vState < -CURSOR_THRESHOLD) {
			ret = VDir.Up;
		} else if (vState > CURSOR_THRESHOLD) {
			ret = VDir.Down;
		}
		return ret;	}

}
