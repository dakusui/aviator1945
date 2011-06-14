package oreactor.core;

import java.awt.DisplayMode;
import java.util.LinkedList;
import java.util.List;

import oreactor.annotations.ExtensionPoint;
import oreactor.exceptions.ArgumentException;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorExitException;
import oreactor.exceptions.OpenReactorQuitException;
import oreactor.video.PlaneDesc;

public abstract class Reactor {
	public static enum ScreenSize {
		_2K       { protected int[] size() {return new int[]{2048, 1152};} },
		_4K       { protected int[] size() {return new int[]{4096, 2160};} },
		_4M       { protected int[] size() {return new int[]{2304, 1728};} },
		_4xFullHD { protected int[] size() {return new int[]{3840, 2160};} },
		_8K       { protected int[] size() {return new int[]{8192, 4320};} },
		CIF       { protected int[] size() {return new int[]{ 352,  288};} },
		DCGA      { protected int[] size() {return new int[]{ 640,  400};} },
		EGA       { protected int[] size() {return new int[]{ 640,  350};} },
		FW_WXGA   { protected int[] size() {return new int[]{1366,  768};} },
		FWVGA     { protected int[] size() {return new int[]{ 854,  480};} },
		FWVGA_P   { protected int[] size() {return new int[]{ 864,  480};} },
		HVGA      { protected int[] size() {return new int[]{ 480,  320};} },
		iMac27in  { protected int[] size() {return new int[]{2560, 1440};} },
		MAC16in   { protected int[] size() {return new int[]{ 832,  624};} },
		MAC21in   { protected int[] size() {return new int[]{1152,  870};} },
		PC98HR    { protected int[] size() {return new int[]{1120,  750};} },
		QCIF      { protected int[] size() {return new int[]{ 176,  144};} },
		QuadVGA   { protected int[] size() {return new int[]{1280,  960};} },
		QUXGA     { protected int[] size() {return new int[]{3200, 2400};} },
		QUXGAWIDE { protected int[] size() {return new int[]{3840, 2400};} },
		QVGA      { protected int[] size() {return new int[]{ 320,  240};} },
		QWXGA     { protected int[] size() {return new int[]{2048, 1152};} },
		QXGA      { protected int[] size() {return new int[]{2048, 1536};} },
		sQCIF     { protected int[] size() {return new int[]{ 128,   96};} },
		SVGA      { protected int[] size() {return new int[]{ 800,  600};} },
		SXGA      { protected int[] size() {return new int[]{1280, 1024};} },
		SXGA_P    { protected int[] size() {return new int[]{1400, 1050};} },
		UWGA      { protected int[] size() {return new int[]{1024,  480};} },
		UWSVGA    { protected int[] size() {return new int[]{1280,  600};} },
		UXGA      { protected int[] size() {return new int[]{1600, 1200};} },
		VGA       { protected int[] size() {return new int[]{ 640,  480};} },
		WQVGA     { protected int[] size() {return new int[]{ 400,  240};} },
		WQXGA     { protected int[] size() {return new int[]{2560, 1600};} },
		WSVGA     { protected int[] size() {return new int[]{1024,  576};} },
		WSVGA_    { protected int[] size() {return new int[]{1024,  600};} },
		WSXGA     { protected int[] size() {return new int[]{1600, 1024};} },
		WSXGA_P   { protected int[] size() {return new int[]{1680, 1050};} },
		WUXGA     { protected int[] size() {return new int[]{1920, 1200};} },
		WVGA      { protected int[] size() {return new int[]{ 800,  480};} },
		WXGA      { protected int[] size() {return new int[]{1280,  768};} },
		WXGA_     { protected int[] size() {return new int[]{1280,  800};} },
		WXGA_P    { protected int[] size() {return new int[]{1440,  900};} },
		WXGA_PP   { protected int[] size() {return new int[]{1600,  900};} },
		XGA       { protected int[] size() {return new int[]{1024,  768};} },
		XGA_P     { protected int[] size() {return new int[]{1280,  800};} };
		public int height() {
			return (this.size())[1];
		}
		public int width() {
			return (this.size())[0];
		}
		abstract protected  int[] size();
	}
	enum State {
		Scramed {
			@Override
			public void run(Reactor reactor, Context c) throws OpenReactorException {
				ExceptionThrower.throwReactorScrammedException();
			}
			
		},
		Exitted {
			@Override
			public void run(Reactor reactor, Context c) throws OpenReactorException {
			}
		},
		Running {
			@Override
			public void run(Reactor reactor, Context c) throws OpenReactorException {
				reactor.run(c);
			}
		};
		protected abstract void run(Reactor reactor, Context c) throws OpenReactorException;
	}
	private Logger logger;	
	private List<PlaneDesc> planeDescs = new LinkedList<PlaneDesc>();
	private Settings settings;
	private Statistics statistcs;

