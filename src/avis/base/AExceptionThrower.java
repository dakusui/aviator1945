package avis.base;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.LineUnavailableException;

import avis.motion.MMachineStateException;
import avis.sound.SAudioException;
import avis.spec.AImageException;
import avis.spec.ASpecException;
import avis.spec.ASpriteSpec;


public class AExceptionThrower {

	public static void throwSpecifiedKeyNotFoundException(String key) throws ASpecException {
		throw new ASpecException("Specified key <" + key + "> was not found in the property.");
	}

	public static void throwSpecifiedKeyMalformattedException(String key,
			String s) throws ASpecException {
		throw new ASpecException("Specified key <" + key + "> has malformatted value <" + s + ">");
	}

	public static void throwIOFailedException(String filename, Exception e) throws AException {
		throw new AIOException("Failed to read spec file <" + filename + "> ",e);
	}

	public static void throwFailedToLoadMidiResource(String resourceName,
			Exception e) throws SAudioException {
		throw new SAudioException("Failed to load midi resource <" + resourceName + ">.", e);
	}

	public static void throwAudioLineWasUnavailable(AudioFormat format,
			LineUnavailableException e) throws SAudioException {
		throw new SAudioException("Failed to get audio line for format <" + format + ">", e);
	}

	public static void throwAudioStreamWasNotAvailableException(
			String resourceName, Exception e) throws SAudioException {
		throw new SAudioException("Failed to get audio stream for resource <" + resourceName + ">", e);
	}

	public static void throwFailedToLoadImage(String resourceName, Exception e) throws AImageException {
		throw new AImageException("Failed to load image resource <" + resourceName + ">", e);
	}

	public static void throwMMachineStateException(Object machine,
			Object expected, Object actual) throws MMachineStateException {
		throw new MMachineStateException("State of <" + machine + "> was <" + actual + "> while expecting <" + expected + ">");
	}

	public static void throwSpriteSpecInstanciationException(
			Class<? extends ASpriteSpec> class1, Object resource,
			Exception e) throws ASpecException {
		throw new ASpecException("Failed to instanciate class <" + class1 + ">", e);
	}

	public static void throwInputDeviceException(String string, IOException e) throws AException {
		throw new AException(string, e);
	}
}
