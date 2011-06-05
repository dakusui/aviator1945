package mu64.motion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Per application basis
 * @author hiroshi
 *
 */
public abstract class Group implements Iterable<MMachine> {
	List<MMachine> mmachines = new LinkedList<MMachine>();
	private String name;
	
	protected Group(String name) {
		this.name = name;
	}
	
	void register(MMachine m) {
		this.mmachines.add(m);
	}
	
	void unregister(MMachine m) {
		this.mmachines.remove(m);
	}
	
	public Iterator<MMachine> iterator() {
		return mmachines.iterator();
	}
	
	public String name() {
		return this.name;
	}
	
	public abstract boolean doesInteract(Group another);
	public abstract boolean doesCollide(Group another);
	
	public String toString() {
		return this.getClass().getSimpleName() + ":<" + this.name + ">";
	}
}
