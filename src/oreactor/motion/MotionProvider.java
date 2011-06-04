package oreactor.motion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class MotionProvider implements List<MMachine> {
	static class Tuple {
		MMachine a;
		MMachine b;

		Tuple(MMachine a, MMachine b) {
			this.a = a;
			this.b = b;
		}

		@Override
		public int hashCode() {
			return a.hashCode() + b.hashCode();
		}

		@Override
		public boolean equals(Object another) {
			if (another == null) {
				return false;
			}
			if (another instanceof Tuple) {
				return false;
			}
			Tuple anotherTuple = (Tuple) another;
			if (anotherTuple.b.equals(this.b) && anotherTuple.a.equals(a)) {
				return true;
			}
			if (anotherTuple.b.equals(this.a) && anotherTuple.a.equals(b)) {
				return true;
			}
			return false;
		}
	}

	static enum InteractionMode {
		Generic {
			@Override
			boolean doesInteract_(Group g, Group h) {
				return g.doesInteract(h);
			}

			@Override
			void exec_(MMachine m, MMachine n, MotionProvider provider) {
				double d = provider.distance(m, n);
				if (m.touches(n, d)) {
					m.performInteractionWith(n, d);
					n.performInteractionWith(m, d);
				}
			}
		},
		Collision {
			@Override
			boolean doesInteract_(Group g, Group h) {
				return g.doesCollide(h);
			}

			@Override
			void exec_(MMachine m, MMachine n, MotionProvider provider) {
				double d = provider.distance(m, n);
				if (m.touches(n, d)) {
					m.performCollisionWith(n, d);
					n.performCollisionWith(m, d);
				}
			}
		};
		abstract boolean doesInteract_(Group g, Group h);

		abstract void exec_(MMachine m, MMachine n, MotionProvider provider);

		boolean doesInteract(Group g, Group h) {
			boolean ret = doesInteract_(g, h);
			assert ret == doesInteract_(h, g) : "Inconsistent interaction implementation!";
			return ret;
		}

		public void exec(Group g, Group h, MotionProvider provider) {
			if (g != h) {
				for (MMachine m : g) {
					for (MMachine n : h) {
						exec_(m, n, provider);
					}
				}
			} else {

			}
		}
	}

	List<Group> groups = new ArrayList<Group>();
	List<MMachine> machines = new LinkedList<MMachine>();
	Map<Tuple, Double> distanceCache = new HashMap<Tuple, Double>();
	
	List<MMachine> machines() {
		return Collections.unmodifiableList(this.machines);
	}

	public double distance(MMachine m1, MMachine m2) {
		Tuple k = new Tuple(m1, m2);
		Double ret;
		if ((ret = distanceCache.get(k)) == null) {
			ret = new Double(calculateDistance(m1, m2));
			distanceCache.put(k, ret);
		}
		return ret.doubleValue();
	}
	
	protected abstract double calculateDistance(MMachine m1, MMachine m2);
	protected abstract Attributes createAttributes();
	protected abstract Motion createMotionObject();

	public void prepareAction() {
		List<MMachine> mmachines = this.machines();
		for (MMachine m : mmachines) {
			m.prepare();
		}
	}

	public void performAction() {
		List<MMachine> mmachines = this.machines();
		for (MMachine m : mmachines) {
			m.performAction(this);
		}
	}

	public void performInteraction(InteractionMode mode) {
		for (Group g : groups) {
			for (Group h : groups.subList(groups.indexOf(g), groups.size())) {
				if (mode.doesInteract(g, h)) {
					mode.exec(g, h, this);
				}
			}
		}
	}

	public void commit() {
		List<MMachine> mmachines = this.machines();
		for (MMachine m : mmachines) {
			m.commit();
		}
	}

	void reset() {
		distanceCache.clear();
	}

	public static void main(String[] args) {
		System.out.println(100 + Integer.MAX_VALUE);
	}

	protected abstract boolean isAcceptable(Drivant drivant);

	protected abstract boolean isAcceptable(Attributes attr);
}
