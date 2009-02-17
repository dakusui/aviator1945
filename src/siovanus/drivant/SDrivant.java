package siovanus.drivant;

import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.Parameters;

public abstract class SDrivant extends Drivant {
	
	public SDrivant() {
		super();
	}

	public void accelerate() {
		Parameters p = parameters;
		double aa = Math.max(accelerationCoefficient(), 0) * p.acceleration;
		p.velocity = p.velocity + aa;
		p.velocity = Math.min(p.velocity, p.maxV);
	}
	
	protected double accelerationCoefficient() {
		return 1.0;
	}
	
	public void decelerate() {
		Parameters p = parameters;
		p.velocity = p.velocity - p.acceleration;
		p.velocity = Math.max(p.velocity, p.minV);
	}

	public final void perform() {
		Parameters p = parameters;
		p.x += p.velocity * Avis.cos((int) p.direction);
		p.y += p.velocity * Avis.sin((int) p.direction);
		
		performDrivantAction();
		if (p.x > 6000 || p.x < -6000 || p.y > 4500 || p.y < -4500) {
			this.invalidate();
		}
	}
	abstract protected void performDrivantAction();

	public double distance(Drivant another) {
		double dx = (this.x() - another.x());
		double dy = (this.y() - another.y());
		double r = Math.abs(dx) + Math.abs(dy);
		return r;
	}
}
