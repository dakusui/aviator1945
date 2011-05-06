package oreactor.core;

import oreactor.exceptions.OpenReactorException;

public abstract class BaseEngine {
	final protected Settings settings;

	protected BaseEngine(Settings settings) {
		this.settings = settings;
	}
	
	public void initialize(Context c) throws OpenReactorException {
	}

	public void prepare() throws OpenReactorException {
	}
	
	public abstract void run() throws OpenReactorException;

	public void finish() throws OpenReactorException {
	}
	
	public void terminate(Context c) throws OpenReactorException {
	}
}
