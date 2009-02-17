package siovanus.drivant;

import avis.motion.Drivant;

public class SEmittableDrivant extends VolatileDrivant {

	public static class SEmittableParameters extends VolatileParameters {
		/**
		 * シリアル・バージョン
		 */
		private static final long serialVersionUID = 6343135860082121072L;
		public Drivant parent;
	}

	public SEmittableDrivant() {
		super();
	}

	@Override
	protected void copyVolatileParameters_Protected(VolatileParameters parameters) {
	}

	@Override
	protected VolatileParameters currentVolatileParameters_Protected() {
		SEmittableParameters ret = new SEmittableParameters();
		return ret;
	}
	@Override
	protected void perforamVolatileDrivantAction() {
		decelerate();
	}
}
