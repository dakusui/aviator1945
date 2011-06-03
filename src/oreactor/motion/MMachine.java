package oreactor.motion;

public class MMachine {
	/**
	 * Per application basis
	 * @author hiroshi
	 *
	 */
	static abstract class Behavior {
		abstract void perform(State s);
	}
	/**
	 * Per application basis
	 */
	static abstract class State implements Cloneable {
		State cloneState() {
			try {
				return (State) this.clone();
			} catch (CloneNotSupportedException e) {
				return null;
			}
		}
		public abstract void apply(Behavior b);
	}
	State inprocess;
	State state;
	
	public void perform() {
		Behavior b = null;
		this.inprocess = state.cloneState();
		this.inprocess.apply(b);
	}
	
	public void commit() {
		if (this.inprocess != null) {
			this.state = this.inprocess;
			this.inprocess = null;
		}
	}
}
