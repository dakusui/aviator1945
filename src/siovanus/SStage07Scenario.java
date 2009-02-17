package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import siovanus.drivant.aviator.RunningAwayAviator;
import siovanus.drivant.aviator.SmartAviator;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage07Scenario extends SStageScenario {

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
				message("STAGE 07", 
						380, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						250, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(3) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(-2000, 3800, 192);
			}
		});
		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-2200, 3800, 192, false, new SmartAviator());
				createNewEnemy_Avenger(-1800, 3800, 192, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(23) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(2000, 3800, 192);
				createNewEnemy_B17(3000, 3800, 192);
			}
		});
		ret.add(new SScenarioEvent(26) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2200, 3800, 192, false, new RunningAwayAviator());
				createNewEnemy_Avenger(1800, 3800, 192, false, new SmartAviator());
				createNewEnemy_Avenger(3200, 3800, 192, false, new SmartAviator());
				createNewEnemy_Avenger(2800, 3800, 192, false, new RunningAwayAviator());
			}
		});
		ret.add(new SScenarioEvent(50) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(800, 3800, 192);
				createNewEnemy_B17(1000, 3800, 192);
				createNewEnemy_B17(1300, 3800, 192);
				createNewEnemy_Avenger(1500, 3800, 192);
			}
		});
		ret.add(new SScenarioEvent(55) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(800, 3800, 192, false, new SmartAviator());
				createNewEnemy_B17(1100, 3800, 192);
				createNewEnemy_B17(1400, 3800, 192);
				createNewEnemy_Avenger(1500, 3800, 192, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(60) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(800, 3800, 192, false, new SmartAviator());
				createNewEnemy_B17(1200, 3800, 192);
				createNewEnemy_B17(1400, 3800, 192);
				createNewEnemy_F4U(1500, 3800, 192, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(120) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(5500, 300, 128);
				createNewEnemy_B17(5500, 700, 128);
			}
		});
		ret.add(new SScenarioEvent(124) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B29(5500, 500, 128);
			}
		});
		ret.add(new SScenarioEvent(160) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-800, 3800, 192, false, new SmartAviator());
				createNewEnemy_B29(-1200, 3800, 192);
				createNewEnemy_B29(-1500, 3800, 192);
				createNewEnemy_F4U(-1800, 3800, 192, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(170) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(-600, 3800, 192);
				createNewEnemy_F4U(-800, 3800, 192, false, new SmartAviator());
				createNewEnemy_B29(-1200, 3800, 192);
				createNewEnemy_B29(-1500, 3800, 192);
				createNewEnemy_F4U(-1800, 3800, 192, false, new SmartAviator());
				createNewEnemy_P51D(-2000, 3800, 192);
			}
		});
		return ret;
	}

	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 200) || numberOfTotalEnemiesShotDown() >= 32;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_LISZT143;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/ogasawara_ckt-78-4_c10_9.png";
	}

}
