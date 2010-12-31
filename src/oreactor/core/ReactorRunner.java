package oreactor.core;

import java.awt.Font;

import oreactor.exceptions.ArgumentException;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class ReactorRunner {
	private ArgParser argParser;
	private Reactor reactor;

	ReactorRunner(String[] args) throws OpenReactorException {
		argParser = ArgParser.createArgParser(args);
	}
	
	Settings.JoystickMode chooseJoyStickMode() throws ArgumentException {
		Settings.JoystickMode ret = (Settings.JoystickMode) argParser.parseEnum("joystick", Settings.JoystickMode.class, Settings.JoystickMode.DISABLED); ;
		return ret;
	}
	
	Settings.FrameMode chooseFrameMode() throws ArgumentException {
		Settings.FrameMode ret = (Settings.FrameMode) argParser.parseEnum("joystick", Settings.FrameMode.class, Settings.FrameMode.NONDROP); ;
		return ret;
	}
	
	Settings.ScreenSize chooseScreenSize() throws ArgumentException {
		Settings.ScreenSize ret = (Settings.ScreenSize) argParser.parseEnum("screensize", Settings.ScreenSize.class, Settings.ScreenSize.XGA); ;
		return ret;
	}
	
	Settings.VideoMode chooseVideoMode() throws ArgumentException {
		Settings.VideoMode ret = (Settings.VideoMode) argParser.parseEnum("video", Settings.VideoMode.class, Settings.VideoMode.NORMAL);
		return ret;
	}
	
	Settings.RenderingMode chooseRenderingMode() throws ArgumentException {
		Settings.RenderingMode ret = (Settings.RenderingMode) argParser.parseEnum("rendering", Settings.RenderingMode.class, Settings.RenderingMode.BUFFERED);
		return ret;
	}
	
	Settings.SoundMode chooseSoundMode() throws ArgumentException {
		Settings.SoundMode ret = (Settings.SoundMode) argParser.parseEnum("sound", Settings.SoundMode.class, Settings.SoundMode.ENABLED);
		return ret;
	}

	Settings.RunningMode chooseRunningMode() throws ArgumentException {
		Settings.RunningMode ret = (Settings.RunningMode) argParser.parseEnum("running", Settings.RunningMode.class, Settings.RunningMode.NORMAL);
		return ret;
	}
	
	Settings.LoggingMode chooseLoggingMode() throws ArgumentException {
		Settings.LoggingMode ret = (Settings.LoggingMode) argParser.parseEnum("logging", Settings.LoggingMode.class, Settings.LoggingMode.STATISTICS);
		return ret;
	}
	
	Font chooseFont() throws ArgumentException {
		String fontName = argParser.pickUpValue("font", "Serif");
		return new Font(fontName, Font.PLAIN, 12);
	}
	
	Reactor chooseReactor() throws OpenReactorException {
		Reactor ret = null;
		String reactorClassName = argParser.pickUpValue("reactor", null);
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
	
	Settings parseArgs() throws OpenReactorException {
		Settings ret = null;
		ret = new Settings();
		ret.font(chooseFont());
		ret.frameMode(chooseFrameMode());
		ret.joystickMode(chooseJoyStickMode());
		ret.loggingMode(chooseLoggingMode());
		ret.renderingMode(chooseRenderingMode());
		ret.runningMode(chooseRunningMode());
		ret.screenSize(chooseScreenSize());
		ret.soundMode(chooseSoundMode());
		ret.videoMode(chooseVideoMode());
		
		this.reactor = chooseReactor();

		return ret;
	}
	
	void runReactor(Settings settings) throws OpenReactorException {
		this.reactor.perform(settings);
	}
	
	public static void main(String[] args) throws OpenReactorException {
		ReactorRunner reactorRunner = new ReactorRunner(args);
		Settings settings = reactorRunner.parseArgs();
		reactorRunner.runReactor(settings);
	}

}
