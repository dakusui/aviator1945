package siovanus.drivant;

import siovanus.Siovanus;
import avis.base.Avis;

public class VesselGunDrivant extends AviatorDrivant {

	/**
	 * 次にショットの発射を行う銃を管理するメンバ
	 */
	int nextGun = 0;
	double[] gunDelta   = new double[]{246, 0, 10};
	
	public VesselGunDrivant() {
		super();
	}

	@Override
	public double gunDeltaX() {
		int th = (int) ((gunDelta[nextGun] + direction) % Avis.DIRECTION_STEPS);
		return Avis.cos(th) * 100;
	}

	@Override
	public double gunDeltaY() {
		int th = (int) ((gunDelta[nextGun] + direction) % Avis.DIRECTION_STEPS);
		return Avis.sin(th) * 100;
	}

	@Override
	public double gunBias() {
		return 0;
	}
	
	public void shot() {
		if (shotInterval <= 0 && distanceToTarget < 400) {
			shotInterval = 30;
			nextGun++;
			nextGun = nextGun % gunDelta.length;
			observer.emit(this, Siovanus.bigShotSpec);
		}
	}
	@Override
	public void bomb() {
	}

}
