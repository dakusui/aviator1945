package oreactor.core;

import oreactor.exceptions.OpenReactorException;

public abstract class BaseEngine extends BaseGear {
	final protected Settings settings;

	protected BaseEngine(Settings settings) {
		this.settings = settings;
	}
	
	public abstract void run() throws OpenReactorException;
}
