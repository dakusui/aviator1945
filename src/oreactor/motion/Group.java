package oreactor.motion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Per application basis
 * @author hiroshi
 *
 */
public abstract class Group implements List<MMachine> {
	List<MMachine> mmachines = new LinkedList<MMachine>();
	
	void register(MMachine m) {
		this.mmachines.add(m);
	}
	
	void unregister(MMachine m) {
		this.mmachines.remove(m);
	}
	
	public Iterator<MMachine> iterator() {
		return mmachines.iterator();
	}
	
	public abstract boolean doesInteract(Group another);
	public abstract boolean doesCollide(Group another);
}
