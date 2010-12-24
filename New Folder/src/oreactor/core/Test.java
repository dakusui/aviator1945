package oreactor.core;

import org.json.JSONException;
import org.json.JSONObject;

public class Test {
	public static void main(String[] args) throws JSONException {
		JSONObject o = new JSONObject("{'hello':'world'}");
		System.out.println(o.get("hello"));
	}
}
