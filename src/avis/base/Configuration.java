package avis.base;

import java.awt.Color;
import java.awt.Font;
import java.lang.reflect.Field;

import avis.session.ASession;


public class Configuration {

	public static enum SoundMode {
		SILENT, NORMAL, MULTITHREADED, BGMONLY
	}
	public static enum DebugMode {
		SCENARIOSKIP, GRID, NORMAL
	}
	public static enum VideoMode {
		FULL, NORMAL
	}
	public static enum FrameMode {
		DROP, NONDROP
	}	
	public static enum RenderingMode {
		VOLATILE, BUFFERED
	};
	public static enum BgQualityMode {
		HIGH, LOW
	};
	public static enum JoystickMode {
		JOYPAD, DISABLED, PS3;
	}
	public static enum RotationMode {
		ENABLED, DISABLED
	}
	public static RenderingMode renderingMode = RenderingMode.BUFFERED;
	
	public static Configuration.DebugMode debugMode = Configuration.DebugMode.NORMAL;
	public static Configuration.SoundMode soundMode = Configuration.SoundMode.NORMAL;
	public static Configuration.VideoMode videoMode = Configuration.VideoMode.NORMAL;
	public static Configuration.FrameMode frameMode = Configuration.FrameMode.NONDROP;
	public static Configuration.BgQualityMode bgQualityMode = Configuration.BgQualityMode.HIGH;
	public static Configuration.JoystickMode joystickMode = Configuration.JoystickMode.JOYPAD;
	public static Configuration.RotationMode rotationMode = Configuration.RotationMode.ENABLED;
	
	public static Font defaultFont;
	public static Color defaultColor = Color.white;
	public static ASession session;
	//public static MotionController controller;

	public static String version = "0.90";


	public static String dump() {
		String ret = "Configuration=<";
		Field[] fields = Configuration.class.getDeclaredFields();
		for (int i = 0; i < fields.length; i ++) {
			try {
				ret = ret + fields[i].getName() + "=<" + fields[i].get(null) + ">";
			} catch (IllegalArgumentException e) {
				Avis.logger().error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				Avis.logger().error(e.getMessage(), e);
			}
			if (i < fields.length -1) {
				ret = ret + ",";
			}
		}
		ret = ret + ">";
		return ret;
	}
}
