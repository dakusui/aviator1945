package avis.spec;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import avis.base.ABaseResourceLoader;
import avis.base.AException;
import avis.base.AExceptionThrower;
import avis.base.AResourceObserver;
import avis.base.Avis;

public class ASpriteSpecManager extends ABaseResourceLoader {
	protected Map<String, ASpriteSpec> specMap = new HashMap<String, ASpriteSpec>();
	
	public ASpriteSpecManager(AResourceObserver observer) {
		super(observer);
	}

	public ASpriteSpec getSpriteSpec(String name) {
		return specMap.get(name);
	}
	
	public Iterator<ASpriteSpec> spriteSpecs() {
		return specMap.values().iterator();
	}
	
	public void unload(String name) {
		ASpriteSpec spec = specMap.get(name);
		spec.dispose();
		specMap.remove(name);
	}
	
    public ASpriteSpec loadSpriteSpec(String name, Class<? extends ASpriteSpec> clazz, Object resource) throws AException {
		ASpriteSpec spec = null;
		observer.startLoading(name);
		long before = System.currentTimeMillis();
		try {
			spec = clazz.newInstance();
			spec.init(resource);
			spec.name(name);
			if (specMap.containsKey(spec.name())) {
				unload(spec.name());
			}
			specMap.put(spec.name(), spec);
		} catch (InstantiationException e) {
			AExceptionThrower.throwSpriteSpecInstanciationException(clazz, resource, e);
		} catch (IllegalAccessException e) {
			AExceptionThrower.throwSpriteSpecInstanciationException(clazz, resource, e);
		}
		observer.endLoading(name);
    	Avis.logger().info("Loaded SpriteSpec:name:<" + spec.name() + ">, time=<" + (System.currentTimeMillis() - before) + ">");
		return spec;
    }

	public void loadResources() throws AException {
		// TODO Auto-generated method stub
		
	}
}
