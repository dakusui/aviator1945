package oreactor.music;

import org.json.JSONObject;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;
import oreactor.io.ResourceLoader;

public class MusicClip extends BaseResource {

	public MusicClip(String name) {
		super(name);
	}

	@Override
	public Type type() {
		return Type.MusicClip;
	}

	@Override
	public void init(JSONObject params, ResourceLoader loader) throws OpenReactorException {
		// TODO Auto-generated method stub
		
	}
}
