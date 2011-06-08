package oreactor.core;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.io.ResourceLoader.MidiData;
import oreactor.io.ResourceLoader.SoundData;
import oreactor.io.ResourceMonitor;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

public abstract class BaseEngine  implements ResourceMonitor {
	private Reactor reactor;
	ResourceLoader resourceLoader;
	private Logger logger;

	protected BaseEngine(Reactor reactor) {
		this.reactor = reactor;
		this.logger = Logger.getLogger();
	}
	public void finish() throws OpenReactorException {
	}
	
	public void initialize(Context c) throws OpenReactorException {
		this.resourceLoader = c.getResourceLoader();
	}

	@Override
	public void midiClipLoaded(String name, MidiData midiData) {
	}
	
	@Override
	public void numMidiClips(int numClips) {
	}

	@Override
	public void numPatterns(int numPatterns) {
	}
	
	@Override
	public void numSoundClips(int numPatterns) {
	}
	
	@Override
	public void numSpriteSpecs(int numSpriteSpecs) {
	}

	@Override
	public void patternLoaded(Pattern pattern) {
	}

	public void prepare() throws OpenReactorException {
	}

	public abstract void run() throws OpenReactorException;

	@Override
	public void soundClipLoaded(String name, SoundData soundData) {
	}

	@Override
	public void spriteSpecLoaded(SpriteSpec spriteSpec) {
	}
	
	
	public void terminate(Context c) throws OpenReactorException {
	}

	protected ResourceLoader resourceLoader() {
		return this.resourceLoader;
	}

	protected Settings settings() {
		return this.reactor().settings();
	}

	protected Logger logger() {
		return logger;
	}

	public Reactor reactor() {
		return reactor;
	}
	
}
