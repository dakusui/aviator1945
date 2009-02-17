package avis.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class AKeyboardDevice implements KeyListener, AInputDevice {
    protected boolean up;
    protected boolean right;
    protected boolean down;
    protected boolean left;
    protected boolean space;
    protected boolean shift;
    protected boolean z;
	protected boolean ctrl;
	protected boolean esc;
    protected Stick stick = Stick.INVALID;
	protected boolean p;
	protected boolean help;
    
    public AKeyboardDevice() {
        super();
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = true;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = true;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = true;
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            z = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = true;
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
        	ctrl = true;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            esc = true;
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            p = true;
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            help = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            up = false;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = false;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            down = false;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = false;
            stick = Stick.INVALID;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = false;
        } else if (e.getKeyCode() == KeyEvent.VK_Z) {
            z = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            shift = false;
        } else if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
        	ctrl = false;
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            esc = false;
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            p = false;
        } else if (e.getKeyCode() == KeyEvent.VK_F1) {
            help = false;
        }
    }

    /* (non-Javadoc)
	 * @see avis.input.InputDevice#stick()
	 */
    public Stick cursor() {
        if (stick == Stick.INVALID) {
            update();
        }
        return stick;
    }
    
    void update() {
        if (up != down) {
            if (up) {
                if (right != left) {
                    if (right) {
                        stick = Stick.UP_RIGHT;
                    } else {
                        stick = Stick.UP_LEFT;
                    }
                } else {
                    stick = Stick.UP;
                }
            } else {
                if (right != left) {
                    if (right) {
                        stick = Stick.DOWN_RIGHT;
                    } else {
                        stick = Stick.DOWN_LEFT;
                    }
                } else {
                    stick = Stick.DOWN;
                }
            }
        } else {
            if (right != left) {
                if (right) {
                    stick = Stick.RIGHT;
                } else {
                    stick = Stick.LEFT;
                }
            } else {
                stick = Stick.NEUTRAL;
            }
        }
    }
    
    /* (non-Javadoc)
	 * @see avis.input.InputDevice#trigger(avis.input.AStick.Trigger)
	 */
    public boolean trigger(Trigger trigger) {
    	
        if (trigger == Trigger.SQUARE) {
            return space;
        } else if (trigger == Trigger.CIRCLE) {
            return z;
        } else if (trigger == Trigger.R1) {
        	return shift;
        } else if (trigger == Trigger.L1) {
        	return ctrl;
        } else if (trigger == Trigger.SELECT) {
        	return esc;
        } else if (trigger == Trigger.START) {
        	return p;
        } else if (trigger == Trigger.HELP) {
        	return help;
        } else if (trigger == Trigger.ANY) {
    		return space | z | shift | ctrl | esc | p | help;
    	} else {
    		assert false;
    		return false;
        }
    }

	public void poll() {
		// does nothing
	}
}
