package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import avis.base.AException;
import avis.motion.Drivant;
import avis.session.AScenarioEvent;

public class SStage09Scenario extends SStageScenario {

	@Override
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(512, -600, 64);
				Drivant newJersey1 = createNewEnemy_USSNewJersey(-800,   -1000);
				createNewEnemy_USSNewJersey_Gun(newJersey1,  200,   0,   64,   0, 128);
				createNewEnemy_USSNewJersey_Gun(newJersey1,  300,   0,   64,   0, 128);
				createNewEnemy_USSNewJersey_Gun(newJersey1, -300,   0,  192, 128,   0);
				Drivant newJersey2 = createNewEnemy_USSNewJersey(-1200,   -1500);
				createNewEnemy_USSNewJersey_Gun(newJersey2,  200,   0,   64,   0, 128);
				createNewEnemy_USSNewJersey_Gun(newJersey2,  300,   0,   64,   0, 128);
				createNewEnemy_USSNewJersey_Gun(newJersey2, -300,   0,  192, 128,   0);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 09", 
						380, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						250, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(3) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-1024, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-624, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(23) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(-1024, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(26) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(-624, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(43) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2000, 4200, 192);
				createNewEnemy_Avenger(1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(46) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2000, 4200, 192);
				createNewEnemy_Avenger(1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(49) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2000, 4200, 192);
				createNewEnemy_Avenger(1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(63) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-2000, 4200, 192);
				createNewEnemy_Avenger(-1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(66) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-2000, 4200, 192);
				createNewEnemy_Avenger(-1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(69) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-2000, 4200, 192);
				createNewEnemy_Avenger(-1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(90) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2000, 4200, 192);
				createNewEnemy_F4U(1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(93) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2000, 4200, 192);
				createNewEnemy_F4U(1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(96) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(2000, 4200, 192);
				createNewEnemy_F4U(1600, 4200, 192);
				createNewEnemy_B17(2400, 4200, 192);
				createNewEnemy_B17(2800, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(100) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-2000, 4200, 192);
				createNewEnemy_P51D(-1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(103) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-2000, 4200, 192);
				createNewEnemy_P51D(-1600, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(106) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-2000, 4200, 192);
				createNewEnemy_P51D(-1600, 4200, 192);
				createNewEnemy_B29(2400, 4200, 192);
				createNewEnemy_B29(2800, 4200, 192);
			}
		});
		return ret;
	}
	
	@Override
	protected boolean checkIfMissionCompleted() {
		return secondsElapsed()  > 140 || numberOfTotalEnemiesShotDown() >= 38;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_SYM9_4;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/ogasawara_ckt-78-4_c10_1.png";
	}

}
