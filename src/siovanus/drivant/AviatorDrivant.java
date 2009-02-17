package siovanus.drivant;

import siovanus.drivant.aviator.Aviator;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.Parameters;

public abstract class AviatorDrivant extends SDrivant {
	/**
	 * <code>AviatorDrivant</code>クラスの初期パラメタ指定に使用するクラス。
	 * @author hukai
	 *
	 */
	public static class AviatorParameters extends Parameters {
		/**
		 * シリアル・バージョン
		 */
		private static final long serialVersionUID = -3863609777584496654L;
		/**
		 * このオブジェクトの動作を制御する<code>Aviator</code>オブジェクト
		 */
		public Aviator aviator;
		/**
		 * 傾き
		 */
		public double bank;
		/**
		 * バンク復元速度
		 */
		public double bankRecovery = 1;
		/**
		 * <code>target</code>までの距離
		 */
		public double distanceToTarget = Double.MAX_VALUE;
		/**
		 * このオブジェクトの生存状態を管理するためのメンバ 
		 */
		public int life = 1;
		/**
		 * ロール速度
		 */
		public double rollV;
		/**
		 * 追跡対象となる<code>Drivant</code>オブジェクト
		 */
		public Drivant target;
		/**
		 * 機体の方向(ヨーした方向)
		 */
		public double yawDirection;
		/**
		 * ヨー復元速度
		 */
		public double yawRecovery = 2;
		/**
		 * ヨー速度
		 */
		public double yawV;
	}
	/**
	 * このオブジェクトの制御を行う<code>Aviator</code>オブジェクト
	 */
	protected Aviator aviator;
	/**
	 * 傾き
	 */
	protected double bank;
	/**
	 * バンク復元速度
	 */
	protected double bankRecovery = 2;
	/**
	 * <code>target</code>までの距離
	 */
	protected double distanceToTarget;
	/**
	 * このオブジェクトの生存状態を管理するためのメンバ 
	 */
	protected int life;
	/**
	 * 表示に使用するパターン番号を計算するためのデノミネーター
	 */
	protected int patternDenominator = 8;
	/**
	 * ロール速度
	 */
	protected double rollV;
	/**
	 * この機体が次にショットを発射できるようになるまでのフレーム数
	 */
	protected int shotInterval = 0;
	/**
	 * この機体が次に爆弾を投下できるようになるまでのフレーム数
	 */
	protected int bombInterval = 0;
	/**
	 * このオブジェクトの行動を決定する際に基準とする<code>Drivant</code>オブジェクト
	 */
	protected Drivant target;
	/**
	 * 機体の方向(ヨーした方向)
	 */
	protected double yawDirection;
	/**
	 * ヨー復元速度
	 */
	protected double yawRecovery = 4;
	/**
	 * ヨー速度
	 */
	protected double yawV;
	/**
	 * このクラスのオブジェクトを構築する。
	 * @param session
	 * @param aviator
	 * @param shotSpec
	 */
	protected AviatorDrivant() {
		super();
	}
	
	@Override
	protected double accelerationCoefficient() {
		return Avis.cos((int)direction, (int)yawDirection); 
	}
	
	@Override
	public double bank() {
		return bank;
	}

	protected void consumeShotInterval() {
		// 次にショットを行えるようになるまでの時間間隔を計算する。
        if (shotInterval > 0) {
        	shotInterval--;
        }
        if (bombInterval > 0) {
        	bombInterval--;
        }
    }
	
	@Override
	protected void copyParameters_Protected(Parameters parameters) {
		AviatorParameters p = (AviatorParameters) parameters;
		this.aviator = p.aviator;
		this.bank = p.bank;
		this.bankRecovery = p.bankRecovery;
		this.life = p.life;
		this.rollV = p.rollV;
		this.target = p.target;
		this.yawDirection = p.yawDirection;
		this.yawRecovery = p.yawRecovery;
		this.yawV = p.yawV;
	}
	
	@Override
	protected Parameters snapshot_Protected() {
		AviatorParameters ret = new AviatorParameters();
		ret.aviator = this.aviator;
		ret.bank = this.bank;
		ret.bankRecovery = this.bankRecovery;
		ret.life = this.life;
		ret.rollV = this.rollV;
		ret.target = this.target;
		ret.yawDirection = this.yawDirection;
		ret.yawV = this.yawV;
		ret.bankRecovery = this.bankRecovery;
		ret.yawRecovery = this.yawRecovery;
		return ret;
	}
	
	public double gunBias() {
		return 0;
	}

	public double gunDeltaX() {
		return 0;
	}
	
