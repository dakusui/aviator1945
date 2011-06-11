package mu64.motion;

import oreactor.exceptions.OpenReactorException;
import oreactor.video.sprite.Sprite;

public final class MMachine {
	private Attributes inprocess;
	private Attributes attr;
	private Group      group;
	private MotionProvider provider;
	private Motion nextMotion;
	private Drivant drivant;
	private Sprite sprite;
	private MMachineSpec spec;
	
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
	
	void performAction(MotionProvider provider) throws OpenReactorException {
		this.drivant.perform(this.nextMotion, this, provider);
	}
	
	void performCollisionWith(MMachine another, double distance) {
		this.drivant.performCollisionWith(this.nextMotion, this, another, distance);
	}
	
	void performInteractionWith(MMachine another, double distance) {
		this.drivant.performInteractionWith(this.nextMotion, this, another, distance);
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
			this.group = g;
			this.group.register(this);
		}
	}
	
	public void unbind() {
		if (this.group != null) {
			this.group.unregister(this);
		}
	}
	
	public Attributes attributes() {
		Attributes ret = this.inprocess;
		if (ret == null) {
			ret = this.attr;
		}
		return ret;
	}

	void setDrivant(Drivant drivant) {
		this.drivant = drivant;
	}

	void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	void setSpec(MMachineSpec spec) {
		this.spec = spec;
	}

	public void emit(MotionProvider motionProvider) throws OpenReactorException {
		for (MMachineSpec spec : this.nextMotion.emissions()) {
			MMachine n = spec.buildMMachine(this.provider, this);
			provider.register(n);
		}
	}
	
	public boolean isDestroyed() {
		return this.attributes().isDestroyed();
	}

	public Sprite sprite() {
		return this.sprite;
	}

	public void release() {
		this.spec.release(this);
	}
}
