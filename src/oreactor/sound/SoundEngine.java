package oreactor.sound;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import oreactor.core.BaseEngine;
import oreactor.core.Context;
import oreactor.core.Settings;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader.SoundData;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

public class SoundEngine extends BaseEngine {
	private List<SourceDataLine> availableLines = new LinkedList<SourceDataLine>();
	private List<SoundPlayer> activePlayers = new LinkedList<SoundPlayer>();
	private int maxVoices;
	private long lastRun;
	private Map<String, String> soundClipNames = new HashMap<String, String>();
	
	public SoundEngine(Settings settings) {
		super(settings);
		this.maxVoices = settings.maxVoices();
	}

	@Override
	public void initialize(Context c) throws OpenReactorException {
		super.initialize(c);
		AudioFormat format = null;
		try {
			for (int i = 0; i < maxVoices; i++) {
				/*
				 * encoding=<PCM_UNSIGNED>
				 * sampleRate=<44100.0>
				 * sampleSizeInBits=<8>
				 * channels=<1>
				 * frameSize=<1>
				 * frameRate=<44100.0>
				 * bigEndian=<false>
				 */
				format = new AudioFormat(
						AudioFormat.Encoding.PCM_UNSIGNED, 
						(float)44100.0,
						8,
						1,
						1,
						(float)44100.0,
						false);
		        // ラインを取得
				DataLine.Info info = new DataLine.Info(SourceDataLine.class, format, AudioSystem.NOT_SPECIFIED);
				SourceDataLine newLine = (SourceDataLine)AudioSystem.getLine(info);
		        // ラインを開く
				newLine.open(format);
				availableLines.add(newLine);
			}
		} catch (LineUnavailableException e) {
			ExceptionThrower.throwAudioLineWasUnavailable(format, e);
		}		
	}
	
	
	@Override
	public void run() {
		long duration = 0;
		if (lastRun != 0) {
			long t;
			duration = (t = System.currentTimeMillis()) - lastRun;
			lastRun = t;
		} else {
			lastRun = System.currentTimeMillis();
		}
		for (SoundPlayer p: activePlayers) {
			p.feed(duration);
		}
		List<SoundPlayer> tmp = new LinkedList<SoundPlayer>();
		tmp.addAll(activePlayers);
		for (SoundPlayer p: tmp) {
			if (p.state() == SoundPlayer.State.Finished) {
				release(p);
			}
		}
	}

	public SoundPlayer player(String soundClipName) throws OpenReactorException {
		SoundData data = resourceLoader().getSound(this.soundClipNames.get(soundClipName));
		SoundPlayer ret = new SoundPlayer(data, this);
		activePlayers.add(ret);
		return ret;
	}

	SourceDataLine getLine() {
		SourceDataLine ret = null;
		if (availableLines.size() > 0) {
			ret = availableLines.remove(0);
		}
		return ret;
	}

	void release(SoundPlayer player) {
		if (player != null) {
			activePlayers.remove(player);
			SourceDataLine line = player.line();
			if (line != null) {
				availableLines.add(line);
			}
		}
	}

	@Override
	public void terminate(Context c) throws OpenReactorException {
		this.soundClipNames.clear();
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
		this.soundClipNames.put(name, soundData.resourceUrl());
	}

}
