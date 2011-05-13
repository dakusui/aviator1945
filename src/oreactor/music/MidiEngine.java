package oreactor.music;

import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import oreactor.core.BaseEngine;
import oreactor.core.Settings;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader.MidiData;

public class MidiEngine  extends BaseEngine {

	private MidiPlayer activePlayer;
	private Map<String, MidiData> midiClips = new HashMap<String, MidiData>();
	
	public MidiEngine(Settings settings) {
		super(settings);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		if (this.activePlayer == null) {
			MidiData data = midiClips.get(name);
			if (data == null) {
				ExceptionThrower.throwResourceException("Specified midi resource:<" + name + "> is not found.");
			}
			this.activePlayer = new MidiPlayer(data, 0, this);
		}
		return activePlayer; 
	}
}
