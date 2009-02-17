/**
 * 
 */
package avis.session;

import avis.base.AException;
import avis.motion.Drivant;
import avis.motion.DrivantObserver;
import avis.motion.InteractionHandler;

public abstract class AScenario implements DrivantObserver, InteractionHandler {
	private AScenario next;

	public AScenario() {
	}
	public abstract AScenarioEvent action() throws AException;
	public abstract void init(ASession session) throws AException;
	public void invalidated(Drivant drivant) {
	}

	public void registered(Drivant drivant) {
	}
	
	public abstract String backgroundImageResource();
	
	public void append(AScenario scenario) {
		this.next = scenario;
	}
	
	public AScenario next() {
		return next;
	}
	
	public abstract void reset();
	public void afterAction() {
	}
	public void beforeAction() {
	}
}