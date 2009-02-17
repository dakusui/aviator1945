package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage02Scenario extends SStageScenario {
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(-1024, -3072, 64);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 02", 
						380, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						250, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(2) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewFriend_Zero(-624, -3072, 64);
				createNewFriend_Zero(-1424, -3072, 64);
			}
		});
		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(-1024, 3900, 192, true);
				createNewEnemy_Avenger(-1224, 3700, 192, true);
				createNewEnemy_Avenger(-824, 3700, 192, true);
				message("「距離3,000、『フランク』１、『ゼロ』２。全機、攻撃に移れ！」", 0, 100, Color.pink, 16, 3);
			}
		});
		ret.add(new SScenarioEvent(21) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1024, 3900, 192);
				createNewEnemy_Avenger(1224, 3900, 192);
				createNewEnemy_Avenger(824, 3900, 192);
				message("「その『フランク』はプロだ！相手にするな！」", 0, 100, Color.pink, 16, 3);
			}
		});
		ret.add(new SScenarioEvent(25) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1280, 3850, 0, true);
			}
		});
		ret.add(new SScenarioEvent(26) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1280, 3850, 0, true);
			}
		});
		ret.add(new SScenarioEvent(27) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1280, 3850, 0, true);
				message("「その『フランク』はプロだ！危険過ぎる！」", 0, 100, Color.pink, 16, 3);
			}
		});
		ret.add(new SScenarioEvent(28) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(-1280, 3900, 192);
			}
		});
		ret.add(new SScenarioEvent(40) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「ゴルフ・リーダー！日本軍の新型がこっちに！助けて下さい！」", 0, 100, Color.pink, 16, 3);
				createNewEnemy_Avenger(1280, -3900, 96);
			}
		});
		ret.add(new SScenarioEvent(41) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(1280, -3900, 96);
			}
		});
		ret.add(new SScenarioEvent(42) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(1280, -3900, 96);
			}
		});
		ret.add(new SScenarioEvent(43) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(1280, 3900, 192);
			}
		});
		ret.add(new SScenarioEvent(55) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1280, 3900, 192);
			}
		});
		ret.add(new SScenarioEvent(60) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1280, 3900, 192);
			}
		});
		ret.add(new SScenarioEvent(65) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1280, 3900, 192);
			}
		});
		return ret;
	}

	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 100) || numberOfTotalEnemiesShotDown() >= 17;
	}

	
	@Override
	public String bgm() {
		return SScenario.BGM_CARMEN1_2;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/saku_ccb-75-13_c32b_18.png";
	}

}
