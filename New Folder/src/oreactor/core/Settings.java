package oreactor.core;

import java.util.List;

public class Settings {
	public static enum SoundMode {
		SILENT, NORMAL, MULTITHREADED, BGMONLY
	}
	public static enum DebugMode {
		SCENARIOSKIP, GRID, NORMAL
	}
	public static enum VideoMode {
		FULL, NORMAL
	}
	public static enum ScreenSize {
		sQCIF     { protected int[] size() {return new int[]{ 128,   96};} },
		QCIF      { protected int[] size() {return new int[]{ 176,  144};} },
		QVGA      { protected int[] size() {return new int[]{ 320,  240};} },
		WQVGA     { protected int[] size() {return new int[]{ 400,  240};} },
		CIF       { protected int[] size() {return new int[]{ 352,  288};} },
		HVGA      { protected int[] size() {return new int[]{ 480,  320};} },
		EGA       { protected int[] size() {return new int[]{ 640,  350};} },
		DCGA      { protected int[] size() {return new int[]{ 640,  400};} },
		VGA       { protected int[] size() {return new int[]{ 640,  480};} },
		WVGA      { protected int[] size() {return new int[]{ 800,  480};} },
		FWVGA     { protected int[] size() {return new int[]{ 854,  480};} },
		FWVGA_P   { protected int[] size() {return new int[]{ 864,  480};} },
		SVGA      { protected int[] size() {return new int[]{ 800,  600};} },
		UWGA      { protected int[] size() {return new int[]{1024,  480};} },
		MAC16in   { protected int[] size() {return new int[]{ 832,  624};} },
		WSVGA     { protected int[] size() {return new int[]{1024,  576};} },
		WSVGA_    { protected int[] size() {return new int[]{1024,  600};} },
		UWSVGA    { protected int[] size() {return new int[]{1280,  600};} },
		XGA       { protected int[] size() {return new int[]{1024,  768};} },
		PC98HR    { protected int[] size() {return new int[]{1120,  750};} },
		WXGA      { protected int[] size() {return new int[]{1280,  768};} },
		XGA_P     { protected int[] size() {return new int[]{1280,  800};} },
		MAC21in   { protected int[] size() {return new int[]{1152,  870};} },
		WXGA_     { protected int[] size() {return new int[]{1280,  800};} },
		FW_WXGA   { protected int[] size() {return new int[]{1366,  768};} },
		QuadVGA   { protected int[] size() {return new int[]{1280,  960};} },
		WXGA_P    { protected int[] size() {return new int[]{1440,  900};} },
		SXGA      { protected int[] size() {return new int[]{1280, 1024};} },
		WXGA_PP   { protected int[] size() {return new int[]{1600,  900};} },
		SXGA_P    { protected int[] size() {return new int[]{1400, 1050};} },
		WSXGA     { protected int[] size() {return new int[]{1600, 1024};} },
		WSXGA_P   { protected int[] size() {return new int[]{1680, 1050};} },
		UXGA      { protected int[] size() {return new int[]{1600, 1200};} },
		_2K       { protected int[] size() {return new int[]{2048, 1152};} },
		WUXGA     { protected int[] size() {return new int[]{1920, 1200};} },
		QWXGA     { protected int[] size() {return new int[]{2048, 1152};} },
		QXGA      { protected int[] size() {return new int[]{2048, 1536};} },
		_4M       { protected int[] size() {return new int[]{2304, 1728};} },
		iMac27in  { protected int[] size() {return new int[]{2560, 1440};} },
		WQXGA     { protected int[] size() {return new int[]{2560, 1600};} },
		QUXGA     { protected int[] size() {return new int[]{3200, 2400};} },
		_4xFullHD { protected int[] size() {return new int[]{3840, 2160};} },
		_4K       { protected int[] size() {return new int[]{4096, 2160};} },
		QUXGAWIDE { protected int[] size() {return new int[]{3840, 2400};} },
		_8K       { protected int[] size() {return new int[]{8192, 4320};} };
		public int width() {
			return (this.size())[0];
		}
		public int height() {
			return (this.size())[1];
		}
		abstract protected  int[] size();
	}
	public static enum FrameMode {
		DROP, NONDROP
	}	
	public static enum RenderingMode {
		VOLATILE, BUFFERED
	};
	public static enum PrerenderingMode {
		ENABLED, DISABLED
	}
	public static enum BgQualityMode {
		HIGH, LOW
	};
	public static enum JoystickMode {
		JOYPAD, DISABLED, PS3;
	}
	public static enum RotationMode {
		ENABLED, DISABLED
	}	

	public static enum PlaneType {
		GRAPHICS, SPRITE, PATTTERN;
	}

	private FrameMode frameMode = FrameMode.DROP;
	private List<PlaneType> planes;
	

	public FrameMode frameMode() {
		return this.frameMode;
	}
	
	public List<PlaneType> planeTypes() {
		return this.planes;
	}

	public static Settings instance() {
		return new Settings();
	}
	
	public static Settings parseArgs(String[] args) {
		return new Settings();
	}

	public Class<? extends Reactor> gearClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public RenderingMode renderingMode() {
		// TODO Auto-generated method stub
		return null;
	}
}
