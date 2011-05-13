package oreactor.core;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.io.ResourceLoader.MidiData;
import oreactor.io.ResourceLoader.SoundData;
import oreactor.io.ResourceMonitor;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

public abstract class BaseEngine  implements ResourceMonitor {
	final protected Settings settings;
	ResourceLoader resourceLoader;

	protected BaseEngine(Settings settings) {
		this.settings = settings;
	}
	
	public void initialize(Context c) throws OpenReactorException {
		this.resourceLoader = c.getResourceLoader();
	}

	public void prepare() throws OpenReactorException {
	}
	
	public abstract void run() throws OpenReactorException;

	public void finish() throws OpenReactorException {
	}
	
	public void terminate(Context c) throws OpenReactorException {
	}
	
	protected ResourceLoader resourceLoader() {
		return this.resourceLoader;
	}

	@Override
	public void numPatterns(int numPatterns) {
	}

	@Override
	public void patternLoaded(Pattern pattern) {
	}

	@Override
	public void numSpriteSpecs(int numSpriteSpecs) {
	}

	@Override
	public void spriteSpecLoaded(SpriteSpec spriteSpec) {
	}

	@Override
	public void numSoundClips(int numPatterns) {
	}

	@Override
	public void soundClipLoaded(String name, SoundData soundData) {
	}

	@Override
	public void numMidiClips(int numClips) {
	}

	@Override
	public void midiClipLoaded(String name, MidiData midiData) {
	}
}
