package oreactor.core;

import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorExitException;
import oreactor.exceptions.OpenReactorQuitException;

public abstract class Reactor {
	enum State {
		Running {
			@Override
			public void run(Reactor reactor, Context c) throws OpenReactorException {
				reactor.run(c);
			}
		},
		Exitted {
			@Override
			public void run(Reactor reactor, Context c) throws OpenReactorException {
			}
		};
		protected abstract void run(Reactor reactor, Context c) throws OpenReactorException;
	}
	/**
	 * (1/60Hz) to wait.
	 */
	protected long interval = (1000 * 1000* 1000) / fps();

	protected ArgParser argParser;

	private Statistics statistcs;

	private Logger logger;
	
	public Reactor() {
		this.statistcs = new Statistics();
		this.logger = Logger.getLogger();
	}

	@ExtensionPoint
	protected int fps() {
		return 50;
	}

	@ExtensionPoint
	protected
	Settings loadSettings() throws OpenReactorException {
		Settings ret = null;
		ret = new Settings();
		ret.font(argParser.chooseFont());
		ret.frameMode(argParser.chooseFrameMode());
		ret.joystickMode(argParser.chooseJoyStickMode());
		ret.loggingMode(argParser.chooseLoggingMode());
		ret.runningMode(argParser.chooseRunningMode());
		ret.screenSize(argParser.chooseScreenSize());
		ret.soundMode(argParser.chooseSoundMode());
		ret.videoMode(argParser.chooseVideoMode());
		return ret;
	}

	@ExtensionPoint
	protected Context initialize(Settings settings) throws OpenReactorException {
		Context c = new Context(this, settings);
		c.getJoystickEngine().initialize(c);
		c.getKeyboardEngine().initialize(c);
		c.getMusicEngine().initialize(c);
		c.getNetworkEngine().initialize(c);
		c.getSoundEngine().initialize(c);
		c.getVideoEngine().initialize(c);

		return c;
	}
	
	@ExtensionPoint
	protected void run(Context c) throws OpenReactorException {
		////
		// This method does nothing by default.
	}
	
	private void prepareEngines(Context c) throws OpenReactorException {
		c.getJoystickEngine().prepare();
		c.getKeyboardEngine().prepare();
		c.getMusicEngine().prepare();
		c.getNetworkEngine().prepare();
		c.getSoundEngine().prepare();
		c.getVideoEngine().prepare();
	}

	private void runEngines(Context c) throws OpenReactorException {
		c.getJoystickEngine().run();
		c.getKeyboardEngine().run();
		c.getMusicEngine().run();
		c.getNetworkEngine().run();
		c.getSoundEngine().run();
		c.getVideoEngine().run();
	}


	private void finishEngines(Context c) throws OpenReactorException {
		c.getVideoEngine().finish();		
		c.getSoundEngine().finish();
		c.getNetworkEngine().finish();
		c.getMusicEngine().finish();
		c.getKeyboardEngine().finish();
		c.getJoystickEngine().finish();
	}

	@ExtensionPoint
	protected void terminate(Context c) throws OpenReactorException {
		logger.debug("----");
		logger.debug(this.statistcs.toString());
		logger.debug("----");
		c.getVideoEngine().terminate(c);		
		c.getSoundEngine().terminate(c);
		c.getNetworkEngine().terminate(c);
		c.getMusicEngine().terminate(c);
		c.getKeyboardEngine().terminate(c);
		c.getJoystickEngine().terminate(c);
	}

	public void argParser(ArgParser argParser) {
		this.argParser = argParser;
	}

	public final void execute(Settings settings) throws OpenReactorException {
		logger.info("START:perform");
		logger.info("START:initialization");
		Context c = initialize(settings);
		logger.info("END:initialization");
		State state = State.Running;
		State nextState = null;
		try {
			while (true) {
				long timeSpentForAction = 0;
				long before = System.nanoTime();
				////
				// 1. Perform action
				try {
					state.run(this, c);
				} catch (OpenReactorExitException e) {
					logger.info("User gear:<" + this.getClass().getSimpleName() + "> has been exitted.");
					String msg;
					if ((msg = e.getMessage()) != null) {
						logger.info("Message:<" + msg + ">");
					} else {
						logger.info("Message is not given.");
					}
					nextState = State.Exitted;
				} finally {
					timeSpentForAction = System.nanoTime() - before;
				}
			
				
				////
				// 2. Run engines
				this.prepareEngines(c);
				try {
					this.runEngines(c);
				} finally {
					this.finishEngines(c);
				}

				////
				// 3. Calibrate the interval
				long after = System.nanoTime();
				long timeSpent = after - before;
				long durationToWait = this.interval - timeSpent;
				if (durationToWait > 0) {
					Thread.sleep(durationToWait / 1000000, (int)(durationToWait % 1000000));
				}
				if (timeSpent > this.interval) {
					this.statistcs.frameProcessedNotInTime(timeSpent, timeSpentForAction);
				} else {
					this.statistcs.frameProcessedInTime(timeSpent, timeSpentForAction);
				}
				////
				// 4. Determine the next action to be performed.
				if (nextState != null) {
					state = nextState;
				}
			}
		} catch (InterruptedException e) {
			throw new OpenReactorException(e.getMessage(), e);
		} finally {
			logger.info("START:termination");
			terminate(c);
			logger.info("END:termination");
			logger.info("END:perform");
		}
	}


	protected void exit(String msg) throws OpenReactorExitException {
		throw new OpenReactorExitException(msg);
	}
	
	protected void exit() throws OpenReactorExitException {
		throw new OpenReactorExitException(null);
	}
	
	protected void quit(String msg) throws OpenReactorException {
		throw new OpenReactorQuitException(msg);
	}
	
	protected void quit() throws OpenReactorException {
		throw new OpenReactorQuitException(null);
	}

	@ExtensionPoint
	public abstract int patternWidth();
	
	@ExtensionPoint
	public abstract int patternHeight();

	protected Logger logger() {
		return this.logger;
	}
}

