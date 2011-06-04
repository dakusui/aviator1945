package oreactor.motion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class MotionProvider implements List<MMachine> {
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
						exec_(m ,n, provider);
					}
				}
			} else {
				
			}
		}
	}
	List<Group> groups = new ArrayList<Group>();
	List<MMachine> machines = new LinkedList<MMachine>();

	List<MMachine> machines() {
		return Collections.unmodifiableList(this.machines);
	}
	public abstract MMachine createMMachine(MMachineSpec spec);
	public abstract double distance(MMachine m1, MMachine m2);
	public void performInteraction(InteractionMode mode) {
		for (Group g : groups) {
			for (Group h : groups.subList(groups.indexOf(g), groups.size())) {
				if (mode.doesInteract(g, h)) {
					mode.exec(g, h, this);
				}
			}
		}
	}
	
	void reset() {
		// resets cache for distance map
	}
	public static void main(String[] args) {
		List<Integer> l = new LinkedList<Integer>();
		l.add(1);
		l.add(2);
		l.add(3);
		System.out.println(l.subList(1, 3));
	}
}
