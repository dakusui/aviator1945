package avis.base;


public abstract class ABaseResourceLoader implements AResourceLoader {
	protected AResourceObserver observer;
	
	protected ABaseResourceLoader(AResourceObserver observer) {
		this.observer = observer;
	}
}
