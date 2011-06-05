package oreactor.exceptions;

import java.text.MessageFormat;

import javax.sound.sampled.AudioFormat;

import oreactor.core.Reactor;
import oreactor.video.Viewport;

public class ExceptionThrower {

	@SuppressWarnings({ "unchecked" })
	public static void throwArgumentException(String keyword, String key,
			Class<? extends Enum> k) throws ArgumentException {
		String availableValues = "";
		boolean firstOne = true;
		for (Enum<?> i : k.getEnumConstants()) {
			if (!firstOne) {
				availableValues += ","; 
			}
			availableValues += i.name();
			firstOne = false;
		}
		throw new ArgumentException(MessageFormat.format("Illegal value <{0}> for keyword <{1}> is given. Valid values for this keyword are <{2}>.",
				 key, keyword, availableValues));
	}

	public static void throwReactorIsNotSpecified() throws ArgumentException {
		throw new ArgumentException("Reactor class is not specified. See the help.");
	}

	public static void throwReactorClassNotFoundException(String reactorClassName, ClassNotFoundException e) throws OpenReactorException {
		throw new ArgumentException("Reactor class <" + reactorClassName + "> is not found in the class path.", e); 
	}

	public static void throwFailedToInstanciateReactor(String reactorClassName,
			Throwable e) throws ArgumentException {
		throw new ArgumentException("Failed to instanciate reactor object for <" + reactorClassName + ">. Check if the class satisfies the criteria for reactor classes. See " + oreactor.core.Reactor.class.getCanonicalName() + "'s API reference.", e);
	}

	public static void throwGivenClassIsNotReactorClass(
			Class<? extends Object> reactorClass) throws ArgumentException {
		throw new ArgumentException("Given class:<" + reactorClass.getCanonicalName() + "> is not a sub-class of <" + Reactor.class.getCanonicalName() + ">.");
		
	}

	public static void throwWindowClosedException() throws OpenReactorWindowClosedException {
		throw new OpenReactorWindowClosedException("Main window is closed.");
	}
	
	public static void throwMalformatJsonException(String msg, Throwable t) throws OpenReactorException {
		throw new OpenReactorException(msg, t);
	}

	public static void throwIOException(String msg, Throwable t) throws OpenReactorException {
		throw new OpenReactorException(msg, t);
	}

	public static void throwResourceLoaderInstanciationException(String msg, Throwable t) throws OpenReactorException {
		throw new OpenReactorException("Failed to instanciate a resource loader:<" + msg + ">", t);
	}

	public static void throwMalformedConfigurationException(String msg, Throwable t) throws OpenReactorException {
		throw new OpenReactorException(msg, t);
	}

	public static void throwPatternLoadFailure(String msg, Throwable t) throws OpenReactorException {
		throw new OpenReactorException(msg, t);
	}

	public static void throwResourceNotFoundException(String resourceName,
			Throwable t) throws OpenReactorException {
		throw new OpenReactorException("Resource:<" + resourceName + "> is not found", t);
	}

	public static void throwParameterException(String message) throws OpenReactorException {
		throw new OpenReactorException(message);
	}

	public static void throwResourceException(String message) throws OpenReactorException {
		throw new OpenReactorException(message);
	}
	public static void throwResourceException(String message, Throwable t) throws OpenReactorException {
		throw new OpenReactorException(message, t);
	}

	public static void throwAudioLineWasUnavailable(AudioFormat format,Throwable t) throws OpenReactorException {
		throw new OpenReactorException("Failed to get audio line for format <" + format + ">", t);
	}

	public static void throwViewportStateException(String msg, Viewport viewport) throws OpenReactorException {
		throw new OpenReactorException(msg + ":<" + viewport + ">");
	}

	public static void throwException(String msg) throws OpenReactorException {
		throw new OpenReactorException(msg);
	}
}
