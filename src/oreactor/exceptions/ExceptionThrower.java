package oreactor.exceptions;

import java.text.MessageFormat;

import oreactor.core.Reactor;

public class ExceptionThrower {

	public static void throwArgumentException(String keyword, String key,
			@SuppressWarnings("rawtypes") Class<? extends Enum> k) throws ArgumentException {
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

}
