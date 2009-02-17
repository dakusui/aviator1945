package siovanus;

import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;

import siovanus.drivant.AviatorDrivant;
import siovanus.spec.SRadarSpriteSpec;
import siovanus.sprite.SRadarSprite;
import siovanus.sprite.SShotSprite;
import avis.base.AException;
import avis.base.AResourceLoader;
import avis.base.Avis;
import avis.base.Configuration;
import avis.input.AInputDevice;
import avis.input.AInputDevice.Trigger;
import avis.motion.Drivant;
import avis.motion.MotionController;
import avis.session.AScenario;
import avis.session.AScenarioEvent;
import avis.session.ASession;
import avis.sprite.AMessageSprite;
import avis.sprite.ASprite;
import avis.video.APlane;
import avis.video.AScreen;
import avis.video.AViewport;

public class SSession extends ASession {
	public static final String SE_EXPLOSION = "sound/explosion02.wav";

    public static final String SE_SHOT = "sound/gun02.wav";
	private static final int TRAIL_SIZE = 12;

	public static void main(String[] args) throws Exception {
		String[] nargs = new String[args.length + 1];
		System.arraycopy(args, 0, nargs, 1, args.length);
		nargs[0] = "-session=siovanus.SSession";
		ASession.main(nargs);
	}
	
	private void loadRadar(AScreen screen, MotionController controller) {
		{
			SRadarSpriteSpec p = new SRadarSpriteSpec(controller, screen.backgroundPlane());
			SRadarSprite radar = (SRadarSprite) p.createSprite(1000);
			screen.foregroundPlane().attach(radar);
			radar.put(screen.getWidth() - p.width() - 20, screen.getHeight() - p.height() - 20);
			radar.visible(true);
		}
    	scoreSprite = screen.message("Score:" + "-", 20, 50, Color.red, 16);
    	lifeSprite = screen.message("Life:" + "-", 20, 70, Color.red, 16);
	}

	private int _dx;
	private int _dy;
	private double bgOriginX;
	private double bgOriginY;
	private AMessageSprite lifeSprite;
	private AMessageSprite scoreSprite;
	private int trailIndex;
	private double[] trailTheta = new double[TRAIL_SIZE];
	private double[] trailX = new double[TRAIL_SIZE], trailY = new double[TRAIL_SIZE];

	private double viewMaxX;
	
	private double viewMaxY;

	private double viewMinX;

    
    private double viewMinY;

    private Drivant drivant = null;

	public SSession() throws IOException {
		super();
	}

    public static class SRunningState extends SSession.BaseState {
		public void action(AScenario scenario) throws AException {
			if (!userPaused) {
				session.controller().action();
				AScenarioEvent event = scenario.action();
				event.perform(scenario);
				session.controller().update();
			}
		}
		protected SRunningState(ASession session) {
			super(session);
		}
		boolean userPaused = false;
		AMessageSprite pausingMessage = null;
		public void afterAction(AScenario scenario) {
			SSession ssession = (SSession) this.session;
			if (!userPaused) {
				if (ssession.remainingTicksToSuspend == 0) {
					Iterator<ASprite> iSprites = ssession.screen.backgroundPlane().sprites().iterator();
					while (iSprites.hasNext()) {
						ASprite cur = iSprites.next();
						if (cur instanceof SShotSprite) {
							cur.visible(false);
						}
					}
					ssession.pause();
					ssession.getBgmManager().stop();
					ssession.getSoundManager().stopAll();
					ssession.disableRendering();
				} else if (ssession.remainingTicksToSuspend > 0) {
					if (ssession.remainingTicksToSuspend == 1) {
						((SScenario)scenario).instructionAfterScenario();
					}
					ssession.remainingTicksToSuspend--;
				} else if (session.stick().trigger(Trigger.START)) {
					pausingMessage = ssession.message("PAUSED", -1, 600, Color.green, 24);
					ssession.getBgmManager().stop();
					ssession.getSoundManager().stopAll();
					try {
						Avis.logger().statistics("This scenario=<" + scenario + "> is paused. Statistics info about this scenario run is not reliable anymore.");
						Thread.sleep(250);
					} catch (InterruptedException e) {
						Avis.logger().error("Wait interrupted", e);
					}
					userPaused = true;
				}
			} else {
				if (ssession.stick().trigger(Trigger.START)) {
					try {
						Avis.logger().statistics("This scenario=<" + scenario + "> has been paused. Statistics info about this scenario run is not reliable anymore.");
						Thread.sleep(250);
					} catch (InterruptedException e) {
						Avis.logger().error("Wait interrupted", e);
					}
					userPaused = false;
					pausingMessage.dispose();
					ssession.enableRendering();
					ssession.getBgmManager().resume();
				} else {
					ssession.getBgmManager().stop();
					ssession.getSoundManager().stopAll();
					ssession.disableRendering();
				}
			}
		}
		public void beforeAction(AScenario scenario) throws AException {
		}
    }
    private static final double CAMERA_BIAS = 200;

