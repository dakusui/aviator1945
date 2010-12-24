package oreactor.io;

import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import oreactor.music.MusicClip;
import oreactor.sound.SoundClip;
import oreactor.video.sprite.SpriteSpec;

import org.json.JSONObject;

public class ResourceLoader<T extends Resource> {
	private List<ResourceMonitor> monitors = new LinkedList<ResourceMonitor>();

	public void addMonitor(ResourceMonitor monitor) {
		this.monitors.add(monitor);
	}
	
	protected InputStream openUrl(String resourceName) {
		// see Avis.openUrl
		return null;
	}
	
	protected JSONObject loadJsonObject(String resourceName) {
		return null;
	}
	
	public SpriteSpec loadSpriteSpec(String resourceName) {
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
	
	protected Iterator<? extends Resource> resources(Resource.Type type) {
		return null;
	}
}
