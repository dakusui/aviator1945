package oreactor.io;

import oreactor.io.ResourceLoader.SoundData;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

public interface ResourceMonitor {
	public void numPatterns(int numPatterns);
	public void patternLoaded(Pattern pattern);
	public void numSpriteSpecs(int numSpriteSpecs);
	public void spriteSpecLoaded(SpriteSpec spriteSpec);
	public void numSoundClips(int numPatterns);
	public void soundClipLoaded(String name, SoundData soundData);
}