    public static class SPauseState extends SSession.BaseState {
		int ticksWaiting = 0;
		public SPauseState(ASession session) {
			super(session);
		}
		
		public void action(AScenario scenario) {
			SScenario sscenario = (SScenario)scenario;
			int exitCode = EXIT_NORMAL;
			if (ticksWaiting > 480) {
				if (sscenario.isPassed()) {
					exitCode = EXIT_NEXT;
				} else {
					exitCode = EXIT_RESTART;
				}
				session.exit(exitCode);
			}
			ticksWaiting++;
			if (session.screen().stick().trigger(AInputDevice.Trigger.SQUARE)) {
				SSession ssession = (SSession) session;
				if (ssession.scenarioStatus == SScenarioStatus.Completed) {
					exitCode = EXIT_NEXT;
				} else if (ssession.scenarioStatus == SScenarioStatus.Failed) {
					exitCode = EXIT_CONTINUE;
				}
				session.exit(exitCode);
			}
		
		}

		public void afterAction(AScenario scenario) throws AException {
		}

		public void beforeAction(AScenario scenario) throws AException {
		}
    }

	public static abstract class BaseState implements SSession.State {
		public ASession session;
	
		public BaseState(ASession session) {
			this.session = session;
		}
	}

	@Override
	protected void action(AScenario scenario) throws AException {
		currentState.action(scenario);
	}

	@Override
	protected void afterAction(AScenario scenario) throws AException {
		currentState.afterAction(scenario);
    	APlane backgroundPlane = screen.backgroundPlane(); 
        ////
        // ビューの設定
        int viewIndex;
        viewIndex = (trailIndex + 1) % TRAIL_SIZE;
        AViewport viewport = backgroundPlane.viewport();
        viewport.center(Math.min(viewMaxX, Math.max(viewMinX, trailX[viewIndex] + bgOriginX)), 
        		Math.min(viewMaxY, Math.max(viewMinY, - trailY[viewIndex] + bgOriginY)), 
        		(trailTheta[viewIndex] - Avis.DIRECTION_STEPS / 4) % Avis.DIRECTION_STEPS);
        trailIndex++;
    	trailIndex %= TRAIL_SIZE;
    	if (drivant != null) {
	    	trailX[trailIndex] = drivant.x() + Avis.cos((int) drivant.direction()) * CAMERA_BIAS;
	    	trailY[trailIndex] = drivant.y() + Avis.sin((int) drivant.direction()) * CAMERA_BIAS;
	    	trailTheta[trailIndex] = drivant.direction();
    	} else {
	    	trailX[trailIndex] = initialX;
	    	trailY[trailIndex] = initialY;
	    	trailTheta[trailIndex] = initialDirection;
    	}

		////
		// レイアウト時に使用する、スプライトの移動量
    	_dx = (int) (bgOriginX -  viewport.x() + backgroundPlane.width() / 2);
    	_dy = (int) (bgOriginY -  viewport.y() + backgroundPlane.height() / 2);
	}

	@Override
	protected void beforeAction(AScenario scenario) throws AException {
		currentState.beforeAction(scenario);
	}

	public static interface State {
		public void action(AScenario scenario) throws AException;
	
		public void afterAction(AScenario scenario) throws AException;
	
		public void beforeAction(AScenario scenario) throws AException;
	}


	@Override
    protected void afterRun(AScenario scenario) {
		this.getBgmManager().stop();
		this.getSoundManager().terminate();
    }
	
    @Override
	protected void aftetrLayout() {
	}

    @Override
    protected void afterTerminate(AScenario scenario) {
    }
    
    @Override
    protected String backgroundImageResourceName() {
    	return scenario.backgroundImageResource();
    }
    
    @Override
    protected void beforeLayout() {
	}

	@Override 
    protected void beforeRun(AScenario scenario) {
		start();
    }


	@Override
    protected String[] bgmResources() {
    	return new String[]{
    			SScenario.BGM_CHOPIN_OP39, 
    			SScenario.BGM_CARMEN1_2, 
    			SScenario.BGM_LISZT143, 
    			SScenario.BGM_WALKURENRITT, 
    			SScenario.BGM_IMPROMPTU_4, 
    			SScenario.BGM_QUARTET_10, 
    			SScenario.BGM_SYM9_1, 
    			SScenario.BGM_SYM9_4,
    			SScenario.BGM_BWV1068_2
    	};
    }
	
