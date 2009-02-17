package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import siovanus.drivant.aviator.SmartAviator;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage08Scenario extends SStageScenario {

	@Override
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(2048, 1920, 192);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 08", 
						380, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						250, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(3) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(-1000, -4000, 64);
				createNewEnemy_B17(-1400, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B29(-1200, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(12) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1000, -4000, 64, false, new SmartAviator());
				createNewEnemy_F4U(-1200, -4000, 64, false, new SmartAviator());
				createNewEnemy_Avenger(-1400, -4000, 64, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(33) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(600, -4000, 64);
				createNewEnemy_B17(1000, -4000, 64);
				createNewEnemy_B17(1400, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(36) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(4000, -4000, 64);
				createNewEnemy_B29( 800, -4000, 64);
				createNewEnemy_B29(1200, -4000, 64);
				createNewEnemy_Avenger(1600, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(42) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(600, -4000, 64);
				createNewEnemy_F4U(1400, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(70) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B29(600, -4000, 64);
				createNewEnemy_B29(1000, -4000, 64);
				createNewEnemy_B29(1400, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(75) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(4000, -4000, 64);
				createNewEnemy_B29(800, -4000, 64);
				createNewEnemy_B29(1200, -4000, 64);
				createNewEnemy_P51D(1600, -4000, 64);
			}
		});
		ret.add(new SScenarioEvent(94) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-4000, -4000, 64, false, new SmartAviator());
				createNewEnemy_B17(-800, -4000, 64);
				createNewEnemy_B17(-1200, -4000, 64);
				createNewEnemy_F4U(-1600, -4000, 64, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(98) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(-600, -4000, 64);
				createNewEnemy_B17(-1000, -4000, 64);
				createNewEnemy_B17(-1400, -4000, 64);
			}
		});
		return ret;
	}
	@Override
	protected boolean checkIfMissionCompleted() {
		return secondsElapsed()  > 140 || numberOfTotalEnemiesShotDown() >= 29;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_WALKURENRITT;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/numatsu_ccb-75-30_c3_3.png";
	}

}
