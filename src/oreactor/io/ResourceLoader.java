package oreactor.io;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oreactor.core.Logger;
import oreactor.core.Reactor;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

public class ResourceLoader {
	public static abstract class Data {
		public static enum Type {
			Image {
				@Override
				Class<? extends Data> dataClass() {
					return ImageData.class;
				}
			}, 
			Midi {
				@Override
				Class<? extends Data> dataClass() {
					return MidiData.class;
				}
			}, 
			Sound {
				@Override
				Class<? extends Data> dataClass() {
					return SoundData.class;
				}
			}, 
			Raw {
				@Override
				Class<? extends Data> dataClass() {
					return RawData.class;
				}
			};
			Data create(String resourceUrl) throws OpenReactorException {
				Data ret = null;
				try {
					ret = dataClass().getConstructor(String.class).newInstance(resourceUrl);
				} catch (IllegalArgumentException e) {
					ExceptionThrower.throwResourceException("Failed to load resource:<" + resourceUrl + ">", e);
				} catch (SecurityException e) {
					ExceptionThrower.throwResourceException("Failed to load resource:<" + resourceUrl + ">", e);
				} catch (InstantiationException e) {
					ExceptionThrower.throwResourceException("Failed to load resource:<" + resourceUrl + ">", e);
				} catch (IllegalAccessException e) {
					ExceptionThrower.throwResourceException("Failed to load resource:<" + resourceUrl + ">", e);
				} catch (InvocationTargetException e) {
					ExceptionThrower.throwResourceException("Failed to load resource:<" + resourceUrl + ">", e);
				} catch (NoSuchMethodException e) {
					ExceptionThrower.throwResourceException("Failed to load resource:<" + resourceUrl + ">", e);
				}
				return ret;
			}
			boolean isCompatible(Data d) {
				return dataClass().isAssignableFrom(d.getClass());
			}
			abstract Class<? extends Data> dataClass();
		}
	
		String resourceUrl;
		boolean initialized = false;
		private int refcount;
		
		public Data(String resourceUrl) throws OpenReactorException {
			this.resourceUrl = resourceUrl;
		}
		
		public void init() throws OpenReactorException {
			if (initialized) {
				return;
			}
			InputStream is = openUrl(resourceUrl);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(8192);
			byte[] b = new byte[8192];
			try {
				try {
					while (is.read(b) != -1) {
						baos.write(b);
					}
					this._init(baos.toByteArray());
				} finally {
					is.close();
				}
			} catch (IOException e) {
				ExceptionThrower.throwIOException("Failed to load data from <" + resourceUrl + ">", e);
			}
			this.initialized = true;
		}
	
		protected abstract void _init(byte[] byteArray) throws OpenReactorException;
		
		protected InputStream openUrl(String url) throws OpenReactorException {
			InputStream ret = ClassLoader.getSystemClassLoader().getResourceAsStream(url);
			if (ret == null) {
				ExceptionThrower.throwResourceNotFoundException(url, null);
			}
			return ret;
		}
		
		public abstract InputStream inputStream() throws OpenReactorException;
			
		public int release() throws OpenReactorException {
			this.refcount--;
			if (this.refcount <= 0) {
				this._release();
			}
			return this.refcount;
		}
		
		protected abstract void _release();
	
		public String resourceUrl() {
			return this.resourceUrl;
		}
		
		@Override
		public int hashCode() {
			return this.resourceUrl.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if (o instanceof Data) {
				return this.resourceUrl.equals(((Data)o).resourceUrl);
			}
			return false;
		}
		
		public void inc() {
			this.refcount++;
		}
	}

	public static class RawData extends Data {
		byte[] buf;
	
		public RawData(String resourceUrl) throws OpenReactorException {
			super(resourceUrl);
		}
	
		protected void _init(byte[] byteArray) throws OpenReactorException {
			this.buf = byteArray;
		}
		
		public InputStream inputStream() {
			ByteArrayInputStream ret = new ByteArrayInputStream(buf);
			return ret;
		}
		
		
		protected void _release() {
			this.buf = null;
		}
	
		byte[] rawData() {
			return this.buf;
		}
	}

	public static class SoundData extends RawData {
		AudioFormat format = null;
		
		public SoundData(String resourceUrl) throws OpenReactorException {
			super(resourceUrl);
		}
	
		@Override
		protected InputStream openUrl(String resourceUrl) throws OpenReactorException {
			AudioInputStream ret = null;
			try {
				ret = AudioSystem.getAudioInputStream(super.openUrl(resourceUrl()));
			} catch (UnsupportedAudioFileException e) {
				ExceptionThrower.throwResourceException("Failed to load audio resource:<" + resourceUrl + ">(" + e.getMessage() + ">", e);
			} catch (IOException e) {
				ExceptionThrower.throwResourceException("Failed to load audio resource:<" + resourceUrl + ">(" + e.getMessage() + ">", e);
			}
			this.format = ret.getFormat(); 
			return ret;
		}