    private void calcurateBgOrigin() {
    	APlane backgroundPlane = screen.backgroundPlane(); 
    	bgOriginX = backgroundPlane.background().imageWidth() / 2;
    	bgOriginY = backgroundPlane.background().imageHeight() / 2;
	}

    public void userDrivantCreated(Drivant userDrivant) {
        // 01. modification
        for (int i = 0; i < TRAIL_SIZE; i++) {
        	trailX[i] =  userDrivant.x() + Avis.cos(userDrivant.direction())* CAMERA_BIAS;;
        	trailY[i] =  userDrivant.y() + Avis.sin(userDrivant.direction())* CAMERA_BIAS;
        	trailTheta[i] = userDrivant.direction();
        }
        this.drivant = userDrivant;
    }
    
    @Override
    protected AScenario createDefaultScenario() throws AException {
    	AScenario ret = new SStage01Scenario();
    	ret.init(this);
		return ret;
    }

    public void explosion(AScenario scenario, Drivant drivant) {
    	if (drivant instanceof AviatorDrivant) {
    		Siovanus.explosionSpec.createMMachine(1000, drivant.x(), drivant.y(), drivant.direction(), drivant.velocity(), null, (AviatorDrivant) drivant);
    	} else {
    		Siovanus.explosionSpec.createMMachine(1000, drivant.x(), drivant.y(), drivant.direction(), drivant.velocity(), null, null);
    	}
	}
    
    public void minorExplosion(AScenario scenario, Drivant drivant) {
    	if (drivant instanceof AviatorDrivant) {
    		Siovanus.minorExplosionSpec.createMMachine(1000, drivant.x(), drivant.y(), drivant.direction(), drivant.velocity(), null, (AviatorDrivant) drivant);
    	} else {
        	Siovanus.minorExplosionSpec.createMMachine(1000, drivant.x(), drivant.y(), drivant.direction(), drivant.velocity(), null);
    	}
	}
        
    
	@Override
    public void layout(ASprite sprite, Drivant drivant) {
    	int x, y;
        x = (int) (  drivant.x() ) + _dx;
    	y = (int) (- drivant.y() ) + _dy;
    	sprite.put(x, y, drivant.viewDirection());
    	sprite.pattern((int) drivant.bank() / sprite.spec().patternDenominator());
    }

	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 * @return メッセージの表示に用いたスプライト
	 */
	public AMessageSprite message(String message, int x, int y, Color color, float f) {
		return screen.message(message, x, y, color, f);
	}

	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 * @param durationToDisplay 表示する時間(単位:ticks)
	 */
	public void message(String message, int x, int y, Color color, float f, int durationToDisplay) {
		screen.message(message, x, y, color, f, durationToDisplay);
	}
	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 * @param durationToDisplay 表示する時間(単位:ticks)
	 * @param specName 描画に使用する<code>ASpriteSpec</code>の名前
	 */
	public void message(String message, int x, int y, Color color, float f, int durationToDisplay, String specName) {
		screen.message(message, x, y, color, f, durationToDisplay, specName);
	}

	@Override
    protected String[] soundEffectResources() {
    	return new String[]{SSession.SE_SHOT, SSession.SE_EXPLOSION};
    }

	public void updateLife(int life) {
		lifeSprite.message("Life:" + 
							(life < 0 ? 0
				                      : life)
				);
	}

	public void updateScore(int score) {
		scoreSprite.message("Score:" + score);
	}

	private void updateViewBoundaries(APlane plane) {
    	double w2 = plane.width() * plane.width();
    	double h2 = plane.height() * plane.height();
    	double r = Math.sqrt(w2 + h2);
    	double sin = plane.height() / r;
    	double cos = plane.width() / r;
    	double marginRatio = Math.max(sin, cos);
    	
    	viewMinX = Double.MIN_VALUE;
    	viewMaxX = Double.MAX_VALUE;
    	viewMinY = Double.MIN_VALUE; 
    	viewMaxY = Double.MAX_VALUE;
    	double x1 = (r * marginRatio) * plane.viewport().ratio() + 300;
    	double x2 =  plane.background().imageWidth() - x1;
    	double y1 = (r * marginRatio) * plane.viewport().ratio() + 300;
    	double y2 =  plane.background().imageHeight() - y1;
    	viewMinX = Math.min(x1, x2);
    	viewMaxX = Math.max(x1, x2);
    	viewMinY = Math.min(y1, y2); 
    	viewMaxY = Math.max(y1, y2);
    }

