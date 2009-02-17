package avis.base;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.Properties;

import javax.imageio.ImageIO;


public class Avis implements ALogger{
	static {
		initialize();
	}
	public static final int DIRECTION_STEPS = 256;
	public static final int BANK_STEPS = DIRECTION_STEPS;
	protected static double[] COS;
	protected static double[] SIN;
	protected static boolean initialized;
	protected static Avis instance;
	private static Avis logger;
	private int logLevel;
	private PrintStream logWriter = System.out; 
	
	protected Avis() {
	}
	
	public static String format(long n) {
		String ret = "";
		String comma = "";
		long nn = n;
		while (nn > 0) {
			String tmp;
			if (nn < 1000 ) {
				tmp = Long.toString(nn % 1000);
			} else {
				tmp = "000" + (nn % 1000);
				tmp = tmp.substring(tmp.length() - 3);
			}
			nn = nn / 1000;
			ret = (tmp) + comma + ret;
			comma = ",";
		}
		return ret;
	}
	
	private static void initialize() {
		if (!initialized) {
			COS = new double[DIRECTION_STEPS];
			SIN = new double[DIRECTION_STEPS];
			for (int i = 0; i < DIRECTION_STEPS; i++) {
				COS[i] = Math.cos(((double)(2 * Math.PI * i)) / ((double)DIRECTION_STEPS));
				SIN[i] = Math.sin(((double)(2 * Math.PI * i)) / ((double)DIRECTION_STEPS));
			}
			instance = new Avis();
			initialized = true;
			logger = instance;
		}
	}
	
	public static double cos(int i) {
		assert initialized : "Utilクラスが初期化されていません。";
		if (i < 0) {
			return COS[DIRECTION_STEPS + i];
		}
		return COS[i % DIRECTION_STEPS];
	}

	public static double sin(int i) {
		assert initialized : "Utilクラスが初期化されていません。";
		if (i < 0) {
			return SIN[DIRECTION_STEPS + i];
		}
		return SIN[i % DIRECTION_STEPS];
	}

	public static double sin(int theta1, int theta2) {
		assert initialized : "Utilクラスが初期化されていません。";
		return - cos(theta1) * sin(theta2) + sin(theta1) * cos(theta2);
	}

	public static double cos(int theta1, int theta2) {
		assert initialized : "Utilクラスが初期化されていません。";
		return cos(theta1) * cos(theta2) + sin(theta1) * sin(theta2);
	}
	public static InputStream openUrl(String resourceName) throws AException {
		assert initialized : "Utilクラスが初期化されていません。";
		URL url;
		try {
			logger.info("Loading Resoure:resourceName=<" + resourceName + ">");
			url = instance.getClass().getClassLoader().getResource(resourceName);
			InputStream ret = url.openStream();
			return ret;
		} catch (IOException e) {
			AExceptionThrower.throwIOFailedException(resourceName, e);
			// This path will never be passed.
			return null;
		}
	}
	public static Image readImage(InputStream is) throws AException {
		Image ret;
		try {
			ret = ImageIO.read(is);
		} catch (IOException e) {
			AExceptionThrower.throwIOFailedException("(unknonwn)", e);
			// This path will never be passed.
			ret = null;
		}
		return ret;
	}
	public static void closeStream(InputStream is) {
		try {
			is.close();
		} catch (IOException e) {
			// ignore the failure.
		}
	}
	
	public static Properties loadPropertes(InputStream is) throws AException {
		Properties ret = new Properties();
		try {
			ret.load(is);
		} catch (IOException e) {
			AExceptionThrower.throwIOFailedException("NOT AVAILABLE", e);
		}
		return ret;
	}

	public static double srad2rad(double d) {
		return 2 * Math.PI * d / Avis.DIRECTION_STEPS;
	}

	public static InputStream openFile(File f) throws AException {
		InputStream is;
		try {
			is = new BufferedInputStream(
					new FileInputStream(f)
					);
		} catch (FileNotFoundException e) {
			AExceptionThrower.throwIOFailedException(f.getAbsolutePath(), e);
			// This path will never be passed.
			is = null;
		}
		return is;
	}

	public void debug(String message) {
		if (this.logLevel <= ALogger.DEBUG) {
			logWriter.print("[Debug] "); 
			logWriter.println(message);
		}
	}

	public void error(String message, Throwable t) {
		if (this.logLevel <= ALogger.ERROR) {
			logWriter.print("[Error] ");
			logWriter.println(message + "(" + t.getMessage() + ")");
			StackTraceElement[] trace = t.getStackTrace();
			for (int i = 0; i < trace.length; i ++) {
				logWriter.print("[Error] ");
				logWriter.println(trace[i]);
			}
		}
	}

	public void info(String message) {
		if (this.logLevel <= ALogger.INFO) {
			logWriter.print("[Info] ");
			logWriter.println(message);
		}
	}

	public void setOut(PrintStream ps) {
		this.logWriter = ps;
	}

	public void statistics(String message) {
		if (this.logLevel <= ALogger.STATISTICS) {
			logWriter.print("[Statistics] ");
			logWriter.println(message);
		}
	}

	public void level(int level) {
		this.logLevel = level;
	}

	public int level() {
		return this.logLevel;
	}

	public static ALogger logger() {
		return logger;
	}
	
}
