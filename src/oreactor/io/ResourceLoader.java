package oreactor.io;

import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import oreactor.core.Logger;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

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
	
	protected static ResourceLoader instance = null;

	protected ResourceLoader() {
	}
	
	public static ResourceLoader getResourceLoader(
			Class<? extends ResourceLoader> resourceLoaderClass) throws OpenReactorException {
		ResourceLoader ret = instance;
		synchronized (ResourceLoader.class) {
			if (ret == null) {
				try {
					ret  = resourceLoaderClass.newInstance();
				} catch (SecurityException e) {
					ExceptionThrower.throwResourceLoaderInstanciationException("Failed to instantiate resourceLoader:<" + resourceLoaderClass + ">", e);
				} catch (IllegalArgumentException e) {
					ExceptionThrower.throwResourceLoaderInstanciationException("Failed to instantiate resourceLoader:<" + resourceLoaderClass + ">", e);
				} catch (IllegalAccessException e) {
					ExceptionThrower.throwResourceLoaderInstanciationException("Failed to instantiate resourceLoader:<" + resourceLoaderClass + ">", e);
				} catch (InstantiationException e) {
					ExceptionThrower.throwResourceLoaderInstanciationException("Failed to instantiate resourceLoader:<" + resourceLoaderClass + ">", e);
				}
				instance = ret;
			}
		}
		return ret;
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
}
