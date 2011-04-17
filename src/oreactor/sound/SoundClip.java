package oreactor.sound;

import org.json.JSONObject;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;

public class SoundClip extends BaseResource {

	protected SoundClip(String name) {
		super(name);
	}

	@Override
	public Type type() {
		return Type.SoundClip;
	}

	@Override
	public void load(JSONObject params) throws OpenReactorException {
		// TODO Auto-generated method stub
		
	}
}
