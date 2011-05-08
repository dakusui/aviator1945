package oreactor.sound;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.SourceDataLine;

import oreactor.io.ResourceLoader.SoundData;

public class SoundPlayer {
	static enum State {
		NOP {
		},
		Prepared {
			public void start(SoundPlayer soundPlayer) {
				soundPlayer.cur = 0;
				soundPlayer.line.flush();
				soundPlayer.line.start();
			}
		},
		Playing {
			public State feed(SoundPlayer soundPlayer, long millisec) {
				State ret = this;
				byte[] b = soundPlayer.data.bytes();
				int off = soundPlayer.cur;
				int len = (int) Math.min(soundPlayer.bytesPerMillisec() * millisec, b.length - off);
				soundPlayer.line.write(b, off, len);
				soundPlayer.cur += len;
				if (soundPlayer.cur >= b.length) {
					stop(soundPlayer);
					ret = Finished;
				}
				return ret;
			}

			public void stop(SoundPlayer soundPlayer) {
				soundPlayer.line.stop();		
				soundPlayer.manager.release(soundPlayer);
			}
		},
		Finished {
			
		},
		;
		public State feed(SoundPlayer soundPlayer, long millisec) {
			return this;
		}

		public void stop(SoundPlayer soundPlayer) {
		}

		public void start(SoundPlayer soundPlayer) {
		}
		
		public boolean isPlaying() {
			return this == Playing;
		}
	}
	private int cur = 0;
	SoundData data = null;
	SourceDataLine line = null;
	private SoundEngine manager;
	private State state;
	
	SoundPlayer(SoundData data, SoundEngine manager) {
		this.data = data;
		this.line = manager.getLine();
		this.manager = manager;
		this.state = this.line != null ? State.Prepared : State.NOP;
	}
	
	public int bytesPerMillisec() {
		AudioFormat format = data.format();
		return (int)((format.getChannels() * format.getSampleRate() * format.getSampleSizeInBits() / 8) / 1000);
	}

	public void start() {
		state.start(this);
	}

	public void stop() {
		state.stop(this);
	}

	public void feed(long millisec) {
		state = state.feed(this, millisec);
	}
	
	public boolean isPlaying() {
		return state.isPlaying();
	}

	public SourceDataLine line() {
		return this.line;
	}
}
