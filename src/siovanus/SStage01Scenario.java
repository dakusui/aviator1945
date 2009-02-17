package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import avis.base.AException;
import avis.session.AScenarioEvent;

public class SStage01Scenario extends SStageScenario {
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("昭和廿年、三月。", 0, 100, Color.green, 16, 3);
				createNewUser_Ki84(3000, -2500, 64);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("私は、海軍での制式採用が検討されていたある実験機のパイロットとして、防空任務に当たっていた…。", 0, 120, Color.green, 16, 3);
				((SSession) sscenario.session()).updateLife(sscenario.userDrivant().life());
				((SSession) sscenario.session()).updateScore(sscenario.score());
				message("STAGE 01", 
						-1, 350, Color.green, 64, 5);
				message("Destroy all the approaching enemies", 
						-1, 450, Color.green, 32, 5);
			}
		});

		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-624 * 4, 850 * 4, 192);
			}
		});
		ret.add(new SScenarioEvent(8) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-674 * 4, 850 * 4, 192);
			}
		});
		ret.add(new SScenarioEvent(10) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-724 * 4, 850 * 4, 192);
			}
		});
		
		ret.add(new SScenarioEvent(30) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「エコー・ツーよりエコー・ワン」", 0, 100, Color.pink, 16, 3);
				createNewEnemy_Avenger( -800, 4200, 192, true);
				createNewEnemy_Avenger(-1000, 4500, 192, true);
				createNewEnemy_Avenger(-1200, 4200, 192, true);
			}
		});
		ret.add(new SScenarioEvent(33) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「12時の方向、『オスカー』1機。群れから離れたマヌケらしい。撃墜する！」", 0, 120, Color.pink, 16, 3);
			}
		});
		ret.add(new SScenarioEvent(36) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「エコー・ツー！やめろ！そいつは『オスカー』じゃない！『フランク』だ！」", 0, 140, Color.pink, 16, 3);
			}
		});
		ret.add(new SScenarioEvent(51) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewEnemy_Avenger(-1700, 4000, 192);
				createNewEnemy_Avenger(-1850, 4100, 192);
				createNewEnemy_Avenger(-1850, 4300, 192, true);
				createNewEnemy_Avenger(-2000, 4300, 192);
				createNewEnemy_Avenger(-2150, 4100, 192);
				createNewEnemy_Avenger(-2150, 4300, 192, true);
				createNewEnemy_Avenger(-2300, 4000, 192);
			}
		});
		ret.add(new SScenarioEvent(80) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				message("「みせてもらおうか、日本軍の新型の性能とやらを」", 0, 100, Color.pink, 16, 3);
				createNewEnemy_P51D(-1024, 3072, 192);
				createNewEnemy_P51D(-2048, 3072, 192);
			}
		});
		return ret;
	}

	@Override
	protected boolean checkIfMissionCompleted() {
		return (secondsElapsed()  > 100);
	}

	@Override
	public String bgm() {
		return SScenario.BGM_QUARTET_10;
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/kure_ccg-74-7_c42_1.png";
	}

}
