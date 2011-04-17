package oreactor.io;

import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.music.MusicClip;
import oreactor.sound.SoundClip;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

import org.json.JSONException;
import org.json.JSONObject;

public class ResourceLoader {
	private List<ResourceMonitor> monitors = new LinkedList<ResourceMonitor>();

	protected static Class<? extends ResourceLoader> loaderClass = ResourceLoader.class;
	
	protected static ResourceLoader instance = null;
	
	protected ResourceLoader() {
	}
	
	public static ResourceLoader getResourceLoader() throws OpenReactorException {
		synchronized (ResourceLoader.class) {
			if (instance == null) {
				try {
					instance = loaderClass.newInstance();
				} catch (InstantiationException e) {
					ExceptionThrower.throwResourceLoaderInstanciationException(e.getMessage(), e);
				} catch (IllegalAccessException e) {
					ExceptionThrower.throwResourceLoaderInstanciationException(e.getMessage(), e);
				}
			}
		}
		ResourceLoader ret = instance;
		return ret;
	}
	

	public void addMonitor(ResourceMonitor monitor) {
		this.monitors.add(monitor);
	}
	
	protected InputStream openUrl(String resourceName) {
		// see Avis.openUrl
		InputStream ret = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
		return ret;
	}
	
	protected JSONObject loadJsonObject(String resourceName) throws OpenReactorException {
		JSONObject ret = null;
		try {
			BufferedReader r = new BufferedReader(new InputStreamReader(openUrl(resourceName)));
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
			ExceptionThrower.throwMalformatJsonException("Malformat JSON:<" + "> is given for resource:<" + resourceName + "> (" + e.getMessage() + ")", e);
		} catch (IOException e) {
			ExceptionThrower.throwIOException("Failed to load resource:<" + resourceName + ">", e);
		}
		return ret;
	}
	
	public Image loadImage(String resourceName) throws OpenReactorException {
		Image ret = null;
		InputStream is = openUrl(resourceName);
		try {
			try {
				ret =  ImageIO.read(is);
			} finally {
				is.close();
			}
		} catch (IOException e) {
			ExceptionThrower.throwIOException("Failed to load resource:<" + resourceName + ">", e);
		}
		return ret;
	}
	
	public Pattern loadPattern(String resourceName) {
		return null;
	}
	
	public SoundClip loadSound(String resourceName) {
		return null;
	}
	
	public MusicClip loadMusicClip(String resourceName) {
		return null;
	}

	
	protected void reset() {
		
	}
	
	protected void unload(Resource r) {
		
	}
	
	@SuppressWarnings("unchecked")
	public void loadConfig(String resourceName) throws OpenReactorException {
		JSONObject config = this.loadJsonObject(resourceName);
		try {
			{
				////
				// Loading sprite specs
				JSONObject spriteConfig = config.getJSONObject("spritespecs");
				Iterator<JSONObject> keys = spriteConfig.keys();
				while (keys.hasNext()) {
					String name = keys.next().toString();
					JSONObject v = spriteConfig.getJSONObject(name);
					SpriteSpec spec = new SpriteSpec(name);
					spec.loadRenderer(v.getString("renderer"));
					spec.width(v.getInt("hresolution"));
					spec.height(v.getInt("vresolution"));
					spec.init(v.getJSONObject("params"));
				}
			}
			{
				////
				// Loading patterns
				JSONObject patternConfig = config.getJSONObject("patterns");
				Iterator<JSONObject> keys = patternConfig.keys();
				while (keys.hasNext()) {
					String name = keys.next().toString();
					JSONObject v = patternConfig.getJSONObject(name);
					Pattern p = new Pattern(name);
					p.loadRenderer(v.getString("renderer"));
					p.init(v.getJSONObject("params"));
				}
			}
		} catch (JSONException e) {
			ExceptionThrower.throwMalformedConfigurationException("", null);
		}
	}
	
	public static void main01(String[] args) throws Exception {
		ResourceLoader loader = new ResourceLoader();
		BufferedReader r = new BufferedReader(new InputStreamReader(loader.openUrl("example-sprite-01/example-01.json")));
		String l = null;
		while ((l = r.readLine()) != null) {
			System.out.println(l);
		}
	}
	
	public static void main(String[] args) throws Exception {
		ResourceLoader loader = new ResourceLoader();
		JSONObject obj = loader.loadJsonObject("example-sprite-01/example-01.json");
		System.out.println(obj.get("class"));
		System.out.println(obj.get("width"));
		System.out.println(obj.get("height"));
		System.out.println(obj.get("images"));
	}
}
