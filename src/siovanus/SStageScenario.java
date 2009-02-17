package siovanus;

import java.awt.Color;
import java.util.Iterator;
import java.util.SortedSet;

import avis.base.AException;
import avis.base.Avis;
import avis.session.AScenarioEvent;
import avis.session.ASession;
import avis.sprite.AMessageSprite;

public abstract class SStageScenario extends SScenario {
	protected int tick = 0;
	protected SortedSet<AScenarioEvent> eventList;
	private Iterator<AScenarioEvent> iEventList;
	private AScenarioEvent nextEvent = null;
	protected boolean finished = false;

	protected static int irregularMessageIndex = 0; 
	protected static String[] irregularMessageStrings = new String[] {
		"「この『疾風』の左捩り込みに追随できる航空機など、米英獨佛この地上の何處にも存在しない」",
		"「一式戦とは違うのだよ、一式戦とは！」",
	};
	private AMessageSprite irregularMessage;
	

	@Override
	public AScenarioEvent action() throws AException {
		AScenarioEvent ret = AScenarioEvent.NULL_EVENT;
		try {
			if (!finished && checkIfMissionCompleted()) {
				ssession.disableStatistics();
				missionCompleted();
			}
			if (!finished && checkIfMissionFailed()) {
				ssession.disableStatistics();
				missionFailed();
			}
			if (!(this instanceof SStage00Scenario)) {
				if (!finished && (irregularMessage == null || irregularMessage.isDisposed())) {
					if (tick > 200 && irregularMessageIndex < irregularMessageStrings.length && !userDrivant.isCollisionDetectEnabled()) {
						if (irregularMessage !=  null) {
							irregularMessage.dispose();
						}
						irregularMessage = ssession.message(irregularMessageStrings[irregularMessageIndex], 0, 150, Color.green, 16);
						irregularMessage.lifetime((int) (2.4 * 40));
						irregularMessageIndex ++;
					}
				}
			}
			if (nextEvent == null) {
				return ret;
			}
			if (tick * ssession.interval() >= nextEvent.index() * 1000) {
				ret = nextEvent;
				if (iEventList.hasNext()) {
					nextEvent = iEventList.next();
				} else {
					nextEvent = null;
				}
			}
			////
			// プレーヤキャラクタが範囲外に出た場合、ゲーム終了
			// boundaryCheck();
		} finally {
			tick++;
		}
		return ret;
	}

	protected void missionFailed() {
		ssession.scenarioFinished(SScenarioStatus.Failed);
		ssession.message("Mission Failed", -1, 250, Color.red, (float)64.0);
		showResult(Color.red);
		finished = true; 
		passed = false;
		Avis.logger().info("scenario=<" + this + "> is finished (FAIL).");
		Avis.logger().info(" userDrivant=<" + userDrivant + ">");
		if (userDrivant != null) {
			Avis.logger().debug("userDrivant.valid=<" + userDrivant.isValid() + ">");
		}
	}

	protected void missionCompleted() {
		userDrivant.disableCollisionDetect();
		ssession.scenarioFinished(SScenarioStatus.Completed);
		ssession.message("Mission Completed", -1, 250, Color.cyan, 64);
		showResult(Color.cyan);
		finished = true;
		passed = true;
		Avis.logger().debug("scenario=<" + this + "> is finished (FINISH).");
	}

	private void showResult(Color color) {
		Iterator<String> iLabels = scoreMap.keySet().iterator();
		int y = 320;
		while (iLabels.hasNext()) {
			String curLabel = iLabels.next();
			String numberOfPlanesShotDown = "00" + scoreMap.get(curLabel);
			numberOfPlanesShotDown = numberOfPlanesShotDown.substring(numberOfPlanesShotDown.length() - 3);
			ssession.message(curLabel,               300, y, color, (float)20.0);
			ssession.message(numberOfPlanesShotDown, 700, y, color, (float)20.0);
			y += 30;
		}
	}

	@Override
	public void init(ASession session) throws AException {
		super.init(session);
		
		tick = 0;
		userDrivant = null;
		finished = false;
		passed = false;

		eventList = composeEventList();
		iEventList = eventList.iterator();
		if (iEventList.hasNext()) {
			nextEvent = iEventList.next();
		}
	}

	
	abstract protected SortedSet<AScenarioEvent> composeEventList();

	abstract protected boolean checkIfMissionCompleted();
	protected boolean checkIfMissionFailed() {
		return userDrivant != null && !userDrivant.isValid();
	}

	@Override
	public void reset() {
		super.reset();
		tick = 0;
		userDrivant = null;
		finished = false;
		passed = false;
	}
	
	protected int numberOfEnemiesShotDown(String label) {
		Integer n = scoreMap.get(label);
		return n != null ? n.intValue()
				          : 0;
	}
	
	protected int numberOfTotalEnemiesShotDown() {
		int ret = 0;
		Iterator<String> iLabels = scoreMap.keySet().iterator();
		while (iLabels.hasNext()) {
			ret += numberOfEnemiesShotDown(iLabels.next());
		}
		return ret;
	}
	
	protected int secondsElapsed() {
		return (tick * ssession.interval()) / 1000;
	}

	public void instructionAfterScenario() {
		if (passed) {
			this.ssession.message("[SPACE]/[SQUARE]:Next Stage", -1, 550, Color.cyan, (float)24.0);
		} else {
			this.ssession.message("[SPACE]/[SQUARE]:Continue", -1, 550, Color.red, (float)24.0);
			this.ssession.message("'z'/[CIRCLE]:Quit game", -1, 550, Color.red, (float)24.0);
		}
	}
}
