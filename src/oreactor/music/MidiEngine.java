package oreactor.music;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import oreactor.core.BaseEngine;
import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader.MidiData;

public class MidiEngine  extends BaseEngine {
	static enum Mode {
		ENABLED,
		DISABLED
	}
	
	private Mode mode = Mode.DISABLED;
	private MidiPlayer activePlayer;
	
	private Map<String, MidiData> midiClips = new HashMap<String, MidiData>();
	private Throwable lastException;
	
	public MidiEngine(Reactor reactor) {
		super(reactor);
	}

	@Override
	public void initialize(Context c) throws OpenReactorException {
		super.initialize(c);
		this.mode = Mode.DISABLED;
		if (Settings.BgmMode.ENABLED.equals(settings().bgmMode()) || 
				Settings.BgmMode.ENABLED_FALLBACK.equals(settings().bgmMode()) 
				) {
			Sequencer seq = getSequencer(); 
			if (seq == null) {
				if (Settings.BgmMode.ENABLED.equals(settings().bgmMode())) {
					ExceptionThrower.throwMidiUnavailableException("Midi audio device is unavailable", lastException);
				} else {
					String msg = "Failed to get midi audio device: Falling back";
					logger().info(msg);
					logger().debug(msg, lastException);
				}
			} else {
				seq.close();
				this.mode = Mode.ENABLED;
			}
		}
	}

	@Override
	public void run() {
		// usually, this engine does nothing but monitoring.
	}

	public Sequencer getSequencer() {
		Sequencer ret = null;
		try {
			ret = MidiSystem.getSequencer();
		} catch (MidiUnavailableException e) {
			this.lastException = e;
		}
		return ret;
	}

	public void playerStopped(MidiPlayer midiPlayer) {
		if (this.activePlayer == midiPlayer) {
			this.activePlayer = null;
		}
	}
	
	@Override
	public void midiClipLoaded(String name, MidiData midiData) {
		midiClips.put(name, midiData);
	}

	public MidiPlayer player(String name) throws OpenReactorException {
		if (mode == Mode.ENABLED) {
			if (this.activePlayer == null) {
				MidiData data = midiClips.get(name);
				if (data == null) {
					ExceptionThrower.throwResourceException("Specified midi resource:<" + name + "> is not found.");
				}
				this.activePlayer = new MidiPlayer(data, 0, this);
			}
			return activePlayer;
		} else {
			return MidiPlayer.NULL_PLAYER;
		}
	}
}
