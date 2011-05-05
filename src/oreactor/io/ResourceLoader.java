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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResourceLoader {
	protected static Class<? extends ResourceLoader> loaderClass = ResourceLoader.class;

	public static ResourceLoader getResourceLoader() throws OpenReactorException {
		ResourceLoader ret = null;
		synchronized (ResourceLoader.class) {
			try {
				ret = loaderClass.newInstance();
			} catch (InstantiationException e) {
				ExceptionThrower.throwResourceLoaderInstanciationException(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				ExceptionThrower.throwResourceLoaderInstanciationException(e.getMessage(), e);
			}
		}
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
		ResourceLoader loader = new ResourceLoader();
		JSONObject obj = loader.loadJsonObjectFromUrl("example-sprite-01/example-01.json");
		System.out.println(obj.get("class"));
		System.out.println(obj.get("width"));
		System.out.println(obj.get("height"));
		System.out.println(obj.get("images"));
	}
	
	public static void main01(String[] args) throws Exception {
		ResourceLoader loader = new ResourceLoader();
		BufferedReader r = new BufferedReader(new InputStreamReader(loader.openUrl("example-sprite-01/example-01.json")));
		String l = null;
		while ((l = r.readLine()) != null) {
			System.out.println(l);
		}
	}
	

	private List<ResourceMonitor> monitors = new LinkedList<ResourceMonitor>();
	
	protected ResourceLoader() {
	}
	
	public void addMonitor(ResourceMonitor monitor) {
		this.monitors.add(monitor);
	}
	
	public void loadConfigFromString(String s) throws OpenReactorException {
		JSONObject config = this.loadJsonObjectFromString(s);
		parseJsonObject(config);
	}
	
	public void loadConfigFromUrl(String resourceName) throws OpenReactorException {
		JSONObject config = this.loadJsonObjectFromUrl(resourceName);
		parseJsonObject(config);
	}
	
	public Image loadImage(String resourceName) throws OpenReactorException {
		Image ret = null;
		try {
			InputStream is = openUrl(resourceName);
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
	
	protected JSONObject loadJsonObjectFromString(String s) throws OpenReactorException {
		JSONObject ret = null;
		try {
			ret = new JSONObject(s);
		} catch (JSONException e) {
			ExceptionThrower.throwMalformatJsonException("Malformat JSON:<" + "> is given:<" + s + "> (" + e.getMessage() + ")", e);
		}
		return ret;
	}
	
	protected JSONObject loadJsonObjectFromUrl(String resourceName) throws OpenReactorException {
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

	public MusicClip loadMusicClip(String resourceName) {
		// TODO
		return null;
	}

	public SoundClip loadSound(String resourceName) {
		return null;
	}
	
	protected InputStream openUrl(String resourceName) throws OpenReactorException {
		// see Avis.openUrl
		InputStream ret = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceName);
		if (ret == null) {
			ExceptionThrower.throwResourceNotFoundException(resourceName, null);
		}
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private void parseJsonObject(JSONObject config) throws OpenReactorException {
		try {
			JSONObject spriteConfig = config.getJSONObject("spritespecs");
			int numSpriteSpecs = spriteConfig.length();
			for (ResourceMonitor m: monitors) {
				m.numSpriteSpecs(numSpriteSpecs);
			}
			JSONArray patternConfig = config.getJSONArray("patterns");
			int numPatterns = patternConfig.length();
			for (ResourceMonitor m: monitors) {
				m.numPatterns(numPatterns);
			}
			{
				////
				// Loading sprite specs
				Iterator<String> keys = spriteConfig.keys();
				while (keys.hasNext()) {
					String cur = keys.next();
					String name = cur.toString();
					JSONObject v = spriteConfig.getJSONObject(name);
					SpriteSpec spec = new SpriteSpec(name);
					spec.loadRenderer(v.getString("renderer"));
					spec.width(v.getInt("hresolution"));
					spec.height(v.getInt("vresolution"));
					spec.init(v.getJSONObject("params"), this);
					for (ResourceMonitor m: monitors) {
						m.spriteSpecLoaded(spec);
					}
				}
			}
			{
				////
				// Loading patterns
				for (int i = 0; i < numPatterns; i ++) {
					JSONObject v = patternConfig.getJSONObject(i);
					Pattern p = new Pattern(i);
					p.init(v, this);
					for (ResourceMonitor m: monitors) {
						m.patternLoaded(p);
					}
				}
			}
		} catch (JSONException e) {
			ExceptionThrower.throwMalformedConfigurationException(e.getMessage(), e);
		}
	}
}
