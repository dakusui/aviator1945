package siovanus.drivant;

import siovanus.drivant.aviator.Aviator;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.Parameters;

public abstract class AviatorDrivant extends SDrivant {
	/**
	 * <code>AviatorDrivant</code>�N���X�̏����p�����^�w��Ɏg�p����N���X�B
	 * @author hukai
	 *
	 */
	public static class AviatorParameters extends Parameters {
		/**
		 * �V���A���E�o�[�W����
		 */
		private static final long serialVersionUID = -3863609777584496654L;
		/**
		 * ���̃I�u�W�F�N�g�̓���𐧌䂷��<code>Aviator</code>�I�u�W�F�N�g
		 */
		public Aviator aviator;
		/**
		 * �X��
		 */
		public double bank;
		/**
		 * �o���N�������x
		 */
		public double bankRecovery = 1;
		/**
		 * <code>target</code>�܂ł̋���
		 */
		public double distanceToTarget = Double.MAX_VALUE;
		/**
		 * ���̃I�u�W�F�N�g�̐�����Ԃ��Ǘ����邽�߂̃����o 
		 */
		public int life = 1;
		/**
		 * ���[�����x
		 */
		public double rollV;
		/**
		 * �ǐՑΏۂƂȂ�<code>Drivant</code>�I�u�W�F�N�g
		 */
		public Drivant target;
		/**
		 * �@�̂̕���(���[��������)
		 */
		public double yawDirection;
		/**
		 * ���[�������x
		 */
		public double yawRecovery = 2;
		/**
		 * ���[���x
		 */
		public double yawV;
	}
	/**
	 * ���̃I�u�W�F�N�g�̐�����s��<code>Aviator</code>�I�u�W�F�N�g
	 */
	protected Aviator aviator;
	/**
	 * �X��
	 */
	protected double bank;
	/**
	 * �o���N�������x
	 */
	protected double bankRecovery = 2;
	/**
	 * <code>target</code>�܂ł̋���
	 */
	protected double distanceToTarget;
	/**
	 * ���̃I�u�W�F�N�g�̐�����Ԃ��Ǘ����邽�߂̃����o 
	 */
	protected int life;
	/**
	 * �\���Ɏg�p����p�^�[���ԍ����v�Z���邽�߂̃f�m�~�l�[�^�[
	 */
	protected int patternDenominator = 8;
	/**
	 * ���[�����x
	 */
	protected double rollV;
	/**
	 * ���̋@�̂����ɃV���b�g�𔭎˂ł���悤�ɂȂ�܂ł̃t���[����
	 */
	protected int shotInterval = 0;
	/**
	 * ���̋@�̂����ɔ��e�𓊉��ł���悤�ɂȂ�܂ł̃t���[����
	 */
	protected int bombInterval = 0;
	/**
	 * ���̃I�u�W�F�N�g�̍s�������肷��ۂɊ�Ƃ���<code>Drivant</code>�I�u�W�F�N�g
	 */
	protected Drivant target;
	/**
	 * �@�̂̕���(���[��������)
	 */
	protected double yawDirection;
	/**
	 * ���[�������x
	 */
	protected double yawRecovery = 4;
	/**
	 * ���[���x
	 */
	protected double yawV;
	/**
	 * ���̃N���X�̃I�u�W�F�N�g���\�z����B
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
		// ���ɃV���b�g���s����悤�ɂȂ�܂ł̎��ԊԊu���v�Z����B
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
		// �o���N�ɂ��g�͂ŁA������̈ړ���������B
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
