package oreactor.io;

public interface ResourceMonitor {
	public void startLoading(String name, Resource.Type type);
	public void endLoading(String name, Resource.Type type);
	public void unload(String name, Resource.Type type);
}
