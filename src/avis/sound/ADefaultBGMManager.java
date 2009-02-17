package avis.sound;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import avis.base.AException;
import avis.base.AExceptionThrower;
import avis.base.Avis;


public class ADefaultBGMManager extends ABGMManager {
	/**
	 * A midi meta event to tell end of track.
	 */
	private static final int END_OF_TRACK_MESSAGE = 47;

    /**
     * A midi sequences to play.
     */
    private Map<String, Sequence> sequences = new HashMap<String, Sequence>();

    /**
     * A midi sequencer object.
     */
    private Sequencer sequencer;

	private Sequence playingSequence;

	private long stoppedPosition;

    /**
     * Start point of a midi sequence.
     */
    private long startTick = 0;

    public ADefaultBGMManager() {
	}

    /**
     * 指定されたリソース名のBGMを、このオブジェクトで使用できるようにロードする。
     * リソース名には、そのBGMを配置したURLを指定する。
     * ロード成功時には、与えられたリソース名を、そのBGMを演奏時に指定するためのハンドルを返却する。
     * @param resourceName ロードするBGMリソース名
     * @return BGMを特定するためのハンドル
     * @throws AException 処理時に異常が発生した
     */
    @Override
	public String load(String resourceName) throws AException {
		String ret = null;
		try {
			InputStream is = Avis.openUrl(resourceName);
			try {
				if (sequencer == null) {
		            // get a midi sequencer. 
		        	sequencer = MidiSystem.getSequencer();
		        	// open the sequencer
		            sequencer.open();
		            // register a meta event listener to the sequencer.
		            sequencer.addMetaEventListener(new MyMetaEventListener(sequencer, this.startTick));
		        }
		        // register the sequence to the internal table.
		        sequences.put(ret = resourceName, MidiSystem.getSequence(is));
			} finally {
				Avis.closeStream(is);
			}
		} catch (MidiUnavailableException e) {
			AExceptionThrower.throwFailedToLoadMidiResource(resourceName, e);
		} catch (InvalidMidiDataException e) {
			AExceptionThrower.throwFailedToLoadMidiResource(resourceName, e);
		} catch (IOException e) {
			AExceptionThrower.throwFailedToLoadMidiResource(resourceName, e);
		}
		return ret;
	}
	
    @Override
	public void play(String resourceName) {
		Sequence seq = null;
		stoppedPosition = -1;
		if ((seq = sequences.get(resourceName)) == null) {
            return;
        }
        
        // does nothing if it is being played.
        if (playingSequence == seq) {
            return;
        }

        // stop current sequence if another sequence is given.
        stop();

        try {
            // set the sequence to the sequencer.
            sequencer.setSequence(seq);
            playingSequence = seq;
            // start playing
            sequencer.start();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
	}

    @Override
    public void stop() {
        if (sequencer.isRunning()) {
        	stoppedPosition = sequencer.getTickPosition();
            sequencer.stop();
        }
    }

    /**
     * Event listener to loop back 
     * @author hukai
     */
    private static class MyMetaEventListener implements MetaEventListener {
        private Sequencer sequencer;
		private long startTick;

		public MyMetaEventListener(Sequencer sequencer, long startTick) {
			this.sequencer = sequencer;
			this.startTick = startTick;
		}

		public void meta(MetaMessage meta) {
            if (meta.getType() == END_OF_TRACK_MESSAGE) {
                if (sequencer != null && sequencer.isOpen()) {
                    // rewind the sequencer
                    sequencer.setMicrosecondPosition(startTick);
                    // play it again
                    sequencer.start();
                }
            }
        } 
    }

	@Override
	public void resume() {
		Sequence seq = playingSequence;
		if (playingSequence != null) {
	        try {
	            sequencer.setSequence(seq);
	            // start playing
	            sequencer.setTickPosition(stoppedPosition != -1 ? stoppedPosition
	            		                                      : startTick);
	            sequencer.start();
	        } catch (InvalidMidiDataException e) {
	            e.printStackTrace();
	        }
		}
    }

}
