package avis.motion;

import avis.sprite.ASprite;

public abstract class MMachineSpec<T> {
	private ThreadLocal<T> additionalParameter = new ThreadLocal<T>();
	protected MMachineBuilder builder;
	protected MotionController controller;
	
	protected MMachineSpec() {
	}

	public void builder(MMachineBuilder builder) {
		this.builder = builder;
	}
	public void controller(MotionController controller) {
		this.controller = controller;
	}
	
	public MMachine createMMachine(int priority, double x, double y, int direction, double velocity, Group group, T aux) {
		this.additionalParameter.set(aux);
		MMachine ret = produce(priority, x, y, direction, velocity, group, aux);
		return ret;
	}

	public MMachine createMMachine(int priority, double x, double y, int direction, double velocity, Group group) {
		MMachine ret = produce(priority, x, y, direction, velocity, group, null);
		return ret;
	}
	protected MMachine produce(int priority, double x, double y, int direction, double velocity, Group groupId, T aux) {
		Drivant drivant = drivant();
		drivant.label(label());
		Parameters parameters = drivant.createParameters();
		parameters.x = x;
		parameters.y = y;
		parameters.direction = direction;
		parameters.velocity = velocity;
		parameters.alive =            true; 
		parameters.height =           20;
		parameters.width =            20;
		parameters.groupId =          groupId;
		
		setupParameters(parameters);
		MMachine ret = builder.buildMMachine(
				drivant, 
				parameters,
				spriteSpecName(), 
				spritePriority()
				);
		controller.add(ret);
		ret.sprite().addObserver(builder.plane());
		ret.sprite().priority(priority);
		return ret;
	}

	protected T additionalParameter() {
		return additionalParameter.get();
	}

	protected abstract void setupParameters(Parameters parameters);
	abstract protected String spriteSpecName();
	abstract protected Drivant drivant();
	protected int spritePriority() {
		return ASprite.defaultPriority;
	}

	public void dispose() {
		// usually does nothing
	}

	public abstract String label();
}
