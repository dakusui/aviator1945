package oreactor.core;

public class Logger {
	private static Logger instance = new Logger();
	
	private Logger() {
	}
	
	public void debug(String msg) {
		System.err.println("DEBUG:" + msg);
	}
	public void debug(String msg, Throwable t) {
		System.err.println("DEBUG:" + msg);
		t.printStackTrace(System.err);
	}
	
	public void info(String msg) {
		System.err.println("INFO:" + msg);
	}
	
	public void info(String msg, Throwable t) {
		System.err.println("INFO:" + msg);
		t.printStackTrace(System.err);
	}

	public void warn(String msg) {
		System.err.println("WARN:" + msg);
	}
	
	public void warn(String msg, Throwable t) {
		System.err.println("WARN:" + msg);
		t.printStackTrace(System.err);
	}

	public static Logger getLogger() {
		return instance;
	}

	public void error(String message) {
		System.err.println("ERROR:" + message);
	}
	public void error(String message, Throwable t) {
		System.err.println("ERROR:" + message);
		t.printStackTrace(System.err);
	}

}
