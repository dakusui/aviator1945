package oreactor.core;

import oreactor.io.IOEngine;
import oreactor.joystick.JoystickEngine;
import oreactor.keyboard.KeyboardEngine;
import oreactor.music.MusicEngine;
import oreactor.network.NetworkEngine;
import oreactor.sound.SoundEngine;
import oreactor.video.VideoEngine;

public class Context {
	private VideoEngine videoEngine;
	private SoundEngine soundEngine;
	private MusicEngine musicEngine;
	private KeyboardEngine keyboardEngine;
	private JoystickEngine joystickEngine;
	private IOEngine ioEngine;
	private NetworkEngine networkEngine;
	
	public Context(Settings settings) {
		this.videoEngine = new VideoEngine(settings);
		this.soundEngine = new SoundEngine(settings);
		this.musicEngine = new MusicEngine(settings);
		this.keyboardEngine = new KeyboardEngine(settings);
		this.joystickEngine = new JoystickEngine(settings);
		this.ioEngine = new IOEngine(settings);
		this.networkEngine = new NetworkEngine(settings);
	}
	
	public VideoEngine getVideoEngine() {
		return this.videoEngine;
	}
	
	public SoundEngine getSoundEngine() {
		return this.soundEngine;
	}
	
	public MusicEngine getMusicEngine() {
		return this.musicEngine;
	}
	
	public KeyboardEngine getKeyboardEngine() {
		return this.keyboardEngine;
	}
	
	public JoystickEngine getJoystickEngine() {
		return this.joystickEngine;
	}
	
	public IOEngine getIOEngine() {
		return this.ioEngine;
	}
	public NetworkEngine getNetworkEngine() {
		return this.networkEngine;
	}
}