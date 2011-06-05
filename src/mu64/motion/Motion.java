package mu64.motion;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Per application basis
 * @author hiroshi
 *
 */
public abstract class Motion implements Cloneable {
	protected List<MMachine> collideWith = new LinkedList<MMachine>();
	protected List<MMachineSpec> emissions = new LinkedList<MMachineSpec>();
	private boolean destroyed;
	
	void addCollision(MMachine m) {
		this.collideWith.add(m);
	}
	
	public List<MMachine> collidesWith() {
		return Collections.unmodifiableList(this.collideWith);
	}
	
	public void addEmission(MMachineSpec spec) {
		if (spec != null) {
			emissions.add(spec);
		}
	}
	
	List<MMachineSpec> emissions() {
		return emissions;
	}
	
	public Motion cloneMotion() {
		try {
			Motion ret = (Motion) this.clone();
			return ret;
		} catch (CloneNotSupportedException e) {
			return null;
		}
	}
	
	public void destroy() {
		this.destroyed = true;
	}
	
	public boolean isDestroyed() {
		return this.destroyed;
	}
}