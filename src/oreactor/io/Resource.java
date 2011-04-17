package oreactor.io;

import org.json.JSONObject;

import oreactor.exceptions.OpenReactorException;

public interface Resource {
	public static enum Type {
		SpriteSpec, Pattern, Image, SoundClip, MusicClip, Misc
	}
	public String name();
	public Type type();
	public void init(JSONObject json) throws OpenReactorException;
}
