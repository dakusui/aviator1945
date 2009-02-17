package siovanus.drivant;

import avis.motion.Drivant;
import avis.motion.DrivantObserver;
import avis.motion.MMachineSpec;

public class VesselDrivant extends AviatorDrivant implements DrivantObserver {
	public VesselDrivant() {
		
	}
	
	@Override
	public void bomb() {
		// Does nothing
	}

	@Override
	public void shot() {
		
	}

	@SuppressWarnings("unchecked")
	public void emit(Drivant source, MMachineSpec emittable) {
		// Does nothing
	}

	public void invalidated(Drivant drivant) {
		// TODO Auto-generated method stub
		
	}

	public void registered(Drivant drivant) {
		// TODO Auto-generated method stub
		
	}

}
