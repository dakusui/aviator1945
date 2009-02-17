package siovanus.drivant;

import avis.motion.Drivant;
import avis.motion.Parameters;


/**
 * 発生後比較的短い時間で消滅し、概ね慣性の法則だけにしたがって運動する物体を表現する<code>Drivant</code>。 
 * @author hukai
 */
public abstract class VolatileDrivant extends SDrivant {
	/**
	 * <code>VolatileDrivant</code>オブジェクトの初期パラメタを表現するクラス。
	 * @author hukai
	 */
	public static abstract class VolatileParameters extends Parameters {
		/**
		 * シリアル・バージョン
		 */
		private static final long serialVersionUID = 3335836896502989796L;
		/**
		 * 消滅までの残存時間。
		 */
		public int lifeTime;
		public Drivant parent;
	}

	protected Drivant parent;
	
	protected int initialLifeTime;

	protected int lifeTime;
	
	public VolatileDrivant() {
	}

	@Override
	protected void copyParameters_Protected(Parameters parameters) {
		VolatileParameters p = (VolatileParameters) parameters;
		copyVolatileParameters_Protected(p);
		this.lifeTime = p.lifeTime;
		this.parent = p.parent;
	}

	abstract protected void copyVolatileParameters_Protected(VolatileParameters p);

	@Override
	protected Parameters snapshot_Protected() {
		VolatileParameters p = currentVolatileParameters_Protected();
		p.lifeTime = this.lifeTime;
		p.parent = this.parent;
		return p;
	}

	abstract protected VolatileParameters currentVolatileParameters_Protected();

	@Override
	protected void performDrivantAction() {
		VolatileParameters p = (VolatileParameters)parameters;
		if (--p.lifeTime <= 0) p.alive = false;
		perforamVolatileDrivantAction();
	}

	abstract protected void perforamVolatileDrivantAction();
}
