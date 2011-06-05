/**
 * 
 */
package mu64.motion.shootergame;

import mu64.motion.Attributes;
import mu64.motion.Drivant;
import mu64.motion.Group;
import mu64.motion.MMachine;
import mu64.motion.Motion;
import mu64.motion.MotionProvider;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.sprite.Sprite;

public class SGMotionProvider extends MotionProvider {
	public SGMotionProvider() throws OpenReactorException {
		this.addGroup(SGGroup.FRIEND);
		this.addGroup(SGGroup.ENEMY);
	}
	@Override
	protected double calculateDistance(MMachine m1, MMachine m2) {
		SGAttrs m1a = (SGAttrs) m1.attributes();
		SGAttrs m2a = (SGAttrs) m2.attributes();
		double ret = 0;
		double dx = m2a.x - m1a.x;
		double dy = m2a.y - m1a.y;
		ret = Math.sqrt(dx*dx + dy * dy);
		return ret;
	}

	@Override
	protected Attributes createAttributes() {
		return new SGAttrs();
	}

	@Override
	protected Motion createMotionObject() {
		return new SGMotion();
	}

	@Override
	protected boolean isAcceptable(Drivant drivant) {
		return drivant instanceof SGBaseDrivant;
	}

	@Override
	protected boolean isAcceptable(Attributes attrs) {
		return attrs instanceof SGAttrs;
	}

	@Override
	protected boolean isAcceptable(Group group) {
		return true;
	}

	@Override
	protected void putStrite(Sprite sprite, Attributes attributes) {
		SGAttrs attrs = (SGAttrs) attributes;
		sprite.put(attrs.x(), attrs.y(), attrs.direction());
	}

	@Override
	protected boolean touches(MMachine m, MMachine n, double d) {
		if (d < 32) {
			return true;
		}
		return false;
	}
}