package oreactor.core;

import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorExitException;

public class Reactor {
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
	
	protected long interval;

	protected ArgParser argParser;

	private Statistics statistcs;
	
	public Reactor() {
		this.statistcs = new Statistics();
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
	protected Context initialize(Settings settings) {
		return new Context(settings);
	}
	
	@ExtensionPoint
	protected void run(Context c) throws OpenReactorException {
		////
		// This method does nothing by default.
	}
	
	@ExtensionPoint
	protected void terminate(Context c) {
		System.err.println("----");
		System.err.println(this.statistcs);
		System.err.println("----");
	}

	public void argParser(ArgParser argParser) {
		this.argParser = argParser;
	}

	public final void execute(Settings settings) throws OpenReactorException {
		System.err.println("START:perform");
		System.err.println("START:initialization");
		Context c = initialize(settings);
		System.err.println("END:initialization");
		State state = State.Running;
		State nextState = null;
		try {
			long frameDebt = 0;
			while (true) {
				
				long before = System.nanoTime();
				////
				// 1. Perform action
				try {
					state.run(this, c);
				} catch (OpenReactorExitException e) {
					System.err.println("User gear:<" + this.getClass().getSimpleName() + "> has been exitted.");
					String msg;
					if ((msg = e.getMessage()) != null) {
						System.err.println("Message:<" + msg + ">");
					} else {
						System.err.println("Message is not given.");
					}
					nextState = State.Exitted;
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
				long timeSpent = after - before + frameDebt;
				long durationToWait = this.interval - timeSpent;
				if (frameDebt <= 0) {
					Thread.sleep(Math.max(durationToWait / 1000000, 0));
				}
				frameDebt = (settings.frameMode() == Settings.FrameMode.NONDROP) ? 0
						: Math.max(0, -(durationToWait));
				if (frameDebt > 0) {
					this.statistcs.frameProcessedNotInTime(timeSpent);
				} else {
					this.statistcs.frameProcessedInTime(timeSpent);
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
			terminate(c);
		}
	}

	private void prepareEngines(Context c) throws OpenReactorException {
		c.getIOEngine().prepare();
		c.getJoystickEngine().prepare();
		c.getKeyboardEngine().prepare();
		c.getMusicEngine().prepare();
		c.getNetworkEngine().prepare();
		c.getSoundEngine().prepare();
		c.getVideoEngine().prepare();
	}

	private void runEngines(Context c) throws OpenReactorException {
		c.getIOEngine().run();
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
		c.getIOEngine().finish();
	}

	protected void exit(String msg) throws OpenReactorExitException {
		throw new OpenReactorExitException(msg);
	}
	
	protected void exit() throws OpenReactorExitException {
		throw new OpenReactorExitException(null);
	}
}

