package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.aviator.Aviator;
import siovanus.drivant.aviator.SmartAviator;
import avis.base.AException;
import avis.base.Avis;
import avis.input.AInputDevice.Trigger;
import avis.motion.Drivant;
import avis.session.AScenarioEvent;

public class SStage00Scenario extends SStageScenario {
	AviatorDrivant specialDrivant;
	protected SortedSet<AScenarioEvent> composeEventList() {
		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewFriend_Ki84(2000, -1500, 64);
				createNewFriend_Ki84(2500, -2000, 64);
				createNewFriend_Ki84(3000, -2500, 64);
				createNewUser_Ki84(500, -500, 64);
				createNewEnemy_P51D(-500, 500, 192);
				createNewEnemy_P51D(-1024, 3072, 192);
				createNewEnemy_P51D(-1024, 3572, 192);
				createNewEnemy_P51D(-1024, 4072, 192);
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				title("Aviator", 
						350, Color.green, 64, -1);
				title("                 1945", 
						380, Color.red, 32, 5);
			}
		});
		ret.add(new SScenarioEvent(5) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				title("Powered by Avis++ Framework", 400, Color.green, 12, -1);
				title("Copyright (C) 2009 INUWI Software Lab.", 500, Color.green, 16, -1);
			}

		});
		ret.add(new SScenarioEvent(11) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				title("                 1945", 
						380, Color.red, 32, 5);
			}

		});
		ret.add(new SScenarioEvent(21) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				title("                 1945", 
						380, Color.red, 32, -1);
			}

		});
		return ret;
	}
	
	@Override
	public AScenarioEvent action() throws AException {
		if (userDrivant != null) {
			userDrivant.disableCollisionDetect();
		}
		AScenarioEvent ret = super.action();
		if (userDrivant != null) {
			userDrivant.disableCollisionDetect();
		}
		return ret;
	}

	@Override
	public Aviator createUserAviator() {
		return new SmartAviator();
	}
	
	@Override
	protected boolean checkIfMissionCompleted() {
		return ssession.stick().trigger(Trigger.SQUARE) || tick > 30 * 40;
	}

	@Override
	protected boolean checkIfMissionFailed() {
		return false;
	}
	
	@Override
	public String bgm() {
		return null;
	}
	@Override
	public void invalidated(Drivant drivant) {
		if (drivant instanceof AviatorDrivant) {
			if (drivant.groupId() == SGroup.Enemy_Aerial) {
				createNewEnemy_P51D(-1024, 3072, 192);
			} else if (drivant.groupId() == SGroup.Player_Aerial) {
				createNewFriend_Ki84(2000, -1500, 64);
			}
		}
	}
		
	public void missionCompleted() {
		userDrivant.disableCollisionDetect();
		ssession.scenarioFinished(SScenarioStatus.Completed);
		finished = true;
		Avis.logger().debug("scenario=<" + this + "> is finished (FINISH).");
		
	}

	@Override
	public String getBackgroundResourceName() {
		return "image/background/numatsu_ccb-75-30_c3_3.png";
	}
	@Override
	public void instructionAfterScenario() {
		ssession.message("[SPACE]:Start Game", -1, 600, Color.cyan, (float)24.0); 
	}

}
