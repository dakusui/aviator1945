package oreactor.motion;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Per application basis
 * @author hiroshi
 *
 */
public abstract class Motion {
	List<MMachine> collideWith = new LinkedList<MMachine>();
	
	void collideWith(MMachine m) {
		this.collideWith.add(m);
	}
	
	public List<MMachine> collideWith() {
		return Collections.unmodifiableList(this.collideWith);
	}
}