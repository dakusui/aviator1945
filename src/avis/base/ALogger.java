package avis.base;

import java.io.PrintStream;

public interface ALogger {
	public static final int DEBUG = 100;
	public static final int INFO = 1000;
	public static final int STATISTICS = 10000;
	public static final int ERROR = 100000;
	public void level(int level);
	public int level();
	public void debug(String message);
	public void info(String message);
	public void statistics(String message);
	public void error(String message, Throwable t);
	public void setOut(PrintStream ps);
}
