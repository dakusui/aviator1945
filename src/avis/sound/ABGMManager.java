package avis.sound;

import avis.base.AException;

public abstract class ABGMManager {
	public static ABGMManager NULL_BGM_MANAGER = new ABGMManager() {
		@Override
		public String load(String resourceName) throws AException {
			return resourceName;
		}

		@Override
		public void play(String resourceName) {
		}

		@Override
		public void stop() {
		}

		@Override
		public void resume() {
		}
		
	};

	public abstract String load(String resourceName) throws AException;

	public abstract void play(String resourceName);

	public abstract void stop();
	
	public abstract void resume();

}
