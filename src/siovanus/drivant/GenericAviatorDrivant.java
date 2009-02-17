package siovanus.drivant;


import siovanus.Siovanus;
import avis.base.Avis;

public class GenericAviatorDrivant extends AviatorDrivant {
	protected static enum Gun {
		RIGHT,
		LEFT
	}
	/**
	 * 次にショットの発射を行う銃を管理するメンバ
	 */
	protected Gun nextGun = Gun.RIGHT;
	
	public GenericAviatorDrivant() {
		super();
	}

	@Override
	public double gunDeltaX() {
		double sign = 1;
		if (nextGun == Gun.LEFT) {
			sign = -1;
		}
		return - sign * Avis.sin((int) yawDirection) * this.width * Avis.cos((int) this.bank)/2  + Avis.cos((int)yawDirection) * this.height*2;
	}

	@Override
	public double gunDeltaY() {
		double sign = 1;
		if (nextGun == Gun.LEFT) {
			sign = -1;
		}
		return sign * Avis.cos((int) yawDirection) * this.width * Avis.cos((int) this.bank)/2 + Avis.sin((int)yawDirection) * this.height*2;
	}

	@Override
	public double gunBias() {
		double ret = - Avis.cos((int) bank) ;
		if (nextGun == Gun.LEFT) {
			ret = -ret;
		}
		return ret;
	}
	
	public void shot() {
		if (shotInterval <= 0) {
			shotInterval = 5;
			if (nextGun == Gun.RIGHT) {
				nextGun = Gun.LEFT;
			} else {
				nextGun = Gun.RIGHT;
			}
			observer.emit(this, Siovanus.shotSpec);
		}
	}
	@Override
	public void bomb() {
		if (bombInterval <= 0) {
			bombInterval = 20;
			observer.emit(this, Siovanus.bombSpec);
		}	
	}
}
