package oreactor.core;

import java.util.List;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.joystick.JoystickEngine;
import oreactor.keyboard.KeyboardEngine;
import oreactor.motion.MotionEngine;
import oreactor.music.MidiEngine;
import oreactor.network.NetworkEngine;
import oreactor.sound.SoundEngine;
import oreactor.video.PlaneDesc;
import oreactor.video.Screen;
import oreactor.video.VideoEngine;

public class Context {
	private VideoEngine videoEngine;
	private SoundEngine soundEngine;
	private MidiEngine musicEngine;
	private KeyboardEngine keyboardEngine;
	private JoystickEngine joystickEngine;
	private NetworkEngine networkEngine;
	private ResourceLoader resourceLoader;
	private MotionEngine motionEngine;
	
	public Context(Reactor reactor, Settings settings) throws OpenReactorException {
		this.videoEngine = new VideoEngine(settings);
		List<PlaneDesc> planeDescs = settings.planeInfoItems();
		Screen s = this.videoEngine.screen();
		System.err.println("List of plane info items:<" + planeDescs + ">");
		for (PlaneDesc desc : planeDescs) {
			s.createPlane(desc);
		}
		this.soundEngine = new SoundEngine(settings);
		this.musicEngine = new MidiEngine(settings);
		this.keyboardEngine = new KeyboardEngine(settings);
		this.joystickEngine = new JoystickEngine(settings);
		this.networkEngine = new NetworkEngine(settings);
		this.motionEngine = new MotionEngine(settings);
		this.motionEngine.setProvider(reactor.motionProvider());
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

	public MotionEngine getMotionEngine() {
		return this.motionEngine;
	}
}
