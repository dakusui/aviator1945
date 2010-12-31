package oreactor.core;

import java.util.LinkedList;
import java.util.List;

import oreactor.exceptions.ArgumentException;
import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;

public class ArgParser {
	public static enum TestEnum {
		Hello, World;
	}
	
	private List<String> rest;

	private List<Tuple> tuples;

	static class Tuple {
		String key;
		String value;
		public String toString() {
			return "<" + this.key + ">=<" + this.value + ">";
		}
	}

	ArgParser(List<Tuple> tuples, List<String> rest) {
		this.tuples = tuples;
		this.rest = rest;
	}

	public List<Tuple> getRemainingTuples() {
		return tuples;
	}
	
	public List<String> getUnsupportedArguments() {
		return rest;
	}
	
	@SuppressWarnings("unchecked")
	public Enum<?> parseEnum(String keyword, Class<? extends Enum> k, Enum<?> defaultValue) throws ArgumentException {
		Enum<?> ret = null;
		if (ret == null) {
			ret = defaultValue;
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private Enum<?> pickupEnumValue(String keyword, Class<? extends Enum> k) throws ArgumentException {
		Enum<?> ret = null;
		for (Tuple t : this.tuples) {
			if (t.key.equals(keyword)) {
				try {
					ret = Enum.valueOf(k, t.value);
					this.tuples.remove(t);
					break;
				} catch (IllegalArgumentException e) {
					ExceptionThrower.throwArgumentException(keyword, t.value, k);
				}
			}
		}
		return ret;
	}

	public String pickUpValue(String keyword, String defaultValue) throws ArgumentException{
		String ret = defaultValue;
		for (Tuple t : this.tuples) {
			if (t.key.equals(keyword)) {
				this.tuples.remove(t);
				return t.value;
			}
		}
		return ret;
	}

	static Tuple split(String str) {
		if (null == str || !str.startsWith("--")) {
			return null;
		}
		String s = str.substring(2);
		Tuple ret = new Tuple();
		int i = s.indexOf('=');
		if (i < 0) {
			ret.key = s;
		} else {
			ret.key = s.substring(0, i);
			ret.value = s.substring(i + 1);
		}
		return ret;
	}
	
	public static void main(String[] args) throws Exception {
		List<Tuple> tuples = new LinkedList<Tuple>();
		List<String> rest = new LinkedList<String>();
		for (String i : args) {
			Tuple t = split(i);
			if (t == null) {
				rest.add(i);
			} else {
				tuples.add(t);
			}
		}
		
		ArgParser parser = new ArgParser(tuples, rest);
		System.out.println(parser.pickupEnumValue("key", TestEnum.class));
		System.out.println(parser.getRemainingTuples());
	}
	
	public static ArgParser createArgParser(String[] args) throws OpenReactorException {
		ArgParser ret = null;
		List<Tuple> tuples = new LinkedList<Tuple>();
		List<String> rest = new LinkedList<String>();
		for (String i : args) {
			Tuple t = split(i);
			if (t == null) {
				rest.add(i);
			} else {
				tuples.add(t);
			}
		}
		ret = new ArgParser(tuples, rest);
		return ret;
	}
}
