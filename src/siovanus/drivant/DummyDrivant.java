package siovanus.drivant;

import avis.motion.Parameters;

public class DummyDrivant extends SDrivant {

	public DummyDrivant() {
		super();
	}

	@Override
	protected void performDrivantAction() {
		// does nothing
	}

	@Override
	protected void copyParameters_Protected(Parameters parameters) {
		// does nothing
	}

	@Override
	protected Parameters snapshot_Protected() {
		return new Parameters();
	}


}
