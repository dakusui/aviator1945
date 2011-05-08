package oreactor.core;

import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;

public abstract class BaseEngine {
	final protected Settings settings;
	ResourceLoader resourceLoader;

	protected BaseEngine(Settings settings) {
		this.settings = settings;
	}
	
	public void initialize(Context c) throws OpenReactorException {
		this.resourceLoader = c.getResourceLoader();
	}

	public void prepare() throws OpenReactorException {
	}
	
	public abstract void run() throws OpenReactorException;

	public void finish() throws OpenReactorException {
	}
	
	public void terminate(Context c) throws OpenReactorException {
	}
	
	protected ResourceLoader resourceLoader() {
		return this.resourceLoader;
	}
}
