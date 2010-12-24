package oreactor.joystick;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardDevice implements KeyListener, InputDevice {
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
    
	protected boolean l1;
	protected boolean r1;
	protected boolean l2;
	protected boolean r2;
	
    public KeyboardDevice() {
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
    public Stick stick() {
        if (stick == Stick.INVALID) {
            update();
        }
        return stick;
    }
    
    void update() {
    	r1 = r2 = l1 = l2 = false;
    	if (up != down) {
            if (up) {
            	if (right != left) {
                    if (right) {
                    	stick = Stick.UP_RIGHT;
                    	if (shift) {
                    		r1 = true;
                    	} 
                    	if (ctrl) {
                    		r2 = true;
                    	}
                    } else {
                    	stick = Stick.UP_LEFT;
                    	if (shift) {
                    		l1 = true;
                    	} 
                    	if (ctrl) {
                    		l2 = true;
                    	}
                    }
                } else {
                    stick = Stick.UP;
                }
            } else {
                if (right != left) {
                    if (right) {
                    	stick = Stick.DOWN_RIGHT;
                    	if (shift) {
                    		r1 = true;
                    	} 
                    	if (ctrl) {
                    		r2 = true;
                    	}
                    } else {
                        stick = Stick.DOWN_LEFT;
                    	if (shift) {
                    		l1 = true;
                    	} 
                    	if (ctrl) {
                    		l2 = true;
                    	}
                    }
                } else {
                    stick = Stick.DOWN;
                }
            }
        } else {
            if (right != left) {
                if (right) {
                    stick = Stick.RIGHT;
                	if (shift) {
                		r1 = true;
                	} 
                	if (ctrl) {
                		r2 = true;
                	}
                } else {
                    stick = Stick.LEFT;
                	if (shift) {
                		l1 = true;
                	} 
                	if (ctrl) {
                		l2 = true;
                	}
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
        	return r1;
        } else if (trigger == Trigger.L1) {
        	return l1;
        } else if (trigger == Trigger.R2) {
        	return r2;
        } else if (trigger == Trigger.L2) {
        	return l2;
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
