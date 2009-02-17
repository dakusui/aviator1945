package avis.session;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.io.IOException;
import java.util.Iterator;

import javax.sound.sampled.LineUnavailableException;

import com.centralnexus.input.Joystick;

import avis.base.AException;
import avis.base.AResourceLoader;
import avis.base.AResourceObserver;
import avis.base.Avis;
import avis.base.Configuration;
import avis.base.Configuration.BgQualityMode;
import avis.base.Configuration.FrameMode;
import avis.base.Configuration.JoystickMode;
import avis.base.Configuration.RotationMode;
import avis.input.AInputDevice;
import avis.input.AInputDevice.Trigger;
import avis.motion.Drivant;
import avis.motion.MMachine;
import avis.motion.MMachineBuilder;
import avis.motion.MMachineBuilderImpl;
import avis.motion.MMachineSpec;
import avis.motion.MotionController;
import avis.sound.ABGMManager;
import avis.sound.ADefaultBGMManager;
import avis.sound.ADefaultSoundManager;
import avis.sound.AMultiThreadedSoundManager;
import avis.sound.ASoundManager;
import avis.spec.ASpriteSpecManager;
import avis.sprite.ASprite;
import avis.video.AScreen;
import avis.video.AVideoEngine;

public abstract class ASession implements AResourceObserver {
	public static final int EXIT_ABNORMAL = -1;
	public static final int EXIT_NORMAL = 0;
	public static final int EXIT_NEXT = 1;
	public static final int EXIT_CONTINUE = 2;
	public static final int EXIT_RESTART = 3;
	public static final int EXIT_QUIT = 4;

	public static class Statistics {
		public int numberOfTimesActionPerformed;
		public long actionRunningTime;
		public long layoutRunningTime;
		public long videoRenderingRunningTime;
		public long videoBgRenderingRunningTime;
		public long soundRenderingRunningTime;
		public long amountTimeSpent;

		public void add(Statistics another) {
			this.numberOfTimesActionPerformed += another.numberOfTimesActionPerformed;
			this.actionRunningTime += another.actionRunningTime;
			this.videoRenderingRunningTime += another.videoRenderingRunningTime;
			this.soundRenderingRunningTime += another.soundRenderingRunningTime;
			this.amountTimeSpent += another.amountTimeSpent;
		}

		public void dump() {
			Avis.logger().statistics(
					"number of frames performed=<"
							+ numberOfTimesActionPerformed + ">");
			Avis.logger().statistics(
					"mean time spent per frame=<" + (double) amountTimeSpent
							/ (double) numberOfTimesActionPerformed / 1000000
							+ "[msec]>");
			Avis.logger().statistics(
					"mean time spent for action=<" + (double) actionRunningTime
							/ (double) numberOfTimesActionPerformed / 1000000
							+ "[msec]>");
			Avis.logger().statistics(
					"mean time spent for layout=<" + (double) layoutRunningTime
							/ (double) numberOfTimesActionPerformed / 1000000
							+ "[msec]>");
			Avis.logger().statistics(
					"mean time spent for video rendering=<"
							+ (double) videoRenderingRunningTime
							/ (double) numberOfTimesActionPerformed / 1000000
							+ "[msec]>");
			Avis.logger().statistics(
					"mean time spent for sound rendering=<"
							+ (double) soundRenderingRunningTime
							/ (double) numberOfTimesActionPerformed / 1000000
							+ "[msec]>");
		}
	}

	private static final int MAX_VOICES = 2;

	/**
	 * <code>serialVersionUID</code>:シリアルバージョン
	 */
	private static final long serialVersionUID = 1L;

