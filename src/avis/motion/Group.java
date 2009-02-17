package avis.motion;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public abstract class Group {
	private static Set<Group> set = new TreeSet<Group>();
	private String tag;
	protected Group(String tag) {
		this.tag = tag;
		set.add(this);
	}
	public String toString() {
		return tag;
	}
	public static Iterator<Group> iterator() {
		return set.iterator();
	}
	
	@Override
	public boolean equals(Object another) {
		if (another instanceof Group) {
			return this.toString().equals(another.toString());
		}
		return false;
	}
	
	public boolean interacts(Group another) {
		return !this.equals(another);
	}
	public boolean collides(Group another) {
		return !this.equals(another);
	}
}