	/**
	 * (1/60Hz) to wait.
	 */
	protected long interval = (1000 * 1000* 1000) / fps();
	private State nextState;

	public Reactor() {
		this.statistcs = new Statistics();
		this.logger = Logger.getLogger();
	}
	public void addPlaneDesc(PlaneDesc desc) {
		this.planeDescs.add(desc);
	}

	public final void execute(Settings settings) throws OpenReactorException {
		logger.info("START:perform");
		logger.info("START:initialization");
		Context c = initialize(settings);
		logger.info("END:initialization");
		State state = State.Running;
		nextState = null;
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
					nextState(State.Exitted);
				} finally {
					timeSpentForAction = System.nanoTime() - before;
				}
				
				////
				// 2. Run the other engines
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
				State nextState = nextState();
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
	
	private State nextState() {
		return nextState;
	}
	private void nextState(State nextState) {
		this.nextState = nextState;
	}
	public void scram() {
		this.nextState(State.Scramed);
	}
	public Settings settings() {
		return this.settings;
	}

	@ExtensionPoint
	public abstract int patternHeight();
	
	@ExtensionPoint
	public abstract int patternWidth();
	public List<PlaneDesc> planeInfoItems() {
		return this.planeDescs ;
	}
	
	@ExtensionPoint
	public int screenColorDepth() {
		return DisplayMode.BIT_DEPTH_MULTI;
	}
	
	@ExtensionPoint
	public int screenHeight() {
		return this.screenSize().height();
	}
	
	@ExtensionPoint
	public int screenRefreshRate() {
		return DisplayMode.REFRESH_RATE_UNKNOWN;
	}

	@ExtensionPoint
	public int screenWidth() {
		return this.screenSize().width();
	}
	@ExtensionPoint
	protected ScreenSize screenSize() {
		return ScreenSize.XGA;
	}
	private void finishEngines(Context c) throws OpenReactorException {
		c.getVideoEngine().finish();		
		c.getSoundEngine().finish();
		c.getNetworkEngine().finish();
		c.getMusicEngine().finish();
		c.getKeyboardEngine().finish();
		c.getJoystickEngine().finish();
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

	protected void exit() throws OpenReactorExitException {
		throw new OpenReactorExitException(null);
	}

	protected void exit(String msg) throws OpenReactorExitException {
		throw new OpenReactorExitException(msg);
	}


	@ExtensionPoint
	protected int fps() {
		return 50;
	}
	
	@ExtensionPoint
	protected Context initialize(Settings settings) throws OpenReactorException {
		this.settings = settings;
		Context c = new Context(this);
		c.getJoystickEngine().initialize(c);
		c.getKeyboardEngine().initialize(c);
		c.getMusicEngine().initialize(c);
		c.getNetworkEngine().initialize(c);
		c.getSoundEngine().initialize(c);
		c.getVideoEngine().initialize(c);
		return c;
	}
	
	protected Logger logger() {
		return this.logger;
	}

	protected void quit() throws OpenReactorException {
		throw new OpenReactorQuitException(null);
	}
	
	protected void quit(String msg) throws OpenReactorException {
		throw new OpenReactorQuitException(msg);
	}

	@ExtensionPoint
	protected void run(Context c) throws OpenReactorException {
		////
		// This method does nothing by default.
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

	static Reactor loadReactor(String reactorClassName) throws ArgumentException,
	OpenReactorException {
		Reactor ret = null;
		if (reactorClassName == null) {
			ExceptionThrower.throwReactorIsNotSpecified();
		}
		try {
			Class<? extends Object> reactorClass = Class.forName(reactorClassName);
			try {
				Object o = reactorClass.newInstance();
				if (o instanceof Reactor) {
					ret = (Reactor)o;
				} else {
					ExceptionThrower.throwGivenClassIsNotReactorClass(reactorClass);
				}
			} catch (InstantiationException e) {
				ExceptionThrower.throwFailedToInstanciateReactor(reactorClassName, e);
			} catch (IllegalAccessException e) {
				ExceptionThrower.throwFailedToInstanciateReactor(reactorClassName, e);
			}
		} catch (ClassNotFoundException e) {
			ExceptionThrower.throwReactorClassNotFoundException(reactorClassName, e);
		}
		return ret;
	}
}

