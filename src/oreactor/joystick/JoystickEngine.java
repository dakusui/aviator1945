package oreactor.joystick;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import oreactor.core.BaseEngine;
import oreactor.core.Context;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;

public class JoystickEngine  extends BaseEngine {
	public static enum Stick {
	    NEUTRAL {
			@Override
			public int x() {
				return 0;
			}

			@Override
			public int y() {
				return 0;
			}
		},
		UP {
			@Override
			public int x() {
				return 0;
			}

			@Override
			public int y() {
				return -1;
			}
		},
	    UP_RIGHT {
			@Override
			public int x() {
				return 1;
			}

			@Override
			public int y() {
				return -1;
			}
		},
	    RIGHT {
			@Override
			public int x() {
				return 1;
			}

			@Override
			public int y() {
				return 0;
			}
		},
	    DOWN_RIGHT {
			@Override
			public int x() {
				return 1;
			}

			@Override
			public int y() {
				return 1;
			}
		},
	    DOWN {
			@Override
			public int x() {
				return 0;
			}

			@Override
			public int y() {
				return 1;
			}
		},
	    DOWN_LEFT {
			@Override
			public int x() {
				return -1;
			}

			@Override
			public int y() {
				return 1;
			}
		},
	    LEFT {
			@Override
			public int x() {
				return -1;
			}
			@Override
			public int y() {
				return 0;
			}
		},
	    UP_LEFT {
			@Override
			public int x() {
				return -1;
			}
			@Override
			public int y() {
				return -1;
			}
		},
	    INVALID {
			@Override
			public int x() {
				return 0;
			}
			@Override
			public int y() {
				return 0;
			}
		};
	    
	    public abstract int x();
	    public abstract int y();
	}

	public static enum Trigger {
	    TRIANGLE, 
	    SQUARE, 
	    CIRCLE, 
	    CROSS, 
	    R1,
	    R2,
	    L1,
	    L2,
	    START,
	    SELECT,
	    ANY, 
	    HELP
	}

	public static enum Type {
		Keyboard,
		PS3Joystick,
		Joypad,
	}
	
	List<InputDevice> devices = new LinkedList<InputDevice>();
	
	public JoystickEngine(Settings settings) {
		super(settings);
	}

	@Override 
	public void initialize(Context c) throws OpenReactorException {
		devices.add(new KeyboardDevice());
	}
	
	@Override
	public void run() {
		for (InputDevice d : devices) {
			d.poll();
		}
	}
	
	public List<InputDevice> devices() {
		Collections.unmodifiableList(this.devices);
		return this.devices;
	}
}
