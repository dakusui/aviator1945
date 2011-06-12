package oreactor.core;

import java.awt.Font;

public class Settings {
	public static enum BgmMode {
		DISABLED, ENABLED, ENABLED_FALLBACK;
	}
	public static enum LoggingMode {
		ERROR, INFO, STATISTICS, TRACE
	}
	public static enum ExecutionMode {
		DEBUG, NORMAL
	}
	public static enum SoundMode {
		DISABLED, ENABLED, ENABLED_FALLBACK
	}
	public static enum VideoMode {
		NORMAL, FULL, FULL_FALLBACK
	}
	public static Settings instance() {
		return new Settings();
	}
	private Font font;
	private LoggingMode loggingMode;
	private ExecutionMode executionMode;
	private SoundMode soundMode;
	private VideoMode videoMode;
	private BgmMode bgmMode;

	public Font font() {
		return this.font;
	}
	
	public void font(Font font) {
		this.font = font;
	}

	public LoggingMode loggingMode() {
		return this.loggingMode;
	}
	
	public void loggingMode(LoggingMode loggingMode) {
		this.loggingMode = loggingMode;
	}

	public int maxVoices() {
		return 8;
	}

	public ExecutionMode runningMode() {
		return this.executionMode;
	}
	
	public void executionMode(ExecutionMode runningMode) {
		this.executionMode = runningMode;
		
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

	public void bgmMode(BgmMode bgmMode) {
		this.bgmMode = bgmMode;
	}
	
	public BgmMode bgmMode() {
		return this.bgmMode;
	}
}
