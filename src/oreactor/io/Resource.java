package oreactor.io;

public interface Resource {
	public static enum Type {
		SpriteSpec, Pattern, Image, SoundClip, MusicClip, Misc
	}
	public String name();
	public Type type();
}
