package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import siovanus.drivant.aviator.RunningAwayAviator;
import siovanus.drivant.aviator.SmartAviator;
import avis.base.AException;
import avis.motion.Drivant;
import avis.session.AScenarioEvent;

public class SStage06Scenario extends SStageScenario {

	@Override
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(1920, 2048, 192);
				Drivant newJersey1 = createNewEnemy_USSNewJersey(-1800,   500);
				createNewEnemy_USSNewJersey_Gun(newJersey1,  200,   0,   64,   0, 128);
				createNewEnemy_USSNewJersey_Gun(newJersey1,  300,   0,   64,   0, 128);
				createNewEnemy_USSNewJersey_Gun(newJersey1, -300,   0,  192, 128,   0);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 06", 
						-1, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						-1, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(10) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-800, 4200, 192);
				createNewEnemy_Avenger( 800, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(14) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1000, 4200, 192);
				createNewEnemy_Avenger( -600, 4200, 192);
				createNewEnemy_Avenger(  600, 4200, 192);
				createNewEnemy_Avenger( 1000, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(18) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-300, 4200, 192);
				createNewEnemy_F4U( 300, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(22) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(   0, 4200, 192);
			}
		});
		ret.add(new SScenarioEvent(26) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-400, 4400, 192);
				createNewEnemy_Avenger(   0, 4000, 192);
				createNewEnemy_Avenger( 400, 4400, 192);
			}
		});
		ret.add(new SScenarioEvent(60) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-800, 4000, 192);
				createNewEnemy_F4U( 800, 4000, 192);
			}
		});
		ret.add(new SScenarioEvent(64) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1000, 4000, 192);
				createNewEnemy_Avenger( -600, 4000, 192);
				createNewEnemy_Avenger(  600, 4000, 192);
				createNewEnemy_Avenger( 1000, 4000, 192);
			}
		});
		ret.add(new SScenarioEvent(68) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-300, 4000, 192);
				createNewEnemy_F4U( 300, 4000, 192);
			}
		});
		ret.add(new SScenarioEvent(72) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D(   0, 4000, 192);
			}
		});
		ret.add(new SScenarioEvent(76) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-400, 4400, 192, false, new SmartAviator());
				createNewEnemy_F4U(   0, 4000, 192, false, new SmartAviator());
				createNewEnemy_Avenger( 400, 4400, 192, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(100) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-5200, 2000, 0);
				createNewEnemy_Avenger(-5300, 1800, 0);
			}

		});
		ret.add(new SScenarioEvent(104) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-5200, 2000, 0, new SmartAviator());
				createNewEnemy_Avenger(-5300, 1800, 0, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(108) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-5200, 2000, 0, new RunningAwayAviator());
				createNewEnemy_Avenger(-5300, 1800, 0, new RunningAwayAviator());
			}
		});
		ret.add(new SScenarioEvent(112) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-5200, 2000, 0, false, new SmartAviator());
				createNewEnemy_Avenger(-5300, 1800, 0, false, new SmartAviator());
			}
		});
		ret.add(new SScenarioEvent(130) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(5200, 1800, 128);
			}
		});
		ret.add(new SScenarioEvent(134) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(5200, 1800, 128);
			}
		});
		ret.add(new SScenarioEvent(138) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(5200, 1800, 128);
			}
		});
		ret.add(new SScenarioEvent(142) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(5200, 1800, 128);
			}
		});
		ret.add(new SScenarioEvent(160) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_P51D( 5200, 1800, 128);
				createNewEnemy_P51D(-5300, 1800, 0);
			}
		});
		return ret;
	}

	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 180) || numberOfTotalEnemiesShotDown() >= 37;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_SYM9_1;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/ogasawara_ckt-78-4_c10_4.png";
	}
}
