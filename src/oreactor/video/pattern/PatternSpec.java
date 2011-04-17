package oreactor.video.pattern;

import org.json.JSONObject;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.BaseResource;

public final class PatternSpec extends BaseResource {

	protected PatternSpec(String name) {
		super(name);
	}

	@Override
	public Type type() {
		return Type.Pattern;
	}

	@Override
	public void load(JSONObject json) throws OpenReactorException {
		// TODO Auto-generated method stub
		
	}

}
