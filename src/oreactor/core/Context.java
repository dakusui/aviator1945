package oreactor.core;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.joystick.JoystickEngine;
import oreactor.keyboard.KeyboardEngine;
import oreactor.music.MidiEngine;
import oreactor.network.NetworkEngine;
import oreactor.sound.SoundEngine;
import oreactor.video.VideoEngine;

public class Context {
	private VideoEngine videoEngine;
	private SoundEngine soundEngine;
	private MidiEngine musicEngine;
	private KeyboardEngine keyboardEngine;
	private JoystickEngine joystickEngine;
	private NetworkEngine networkEngine;
	private ResourceLoader resourceLoader;
	
	public Context(Reactor reactor) throws OpenReactorException {
		this.videoEngine = new VideoEngine(reactor);
		this.soundEngine = new SoundEngine(reactor);
		this.musicEngine = new MidiEngine(reactor);
		this.keyboardEngine = new KeyboardEngine(reactor);
		this.joystickEngine = new JoystickEngine(reactor);
		this.networkEngine = new NetworkEngine(reactor);
		this.resourceLoader = ResourceLoader.getResourceLoader(reactor);
	}
	
	public VideoEngine getVideoEngine() {
		return this.videoEngine;
	}
	
	public SoundEngine getSoundEngine() {
		return this.soundEngine;
	}
	
	public MidiEngine getMusicEngine() {
		return this.musicEngine;
	}
	
	public KeyboardEngine getKeyboardEngine() {
		return this.keyboardEngine;
	}
	
	public JoystickEngine getJoystickEngine() {
		return this.joystickEngine;
	}
	
	public NetworkEngine getNetworkEngine() {
		return this.networkEngine;
	}
	
	public ResourceLoader getResourceLoader() {
		return this.resourceLoader;
	}
}
