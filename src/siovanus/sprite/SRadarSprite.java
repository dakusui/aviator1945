package siovanus.sprite;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.ImageObserver;
import java.util.Iterator;

import siovanus.SGroup;
import siovanus.spec.SRadarSpriteSpec;
import avis.base.APoint;
import avis.base.Avis;
import avis.motion.Drivant;
import avis.motion.MMachine;
import avis.motion.MotionController;
import avis.spec.ASpriteSpec;
import avis.video.APlane;
import avis.video.AViewport;


public class SRadarSprite extends SShapeSprite {
	protected MotionController controller;
	protected Color color;
	private AffineTransform _transform;
	private AViewport viewport;
	private APlane targetPlane;
	private double horizontalRatio;
	private double verticalRatio;
	private double originX;
	private double originY;
	
	public SRadarSprite() {
		super();
	}

	@Override
	protected void init_Protected(ASpriteSpec p) {
		Avis.logger().debug("Radar Sprite initialized:<" + this + ">");
		SRadarSpriteSpec spec = (SRadarSpriteSpec) this.spec;
		this.targetPlane = spec.targetPlane();
		this.viewport = spec.targetPlane().viewport();
		this.color = spec.color();
		this.controller = spec.controller();
	}
	
	@Override
	public int width() {
		return ((SRadarSpriteSpec) spec).width();
	}

	@Override
	public int height () {
		return ((SRadarSpriteSpec) spec).height();
	}
	
	@Override
	public void paint_Protected(Graphics graphics, ImageObserver observer) {
		this.horizontalRatio = ((double)width() / targetPlane.background().imageWidth());
		this.verticalRatio = ((double)height() / targetPlane.background().imageHeight());
		this.originX = targetPlane.background().imageWidth() / 2;
		this.originY = targetPlane.background().imageHeight() / 2;
		this._transform = AffineTransform.getScaleInstance(horizontalRatio, - verticalRatio);
		graphics.setColor(this.color);
		graphics.fillRect(x, y, width(), height());
		APoint src  = new APoint();
		APoint dest = new APoint();
		int biasX = this.x + width() / 2 ;
		int biasY = this.y + height() / 2 ;
		int halfWidth =  width();
		int halfHeight =  height();
		Iterator<MMachine> iMMachines = controller.mmachines().iterator();
		while (iMMachines.hasNext()) {
			MMachine mmachine = iMMachines.next();
			Drivant drivant = mmachine.drivant();
			if (drivant.groupId() == SGroup.Player_Aerial) {
				graphics.setColor(Color.CYAN);
			} else if (drivant.groupId() == SGroup.Enemy_Surface) {
				graphics.setColor(Color.BLUE);
			} else if (drivant.groupId() == SGroup.Enemy_Aerial) {
				graphics.setColor(Color.ORANGE);
			} else if (drivant.groupId() == SGroup.Enemy_Surface) {
				graphics.setColor(Color.RED);
			}
			src.set(drivant.x(), drivant.y());
			_transform.transform(src, dest);
			if (dest.x() < - halfWidth / 2 - 20 || dest.x() > halfWidth / 2 + 20 ||
				dest.y() < - halfHeight / 2 - 20 || dest.y() > halfHeight / 2 + 20) {
				continue;
			}
			graphics.fillRect((int)(dest.x()) + biasX - 2,
					          (int)(dest.y()) + biasY - 2,
					          4,
					          4
					          );
		}
		graphics.setColor(Color.BLUE);
		int vw = (int)(targetPlane.width() * horizontalRatio);
		int vh = (int)(targetPlane.height() * verticalRatio);
		int centerX = biasX + (int)((viewport.x() - originX) * horizontalRatio);
		int centerY = biasY + (int)((viewport.y() - originY) * verticalRatio);
		Graphics2D gg = (Graphics2D) graphics;
		AffineTransform backup = gg.getTransform();
		try {
			gg.rotate( Avis.srad2rad(-viewport.theta()) , centerX, centerY);
			gg.drawRect(
					(int)(centerX - vw / 2),
					(int)(centerY - vh / 2),
					vw, 
					vh);
			gg.drawLine(centerX, centerY - 5, centerX + 5, centerY + 10);
			gg.drawLine(centerX, centerY - 5, centerX - 5, centerY + 10);
			gg.drawLine(centerX - 5 , centerY + 10, centerX + 5, centerY + 10);
			
		} finally {
			gg.setTransform(backup);
		}
		src = dest = null;
	}

}
