package oreactor.core;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import oreactor.exceptions.ArgumentException;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class ArgParser {
	public static enum TestEnum {
		Hello, World;
	}
	
	private List<String> rest;

	private List<Tuple> tuples;

	static class Tuple {
		String key;
		String value;
		public String toString() {
			return "<" + this.key + ">=<" + this.value + ">";
		}
	}

	ArgParser(List<Tuple> tuples, List<String> rest) {
		this.tuples = tuples;
		this.rest = rest;
	}

	public List<Tuple> getRemainingTuples() {
		return tuples;
	}
	
	public List<String> getUnsupportedArguments() {
		return rest;
	}
	
	public Enum<?> parseEnum(String keyword, Class<? extends Enum<?>> k, Enum<?> defaultValue) throws ArgumentException {
		Enum<?> ret = null;
		if (ret == null) {
			ret = defaultValue;
		}
		return ret;
	}

	@SuppressWarnings({ "unchecked", "rawtypes"})
	private Enum<?> pickupEnumValue(String keyword, Class<? extends Enum> k) throws ArgumentException {
		Enum<?> ret = null;
		for (Tuple t : this.tuples) {
			if (t.key.equals(keyword)) {
				try {
					ret = Enum.valueOf(k, t.value);
					this.tuples.remove(t);
					break;
				} catch (IllegalArgumentException e) {
					ExceptionThrower.throwArgumentException(keyword, t.value, k);
				}
			}
		}
		return ret;
	}

	public String pickUpValue(String keyword, String defaultValue) throws ArgumentException{
		String ret = defaultValue;
		for (Tuple t : this.tuples) {
			if (t.key.equals(keyword)) {
				this.tuples.remove(t);
				return t.value;
			}
		}
		return ret;
	}

	static Tuple split(String str) {
		if (null == str || !str.startsWith("--")) {
			return null;
		}
		String s = str.substring(2);
		Tuple ret = new Tuple();
		int i = s.indexOf('=');
		if (i < 0) {
			ret.key = s;
		} else {
			ret.key = s.substring(0, i);
			ret.value = s.substring(i + 1);
		}
		return ret;
	}
	Settings.JoystickMode chooseJoyStickMode() throws ArgumentException {
		Settings.JoystickMode ret = (Settings.JoystickMode) this.parseEnum("joystick", Settings.JoystickMode.class, Settings.JoystickMode.DISABLED); ;
		return ret;
	}
	
	Settings.FrameMode chooseFrameMode() throws ArgumentException {
		Settings.FrameMode ret = (Settings.FrameMode) this.parseEnum("joystick", Settings.FrameMode.class, Settings.FrameMode.NONDROP); ;
		return ret;
	}
	
	Settings.ScreenSize chooseScreenSize() throws ArgumentException {
		Settings.ScreenSize ret = (Settings.ScreenSize) this.parseEnum("screensize", Settings.ScreenSize.class, Settings.ScreenSize.XGA); ;
		return ret;
	}
	
	Settings.VideoMode chooseVideoMode() throws ArgumentException {
		Settings.VideoMode ret = (Settings.VideoMode) this.parseEnum("video", Settings.VideoMode.class, Settings.VideoMode.NORMAL);
		return ret;
	}
	
	Settings.RenderingMode chooseRenderingMode() throws ArgumentException {
		Settings.RenderingMode ret = (Settings.RenderingMode) this.parseEnum("rendering", Settings.RenderingMode.class, Settings.RenderingMode.BUFFERED);
		return ret;
	}
	
	Settings.SoundMode chooseSoundMode() throws ArgumentException {
		Settings.SoundMode ret = (Settings.SoundMode) this.parseEnum("sound", Settings.SoundMode.class, Settings.SoundMode.ENABLED);
		return ret;
	}

	Settings.RunningMode chooseRunningMode() throws ArgumentException {
		Settings.RunningMode ret = (Settings.RunningMode) this.parseEnum("running", Settings.RunningMode.class, Settings.RunningMode.NORMAL);
		return ret;
	}
	
	Settings.LoggingMode chooseLoggingMode() throws ArgumentException {
		Settings.LoggingMode ret = (Settings.LoggingMode) this.parseEnum("logging", Settings.LoggingMode.class, Settings.LoggingMode.STATISTICS);
		return ret;
	}
	
	Font chooseFont() throws ArgumentException {
		String fontName = this.pickUpValue("font", "Serif");
		return new Font(fontName, Font.PLAIN, 12);
	}
	
	Reactor chooseReactor() throws OpenReactorException {
		Reactor ret = null;
		String reactorClassName = this.pickUpValue("reactor", null);
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
	
	public static void main(String[] args) throws Exception {
		List<Tuple> tuples = new LinkedList<Tuple>();
		List<String> rest = new LinkedList<String>();
		for (String i : args) {
			Tuple t = split(i);
			if (t == null) {
				rest.add(i);
			} else {
				tuples.add(t);
			}
		}
		
		ArgParser parser = new ArgParser(tuples, rest);
		System.out.println(parser.pickupEnumValue("key", TestEnum.class));
		System.out.println(parser.getRemainingTuples());
	}
	
	public static ArgParser createArgParser(String[] args) throws OpenReactorException {
		ArgParser ret = null;
		List<Tuple> tuples = new LinkedList<Tuple>();
		List<String> rest = new LinkedList<String>();
		for (String i : args) {
			Tuple t = split(i);
			if (t == null) {
				rest.add(i);
			} else {
				tuples.add(t);
			}
		}
		ret = new ArgParser(tuples, rest);
		return ret;
	}
}
