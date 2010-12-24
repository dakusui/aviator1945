package oreactor.joystick;

import java.io.IOException;

import com.centralnexus.input.Joystick;

public class PS3JoystickDevice extends JoystickDevice {

	public PS3JoystickDevice() throws IOException {
		super();
	}
	
	@Override
 	protected VDir vDirection() {
		VDir ret = VDir.Neutral;
		int vState = buttons & (Joystick.BUTTON5 | Joystick.BUTTON7);
		if (vState == Joystick.BUTTON5) {
			ret = VDir.Up;
		} else if (vState == Joystick.BUTTON7){
			ret = VDir.Down;
		}
		return ret;
	}

 	@Override
	protected HDir hDirection() {
		HDir ret = HDir.Neutral;
		int hState = buttons & (Joystick.BUTTON8 | Joystick.BUTTON6);
		if (hState == Joystick.BUTTON8) {
			ret = HDir.Left;
		} else if (hState == Joystick.BUTTON6){
			ret = HDir.Right;
		}
		return ret;
	}

 	public boolean trigger(Trigger trigger) {
        if (trigger == Trigger.SQUARE) {            // SQUARE
            return (buttons & Joystick.BUTTON16) != 0;
        } else if (trigger == Trigger.CIRCLE) {     // CIRCLE
            return (buttons & Joystick.BUTTON14) != 0;
        } else if (trigger == Trigger.R1) {    // R1
        	return (buttons & Joystick.BUTTON12) != 0;
        } else if (trigger == Trigger.L1) {     // L1
        	return (buttons & Joystick.BUTTON11) != 0;
        } else if (trigger == Trigger.R2) {    // R2
        	return (buttons & Joystick.BUTTON10) != 0;
        } else if (trigger == Trigger.L2) {     // L2
        	return (buttons & Joystick.BUTTON9) != 0;
        } else if (trigger == Trigger.SELECT) {    // SELECT
        	return (buttons & Joystick.BUTTON1) != 0;
        } else if (trigger == Trigger.START) {   // START
        	return (buttons & Joystick.BUTTON4) != 0;
        } else if (trigger == Trigger.HELP) {    // PlayStation
        	return (buttons & Joystick.BUTTON17) != 0;
        } else if (trigger == Trigger.ANY) {
    		return buttons != 0;
    	} else {
    		assert false;
    		return false;
        }
	}

}
