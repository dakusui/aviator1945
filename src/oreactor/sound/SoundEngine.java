package oreactor.sound;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

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

public class SoundEngine extends BaseEngine {
	private List<SourceDataLine> availableLines = Collections.synchronizedList(new LinkedList<SourceDataLine>());
	private List<SoundPlayer> activePlayers = Collections.synchronizedList(new LinkedList<SoundPlayer>());
	private int maxVoices;
	private long lastRun;
	
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
		}
		for (SoundPlayer p: activePlayers) {
			p.feed(duration);
		}
	}

	public SoundPlayer player(String resourceUrl) throws OpenReactorException {
		SoundData data = resourceLoader().getSound(resourceUrl);
		return new SoundPlayer(data, this);
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

}
