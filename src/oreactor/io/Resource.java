package oreactor.io;

import org.json.JSONObject;

import oreactor.exceptions.OpenReactorException;

public interface Resource {
	public static enum Type {
		SpriteSpec, Pattern, Image, SoundClip, MusicClip, Misc
	}
	public Type type();
	public void init(JSONObject json, ResourceLoader loader) throws OpenReactorException;
	public void release() throws OpenReactorException;
}
