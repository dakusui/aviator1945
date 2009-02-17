package avis.session;

import avis.base.AException;

public abstract class AScenarioEvent implements Comparable<AScenarioEvent>{
	protected int index = 0;
	public static final AScenarioEvent NULL_EVENT = new AScenarioEvent() {
		@Override
		public void perform(AScenario scenario) throws AException {}
	};
	public int index() {
		return index;
	}
	protected AScenarioEvent() {
		this(0);
	}
	protected AScenarioEvent(int index) {
		this.index = index;
	}
	public abstract void perform(AScenario scenario) throws AException;
	public int compareTo(AScenarioEvent o) {
		return index - o.index;
	}
	public String toString() {
		return "EVENT:" + index + "<" + this.hashCode() + ">";
	}
}
