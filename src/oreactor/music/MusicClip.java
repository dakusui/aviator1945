package oreactor.music;

import org.json.JSONObject;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;

public class MusicClip extends BaseResource {

	public MusicClip(String name) {
		super(name);
	}

	@Override
	public Type type() {
		return Type.MusicClip;
	}

	@Override
	public void load(JSONObject params) throws OpenReactorException {
		// TODO Auto-generated method stub
		
	}
}
