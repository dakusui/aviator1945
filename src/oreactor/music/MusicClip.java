package oreactor.music;

import oreactor.io.BaseResource;

public class MusicClip extends BaseResource {

	public MusicClip(String name) {
		super(name);
	}

	@Override
	public Type type() {
		return Type.MusicClip;
	}
}
