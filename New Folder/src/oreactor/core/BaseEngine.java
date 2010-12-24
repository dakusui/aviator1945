package oreactor.core;

public abstract class BaseEngine extends BaseGear {
	final protected Settings settings;

	protected BaseEngine(Settings settings) {
		this.settings = settings;
	}
	
	public abstract void run();
}
