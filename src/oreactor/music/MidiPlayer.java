package oreactor.music;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader.MidiData;


public class MidiPlayer {
	/**
	 * A midi meta event to tell end of track.
	 */
	private static final int END_OF_TRACK_MESSAGE = 47;
	
	/**
	 * A data object to hold a midi sequence to play.
	 */
	MidiData midi = null;

	/**
	 * A sequencer object.
	 */
	private Sequencer sequencer;

	/**
	 * The position the sequence is stooped. This field is used for resuming.
	 */
	private long stoppedPosition = -1;

	private MidiEngine manager;

	/**
	 * Create an object of this class.
	 * @param data A data object to hold the midi sequence to playback.
	 * @param repeatTick The position to repeat the sequence. If negative number is specified, the sequence is not repeated.
	 * @param manager A manager object which holds midi sequencer.
	 */
	public MidiPlayer(MidiData data, final long repeatTick_, MidiEngine manager) {
		this.midi = data;
		this.sequencer = manager.getSequencer();
		this.manager = manager;
		if (repeatTick_ >= 0) {
			this.sequencer.addMetaEventListener(new MetaEventListener() {
				long repeatTick = repeatTick_; ;
				@Override
				public void meta(MetaMessage meta) {
					if (meta.getType() == END_OF_TRACK_MESSAGE) {
						if (sequencer != null && sequencer.isOpen()) {
							sequencer.setMicrosecondPosition(repeatTick);
							sequencer.start();
						}
					}
				}
			});
		} else {
			this.sequencer.addMetaEventListener(new MetaEventListener() {
				@Override
				public void meta(MetaMessage meta) {
					if (meta.getType() == END_OF_TRACK_MESSAGE) {
						stop();
					}
				}
			});
		}
	}

	public void play() throws OpenReactorException {
		pause();
		try {
			this.sequencer.setSequence(this.midi.sequence());
			this.sequencer.start();
		} catch (InvalidMidiDataException e) {
			ExceptionThrower.throwResourceException("Invalid midi resource was given:" + e.getMessage(), e);
		}
	}
	
	public void pause() {
		if (this.sequencer.isRunning()) {
			this.stoppedPosition = sequencer.getTickPosition();
			this.sequencer.stop();
		}
	}
	
	public void stop() {
		if (this.sequencer.isRunning()) {
			this.stoppedPosition = -1;
			this.sequencer.stop();
		}
		manager.playerStopped(this);
	}
	
	public void resume() throws OpenReactorException {
		Sequence seq = this.midi.sequence(); 
		if (seq != null && sequencer != null && sequencer.isOpen()) {
			try {
				sequencer.setSequence(seq);
				sequencer.setTickPosition(
						stoppedPosition != -1 ? stoppedPosition
								              : 0 
						);
			} catch (InvalidMidiDataException e) {
				ExceptionThrower.throwResourceException("Invalid midi resource was given:" + e.getMessage(), e);
			}
		}
	}
}