	/**
	 * 負 - サスペンドしない、またはサスペンドを解除する / 0 - サスペンド中 / 1以上 - suspendを行うまでの待ち時間(tick数)
	 */
	int remainingTicksToSuspend = -1;

	private ASprite blurSprite;

	private SScenarioStatus scenarioStatus;

	private double initialX;

	private double initialY;

	private double initialDirection;

	private State pauseState;

	private State runningState;

	private State currentState;
	
	public void suspend(int ticks) {
		try {
			this.blurSprite = screen.createSprite(screen.foregroundPlane(), Siovanus.SPRITESPEC_DEFAULT_BLUR,  2000);
			this.blurSprite.put(0,0);
		} catch (AException e) {
			// SBlurEffectSpriteの表示で例外が送出されることはないため、なにもしないで続行
		}
		remainingTicksToSuspend = ticks;
	}

	public SScenarioStatus scenarioFinished(SScenarioStatus scenarioStatus) {
		if (this.scenarioStatus == null) {
			this.scenarioStatus = scenarioStatus;
			suspend(50);
		}
		return this.scenarioStatus;
	}
	
	public void pause() {
		currentState(this.pauseState);
	}
	
	public void start() {
		currentState(this.runningState);
    	this.getBgmManager().play(((SScenario) scenario).bgm());
		remainingTicksToSuspend = -1;
		if (blurSprite != null) {
			this.blurSprite.dispose();
		}
		this.scenarioStatus = null;
	}

	@Override
	protected AScenario createScenario() {
		SScenario cur = new SStage00Scenario();
		SScenario first = cur;
		SScenario last = first;
		last.append(last = new SStage01Scenario());
		last.append(last = new SStage02Scenario());
		last.append(last = new SStage03Scenario());
		last.append(last = new SStage04Scenario());
		last.append(last = new SStage05Scenario());
		last.append(last = new SStage06Scenario());
		last.append(last = new SStage07Scenario());
		last.append(last = new SStage08Scenario());
		last.append(last = new SStage09Scenario());
		last.append(last = new SStage99Scenario());
		return cur;
	}

	@Override
	protected void init(AScenario scenario) throws AException {
		Avis.logger().info("Scenario initialization process started (free=<" + Runtime.getRuntime().freeMemory() + ">, memory=<" + Runtime.getRuntime().maxMemory() + ">");
		System.gc();
		super.init(scenario);
    	loadRadar(screen, controller);
		Avis.logger().info("                                       (free=<" + Runtime.getRuntime().freeMemory() + ">, memory=<" + Runtime.getRuntime().maxMemory() + ">");
    	SScenario sscenario = (SScenario) scenario;
    	initialX         = sscenario.initialX();
    	initialY         = sscenario.initialY();
    	initialDirection = sscenario.initialDirection();
    	String bgResourceName = sscenario.getBackgroundResourceName();
    	double ratio = 4.0;
    	if (Configuration.bgQualityMode == Configuration.BgQualityMode.LOW) {
    		int index = bgResourceName.lastIndexOf('/');
    		String filename = bgResourceName.substring(index + 1);
    		bgResourceName = bgResourceName.substring(0, index + 1) + "lq." + filename;
    		ratio = 8.0;
    	}
    	screen.backgroundPlane().background().load(bgResourceName);
    	screen.backgroundPlane().viewport().zoom(ratio);
    	updateViewBoundaries(screen.backgroundPlane());
        calcurateBgOrigin();
		System.gc();
		this.pauseState = createPauseState();
		this.runningState = createRunningState();
		Avis.logger().statistics("GC result:free/total/max=<" + Avis.format(Runtime.getRuntime().freeMemory()) + "[B]/" +Avis.format(Runtime.getRuntime().totalMemory()) + "[B]/" + Avis.format(Runtime.getRuntime().maxMemory()) + "[B]>");
	}

	public SSession.State currenState() {
		return currentState;
	}

	public void currentState(SSession.State state) {
		currentState = state;
	}

	protected SSession.State createPauseState() {
		return new SPauseState(this);
	}

	protected SSession.State createRunningState() {
		return new SRunningState(this);
	}

	@Override
	public AResourceLoader createResourceLoader() {
		return new AResourceLoader() {
			public void loadResources() throws AException {
				Siovanus.load(builder, controller);
				Siovanus.loadSpriteSpecs(videoEngine().getSpriteSpecManager());
			}
		};
	}

}
