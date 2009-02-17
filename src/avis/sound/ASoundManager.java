package avis.sound;

import java.util.Set;
import java.util.TreeSet;

import avis.base.AException;

public abstract class ASoundManager {
	public static final ASoundManager NULL_SOUND_MANAGER = new ASoundManager() {
		final Set<String> dummy = new TreeSet<String>();
		@Override
		public String load(String resourceName) throws AException {
			return resourceName;
		}

		@Override
		public void play(String resourceName) throws AException {
		}

		@Override
		public void prepare(int maxVoices) throws SAudioException {
		}

		@Override
		public void render() {
		}

		@Override
		public void stop(String resourceName) {
		}

		@Override
		protected Set<String> resources() {
			return dummy;
		}

		@Override
		public void terminate() {
		}
		
	};
	public abstract void prepare(int maxVoices) throws SAudioException;
	public abstract String load(String resourceName) throws AException;
	public abstract void play(String resourceName) throws AException;
    public abstract void render();
	public abstract void stop(String resourceName);
	protected abstract Set<String> resources();
	public void stopAll() {
		Set<String> res = resources();
		for (String i: res) {
			stop(i);
		}
		return;
	}

	public abstract void terminate();
}