		public byte[] bytes() {
			return rawData();
		}

		public AudioFormat format() {
			return this.format;
		}
	}

	public static class ImageData extends Data {
		Image image = null;
		public ImageData(String resourceUrl) throws OpenReactorException {
			super(resourceUrl);
		}
		
		@Override
		public void _init(byte[] byteArray) throws OpenReactorException { 
			try {
				this.image = ImageIO.read(new ByteArrayInputStream(byteArray));
			} catch (IOException e) {
				ExceptionThrower.throwResourceException("Failed to load image resource:<" + resourceUrl + ">(" + e.getMessage() + ">", e);
			}
		}
		
		protected void _release() {
			this.image = null;
		}
	
		@Override
		public InputStream inputStream() throws OpenReactorException { 
			ExceptionThrower.throwResourceException("This method:<inputStream()> is not available for this object:<" + this + ">.");
			return null;
		}

		public Image image() {
			return this.image;
		}
	}
	
	public static class MidiData extends Data {
		Sequence seq;
		public MidiData(String resourceUrl) throws OpenReactorException {
			super(resourceUrl);
		}

		@Override
		public void _init(byte[] byteArray) throws OpenReactorException { 
			try {
				this.seq = MidiSystem.getSequence(new ByteArrayInputStream(byteArray));
			} catch (IOException e) {
				ExceptionThrower.throwResourceException("Failed to load midi resource:<" + resourceUrl + ">(" + e.getMessage() + ">", e);
			} catch (InvalidMidiDataException e) {
				ExceptionThrower.throwResourceException("Failed to load midi resource:<" + resourceUrl + ">(" + e.getMessage() + ">", e);
			}
		}
		
		protected void _release() {
			this.seq = null;
		}
	
		@Override
		public InputStream inputStream() throws OpenReactorException { 
			ExceptionThrower.throwResourceException("This method:<inputStream()> is not available for this object:<" + this + ">.");
			return null;
		}

		public  Sequence sequence() {
			return this.seq;
		}
	}
	
	protected static Logger logger = Logger.getLogger();

	private Map<String, Data> dataMap = new HashMap<String, Data>();

	private Reactor reactor;
	
	public Reactor reactor() {
		return this.reactor;
	}
	
	public static ResourceLoader getResourceLoader(
			Reactor reactor) throws OpenReactorException {
		ResourceLoader ret = null;
		ret  = new ResourceLoader();
		ret.reactor(reactor);
		return ret;
	}

	private void reactor(Reactor reactor) {
		this.reactor = reactor;
	}

	public Data get(String resourceUrl, Data.Type type) throws OpenReactorException {
		Data ret = null;
		if (resourceUrl == null) {
			ExceptionThrower.throwParameterException("'resourceUrl' must not be null.");
		}
		if (type == null) {
			ExceptionThrower.throwParameterException("'type' must not be null.");
		}
		if ((ret = this.dataMap.get(resourceUrl)) == null) {
			ret = type.create(resourceUrl);
			ret.init();
			this.dataMap.put(resourceUrl, ret);
		}
		if (ret != null) {
			if (type.isCompatible(ret)) {
				ret.inc();
			} else {
				ExceptionThrower.throwResourceException("Requested resource:<" + resourceUrl + "> is not compatible with <" + type + ">");
			}
		}
		return ret;
	}
	
	public RawData getRaw(String resourceUrl) throws OpenReactorException {
		return (RawData) get(resourceUrl, Data.Type.Raw);
	}

	public SoundData getSound(String resourceUrl) throws OpenReactorException {
		return (SoundData) get(resourceUrl, Data.Type.Sound);
	}

	public MidiData getMidi(String resourceUrl) throws OpenReactorException {
		return (MidiData) get(resourceUrl, Data.Type.Midi);
	}
	
	public ImageData getImage(String resourceUrl) throws OpenReactorException {
		return (ImageData) get(resourceUrl, Data.Type.Image);
	}
	
	void release(String resourceUrl) throws OpenReactorException {
		Data d = this.dataMap.get(resourceUrl);
		if (d == null) {
			ExceptionThrower.throwResourceException("Specified resource is not managed.:<" + resourceUrl + ">");
		}
		if (d.release() <= 0) {
			this.dataMap.remove(resourceUrl);
		}
	}
	
	public void reset() {
		this.dataMap.clear();
	}
	
	////
	private List<ResourceMonitor> monitors = new LinkedList<ResourceMonitor>();
	
	public void addMonitor(ResourceMonitor monitor) {
		this.monitors.add(monitor);
	}
	
	public void removeMonitor(ResourceMonitor monitor) {
		this.monitors.remove(monitor);
	}
	
	public List<ResourceMonitor> monitors() {
		return Collections.unmodifiableList(this.monitors);
	}
	
