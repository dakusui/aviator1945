package oreactor.io;

public abstract class BaseResource implements Resource {
	private String name;

	protected BaseResource(String name) {
		this.name = name;
	}
	
	@Override
	public String name() {
		return this.name;
	}
}
