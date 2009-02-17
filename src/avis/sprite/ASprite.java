package avis.sprite;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import avis.spec.ASpriteSpec;


public abstract class ASprite implements Comparable<ASprite> {
	protected static final EventDispatcher dispatcher = new EventDispatcher();
    protected int x;
    protected int y;
    protected int index;
    protected boolean visible = false;
	protected int pattern = 0;;
	protected ASpriteSpec spec = null;
	private boolean disposed;
	private IASpriteObserver observer;
	private int priority;
	public static int defaultPriority = 100;
	
	public ASprite() {
        this.disposed = false;
        this.observer = IASpriteObserver.NULL_OBSERVER;
    }

	public void priority(int priority) {
		this.priority = priority;
		dispatcher.priorityChanged(this);
	}
	
	public int priority() {
		return this.priority;
	}

	public void addObserver(IASpriteObserver observer) {
		if (observer == IASpriteObserver.NULL_OBSERVER ||
				observer == dispatcher) {
			return;
		}
		if (this.observer == IASpriteObserver.NULL_OBSERVER) {
			this.observer = dispatcher;
		} 
		dispatcher.add(observer, this);
	}
	public void removeObserver(IASpriteObserver observer) {
		if (observer != dispatcher) {
			return;
		}
		dispatcher.remove(observer);
	}
	
	public Set<IASpriteObserver> observers() {
		return dispatcher.oberversFor(this);
	}
    
	public int width() {
		return 0;
	}

	public int height () {
		return 0;
	}
	
	public void put(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void put(int x, int y, int i) {
    	put(x, y);
    	this.index = i;
    }
    
    public void visible(boolean v) {
        this.visible = v;
        this.observer.setVisible(this, v);
    }
    
    public boolean visible() {
        return this.visible;
    }
    
    public void pattern(int i) {
    	this.pattern = i;
    }

    public int pattern() {
    	return this.pattern;
    }
    
    public void paint(Graphics graphics, ImageObserver observer) {
    	if (!disposed) {
    		paint_Protected(graphics, observer);
    	}
    }
    protected abstract void paint_Protected(Graphics graphics, ImageObserver observer);


	public void init(ASpriteSpec p) {
		this.spec = p;
		init_Protected(p);
	}

	protected abstract void init_Protected(ASpriteSpec p);

	public ASpriteSpec spec() {
		return this.spec;
	}

	public boolean isDisposed() {
		return disposed;
	}
	
	public void dispose() {
		disposed = true;
		observer.disposed(this);
	}

	public int y() {
		return y;
	}

	public int x() {
		return x;
	}

	public boolean touches(Rectangle rect) {
		int dx = x - (rect.x + rect.width/2), dy = y - (rect.y + rect.height/2);
		if ( dx * dx  <= rect.width * rect.width + this.width() * this.width() && 
			 dy * dy  <= rect.height * rect.height + this.height() * this.height() ) {
			return true;
		}
		return false;
	}
	
	public int compareTo(ASprite o) {
		// priority‚ª‚‚¢(‘å‚«‚¢)‚à‚Ì‚Ù‚ÇA‘O–Ê‚É•\Ž¦‚³‚ê‚é
		return this.priority - o.priority;
	}
	
	public static class EventDispatcher implements IASpriteObserver {
		Set<IASpriteObserver> observers = new HashSet<IASpriteObserver>();
		Map<IASpriteObserver, Set<ASprite>> destinationMap = new HashMap<IASpriteObserver, Set<ASprite>>();
		
		public void disposed(ASprite sprite) {
			Iterator<IASpriteObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				IASpriteObserver cur = iObservers.next();
				Set<ASprite> destinations = destinationMap.get(cur);
				if (destinations.contains(sprite)) {
					cur.disposed(sprite);
				}
			}
		}

		public void priorityChanged(ASprite sprite) {
			Iterator<IASpriteObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				IASpriteObserver cur = iObservers.next();
				Set<ASprite> destinations = destinationMap.get(cur);
				if (destinations.contains(sprite)) {
					cur.priorityChanged(sprite);
				}
			}
		}

		public void setVisible(ASprite sprite, boolean visible) {
			Iterator<IASpriteObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				IASpriteObserver cur = iObservers.next();
				Set<ASprite> destinations = destinationMap.get(cur);
				if (destinations.contains(sprite)) {
					cur.setVisible(sprite, visible);
				}
			}
		}
		
		public Set<IASpriteObserver> oberversFor(ASprite sprite) {
			Set<IASpriteObserver> ret = new HashSet<IASpriteObserver>();
			Iterator<IASpriteObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				IASpriteObserver cur = iObservers.next();
				Set<ASprite> destinations = destinationMap.get(cur);
				if (destinations.contains(sprite)) {
					ret.add(cur);
				}
			}
			return ret;
		}
		
		public void add(IASpriteObserver observer, ASprite sprite) {
			observers.add(observer);
			if (!destinationMap.containsKey(observer)) {
				destinationMap.put(observer, new HashSet<ASprite>());
			}
			Set<ASprite> destinations = destinationMap.get(observer);
			destinations.add(sprite);
		}
		
		public void remove(IASpriteObserver observer) {
			observers.remove(observer);
			if (!destinationMap.containsKey(observer)) {
				destinationMap.remove(observer);
			}
		}
	}
}
