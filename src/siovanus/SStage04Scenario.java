package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage04Scenario extends SStageScenario {

	@Override
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(3192, -2560, 64);
				createNewFriend_Zero(2992, -2760, 64);
				createNewFriend_Zero(2792, -2960, 64);
				createNewFriend_Zero(2592, -3160, 64);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 04", 
						380, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						250, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(3) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「高度20,000ft。フランクを相手にするには危険過ぎる高度だ」", 0, 120, Color.pink, 16, 3);
				createNewEnemy_F4U(-4096, 3600, 192);
				createNewEnemy_Avenger(-3696, 3200, 192);
				createNewEnemy_Avenger(-4496, 3200, 192);
			}
		});
		ret.add(new SScenarioEvent(13) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-4096, 3600, 192);
				createNewEnemy_Avenger(-3696, 3200, 192);
				createNewEnemy_Avenger(-4496, 3200, 192);
			}
		});
		ret.add(new SScenarioEvent(23) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(4096, 3600, 192);
				createNewEnemy_Avenger(3696, 3200, 192);
				createNewEnemy_Avenger(4496, 3200, 192);
			}
		});
		ret.add(new SScenarioEvent(33) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(4096, 3600, 192);
				createNewEnemy_Avenger(3696, 3200, 192);
				createNewEnemy_Avenger(4496, 3200, 192);
			}
		});
		ret.add(new SScenarioEvent(53) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(-4096, -3600, 64);
				createNewEnemy_F4U(-3696, -3200, 64);
				createNewEnemy_F4U(-4496, -3200, 64);
			}
		});
		ret.add(new SScenarioEvent(63) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(-4096, -3600, 64);
				createNewEnemy_F4U(-3696, -3200, 64);
				createNewEnemy_F4U(-4496, -3200, 64);
			}
		});
		return ret;
	}

	protected int howManyEnemiesToShootDown() {
		return 2;
	}

	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 90) || numberOfTotalEnemiesShotDown() >= 18;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_QUARTET_10;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/kure_ccg-81-4_c8a_2.png";
	}

}
