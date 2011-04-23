package oreactor.core;

import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorExitException;

public class Reactor {
	enum Action {
		Running {
			@Override
			public void run(Reactor reactor, Context c) throws OpenReactorException {
				reactor.action(c);
			}
		},
		Exitted {
			@Override
			public void run(Reactor reactor, Context c) {
			}
		};
		protected abstract void run(Reactor reactor, Context c) throws OpenReactorException;
	}
	
	protected long interval;

	protected ArgParser argParser;
	
	public Reactor() {
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
		ret.renderingMode(argParser.chooseRenderingMode());
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
	protected void action(Context c) throws OpenReactorException {
		////
		// This method does nothing by default.
	}
	
	@ExtensionPoint
	protected void terminate(Context c) {
	}

	@ExtensionPoint
	protected void postMortem(Settings settings) {
		// does nothing by default.
	}

	public void argParser(ArgParser argParser) {
		this.argParser = argParser;
	}

	public final void perform(Settings settings) throws OpenReactorException {
		System.out.println("START:perform");
		System.out.println("START:initialization");
		Context c = initialize(settings);
		System.out.println("END:initialization");
		Action action = Action.Running;
		try {
			long frameDebt = 0;
			while (true) {
				long before = System.nanoTime();
				////
				// 1. Action
				this.prepareEngines(c);
				try {
					action.run(this, c);
				} catch (OpenReactorExitException e) {
					System.err.println("User gear:<" + this.getClass().getSimpleName() + "> has been exitted.");
					String msg;
					if ((msg = e.getMessage()) != null) {
						System.err.println("Message:<" + msg + ">");
					} else {
						System.err.println("Message is not given.");
					}
					action = Action.Exitted;
				} finally {
					this.finishEngines(c);
				}
				////
				// 2. Render
				this.runEngines(c);

				////
				// 3. calibrate the interval
				long after = System.nanoTime();
				long timeSpent = after - before + frameDebt;
				long durationToWait = this.interval - timeSpent;
				if (frameDebt <= 0) {
					Thread.sleep(Math.max(durationToWait / 1000000, 0));
				}
				frameDebt = (settings.frameMode() == Settings.FrameMode.NONDROP) ? 0
						: Math.max(0, -(durationToWait));
			}
		} catch (InterruptedException e) {
			throw new OpenReactorException(e.getMessage(), e);
		} finally {
			terminate(c);
		}
	}

	private void prepareEngines(Context c) {
		/*
		c.getIOEngine().prepare();
		c.getJoystickEngine().prepare();
		c.getKeyboardEngine().prepare();
		c.getMusicEngine().prepare();
		c.getNetworkEngine().prepare();
		c.getSoundEngine().prepare();
		c.getVideoEngine().prepare();
		*/
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


	private void finishEngines(Context c) {
		/*
		c.getVideoEngine().finish();		
		c.getSoundEngine().finish();
		c.getNetworkEngine().finish();
		c.getMusicEngine().finish();
		c.getKeyboardEngine().finish();
		c.getJoystickEngine().finish();
		c.getIOEngine().finish();
		*/
	}

	protected void exit(String msg) throws OpenReactorExitException {
		throw new OpenReactorExitException(msg);
	}
	
	protected void exit() throws OpenReactorExitException {
		throw new OpenReactorExitException(null);
	}
}

