package avis.sound;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import avis.base.AException;
import avis.base.AExceptionThrower;
import avis.base.Avis;


public class ADefaultSoundManager extends ASoundManager {
	static protected class PlayingClip {
		String name;
		DataClip data;
		SourceDataLine line;
		protected PlayingClip(String name, DataClip data, SourceDataLine line) {
			this.name = name;
			this.data = data;
			this.line = line;
		}
		public void feed(int duration) {
            // サンプルレートを計算する
            this.data.calculateSampleRate(duration);
            
            // 1フレームで送信するバイト数を計算する
            // 残りバイト数の方が小さい場合はそっちを選ぶ
            int bytes = Math.min(
            		this.data.sampleRate, 
            		this.data.data.length - this.data.index
            		);

            if (bytes > 0) {
                // ラインの再生バッファにbytesだけデータを書き込む
                // データを書き込むと再生される
                this.line.write(this.data.data, this.data.index, bytes);
                // 再生したバイト分だけindexをすすめる
                this.data.index += bytes;
            }
            
            // DataClipを全部再生したら停止
            if (this.data.index >= this.data.data.length) {
            	stop();
            }
		}
		public boolean isFinished() {
			return !this.data.running;
		}
		
		public void start() {
			this.data.index = 0;
			this.data.running = true;
			this.line.flush();
			this.line.start();
		}
		public void stop() {
			this.data.running = false;
			this.line.stop();
		}
	}
	
    // WAVEファイルデータ
    protected Map<String,DataClip> clips = new HashMap<String, DataClip>();
    // ライン（オーディオデータを再生する経路）
    protected List<SourceDataLine> availableLines = new LinkedList<SourceDataLine>();
    // Currently playing clips
    protected List<PlayingClip> playingClips = new LinkedList<PlayingClip>(); 

    protected long last = -1;

	public ADefaultSoundManager() throws SAudioException {
	}
	
	/**
	 * Prepares this object.
	 * Call <code>load</code> at least once before you call this method.
	 * @param maxVoices
	 * @throws SAudioException
	 */
	public void prepare(int maxVoices) throws SAudioException {
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
				Avis.logger().info("Sound: <" + i + ">=<" + newLine + ">");
		        // ラインを開く
				newLine.open(format);
				availableLines.add(newLine);
			}
		} catch (LineUnavailableException e) {
			AExceptionThrower.throwAudioLineWasUnavailable(format, e);
		}
	}
	
	public String load(String resourceName) throws AException {
        AudioInputStream ais = null;
        DataClip ret = null;
		try {
			ais = AudioSystem.getAudioInputStream(Avis.openUrl(resourceName));
			Avis.logger().info("Sound: format=<" + ais.getFormat() + ">");
	        // WAVEデータを取得
	        ret = new DataClip(ais);
	        // WAVEデータを登録
	        clips.put(resourceName, ret);
	        ais.close();
		} catch (UnsupportedAudioFileException e) {
			AExceptionThrower.throwAudioStreamWasNotAvailableException(resourceName, e);
		} catch (IOException e) {
			AExceptionThrower.throwAudioStreamWasNotAvailableException(resourceName, e);
		}
		return resourceName;
	}

	protected PlayingClip alloc(String key) {
		SourceDataLine line = null;
		PlayingClip ret = null;
		if (availableLines.size() > 0 && (line = this.availableLines.remove(0)) != null) {
			DataClip data = clips.get(key);
			if (data != null) {
				ret = new PlayingClip(key, data, line);
			}
		}
		Avis.logger().info("Sound: line=<" + line + ">");
		return ret;
	}
	
	/**
	 * Free the <code>clip</code> given.
	 * @param clip <code>clip</code> to free. if null, does nothing.
	 */
	protected void free(PlayingClip clip) {
		if (clip != null) {
			availableLines.add(clip.line);
        	playingClips.remove(clip);
		}
	}

	public void play(String resourceName) throws AException {
		if (!clips.containsKey(resourceName)) {
			return;
		}
		if (last == -1) {
			last = System.currentTimeMillis();
		}
		PlayingClip clip = alloc(resourceName);
		if (clip != null) {
			clip.start();
			playingClips.add(clip);
		}
	}
	
    /**
     * 再生
     */
    public void render() {
        long current = System.currentTimeMillis();
        // calculate the duration from the last time.
        int duration = (int)(current - last);
        for (PlayingClip currentClip: playingClips) {
        	currentClip.feed(duration);
        }
        cleanUp();
        last = current;
    }

	@Override
	protected Set<String> resources() {
		return clips.keySet();
	}

	@Override
	public void stop(String resourceName) {
		if (resourceName != null) {
			for (PlayingClip i: playingClips) { 
				if (resourceName.equals(i.name)) {
					i.stop();
					cleanUp();
					break;
				}
			}
		}
	}

	protected void cleanUp() {
        LinkedList<PlayingClip> toBeRemoved = new LinkedList<PlayingClip>();
        for (PlayingClip currentClip: playingClips) {
        	if (currentClip.isFinished()) {
        		toBeRemoved.add(currentClip);
        	}
        }
        for (PlayingClip currentClip: toBeRemoved) {
        	free(currentClip);
        }
        toBeRemoved = null;
    }

	@Override
	public void terminate() {
		stopAll();
		clips.clear();
	}

}
