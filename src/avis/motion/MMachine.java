package avis.motion;

import avis.sprite.ASprite;

public class MMachine {
	Drivant drivant;
	
	private ASprite sprite;

	protected MMachine(Drivant drivant, ASprite sprite) {
		this.drivant = drivant;
		this.sprite = sprite;
	}

	public Drivant drivant() {
		return drivant;
	}
	
	public ASprite sprite() {
		return sprite;
	}
	
	public String toString() {
		return "id=<" + this.hashCode() + ">,drivant=<" + drivant + ">,sprite=<" + sprite + ">"; 
	}
}
