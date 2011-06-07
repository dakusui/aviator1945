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
import oreactor.core.Reactor;
import oreactor.core.Settings.SoundMode;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader.SoundData;

public class SoundEngine extends BaseEngine {
	private enum Mode {
		Disabled {
			@Override
			public SoundPlayer player(SoundEngine engine, String soundClipName) {
				return SoundPlayer.NULL_PLAYER;
			}
			@Override
			public void run(SoundEngine engine) {
			}
		},
		Enabled {
			public SoundPlayer player(SoundEngine engine, String soundClipName) throws OpenReactorException {
				SoundData data = engine.resourceLoader().getSound(engine.soundClipNames.get(soundClipName));
				SoundPlayer ret = new SoundPlayer(data, engine);
				engine.activePlayers.add(ret);
				return ret;
			}
			@Override
			public void run(SoundEngine engine) {
				long duration = 0;
				if (engine.lastRun != 0) {
					long t;
					duration = (t = System.currentTimeMillis()) - engine.lastRun;
					engine.lastRun = t;
				} else {
					engine.lastRun = System.currentTimeMillis();
				}
				for (SoundPlayer p: engine.activePlayers) {
					p.feed(duration);
				}
				List<SoundPlayer> tmp = new LinkedList<SoundPlayer>();
				tmp.addAll(engine.activePlayers);
				for (SoundPlayer p: tmp) {
					if (p.state() == SoundPlayer.State.Finished) {
						engine.release(p);
					}
				}
			}
		};
		abstract SoundPlayer player(SoundEngine engine ,String soundClipName) throws OpenReactorException;
		abstract void run(SoundEngine engine);
	}
	private List<SoundPlayer> activePlayers = new LinkedList<SoundPlayer>();
	private List<SourceDataLine> availableLines = new LinkedList<SourceDataLine>();
	private long lastRun;
	private int maxVoices;
	private Mode mode = Mode.Disabled;
	private Map<String, String> soundClipNames = new HashMap<String, String>();
	public SoundEngine(Reactor reactor) {
		super(reactor);
		this.maxVoices = reactor.getSettings().maxVoices();
	}

	@Override
	public void initialize(Context c) throws OpenReactorException {
		super.initialize(c);
		AudioFormat format = null;
		if (SoundMode.ENABLED.equals(this.settings().soundMode()) || 
			SoundMode.ENABLED_FALLBACK.equals(this.settings().soundMode())) {
			boolean succeeded = false;
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
				succeeded = true;
			} catch (LineUnavailableException e) {
				if (SoundMode.ENABLED.equals(settings().soundMode())) {
				    ExceptionThrower.throwAudioLineWasUnavailable(format, e);
				} else {
					logger().info("Failed to get wave audio device: Falling back.");
				}
			} finally {
				if (succeeded) {
					this.mode = Mode.Enabled;
				} else {
					// falling back
					this.mode = Mode.Disabled;
				}
			}
		}
	}
	
	
	public SoundPlayer player(String soundClipName) throws OpenReactorException {
		return mode.player(this, soundClipName);
	}

	@Override
	public void run() {
		mode.run(this);
	}

	@Override
	public void soundClipLoaded(String name, SoundData soundData) {
		this.soundClipNames.put(name, soundData.resourceUrl());
	}

	@Override
	public void terminate(Context c) throws OpenReactorException {
		this.soundClipNames.clear();
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
