package avis.motion;

public interface DrivantObserver {
	public static final DrivantObserver NULL_OBSERVER = new DrivantObserver() {
		@SuppressWarnings("unchecked")
		public void emit(Drivant source, MMachineSpec emittable) {
		}
		public void invalidated(Drivant drivant) {
		}
		public void registered(Drivant drivant) {
		}
	};
	public void registered(Drivant drivant);
	public void invalidated(Drivant drivant);
	@SuppressWarnings("unchecked")
	public void emit(Drivant source, MMachineSpec emittable);
}
