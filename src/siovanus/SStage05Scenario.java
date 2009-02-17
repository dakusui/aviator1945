package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage05Scenario extends SStageScenario {

	@Override
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(512, -600, 64);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 05", 
						380, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						250, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(3) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(0, 3200, 192);
				createNewEnemy_B29(0, 3400, 192);
				createNewEnemy_F4U(200, 3600, 192);
				createNewEnemy_F4U(-200, 3600, 192);
			}
		});
		ret.add(new SScenarioEvent(23) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(0, 3200, 192);
				createNewEnemy_B29(0, 3400, 192);
				createNewEnemy_F4U(200, 3600, 192);
				createNewEnemy_F4U(-200, 3600, 192);
			}
		});
		ret.add(new SScenarioEvent(24) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewFriend_Zero(1000, -3200, 64);
				createNewFriend_Zero(1000, -3400, 64);
				createNewFriend_Zero(1400, -3600, 64);
				createNewFriend_Zero(600, -3600, 64);
			}
		});
		ret.add(new SScenarioEvent(25) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(1000, 3200, 192);
				createNewEnemy_B29(1000, 3400, 192);
				createNewEnemy_F4U(1200, 3600, 192);
				createNewEnemy_F4U(800, 3600, 192);
			}
		});
		return ret;
	}

	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 50) || numberOfTotalEnemiesShotDown() >= 12;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_CHOPIN_OP39;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/saku_ccb-75-13_c34b_1.png";
	}

}
