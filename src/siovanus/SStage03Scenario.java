package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage03Scenario extends SStageScenario {

	@Override
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「観測船『音羽丸』より入電！敵、戦爆連合少なくとも三百、豊後水道を北上中！」", 0, 100, Color.green, 16, 3);
				createNewUser_Ki84(512, -600, 64);
				createNewFriend_Zero(768, -960, 64);
				createNewFriend_Zero(256, -960, 64);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「全天、敵に覆われ、空が青く見えない！」", 0, 120, Color.green, 16, 3);
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 03", 
						-1, 350, Color.green, 64, 5);
				message("Intercept intruding enemies.", 
						-1, 450, Color.green, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(3) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(-1200, 3072, 192);
				createNewEnemy_B17(-600, 3072, 192);
				createNewEnemy_B17(-0, 3072, 192);
			}
		});
		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_B17(-1200, 3072, 192);
				createNewEnemy_B17(-600, 3072, 192);
				createNewEnemy_B17(-0, 3072, 192);
			}
		});
		ret.add(new SScenarioEvent(9) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-800, 3072, 192);
				createNewEnemy_Avenger(-200, 3072, 192);
				createNewEnemy_Avenger(400, 3072, 192);
			}
		});
		ret.add(new SScenarioEvent(25) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1600, 3072, 192);
				createNewEnemy_Avenger(-1000, 3072, 192);
				createNewEnemy_Avenger(-400, 3072, 192);
			}
		});
		ret.add(new SScenarioEvent(40) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「一体何機いるんだ！？幾ら落としてもキリが無い！」", 0, 120, Color.green, 16, 3);
				createNewEnemy_F4U(800, 3072, 192);
				createNewEnemy_Avenger(200, 3072, 192);
				createNewEnemy_Avenger(-400, 3072, 192);
				createNewEnemy_B17(200, 3172, 192);
			}
		});
		ret.add(new SScenarioEvent(55) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1600, 3072, 192);
				createNewEnemy_Avenger(1000, 3072, 192);
				createNewEnemy_Avenger(400, 3072, 192);
				createNewEnemy_B17(1000, 3072, 192);
				createNewFriend_Ki84(500, 3300, 64);
				createNewFriend_Ki84(900, 3300, 64);
			}
		});
		ret.add(new SScenarioEvent(59) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1600, 3072, 192);
				createNewEnemy_Avenger(1000, 3072, 192);
				createNewEnemy_Avenger(400, 3072, 192);
				createNewEnemy_B17(1000, 3072, 192);
				createNewFriend_Ki84(700, 3300, 64);
			}
		});
		ret.add(new SScenarioEvent(60) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(800, 3072, 192);
				createNewEnemy_F4U(200, 3072, 192);
				createNewEnemy_F4U(-400, 3072, 192);
				createNewEnemy_B17(200, 3172, 192);
			}
		});
		ret.add(new SScenarioEvent(80) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_F4U(1600, 3072, 192);
				createNewEnemy_F4U(1000, 3072, 192);
				createNewEnemy_F4U(400, 3072, 192);
				createNewEnemy_B17(1000, 3072, 192);
			}
		});
		return ret;
	}
	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 100) || numberOfTotalEnemiesShotDown() >= 32;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_WALKURENRITT;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/numatsu_ccb-75-30_c3_1.png";
	}
}
