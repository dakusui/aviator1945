package avis.video;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import avis.base.AException;
import avis.base.Avis;
import avis.base.Configuration;
import avis.spec.ASpriteSpec;
import avis.sprite.ASprite;
import avis.sprite.IASpriteObserver;

public class APlane implements Comparable<APlane>, IASpriteObserver {
	protected ABackground background;
	protected IAPlaneObserver observer;
	protected List<ASprite> sprites;

	protected AViewport viewport;

	protected int width, height;
	private String name;
	private int priority;
	
	public APlane(
			String name,  
			AViewport viewport, 
			ABackground background, 
			int width, 
			int height, 
			int priority,
			IAPlaneObserver observer)
	{
		this.name = name;
		this.sprites = new LinkedList<ASprite>();
		this.background = background;
		this.viewport = viewport;
		this.width = width;
		this.height = height;
		this.priority = priority;
		this.observer = observer;
	}

	public String name() {
		return name;
	}

	public void attach(ASprite sprite) {
		int i = Collections.binarySearch(sprites, sprite);
		sprites.add(i < 0 ? 0
				          : i,
				    sprite);
	}
	
	public boolean detach(ASprite sprite) {
		return sprites.remove(sprite);
	}

	public ASprite createSprite(ASpriteSpec spec, int priority) throws AException {
		ASprite ret = spec.createSprite(priority);
		ret.init(spec);
		ret.visible(true);
		attach(ret);
		return ret;
	}

	protected void gcSprites() {
		Iterator<ASprite> iSprites = sprites.iterator();
		while (iSprites.hasNext()) {
			ASprite cur = iSprites.next();
			if (cur.isDisposed()) {
				iSprites.remove();
			}
		}
	}
	
	public double height() {
		return height;
	}
	
	public void paint(Graphics g) {
		double _halfWidth, _halfHeight;
		int sx1, sy1;
		int sx2, sy2;
		int dx1, dy1;
		int dx2, dy2;
		
		double x = viewport.x(), y = viewport.y();
		double theta = viewport.theta();
		double halfWidth = this.width() / 2;
		double halfHeight = this.height() / 2;
        ////
        // 0. 座標系の回転
		Graphics2D gg = (Graphics2D) g;
		AffineTransform backupTransform = gg.getTransform();
		if (Configuration.rotationMode == Configuration.RotationMode.ENABLED) {
			gg.rotate(Avis.srad2rad(theta), halfWidth, halfHeight);
		}
		Rectangle rect = new Rectangle();
		try {
	        ////
	        // 1. 背景の描画
			if (Configuration.rotationMode == Configuration.RotationMode.ENABLED) {
				_halfWidth  = Math.abs(halfWidth * Avis.cos((int)theta)) + Math.abs(halfHeight * Avis.sin((int)theta));
				_halfHeight = Math.abs(halfWidth * Avis.sin((int)theta)) + Math.abs(halfHeight * Avis.cos((int)theta));
			} else {
				_halfWidth  = halfWidth;
				_halfHeight = halfHeight;
			}
			sx1 = (int) (x - _halfWidth);
			sy1 = (int) (y - _halfHeight);
			sx2 = (int) (x + _halfWidth);
			sy2 = (int) (y + _halfHeight);
			dx1 = (int) (halfWidth - _halfWidth);
			dy1 = (int) (halfHeight - _halfHeight);
			dx2 = (int) (halfWidth + _halfWidth);
			dy2 = (int) (halfHeight + _halfHeight);
			this.background.draw(gg, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
			
			////
			//    表示有無を判定するための、X,Y座標を計算する。
			int _x1 = Math.min(dx1, dx2);
			int _y1 = Math.min(dy1, dy2);
			int _x2 = Math.max(dx1, dx2);
			int _y2 = Math.max(dy1, dy2);
			rect.setBounds(_x1, _y1, _x2 - _x1, _y2 - _y1);
			////
	        // 2. スプライトの描画
	        Iterator<ASprite> iSprites = sprites().iterator();
	        while (iSprites.hasNext()) {
	            ASprite cur = iSprites.next();
	            if (cur.visible() && cur.touches(rect)) {
//OBSV            	cur.paint(gg, observer);
	            	cur.paint(gg, null);
	            }
	        }
	        if (Configuration.debugMode == Configuration.DebugMode.GRID) {
	        	debugGrid(gg);
	        }
	        
	        ////
	        // 3. 廃棄されたスプライトの破棄処理
	        gcSprites();
		} finally {
			gg.setTransform(backupTransform);
		}
	}
	
	private void debugGrid(Graphics2D gg) {
		Color c = gg.getColor();
		try {
			gg.setColor(Color.yellow);
			for (int i = 0; i <= this.width; i += 50) {
				gg.drawLine(i, 0, i, this.height);
			}
			for (int i = 0; i <= this.height; i += 50) {
				gg.drawLine(0, i, this.width, i);
			}
			for (int i = 0; i < this.width; i += 100) {
				for (int j = 0; j <= this.height; j += 100) {
					gg.drawString("(" + i + "," + j + ")", i, j);
				}
			}
		} finally {
			gg.setColor(c);
		}
	}

	public void background(ABackground background) {
		this.background = background;
	}
	public ABackground background() {
		return this.background;
	}
	
	public List<ASprite> sprites() {
		return sprites;
	}
	public AViewport viewport() {
		return viewport;
	}
	public double width() {
		return width;
	}
	
	public int compareTo(APlane o) {
		// priorityが高い(大きい)ものほど、前面に表示される
		return - (this.priority - o.priority);
	}
	
	public int priority() {
		return priority;
	}
	
	public void priority(int priority) {
		this.priority = priority;
		if (observer != null) {
			observer.priorityChanged(this);
		}
	}

	public void clear() {
		while (sprites.size() > 0) {
			ASprite cur = sprites.remove(0);
			cur.dispose(); // 内部でリソースを確保するスプライトに強制的に解放させる
		}
	}

	public void disposed(ASprite sprite) {
	}

	public void priorityChanged(ASprite sprite) {
		if (this.sprites.contains(sprite)) {
			Collections.sort(this.sprites);
//			this.detach(sprite);
//			this.attach(sprite);
		}
	}

	public void setVisible(ASprite sprite, boolean visible) {
	}
}
