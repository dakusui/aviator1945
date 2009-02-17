/**
 * 
 */
package siovanus;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import avis.motion.Group;

public class SGroup extends avis.motion.Group implements Comparable<SGroup> {
	public static final Group Enemy_Aerial   = new SGroup("Enemy_Aerial");
	public static final Group Player_Aerial   = new SGroup("Player_Aerial");
	public static final Group Enemy_Surface  = new SGroup("Enemy_Surface");
	public static final Group Player_Surface = new SGroup("Player_Surface");
	public SGroup(String tag) {
		super(tag);
	}
	public int compareTo(SGroup o) {
		return toString().compareTo(o.toString());
	}
	
	protected static class Pair {
		Group a;
		Group b;
		public Pair(Group a, Group b) {
			this.a = a;
			this.b = b;
		}
		public boolean equals(Group g1, Group g2) {
			if ((g1 == a && g2 == b) || (g1 == b && g2 == a)) {
				return true;
			}
			return false;
		}
	}
	
	private static List<Pair> collidablePairs;
	private static List<Pair> interactablePairs;
	static {
		collidablePairs = new LinkedList<Pair>();
		
		collidablePairs.add(new Pair(Player_Aerial,   Enemy_Aerial));
		collidablePairs.add(new Pair(Player_Surface, Enemy_Surface));

		interactablePairs = new LinkedList<Pair>();
		interactablePairs.add(new Pair(Player_Aerial,   Enemy_Aerial));
		interactablePairs.add(new Pair(Player_Aerial,   Enemy_Surface));
		interactablePairs.add(new Pair(Player_Surface,  Enemy_Aerial));
		interactablePairs.add(new Pair(Player_Surface,  Enemy_Surface));
	}

	private boolean checkReaction(Group another, List<Pair> reactionPairs) {
		Iterator<Pair> iReactablePairs = reactionPairs.iterator();
		while (iReactablePairs.hasNext()) {
			Pair cur = iReactablePairs.next(); 
			if (cur.equals(this, another)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean interacts(Group another) {
		return checkReaction(another, interactablePairs);
	}

	@Override
	public boolean collides(Group another) {
		return checkReaction(another, collidablePairs);
	}

}