	public double gunDeltaY() {
		return 0;
	}
	
	public void notify(AviatorDrivant another, double r) {
		////
		// from aviator drivant
		AviatorParameters p = (AviatorParameters) parameters;
		if (p.distanceToTarget > r) {
			p.distanceToTarget = r;
			p.target = another;
		}
	}

	@Override
	public final void performDrivantAction() {
		this.consumeShotInterval();
		AviatorParameters p = (AviatorParameters) parameters;
		aviator.perform(this, target);
		// バンクによる揚力で、上向きの移動が生じる。
		p.x += velocity * (Avis.sin((int)p.bank) * Avis.sin((int)p.direction) * 0.5);
		p.y += velocity * ( - Avis.sin((int)p.bank) * Avis.cos((int)p.direction) * 0.5);
	}

	public void recoverBank() {
		AviatorParameters p = (AviatorParameters) parameters;
		int criterion = Avis.BANK_STEPS / 2;
		double r = p.bank > criterion ? p.bankRecovery
				                      : - p.bankRecovery;
		p.bank += r;
		p.bank = p.bank >= Avis.BANK_STEPS ? 0
				                            : p.bank < 0 ? 0
				                                         : p.bank;
	}

	public void recoverYaw() {
		recoverYaw(1.0);
	}
	
	
	private void recoverYaw(double ratio) {
		AviatorParameters p = (AviatorParameters) parameters;
		double sin = Avis.sin((int)p.yawDirection, (int)p.direction);
		double r = 2 * p.yawV * ratio * sin;
		p.direction += r;
		if (p.direction < 0) {
			p.direction = (Avis.DIRECTION_STEPS + p.direction) % Avis.DIRECTION_STEPS; 
		} else if (p.direction >= Avis.DIRECTION_STEPS) {
			p.direction = p.direction % Avis.DIRECTION_STEPS;
		} else {
			// does nothing.
		}
		p.velocity = p.velocity - p.acceleration * Math.abs(Avis.sin((int)p.yawDirection, (int)p.direction));
		p.velocity = Math.max(p.velocity, p.minV);
	}
	
	public void recoverYawHalf() {
		recoverYaw(0.5);
	}

	public void rollLeft(boolean rollOnly) {
		rollLeft(1.0, rollOnly);
	}

	public void rollLeft() {
		rollLeft(1.0, false);
	}

	public void rollLeft(double r, boolean rollOnly) {
		AviatorParameters p = (AviatorParameters) parameters;
		if (!rollOnly || p.bank > 192 || p.bank <=128) {
			p.bank = p.bank - p.rollV * r;
			p.bank = p.bank < 0 ? (Avis.BANK_STEPS + p.bank) % Avis.BANK_STEPS
					: p.bank % Avis.BANK_STEPS;
		}
	}

	protected void rollLeftHalf() {
		rollLeft(0.5, false);
	}
	
	public void rollRight() {
		rollRight(1.0, false);
	}

	public void rollRight(boolean rollOnly) {
		rollRight(1.0, rollOnly);
	}
	
	public void rollRight(double r, boolean rollOnly) {
		AviatorParameters p = (AviatorParameters) parameters;
		if (!rollOnly || p.bank < 64 || p.bank >= 128) { 
			p.bank = (p.bank + p.rollV * r) % Avis.BANK_STEPS;
		}
	}

	protected void rollRightHalf() {
		rollRight(0.5, false);
	}

	public abstract void shot();
	public abstract void bomb();

	@Override
	public int viewDirection() {
		return (int) this.yawDirection();
	}

	public double yawDirection() {
		return yawDirection;
	}

	public void yawLeft() {
		AviatorParameters p = (AviatorParameters) parameters;
		p.yawDirection = p.yawDirection + p.yawV;
		p.yawDirection = p.yawDirection % Avis.DIRECTION_STEPS;
	}
	
	public void yawRight() {
		AviatorParameters p = (AviatorParameters) parameters;
		p.yawDirection = p.yawDirection - p.yawV;
		p.yawDirection = p.yawDirection < 0 ? (Avis.DIRECTION_STEPS + p.yawDirection) % Avis.DIRECTION_STEPS
				                            : p.yawDirection % Avis.DIRECTION_STEPS;
	}

	public int life() {
		return life;
	}

	@Override
	public void beforeInteraction() {
		AviatorParameters p = (AviatorParameters) parameters;
		p.distanceToTarget = Double.MAX_VALUE;
	}

	public void target(Drivant drivant) {
		this.target = drivant;
	}
}
