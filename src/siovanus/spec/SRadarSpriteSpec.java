package siovanus.spec;

import java.awt.Color;

import siovanus.sprite.SRadarSprite;
import avis.base.AException;
import avis.motion.MotionController;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;
import avis.video.APlane;

public class SRadarSpriteSpec extends ASpriteSpec {

	protected APlane targetPlane;
	private MotionController controller;

	public SRadarSpriteSpec(MotionController controller, APlane targetPlane) {
		this.controller = controller;
		this.targetPlane = targetPlane;
	}

	public int width() {
		return 256;
	}

	public int height() {
		return 192;
	}

	public Color color() {
		return new Color(255,255,255,128);
	}

	public MotionController controller() {
		return controller;
	}
	
	public APlane targetPlane() {
		return targetPlane;
	}

	@Override
	public ASprite createSprite_Protected() {
		ASprite ret = new SRadarSprite();
		return ret;
	}

	@Override
	public void init(Object resource) throws AException {
		// TODO Auto-generated method stub
		
	}

}
