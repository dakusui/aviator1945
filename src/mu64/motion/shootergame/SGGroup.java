/**
 * 
 */
package mu64.motion.shootergame;

import mu64.motion.Group;

public class SGGroup extends Group {
	public static final SGGroup FRIEND = new SGGroup("Friend");
	public static final SGGroup ENEMY = new SGGroup("Enemy");
	private SGGroup(String name) {
		super(name);
	}

	@Override
	public boolean doesCollide(Group another) {
		return !this.equals(another);
	}

	@Override
	public boolean doesInteract(Group another) {
		return false;
	}
}