package openreactor.nu64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import oreactor.core.Reactor;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.io.ResourceMonitor;
import oreactor.video.pattern.Pattern;
import oreactor.video.sprite.SpriteSpec;

public class Nu64ResourceLoader extends ResourceLoader {

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
		} catch (JSONException e) {
			ExceptionThrower.throwMalformedConfigurationException(e.getMessage(), e);
		}
	}
}
