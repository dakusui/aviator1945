package siovanus;

import java.awt.Color;
import java.util.SortedSet;
import java.util.TreeSet;

import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.aviator.Aviator;
import siovanus.drivant.aviator.SmartAviator;
import avis.base.AException;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.session.AScenarioEvent;

public class SStage99Scenario extends SStageScenario {
	AviatorDrivant specialDrivant;
	long timeStarted = -1;

	protected SortedSet<AScenarioEvent> composeEventList() {
		irregularMessageIndex = irregularMessageStrings.length;

		SortedSet<AScenarioEvent> ret = new TreeSet<AScenarioEvent>();
		final Color creditMessageColor = Color.green;
		final Color creditMessageColor2 = Color.green;
		ret.add(new SScenarioEvent(0) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewUser_Ki84(4500, -3300, 106);
				timeStarted = System.currentTimeMillis();
			}
		});
		ret.add(new SScenarioEvent(1) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("八月十五日", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(2) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("焦土の中、戦ひは終はつた", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(4) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("帝國海軍、最後の榮光、第三九九航空隊", 740, creditMessageColor,
						28, 10);
				createNewFriend_Zero(4000 +250, -3800, 106);
				createNewFriend_Zero(4000 +750, -3800, 106);
			}
		});
		ret.add(new SScenarioEvent(6) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("その戦闘記録の全ては", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(7) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("華頂司令の命令で焼却された", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(10) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("「疾風」(しつぷう)の設計記録もまた", 740, creditMessageColor,
						28, 10);
				createNewFriend_Ki84(4000 -500, -3500, 106);
				createNewFriend_Ki84(4000 +0, -3500, 106);
				createNewFriend_Ki84(4000 +1500, -3500, 106);
				createNewFriend_Ki84(4000 +1000, -3500, 106);
			}
		});
		ret.add(new SScenarioEvent(11) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("四式戦闘機の記録と意図的に混同され", 740, creditMessageColor, 28,
						10);
			}
		});
		ret.add(new SScenarioEvent(12) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("忘れ去られることとなつた", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(14) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("多くの勇猛な米軍パイロットさえ恐れさせた「白い悪魔」", 740,
						creditMessageColor, 28, 10);
				createNewFriend_Zero(4000 +250, -3500, 106);
				createNewFriend_Zero(4000 +750, -3500, 106);
			}
		});
		ret.add(new SScenarioEvent(16) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("その行方もまた、誰も知らない。", 740, creditMessageColor, 28,
						10);
			}
		});
		ret.add(new SScenarioEvent(25) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("「白い悪魔」", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(26) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("それは誰だつたのか？", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(30) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("第三九九航空隊搭乗員の多くが戦死し、", 740, creditMessageColor,
						28, 10);
			}
		});
		ret.add(new SScenarioEvent(31) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("司令であつた華頂博信侯爵も世を去つた今", 740, creditMessageColor,
						28, 10);
			}
		});
		ret.add(new SScenarioEvent(33) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("それを知る術はもうない", 740, creditMessageColor, 28, 10);
			}
		});
		ret.add(new SScenarioEvent(60) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("- 完 -", 570, creditMessageColor, 48, 2);
			}
		});
		ret.add(new SScenarioEvent(61) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewFriend_Zero(4000 +500, -3100, 106);
				createNewFriend_Zero(4000 +400, -3200, 106);
				createNewFriend_Zero(4000 +300, -3300, 106);
				createNewFriend_Zero(4000 +200, -3400, 106);
				createNewFriend_Zero(4000 +100, -3500, 106);
				createNewFriend_Zero(4000 +-500, -3100, 106);
				createNewFriend_Zero(4000 +-400, -3200, 106);
				createNewFriend_Zero(4000 +-300, -3300, 106);
				createNewFriend_Zero(4000 +-200, -3400, 106);
				createNewFriend_Zero(4000 +-100, -3500, 106);
			}
		});
		ret.add(new SScenarioEvent(80) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("制作", 740, creditMessageColor2, 24, 12);
			}
		});
		ret.add(new SScenarioEvent(81) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("INUWI Software Lab.", 760, creditMessageColor2,
						18, 11);
			}
		});
		ret.add(new SScenarioEvent(88) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("音楽", 730, creditMessageColor2, 24, 10);
			}
		});
		ret.add(new SScenarioEvent(89) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit("Carmen Suite No.1, 2.Aragonaise",
						"Georges Bizet(1838-1875）(arranged by Ernest Guiraud)",
						"Tirol", "http://tirolmusic.blogspot.com/", null,
						creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(91) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit("Scherzo No.3 in C-sharp minor Op.39",
						"Frederic Francois Chopin(1810-1849)", "Tirol",
						"http://tirolmusic.blogspot.com/", null,
						creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(93) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Orchestral suites No.3 in D major BWV.1068 (excerpt)",
						"Johann Sebastian Bach(1685-1750)", "Tirol",
						"http://tirolmusic.blogspot.com/", null,
						creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(95) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Ab Irato - Etude de perfectionnement S.143",
						"Franz Liszt(1811-1886)", "Tirol",
						"http://tirolmusic.blogspot.com/", null,
						creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(97) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Exposition du Troisieme mouvement du Quatuor a cordes n 10 de Beethoven",
						"Ludwig van Beethoven",
						"propre travail realise avec Midisoft Recording Session",
						"http://commons.wikimedia.org/wiki/File:Quartet_10_mvt3.MID",
						null, creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(99) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Impromptus D899 No. 2",
						"Franz Schubert",
						"Bernd Krueger",
						"http://www.piano-midi.de/schub.htm",
						"http://commons.wikimedia.org/wiki/File:Schubert_Impromptus_D899_No_2.mid",
						creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(101) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Impromptus D899 No. 4",
						"Franz Schubert",
						"Bernd Krueger",
						"http://www.piano-midi.de/schub.htm",
						"http://commons.wikimedia.org/wiki/File:Schubert_Impromptus_D899_No_4.mid",
						creditMessageColor2);
			}
		});

		ret.add(new SScenarioEvent(103) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Symphony No. 9, Movement 1 Op. 125",
						"Ludwig van Beethoven",
						null,
						"http://commons.wikimedia.org/wiki/File:Sym_9_First_Movement.mid",
						null, creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(105) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				musicLicenseCredit(
						"Symphony No. 9, Movement 4 Op. 125",
						"Ludwig van Beethoven",
						null,
						"http://commons.wikimedia.org/wiki/File:Sym_9_Fourth_Movement.mid",
						null, creditMessageColor2);
			}
		});
		ret.add(new SScenarioEvent(110) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("画像", 730, creditMessageColor2, 24, 10);
			}
		});
		ret.add(new SScenarioEvent(111) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("国土情報ウェブマッピングシステム", 740, creditMessageColor2, 12,
						10);
				creditMessage("国土交通省国土計画局参事官室", 752, creditMessageColor2, 12,
						10);
				creditMessage("National-Land Information Office", 764,
						creditMessageColor2, 10, 10);
				creditMessage("http://w3land.mlit.go.jp/WebGIS", 776,
						creditMessageColor2, 10, 10);
			}
		});
		ret.add(new SScenarioEvent(112) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewFriend_Zero(4000 +100, -3100, 106);
				createNewFriend_Zero(4000 +200, -3200, 106);
				createNewFriend_Zero(4000 +300, -3300, 106);
				createNewFriend_Zero(4000 +400, -3400, 106);
				createNewFriend_Zero(4000 +500, -3500, 106);
				createNewFriend_Zero(4000 +-100, -3100, 106);
				createNewFriend_Zero(4000 +-200, -3200, 106);
				createNewFriend_Zero(4000 +-300, -3300, 106);
				createNewFriend_Zero(4000 +-400, -3400, 106);
				createNewFriend_Zero(4000 +-500, -3500, 106);
			}
		});
		ret.add(new SScenarioEvent(113) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("FlightGear", 740, creditMessageColor2, 12, 10);
				creditMessage("http://www.flightgear.org/", 752,
						creditMessageColor2, 10, 10);
				creditMessage("FlightGear Aircraft Downloads", 764,
						creditMessageColor2, 12, 10);
				creditMessage("http://www.flightgear.org/Downloads/aircraft/",
						776, creditMessageColor2, 10, 10);
			}
		});
		ret.add(new SScenarioEvent(120) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("試験協力", 730, creditMessageColor2, 24, 10);
			}
		});
		ret.add(new SScenarioEvent(121) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("HRA!", 740, creditMessageColor2, 12, 10);
				creditMessage("http://www5d.biglobe.ne.jp/~hra/", 752,
						creditMessageColor2, 10, 10);
			}
		});
		ret.add(new SScenarioEvent(123) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("寝癖", 740, creditMessageColor2, 12, 10);
				creditMessage("http://ninjinsword.at.infoseek.co.jp/", 752,
						creditMessageColor2, 10, 10);
			}
		});
		ret.add(new SScenarioEvent(191) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				createNewFriend_Ki84(4000 +500, -3500, 106);
			}
		});
		ret.add(new SScenarioEvent(195) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage("Aviator", 
						740, Color.green, 64, 10);
				creditMessage("                 1945", 
						770, Color.red, 32, 10);
				creditMessage("Powered by Avis++ Framework", 
						785, Color.green, 16, 10);
			}
		});
		ret.add(new SScenarioEvent(200) {
			@Override
			protected void performSiovanusEvent(SScenario sscenario)
					throws AException {
				creditMessage(
						"Copyright (C) 2009 INUWI Software Lab.",
						740, creditMessageColor2, 16, 10);
			}
		});

		return ret;
	}

	void musicLicenseCredit(String title, String composer, String revisor,
			String url1, String url2, Color c) {
		creditMessage(title, 740, c, 12, 10);
		creditMessage("Composed by " + composer, 752, c, 12, 10);
		if (revisor != null) {
			creditMessage("Revised by " + revisor, 764, c, 12, 10);
		} else {
			creditMessage("PUBLIC DOMAIN", 764, c, 12, 10);
		}
		creditMessage(url1, 776, c, 10, 10);
		if (url2 != null) {
			creditMessage(url2, 788, c, 10, 10);
		}
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
		// 音楽との同期上、tickだけではなく実時間を使用する。
		if (timeStarted != -1) {
			return 
			tick > (205 * 1000) / ssession.interval() && 
			(System.currentTimeMillis() - timeStarted) > 257000 ? true
					                                           : false;
		}
		return false;// timeStarted != -1 && (System.currentTimeMillis() -
						// timeStarted) > 257000; 
	}

	@Override
	protected boolean checkIfMissionFailed() {
		return false;
	}

	@Override
	public String bgm() {
		return SScenario.BGM_BWV1068_2;
	}

	@Override
	public void invalidated(Drivant drivant) {
		if (drivant instanceof AviatorDrivant) {
			if (drivant.groupId() == SGroup.Enemy_Aerial) {
				createNewEnemy_P51D(-1024, 3072, 192);
			} else if (drivant.groupId() == SGroup.Player_Aerial) {
				// createNewFriend_Ki84(4000 +500, -3500, 106);
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
		ssession.message("[SPACE]:Good bye", -1, 600, Color.cyan, (float)24.0); 
	}
}
