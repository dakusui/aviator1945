package oreactor.core;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import oreactor.video.PlaneDesc;

public class Settings {
	public static enum BgmMode {
		DISABLED, ENABLED
	}
	public static enum FrameMode {
		DROP, NONDROP
	}
	
	public static enum JoystickMode {
		DISABLED, ENABLED
	}

	public static enum LoggingMode {
		ERROR, INFO, STATISTICS, TRACE
	}

	public static enum RunningMode {
		DEBUG, NORMAL
	}	
	
	public static enum ScreenSize {
		_2K       { protected int[] size() {return new int[]{2048, 1152};} },
		_4K       { protected int[] size() {return new int[]{4096, 2160};} },
		_4M       { protected int[] size() {return new int[]{2304, 1728};} },
		_4xFullHD { protected int[] size() {return new int[]{3840, 2160};} },
		_8K       { protected int[] size() {return new int[]{8192, 4320};} },
		CIF       { protected int[] size() {return new int[]{ 352,  288};} },
		DCGA      { protected int[] size() {return new int[]{ 640,  400};} },
		EGA       { protected int[] size() {return new int[]{ 640,  350};} },
		FW_WXGA   { protected int[] size() {return new int[]{1366,  768};} },
		FWVGA     { protected int[] size() {return new int[]{ 854,  480};} },
		FWVGA_P   { protected int[] size() {return new int[]{ 864,  480};} },
		HVGA      { protected int[] size() {return new int[]{ 480,  320};} },
		iMac27in  { protected int[] size() {return new int[]{2560, 1440};} },
		MAC16in   { protected int[] size() {return new int[]{ 832,  624};} },
		MAC21in   { protected int[] size() {return new int[]{1152,  870};} },
		PC98HR    { protected int[] size() {return new int[]{1120,  750};} },
		QCIF      { protected int[] size() {return new int[]{ 176,  144};} },
		QuadVGA   { protected int[] size() {return new int[]{1280,  960};} },
		QUXGA     { protected int[] size() {return new int[]{3200, 2400};} },
		QUXGAWIDE { protected int[] size() {return new int[]{3840, 2400};} },
		QVGA      { protected int[] size() {return new int[]{ 320,  240};} },
		QWXGA     { protected int[] size() {return new int[]{2048, 1152};} },
		QXGA      { protected int[] size() {return new int[]{2048, 1536};} },
		sQCIF     { protected int[] size() {return new int[]{ 128,   96};} },
		SVGA      { protected int[] size() {return new int[]{ 800,  600};} },
		SXGA      { protected int[] size() {return new int[]{1280, 1024};} },
		SXGA_P    { protected int[] size() {return new int[]{1400, 1050};} },
		UWGA      { protected int[] size() {return new int[]{1024,  480};} },
		UWSVGA    { protected int[] size() {return new int[]{1280,  600};} },
		UXGA      { protected int[] size() {return new int[]{1600, 1200};} },
		VGA       { protected int[] size() {return new int[]{ 640,  480};} },
		WQVGA     { protected int[] size() {return new int[]{ 400,  240};} },
		WQXGA     { protected int[] size() {return new int[]{2560, 1600};} },
		WSVGA     { protected int[] size() {return new int[]{1024,  576};} },
		WSVGA_    { protected int[] size() {return new int[]{1024,  600};} },
		WSXGA     { protected int[] size() {return new int[]{1600, 1024};} },
		WSXGA_P   { protected int[] size() {return new int[]{1680, 1050};} },
		WUXGA     { protected int[] size() {return new int[]{1920, 1200};} },
		WVGA      { protected int[] size() {return new int[]{ 800,  480};} },
		WXGA      { protected int[] size() {return new int[]{1280,  768};} },
		WXGA_     { protected int[] size() {return new int[]{1280,  800};} },
		WXGA_P    { protected int[] size() {return new int[]{1440,  900};} },
		WXGA_PP   { protected int[] size() {return new int[]{1600,  900};} },
		XGA       { protected int[] size() {return new int[]{1024,  768};} },
		XGA_P     { protected int[] size() {return new int[]{1280,  800};} };
		public int height() {
			return (this.size())[1];
		}
		abstract protected  int[] size();
		public int width() {
			return (this.size())[0];
		}
	};
	
	public static enum SoundMode {
		DISABLED, ENABLED
	}

	public static enum VideoMode {
		FULL, NORMAL
	}

	public static Settings instance() {
		return new Settings();
	}
	private Font font;
	private FrameMode frameMode = FrameMode.DROP;
	private JoystickMode joystickMode;
	private LoggingMode loggingMode;
	private RunningMode runningMode;
	private ScreenSize screenSize;
	private SoundMode soundMode;

	private VideoMode videoMode;
	private List<PlaneDesc> planeDescs = new LinkedList<PlaneDesc>();

	public void addPlaneDesc(PlaneDesc desc) {
		this.planeDescs.add(desc);
	}

	public Font font() {
		return this.font;
	}
	
	public void font(Font font) {
		this.font = font;
	}

	public FrameMode frameMode() {
		return this.frameMode;
	}
	
	public void frameMode(FrameMode frameMode) {
		this.frameMode = frameMode;
	}

	public JoystickMode joystickMode() {
		return this.joystickMode;
	}
	

	public void joystickMode(JoystickMode joyStickMode) {
		this.joystickMode = joyStickMode;
		
	}

	public LoggingMode loggingMode() {
		return this.loggingMode;
	}
	
	public void loggingMode(LoggingMode loggingMode) {
		this.loggingMode = loggingMode;
	}

	public List<PlaneDesc> planeInfoItems() {
		return this.planeDescs ;
	}

	public RunningMode runningMode() {
		return this.runningMode;
	}

	public void runningMode(RunningMode runningMode) {
		this.runningMode = runningMode;
		
	}
	
	public ScreenSize screenSize() {
		return this.screenSize;
	}

	public void screenSize(ScreenSize screenSize) {
		this.screenSize = screenSize;
	}
	
	public SoundMode soundMode() {
		return this.soundMode;
	}

	public void soundMode(SoundMode soundMode) {
		this.soundMode = soundMode;
		
	}
	
	public VideoMode videoMode() {
		return this.videoMode;
	}
	
	public void videoMode(VideoMode videoMode) {
		this.videoMode = videoMode;
	}

	public int maxVoices() {
		return 8;
	}
}
