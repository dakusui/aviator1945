/**
 * 
 */
package siovanus;

import avis.base.AException;
import avis.session.AScenario;
import avis.session.AScenarioEvent;

public abstract class SScenarioEvent extends AScenarioEvent {
	protected SScenarioEvent() {
		super();
	}
	protected SScenarioEvent(int index) {
		super(index);
	}
	@Override
	public	final void perform(AScenario scenario) throws AException {
		SScenario sscenario = (SScenario) scenario;
		performSiovanusEvent(sscenario);
	}
	
	protected abstract void performSiovanusEvent(SScenario sscenario) throws AException;
}