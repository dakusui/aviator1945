package oreactor.motion;

public final class MMachine {
	private Attributes inprocess;
	private Attributes attr;
	private Group      group;
	private MotionProvider provider;
	private Motion nextMotion;
	private Drivant drivant;
	
	public MMachine(MotionProvider provider) {
		this.provider = provider;
	}

	void setAttributes(Attributes attr) {
		this.attr = attr;
	}

	void prepare() {
		this.nextMotion = provider.createMotionObject();
		this.inprocess = attr.cloneState();
	}
	
	void performAction(MotionProvider provider) {
		this.drivant.perform(this.nextMotion, this, provider);
	}
	
	void performCollisionWith(MMachine another, double distance) {
		this.drivant.performCollisionWith(this.nextMotion, this, another, distance);
	}
	
	void performInteractionWith(MMachine another, double distance) {
		this.drivant.performCollisionWith(this.nextMotion, this, another, distance);
	}
	
	void commit() {
		if (this.inprocess != null) {
			this.inprocess.apply(nextMotion);
			this.attr = this.inprocess;
			this.inprocess = null;
		}
	}
	
	void rollback() {
		this.inprocess = null;
	}
	
	public void bind(Group g) {
		if (g != null) {
			this.unbind();
			this.group.register(this);
		}
	}
	
	public void unbind() {
		if (this.group != null) {
			this.group.unregister(this);
		}
	}
	
	public Attributes attributes() {
		return this.inprocess;
	}

	boolean touches(MMachine n, double d) {
		return this.attributes().touches(n.attributes(), d);
	}

	public void setDrivant(Drivant drivant) {
		this.drivant = drivant;
	}
}
