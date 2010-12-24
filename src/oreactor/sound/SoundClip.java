package oreactor.sound;

import oreactor.io.BaseResource;

public class SoundClip extends BaseResource {

	protected SoundClip(String name) {
		super(name);
	}

	@Override
	public Type type() {
		return Type.SoundClip;
	}
}
