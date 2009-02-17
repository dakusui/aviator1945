package siovanus.drivant;

import avis.base.Avis;

public class ExplosionDrivant extends VolatileDrivant {
	public static class ExplosionParameters extends VolatileParameters {
		/*
		 * シリアル・バージョンID
		 */
		private static final long serialVersionUID = 2652396106537932173L;
	}
	protected int bankCoefficient;
	private boolean firstTime = true;
	private int bank;

	public ExplosionDrivant() {
		super();
	}
	
	@Override
	public double bank() {
		return Math.max(0, bank);
	}

	@Override
	protected void copyVolatileParameters_Protected(VolatileParameters parameters) {
		// does nothing
	}

	@Override
	protected VolatileParameters currentVolatileParameters_Protected() {
		ExplosionParameters ret = new ExplosionParameters();
		return ret;
	}

	@Override
	protected void perforamVolatileDrivantAction() {
		VolatileParameters p = (VolatileParameters)parameters;
		if (firstTime) {
			bankCoefficient = Math.abs(Avis.BANK_STEPS / p.lifeTime);
			firstTime  = false;
		}
		bank = Math.max(0, Math.min(bankCoefficient * p.lifeTime, Avis.BANK_STEPS - 1));
		
		decelerate();
	}

}
