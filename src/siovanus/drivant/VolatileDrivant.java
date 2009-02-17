package siovanus.drivant;

import avis.motion.Drivant;
import avis.motion.Parameters;


/**
 * �������r�I�Z�����Ԃŏ��ł��A�T�ˊ����̖@�������ɂ��������ĉ^�����镨�̂�\������<code>Drivant</code>�B 
 * @author hukai
 */
public abstract class VolatileDrivant extends SDrivant {
	/**
	 * <code>VolatileDrivant</code>�I�u�W�F�N�g�̏����p�����^��\������N���X�B
	 * @author hukai
	 */
	public static abstract class VolatileParameters extends Parameters {
		/**
		 * �V���A���E�o�[�W����
		 */
		private static final long serialVersionUID = 3335836896502989796L;
		/**
		 * ���ł܂ł̎c�����ԁB
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
