package oreactor.core;

import javax.swing.JApplet;

import oreactor.core.Settings.BgmMode;
import oreactor.core.Settings.ExecutionMode;
import oreactor.core.Settings.LoggingMode;
import oreactor.core.Settings.SoundMode;
import oreactor.core.Settings.VideoMode;
import oreactor.exceptions.ArgumentException;
import oreactor.exceptions.OpenReactorException;

public class ReactorApplet extends JApplet {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -1612530114870579L;
	private static Logger logger = Logger.getLogger();
	protected Reactor reactor = null;
	private Settings settings;
	private Thread thread;

	@Override
	public void init() {
		String reactorClassName = getParameter("reactor");
		try {
			this.reactor = Reactor.loadReactor(reactorClassName);
			this.settings = loadSettings();
		} catch (ArgumentException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		} catch (OpenReactorException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	public void start() {
		thread = new Thread() {
			@Override
			public void run() {
				try {
					reactor.execute(settings);
				} catch (ArgumentException e) {
					logger.error(e.getMessage());
					logger.debug(e.getMessage(), e);
				} catch (OpenReactorException e) {
					String msg = "Quitting applet...";
					logger.info(msg);
					logger.debug(msg, e);
				}
			}
		};
		thread.start();
	}

	public void stop() {
		if (reactor != null) {
			this.reactor.scram();
		}
	}

	public void destroy() {
		this.thread = null;
		this.reactor = null;
		this.settings = null;
	}

	protected
	Settings loadSettings() throws OpenReactorException {
		Settings ret = null;
		ret = new Settings();
		ret.loggingMode(LoggingMode.valueOf(getParameter("logging", "ERROR")));
		ret.executionMode(ExecutionMode.valueOf(getParameter("exec", "NORMAL")));
		ret.soundMode(SoundMode.valueOf(getParameter("sound", "ENABLED_FALLBACK")));
		ret.bgmMode(BgmMode.valueOf(getParameter("bgm", "ENABLED_FALLBACK")));
		ret.videoMode(VideoMode.valueOf(getParameter("video", "FULL_FALLBACK")));
		return ret;
	}

	protected
	String getParameter(String key, String defaultValue) {
		String ret = super.getParameter(key);
		return ret != null ? ret : defaultValue;
	}
}
