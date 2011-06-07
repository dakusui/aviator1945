package oreactor.core;

import java.util.List;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.joystick.JoystickEngine;
import oreactor.keyboard.KeyboardEngine;
import oreactor.music.MidiEngine;
import oreactor.network.NetworkEngine;
import oreactor.sound.SoundEngine;
import oreactor.video.PlaneDesc;
import oreactor.video.Screen;
import oreactor.video.VideoEngine;

public class Context {
	private static final Logger logger = Logger.getLogger();
	private VideoEngine videoEngine;
	private SoundEngine soundEngine;
	private MidiEngine musicEngine;
	private KeyboardEngine keyboardEngine;
	private JoystickEngine joystickEngine;
	private NetworkEngine networkEngine;
	private ResourceLoader resourceLoader;
	
	public Context(Reactor reactor) throws OpenReactorException {
		this.videoEngine = new VideoEngine(reactor);
		List<PlaneDesc> planeDescs = reactor.planeInfoItems();
		Screen s = this.videoEngine.screen();
		logger.debug("List of plane info items:<" + planeDescs + ">");
		for (PlaneDesc desc : planeDescs) {
			s.createPlane(desc);
		}
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