	protected static Configuration.DebugMode chooseDebugMode(String[] args)
			throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-debug=GRID".equals(args[i])) {
					return Configuration.DebugMode.GRID;
				}
				if ("-debug=SCENARIOSKIP".equals(args[i])) {
					return Configuration.DebugMode.SCENARIOSKIP;
				}
				if ("-debug=NORMAL".equals(args[i])) {
					return Configuration.DebugMode.NORMAL;
				}
			}
		}
		return Configuration.DebugMode.NORMAL;
	}

	protected static int chooseLogLevel(String[] args) throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-log=DEBUG".equals(args[i])) {
					return Avis.DEBUG;
				}
				if ("-log=INFO".equals(args[i])) {
					return Avis.INFO;
				}
				if ("-log=STATISTICS".equals(args[i])) {
					return Avis.STATISTICS;
				}
				if ("-log=ERROR".equals(args[i])) {
					return Avis.ERROR;
				}

			}
		}
		return Avis.STATISTICS;
	}

	@SuppressWarnings("unchecked")
	protected static ASession chooseSession(String[] args) throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				String key;
				if (args[i].startsWith(key = "-session=")) {
					String className = args[i].substring(key.length());
					Class clazz = Class.forName(className);
					Object ret = clazz.newInstance();
					if (ret instanceof ASession) {
						return (ASession) ret;
					}
				}
			}

		}
		throw new Exception("No session specified.");
	}

	protected static Font chooseFont(String[] args) throws Exception {
		Font ret = new Font("Serif", Font.PLAIN, 12);
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if (args[i].startsWith("-font=")) {
					String fontName = args[i].substring(6).replace("\"", "");
					ret = new Font(fontName, Font.PLAIN, 12);
					return ret;
				}
			}
		}
		return ret;
	}

	protected static Configuration.SoundMode chooseSoundMode(String[] args)
			throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-sound=SILENT".equals(args[i])) {
					return Configuration.SoundMode.SILENT;
				}
				if ("-sound=BGMONLY".equals(args[i])) {
					return Configuration.SoundMode.BGMONLY;
				}
				if ("-sound=MULTITHREADED".equals(args[i])) {
					return Configuration.SoundMode.MULTITHREADED;
				}
			}
		}
		return Configuration.SoundMode.NORMAL;
	}

	protected static Configuration.RenderingMode chooseRenderingMode(String[] args)
			throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-rendering=VOLATILE".equals(args[i])) {
					return Configuration.RenderingMode.VOLATILE;
				}
				if ("-rendering=BUFFERED".equals(args[i])) {
					return Configuration.RenderingMode.BUFFERED;
				}
			}
		}
		return Configuration.RenderingMode.BUFFERED;
	}

	protected static Configuration.VideoMode chooseVideoMode(String[] args)
			throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-video=FULL".equals(args[i])) {
					return Configuration.VideoMode.FULL;
				}
			}
		}
		return Configuration.VideoMode.NORMAL;
	}

	protected static FrameMode chooseFrameMode(String[] args) throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-frame=DROP".equals(args[i])) {
					return Configuration.FrameMode.DROP;
				}
				if ("-frame=NONDROP".equals(args[i])) {
					return Configuration.FrameMode.NONDROP;
				}
			}
		}
		return Configuration.FrameMode.NONDROP;
	}

	protected static BgQualityMode chooseBgQualityMode(String[] args)
			throws Exception {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-bgquality=LOW".equals(args[i])) {
					return Configuration.BgQualityMode.LOW;
				}
				if ("-bgquality=HIGH".equals(args[i])) {
					return Configuration.BgQualityMode.HIGH;
				}
			}
		}
		return Configuration.BgQualityMode.HIGH;
	}
	protected static JoystickMode chooseJoystickMode(String[] args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-joystick=DISABLED".equals(args[i])) {
					return Configuration.JoystickMode.DISABLED;
				}
				if ("-joystick=PS3".equals(args[i])) {
					return Configuration.JoystickMode.PS3;
				}
				if ("-joystick=JOYPAD".equals(args[i])) {
					return Configuration.JoystickMode.JOYPAD;
				}
			}
		}
		return Configuration.JoystickMode.JOYPAD;
	}
	protected static RotationMode chooseRotationMode(String[] args) {
		if (args.length > 0) {
			for (int i = 0; i < args.length; i++) {
				if ("-rotation=DISABLED".equals(args[i])) {
					return Configuration.RotationMode.DISABLED;
				}
			}
		}
		return Configuration.RotationMode.ENABLED;
	}

	@SuppressWarnings("unchecked")
	protected static ASession createSession(String[] args) throws Exception {
		if (args.length < 1) {
			throw new Exception("A class name should be given as a parameter.");
		}
		Class<ASession> clazz = (Class<ASession>) Class.forName(args[0]);

		ASession session = clazz.newInstance();
		return session;
	}

	public static void main(String[] args) throws Exception {
		Avis.logger().level(chooseLogLevel(args));
		System.setErr(System.out);
		Configuration.session = chooseSession(args);
		Configuration.debugMode = chooseDebugMode(args);
		Configuration.soundMode = chooseSoundMode(args);
		Configuration.videoMode = chooseVideoMode(args);
		Configuration.frameMode = chooseFrameMode(args);
		Configuration.defaultFont = chooseFont(args);
		Configuration.renderingMode = chooseRenderingMode(args);
		Configuration.bgQualityMode = chooseBgQualityMode(args);
		Configuration.joystickMode = chooseJoystickMode(args);
		Configuration.rotationMode = chooseRotationMode(args);
		Avis.logger().statistics(Configuration.dump());
		Avis.logger().statistics(System.getProperties().toString());
		int ret = EXIT_ABNORMAL;
		try {
			Configuration.session.perform();
			ret = EXIT_NORMAL;
		} catch (Throwable t) {
			t.printStackTrace(System.out);
		} finally {
			System.exit(ret);
		}
	}


	private ABGMManager bgmManager;
	protected MMachineBuilder builder = null;

	protected MotionController controller;

	private boolean exit;

	private int exitCode;

	protected int interval;

	protected AScenario scenario = null;

	protected AScreen screen;

	private ASoundManager soundManager;

	protected ASpriteSpecManager spriteSpecManager;
	private boolean renderingEnabled;
	private boolean statisticsEnabled;
	private Image logoImage;

	public ASession() throws IOException {
		super();
	}

	protected void afterInit(AScenario scenario) {
	}

	protected void afterRun(AScenario scenario) {
	}

	protected void afterTerminate(AScenario scenario) {
	}

	protected void aftetrLayout() {
	}

	abstract protected String backgroundImageResourceName();

	protected void beforeInit(AScenario scenario) {
	}

	protected void beforeLayout() {
	}

	protected void beforeRun(AScenario scenario) {
	}

	protected void beforeTerminate(AScenario scenario) {
	}

	abstract protected String[] bgmResources();

	protected AScenario createDefaultScenario() throws AException {
		// returns dummy scenario object
		return new AScenario() {
			@Override
			public AScenarioEvent action() {
				return AScenarioEvent.NULL_EVENT;
			}

			@Override
			public String backgroundImageResource() {
				return null;
			}

			@Override
			public void init(ASession session) throws AException {
			}

			public void registered(Drivant drivant) {
			}

			@SuppressWarnings("unchecked")
			public void emit(Drivant source, MMachineSpec emittable) {
			}

			@Override
			public void reset() {
			}

			public void handleCollision(Drivant d1, Drivant d2, double distance) {
			}

			public boolean collides(Drivant d1, Drivant d2, double distance) {
				return false;
			}

			public void handleInteraction(Drivant d1, Drivant d2,
					double distance) {
			}
		};
	}

	public int exec(AScenario scenario) throws Exception {
		int ret = EXIT_ABNORMAL;
		ASession session = this;
		session.beforeInit(scenario);
		try {
			session.init(scenario);
		} finally {
			session.afterInit(scenario);
		}
		try {
			session.beforeRun(scenario);
			try {
				ret = session.run(scenario);
			} finally {
				session.afterRun(scenario);
			}
		} finally {
			session.beforeTerminate(scenario);
			try {
				session.terminate(scenario);
			} finally {
				session.afterTerminate(scenario);
			}
		}
		return ret;
	}

	public void exit(int exitCode) {
		this.exitCode(exitCode);
		this.exit = true;
	}

	public ABGMManager getBgmManager() {
		return bgmManager;
	}

	public ASoundManager getSoundManager() {
		return soundManager;
	}

	protected MotionController createMotionController() {
		MotionController ret = new MotionController();
		ret.init();
		return ret;
	}

	private void initBgm() throws AException {
		this.bgmManager = new ADefaultBGMManager();
		if (Configuration.soundMode == Configuration.SoundMode.SILENT) {
			this.bgmManager = ABGMManager.NULL_BGM_MANAGER;
			return;
		}

		String[] bgms = bgmResources();
		for (int i = 0; i < bgms.length; i++) {
			getBgmManager().load(bgms[i]);
		}
	}

	private void initSound() throws AException {
		String[] soundEffects = soundEffectResources();
		this.soundManager = null;
		if (Configuration.soundMode == Configuration.SoundMode.SILENT
				|| Configuration.soundMode == Configuration.SoundMode.BGMONLY) {
			this.soundManager = ASoundManager.NULL_SOUND_MANAGER;
			return;
		}
		ASoundManager tmpSoundManager = null;
		try {
			tmpSoundManager = new ADefaultSoundManager();
			for (int i = 0; i < soundEffects.length; i++) {
				tmpSoundManager.load(soundEffects[i]);
			}
			tmpSoundManager.prepare(MAX_VOICES);
		} catch (AException e) {
			e.printStackTrace();
		} finally {
			if (tmpSoundManager == null) {
				System.err
						.println("FAILED TO INITIALIZE SoundManager, using dummy SoundManager instead.");
				this.soundManager = ASoundManager.NULL_SOUND_MANAGER;
			} else {
				if (Configuration.soundMode == Configuration.SoundMode.MULTITHREADED) {
					this.soundManager = new AMultiThreadedSoundManager(
							tmpSoundManager);
				} else {
					this.soundManager = tmpSoundManager;
				}
			}
		}
	}

	public int interval() {
		return interval / 1000000;
	}

	protected void layout() {
		Iterator<MMachine> iMMachines = controller.mmachines().iterator();
		while (iMMachines.hasNext()) {
			MMachine mmachine = iMMachines.next();
			Drivant drivant = mmachine.drivant();
			ASprite sprite = mmachine.sprite();
			layout(sprite, drivant);
		}
	}

	/**
	 * モデル座標系から、表示座標系への変換を行うメソッドのエントリポイント。
	 */
	public abstract void layout(ASprite sprite, Drivant drivant);

	public int run(AScenario scenario) throws LineUnavailableException,
			IOException, AException {
		exit = false;
		enableRendering();
		this.controller.dump();
		Statistics overall = new Statistics();
		Statistics normal = new Statistics();
		Statistics framedrops = new Statistics();
		Statistics current = new Statistics();

		long frameDebt = 0;
		enableStatistics();
		while (true) {
			if (exit)
				break;
			stick().poll();
			if (screen.isClosed() || stick().trigger(Trigger.SELECT)) {
				exitCode(EXIT_QUIT);
				break;
			}
			if (Configuration.debugMode == Configuration.DebugMode.SCENARIOSKIP && stick().trigger(Trigger.HELP)) {
				exitCode(EXIT_NEXT);
				break;
			}
			long before = System.nanoTime();
			// //
			// 1. Action
			beforeAction(scenario);
			action(scenario);
			afterAction(scenario);
			long p0;
			current.actionRunningTime = (p0 = System.nanoTime()) - before;

			if (this.renderingEnabled && frameDebt <= 0) {
				beforeLayout();
				layout();
				aftetrLayout();
				long p1;
				current.layoutRunningTime = (p1 = System.nanoTime()) - p0;
				// //
				// 2. Render
				screen.render();
				long p2;
				current.videoRenderingRunningTime = (p2 = System.nanoTime())
						- p1;
				// process sound effect.
				soundManager.render();
				current.soundRenderingRunningTime = System.nanoTime() - p2;
			}
			// //
			// 3. Gc:既に無効状態になっているDrivantを各種の一覧から削除(gc)
			controller.scavengeMMachines();

			long after = System.nanoTime();
			long timeSpent = after - before + frameDebt;
			try {
				long durationToWait = interval - timeSpent;
				if (renderingEnabled && isStatisticsEnabled()) {
					current.numberOfTimesActionPerformed = 1;
					current.amountTimeSpent = timeSpent;
					overall.add(current);
					if (durationToWait < 0) {
						framedrops.add(current);
					} else {
						normal.add(current);
					}
				}
				if (frameDebt <= 0) {
					Thread.sleep(Math.max(durationToWait / 1000000, 0));
				}
				frameDebt = (Configuration.frameMode == Configuration.FrameMode.NONDROP) ? 0
						: Math.max(0, -(durationToWait));
			} catch (InterruptedException e) {
				assert false : "割り込み例外発生";
			}
		}
		disableStatistics();
		Avis.logger().statistics("Overall");
		overall.dump();
		Avis.logger().statistics("Normal");
		normal.dump();
		Avis.logger().statistics("Framedrops");
		framedrops.dump();
		return exitCode();
	}

	public void enableStatistics() {
		this.statisticsEnabled = true;
	}

	public void disableStatistics() {
		this.statisticsEnabled = false;
	}

	public boolean isStatisticsEnabled() {
		return statisticsEnabled;
	}

	protected void afterAction(AScenario scenario) throws AException {
		scenario.afterAction();
	}

	protected void action(AScenario scenario) throws AException {
		scenario.action();
	}

	protected void beforeAction(AScenario scenario) throws AException {
		scenario.beforeAction();
	}

	public AScenario scenario() {
		return scenario;
	}

	public AScreen screen() {
		return screen;
	}

	public void soundEffect(String resourceName) {
		synchronized (soundManager) {
			try {
				soundManager.play(resourceName);
			} catch (AException e) {
				e.printStackTrace();
			}
		}
	}

	abstract protected String[] soundEffectResources();

	public AInputDevice stick() {
		return screen.stick();
	}

	public void terminate(AScenario scenario) {
		screen.clear();
		soundManager.terminate();
	}

	public void exitCode(int exitCode) {
		this.exitCode = exitCode;
	}

	public int exitCode() {
		return exitCode;
	}

	public AVideoEngine videoEngine() {
		return AVideoEngine.instance();
	}
	public void perform() throws Exception {
		AVideoEngine videoEngine = AVideoEngine.instance();
		videoEngine.resourceObserver(this);
		try {
			long timeInitializationStarted = System.currentTimeMillis();
			videoEngine.size(1024, 768);
			this.screen = videoEngine.getScreen();
			splash(screen);
			this.spriteSpecManager = videoEngine.getSpriteSpecManager();
			this.controller = createMotionController();
			this.builder = MMachineBuilderImpl.getInstance(spriteSpecManager,
					screen.backgroundPlane());

			AResourceLoader loader = createResourceLoader();
			loader.loadResources();
			AScenario first = createScenario();
			AScenario cur = first;
			boolean restart = false;

			Avis.logger().statistics(
					"Time spent for initialization=<"
							+ Avis.format(System.currentTimeMillis()
									- timeInitializationStarted) + "[msec]>");
			outer: do {
				int scenarioCode;
				if (restart) {
					cur = first;
					restart = false;
				}
				while ((scenarioCode = performScenario(cur)) == ASession.EXIT_CONTINUE) {
					cur.reset();
				}
				if (scenarioCode == ASession.EXIT_RESTART) {
					restart = true;
					cur = first;
					cur.reset();
					continue outer;
				}
				if (scenarioCode == ASession.EXIT_QUIT
						|| scenarioCode == ASession.EXIT_ABNORMAL) {
					break outer;
				}
			} while ((cur = (AScenario) cur.next()) != null);
		} finally {
			videoEngine.terminate();
		}
	}

	public abstract AResourceLoader createResourceLoader();

	public void startLoading(String resourceName) {
		splash(screen);
		Graphics2D gg = (Graphics2D) screen.getGraphics();
		gg.setColor(Color.white);
		drawCenteredString(gg, screen, "Loading:<" + resourceName + ">", (int) (1.6 * screen.getHeight() /3), 16);
	}
	
	public void endLoading(String resourceName) {
	}
	
	private void drawCenteredString(Graphics2D gg, AScreen screen, String message, int y, float size) {
		int width = screen.getWidth();
		Font f = Configuration.defaultFont.deriveFont(size);
		GlyphVector gv = f.createGlyphVector(gg.getFontRenderContext(), message);
		double gvWidth = gv.getGlyphPosition(gv.getNumGlyphs()).getX() - gv.getGlyphPosition(0).getX();
		gg.setFont(f);
		gg.drawString(message, (int) ((width - gvWidth) / 2), y);
	}
	
	
	private Image createLogo(AScreen screen) {
		int width = screen.getWidth() / 2;
		int height = screen.getHeight() / 4;
		Image ret = screen.createImage(width, height);
		Graphics2D gg = (Graphics2D) ret.getGraphics();
		{
			gg.setColor(Color.black);
			gg.fillRect(0, 0, width, height);
			gg.setColor(Color.white);
			FontRenderContext context = gg.getFontRenderContext();
			Font f = Configuration.defaultFont.deriveFont((float) 100.0).deriveFont(Font.BOLD);
			char[] name = "Avis".toCharArray();
			GlyphVector gv = f.createGlyphVector(context, name);
	
			int gvW = -1, gvH = -1;
			for (int i = 0; i < name.length; i++) {
				int hh = gv.getGlyphOutline(i).getBounds().height;
				if (hh > gvH) {
					gvH = hh;
				}
			}
			gvW = (int)( gv.getGlyphPosition(gv.getNumGlyphs()).getX() - gv.getGlyphPosition(0).getX());
			gg.drawGlyphVector(gv, (int)(width / 2 - (gvW / 2) * 1.4), (float) (height - gvH/1.2 + 10));
		}
		{
			char[] pp = "++".toCharArray();
			Font f = Configuration.defaultFont.deriveFont((float) 60.0).deriveFont(Font.BOLD);
			FontRenderContext context = gg.getFontRenderContext();
			GlyphVector gv = f.createGlyphVector(context, pp);
			int gvH = gv.getGlyphOutline(0).getBounds().height;
			gg.drawGlyphVector(gv, (float) (width / 2 + width / 6.2), (int) gvH * 2  + 10);
		}
		gg.dispose();
		return ret;
	}
	public void splash(AScreen screen) {
		Image backImage = screen.createImage(screen.getWidth(), screen.getHeight());
		Graphics2D gg = (Graphics2D) backImage.getGraphics();
		gg.setColor(Color.blue);
		gg.fillRect(0, 0, screen.getWidth(), screen.getHeight());
		if (this.logoImage == null) {
			this.logoImage = createLogo(screen);
			int x = screen.getWidth() / 4;
			int y = (int) (screen.getHeight() / 2 - logoImage.getHeight(null) * 1.2);
			for (int i = 0; i < 60; i ++) {
				gg.fillRect(0, 0, screen.getWidth(), screen.getHeight());
				double r = (double)(60-i) / (double)60;
				gg.drawImage(
						logoImage, 
						x, 
						(int)(y + r * logoImage.getHeight(null)), 
						x + logoImage.getWidth(null), 
						(y + logoImage.getHeight(null)), 
						0, 
						0, 
						logoImage.getWidth(null),     
						(int)(logoImage.getHeight(null) * (1 - r)), 
						null);
				Graphics g = screen.getGraphics();
				int scrW= screen.getWidth();
				int scrH = screen.getHeight();
				g.drawImage(backImage, 0, 0, scrW, scrH, 0, 0, scrW , scrH,  null);
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
			}
		}
		int x = screen.getWidth() / 4;
		int y = (int) (screen.getHeight() / 2 - logoImage.getHeight(null) * 1.2);
		gg.drawImage(
				logoImage, 
				x, y, x + logoImage.getWidth(null), y + logoImage.getHeight(null), 
				0, 0, logoImage.getWidth(null),     logoImage.getHeight(null), null);
		gg.setColor(Color.white);
		drawCenteredString(gg, screen, "Available Processors:<" + Runtime.getRuntime().availableProcessors() + "[unit]>", (int) (6.4 * screen.getHeight() /10), 16);
		drawCenteredString(gg, screen, "Free memory:<" + Avis.format(Runtime.getRuntime().freeMemory() / (1024 * 1024)) + "[MB]>", (int) (6.8 * screen.getHeight() /10), 16);
		drawCenteredString(gg, screen, "Total memory:<" + Avis.format(Runtime.getRuntime().totalMemory() / (1024 * 1024)) + "[MB]>", (int) (7.0 * screen.getHeight() /10), 16);
		drawCenteredString(gg, screen, "Max memory:<" + Avis.format(Runtime.getRuntime().maxMemory() / (1024 * 1024)) + "[MB]>", (int) (7.2 * screen.getHeight() /10), 16);
		if (Configuration.joystickMode != Configuration.JoystickMode.DISABLED) {
			String detected = "";
			int num = Joystick.getNumDevices();
			boolean firstTime = true;
			for (int i = 0; i < num; i++) {
				if (Joystick.isPluggedIn(i)) {
					if (!firstTime) {
						detected += ",";
						firstTime = false;
					}
					detected += i; 
				}
			}
			if ("".equals(detected)) {
				detected = "NONE";
			}
			drawCenteredString(gg, screen, "Joystick support ENABLED(" + Configuration.joystickMode + "):detected devices=<" + detected + ">" , (int) (7.6 * screen.getHeight() /10), 16);
		} else {
			drawCenteredString(gg, screen, "Joystick support DISABLED", (int) (7.6 * screen.getHeight() /10), 16);
		}

		drawCenteredString(gg, screen, "Avis++ Framework:SYSTEM " + Configuration.version, (int) (8.5 * screen.getHeight() / 10), 24);
		drawCenteredString(gg, screen, "Copyright (C) 2009 INUWI Software Lab.", 9 * screen.getHeight() / 10, 20);
		gg.dispose();
		
		Graphics g = screen.getGraphics();
		int scrW= screen.getWidth();
		int scrH = screen.getHeight();
		g.drawImage(backImage, 0, 0, scrW, scrH, 0, 0, scrW , scrH,  null);
		backImage.flush();
		backImage = null;
	}

	protected abstract AScenario createScenario();

	public int performScenario(AScenario scenario) throws Exception {
		int ret = EXIT_ABNORMAL;
		this.beforeExec(scenario);
		try {
			ret = this.exec(scenario);
		} finally {
			this.afterExec(scenario);
		}
		return ret;
	}

	protected void init(AScenario scenario) throws AException {
		Avis.logger().info("Session#init");
		interval = 25 * 1000000;

		initBgm();
		initSound();

		this.controller.init();
		this.scenario = scenario;
		this.controller.interactionHandler(this.scenario);
		this.scenario.init(this);
		Avis.logger().statistics("Scenario:" + this.scenario());
	}

	public void afterExec(AScenario scenario) {
	}

	public void beforeExec(AScenario scenario) {
	}

	protected void enableRendering() {
		this.renderingEnabled = true;
	}

	protected void disableRendering() {
		this.renderingEnabled = false;
	}

	public MotionController controller() {
		return controller;
	}
}