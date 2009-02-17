package siovanus.drivant;

import siovanus.Siovanus;

public class SBigshotDrivant extends SEmittableDrivant {
	private boolean firstTime = true;

	@Override
	protected void perforamVolatileDrivantAction() {
		super.perforamVolatileDrivantAction();
		if (firstTime) {
			observer.emit(this, Siovanus.explosionSpec);
			firstTime = false;
		}
	}
}
