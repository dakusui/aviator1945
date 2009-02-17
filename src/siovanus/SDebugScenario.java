package siovanus;

import avis.base.AException;
import avis.base.Avis;
import avis.session.AScenarioEvent;

public class SDebugScenario extends SScenario {
	static int MAX_X = 1024;
	static int MAX_Y = 768;
	static int MIN_X = -MAX_X;
	static int MIN_Y = -MAX_Y;
	
	static class SNewEnemyEvent extends SScenarioEvent {
		@Override
		final protected void performSiovanusEvent(SScenario sscenario)
				throws AException {
			double x, y;
			double r = Math.random();
			if (r > 0.5) {
				y = Math.ceil(MAX_Y * 2 * Math.random()) - MAX_Y;
				if (r > 0.75) {
					x =   MAX_X + 50;
				} else {
					x =   MIN_X - 50;
				}
			} else {
				x = Math.ceil(MAX_X * 2 * Math.random()) - MAX_X;
				if (r > 0.25) {
					y =   MAX_X + 50;
				} else {
					y = - MIN_Y - 50;
				}
			}
			sscenario.createNewEnemy_Avenger(
					x, 
					y,
					(int)Math.ceil(Math.random() * Avis.DIRECTION_STEPS)
					);
		}
	}
	
	static class SNewFriendEvent extends SScenarioEvent {
		@Override
		final protected void performSiovanusEvent(SScenario sscenario)
				throws AException {
			double x, y;
			double r = Math.random();
			if (r > 0.5) {
				y = Math.ceil(MAX_Y * 2 * Math.random()) - MAX_Y;
				if (r > 0.75) {
					x =   MAX_X + 50;
				} else {
					x =   MIN_X - 50;
				}
			} else {
				x = Math.ceil(MAX_X * 2 * Math.random()) - MAX_X;
				if (r > 0.25) {
					y =   MAX_X + 50;
				} else {
					y = - MIN_Y - 50;
				}
			}
			sscenario.createNewFriend_Zero(
					x, 
					y,
					(int)Math.ceil(Math.random() * Avis.DIRECTION_STEPS)
					);
		}
	}

	static final SScenarioEvent NEW_ENEMY_EVENT = new SNewEnemyEvent();

	static final SScenarioEvent NEW_FRIEND_EVENT = new SNewFriendEvent();

	public SDebugScenario() {
	}
	
	@Override
	public AScenarioEvent action() {
		AScenarioEvent ret = AScenarioEvent.NULL_EVENT;
		if (Math.random() > 0.995) {
			ret = NEW_ENEMY_EVENT;
		} else if (Math.random() > 0.998) {
			ret = NEW_FRIEND_EVENT;
		}
		return ret;
	}

	@Override
	public String bgm() {
		return BGM_CHOPIN_OP39;
	}

	@Override
	public String getBackgroundResourceName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void instructionAfterScenario() {
		// TODO Auto-generated method stub
		
	}
}
