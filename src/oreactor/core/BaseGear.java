package oreactor.core;

import oreactor.exceptions.OpenReactorStateException;

public abstract class BaseGear {
	public static enum State {
		I, Programmable, Rendering
	}
	private State state;
	
	protected BaseGear() {
		this.state = State.I;
	}
	
	protected void assertProgrammableState() {
		if (this.state != State.Programmable) {
			String msg = "The state of this object:<" + this + ":" + this.state + "> is invalid for this operation";
			throw new  OpenReactorStateException(msg);
		}
	}

	protected void assertRenderingState() {
		if (this.state != State.Rendering) {
			String msg = "The state of this object:<" + this + ":" + this.state + "> is invalid for this operation";
			throw new  OpenReactorStateException(msg);
		}
	}
	
	protected void setState(State s) {
		this.state = s;
	}

	
	public void prepare() {
		this.setState(State.Rendering);
	}
	
	public void finish() {
		this.setState(State.Programmable);
	}

}