	public void loadConfigFromString(String s) throws OpenReactorException {
		JSONObject config = this.loadJsonObjectFromString(s);
		parseJsonObject(config);
	}
	
	public void loadConfigFromUrl(String resourceName) throws OpenReactorException {
		JSONObject config = this.loadJsonObjectFromUrl(resourceName);
		parseJsonObject(config);
	}
	
	protected JSONObject loadJsonObjectFromString(String s) throws OpenReactorException {
		JSONObject ret = null;
		try {
			ret = new JSONObject(s);
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException("Malformat JSON string is given:<" + s + "> (" + e.getMessage() + ")", e);
		}
		return ret;
	}
	
	/**
	 * Note that the corresponding data read from url specified by <code>resourceUrl</code> will not be
	 * freed until <code>reset<code> method is called on this object.
	 * @param resourceUrl url from which a json object should be read.
	 * @return A <code>JSONObject</code> retrieved from the url.
	 * @throws OpenReactorException Failed to load a json object from the url.
	 */
	protected JSONObject loadJsonObjectFromUrl(String resourceUrl) throws OpenReactorException {
		JSONObject ret = null;
		try {
			Data d = get(resourceUrl, Data.Type.Raw);
			BufferedReader r = new BufferedReader(new InputStreamReader(d.inputStream()));
			try {
				String s = null;
				String l = null;
				StringBuffer b = new StringBuffer(1024);
				while ((l = r.readLine()) != null) {
					b.append(l);
				}
				s = b.toString();
				ret = new JSONObject(s);
			} finally {
				r.close();
			}
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException("Malformat JSON:<" + "> is given for resource:<" + resourceUrl + "> (" + e.getMessage() + ")", e);
		} catch (IOException e) {
			ExceptionThrower.throwIOException("Failed to load resource:<" + resourceUrl + ">", e);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private void parseJsonObject(JSONObject config) throws OpenReactorException {
		try {
			JSONObject spriteConfig = config.getJSONObject("spritespecs");
			int numSpriteSpecs = spriteConfig.length();
			for (ResourceMonitor m: monitors()) {
				m.numSpriteSpecs(numSpriteSpecs);
			}
			JSONArray patternConfig = config.getJSONArray("patterns");
			int numPatterns = patternConfig.length();
			for (ResourceMonitor m: monitors()) {
				m.numPatterns(numPatterns);
			}
			JSONObject soundClipConfig = config.getJSONObject("soundclips");
			int numSoundClips = soundClipConfig.length();
			for (ResourceMonitor m: monitors()) {
				m.numSoundClips(numSoundClips);
			}
			JSONObject midiClipConfig = config.getJSONObject("midiclips");
			int numMidiClips = midiClipConfig.length();
			for (ResourceMonitor m: monitors()) {
				m.numMidiClips(numMidiClips);
			}
			{
				////
				// Loading sprite specs
				Iterator<String> keys = spriteConfig.keys();
				while (keys.hasNext()) {
					String cur = keys.next();
					String name = cur.toString();
					JSONObject v = spriteConfig.getJSONObject(name);
					SpriteSpec spec = SpriteSpec.loadSpec(name, v.getString("class"));
					spec.width(v.getInt("hresolution"));
					spec.height(v.getInt("vresolution"));
					spec.init(v.getJSONObject("params"), this);
					for (ResourceMonitor m: monitors()) {
						m.spriteSpecLoaded(spec);
					}
				}
			}
			{
				////
				// Loading patterns
				Reactor r = reactor();
				for (int i = 0; i < numPatterns; i ++) {
					JSONObject v = patternConfig.getJSONObject(i);
					Pattern p = new Pattern(i, r.patternWidth(), r.patternHeight());
					p.init(v, this);
					for (ResourceMonitor m: monitors()) {
						m.patternLoaded(p);
					}
				}
			}
			{
				////
				// Loading sound clips
				Iterator<String> keys = soundClipConfig.keys();
				while (keys.hasNext()) {
					String cur = keys.next();
					String name = cur.toString();
					JSONObject v = soundClipConfig.getJSONObject(name);
					SoundData soundData = this.getSound(v.getString("wave"));
					for (ResourceMonitor m: monitors()) {
						m.soundClipLoaded(name, soundData);
					}
				}
			}
			{
				////
				// Loading midi clips
				Iterator<String> keys = midiClipConfig.keys();
				while (keys.hasNext()) {
					String cur = keys.next();
					String name = cur.toString();
					JSONObject v = midiClipConfig.getJSONObject(name);
					MidiData soundData = this.getMidi(v.getString("midi"));
					for (ResourceMonitor m: monitors()) {
						m.midiClipLoaded(name, soundData);
					}
				}
			}
		} catch (JSONException e) {
			ExceptionThrower.throwMalformedConfigurationException(e.getMessage(), e);
		}
	}
}
