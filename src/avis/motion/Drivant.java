package avis.motion;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import avis.base.Avis;

public abstract class Drivant {
	protected static final EventDispatcher dispatcher = new EventDispatcher();
	/**
	 * 加速度
	 */
	protected double acceleration;
	/**
	 * オブジェクトの有効/無効状態を管理するフラグ
	 */
	protected boolean alive = true;
	/**
	 * 進行方向
	 */
	protected double direction;
	
	/**
	 * グループ識別オブジェクト
	 */
	protected Group groupId;
	
	/**
	 * 最大の速さ,最小の速さ
	 */
	protected double maxV, minV;
	
	/**
	 * このオブジェクトの現在の状態を表すメンバ・オブジェクト
	 */
	protected Parameters parameters;
	
	/**
	 * このオブジェクトが属するコンテキスト
	 */
	protected DrivantObserver observer = DrivantObserver.NULL_OBSERVER ;

	
	/**
	 * 速さ
	 */
	protected double velocity;
	/**
	 * オブジェクトの衝突判定に用いる幅と高さ
	 */
	protected double width = 20;
	protected double height = 20;
	/**
	 * このオブジェクトが更新中状態かどうかを管理するフラグ
	 */
	protected boolean prepared;
	
	/**
	 * 現在の位置
	 */
	protected double x, y;
	
	/**
	 * オブジェクトの識別にアプリケーションが使用するラベル。<code>Avis</code> Frameworkはこの属性を制御には使用しない。
	 */
	protected String label;
	
	/**
	 * 衝突判定機能がこのオブジェクトにおいて有効になっているかどうかを管理するフラグ
	 */
	private boolean collisionDetectEnabled = true;

	public Drivant() {
	}

	public double bank() {
		return 0;
	}

	public void addObserver(DrivantObserver observer) {
		if (observer == DrivantObserver.NULL_OBSERVER ||
				observer == dispatcher) {
			return;
		}
		if (this.observer == DrivantObserver.NULL_OBSERVER) {
			this.observer = dispatcher;
		} 
		dispatcher.add(observer, this);
	}
	public void removeObserver(DrivantObserver observer) {
		if (observer != dispatcher) {
			return;
		}
		dispatcher.remove(observer);
	}
	
	public String label() {
		return label;
	}
	
	public void label(String label) {
		this.label = label;
	}
	
	public Set<DrivantObserver> observers() {
		return dispatcher.oberversFor(this);
	}
	
	public final void commit() {
		Parameters param = this.parameters;
		this.prepared = false;
		copyParameters(param);
	}

	public final void rollback() {
		// TODO: not yet implemented;
		throw new RuntimeException("Not yet implemented");
	}

	protected final void copyParameters(Parameters parameters) {
		this.acceleration = parameters.acceleration;
		this.alive = parameters.alive;
		this.direction = parameters.direction;
		this.groupId = parameters.groupId;
		this.height = parameters.height;
		this.maxV = parameters.maxV;
		this.minV = parameters.minV;
		this.velocity = parameters.velocity;
		this.width = parameters.width;
		this.x = parameters.x;
		this.y = parameters.y;
		copyParameters_Protected(parameters);
	}

	protected abstract void copyParameters_Protected(Parameters parameters);

	public final Parameters snapshot() {
		Parameters parameters = snapshot_Protected();
		parameters.acceleration = this.acceleration;
		parameters.alive = this.alive;
		parameters.direction = this.direction;
		parameters.groupId = this.groupId();
		parameters.height = this.height;
		parameters.maxV = this.maxV;
		parameters.minV = this.minV;
		parameters.velocity = this.velocity;
		parameters.width = this.width;
		parameters.x = this.x;
		parameters.y = this.y;
		return parameters;
	}

	public Parameters parameters() {
		return parameters;
	}
	
	protected abstract Parameters snapshot_Protected();

	public int direction() {
		return (int) direction;
	}

	/**
	 * このオブジェクトが属するグループを一意に識別する文字列を返却する。 このメソッドが返却したオブジェクトに基づいて、
	 * <code>SScreen</code>クラスは、その内部の処理で 当たり判定を制御する。
	 * 
	 * @return このオブジェクトが属するグループを一意に識別するオブジェクト
	 */
	public Group groupId() {
		return this.groupId;
	}

	public final void init(Parameters parameters) {
		copyParameters(parameters);

		this.parameters = parameters;

		assert maxV >= minV;
		assert maxV >= 0 : this.toString(); // バックするキャラクタもあるだろうということで、minVについては検査しない。
		assert acceleration >= 0; 
	}

	public boolean isValid() {
		return alive;
	}

	public final Parameters createParameters() {
		this.parameters = snapshot();
		return parameters;
	}

	public abstract void perform();

	public final Drivant prepare() {
		this.prepared = true;
		return this;
	}

	public double velocity() {
		return velocity;
	}

	public int viewDirection() {
		return (int) this.direction;
	}

	public double x() {
		return x;
	}

	public double y() {
		return y;
	}

	public double width() {
		return width;
	}

	public double height() {
		return height;
	}

	public void invalidate() {
		Parameters parameters = this.parameters;
		dispatcher.invalidated(this);
		parameters.alive = false;
		return;
	}
	
	public static class EventDispatcher implements DrivantObserver {
		Set<DrivantObserver> observers = new HashSet<DrivantObserver>();
		Map<DrivantObserver, Set<Drivant>> destinationMap = new HashMap<DrivantObserver, Set<Drivant>>();
		
		public Set<DrivantObserver> oberversFor(Drivant drivant) {
			Set<DrivantObserver> ret = new HashSet<DrivantObserver>();
			Iterator<DrivantObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				DrivantObserver cur = iObservers.next();
				Set<Drivant> destinations = destinationMap.get(cur);
				if (destinations.contains(drivant)) {
					ret.add(cur);
				}
			}
			return ret;
		}
		
		public void add(DrivantObserver observer, Drivant drivant) {
			observers.add(observer);
			Set<Drivant> destinations = null;
			if ((destinations = destinationMap.get(observer)) == null) {
				destinationMap.put(observer, destinations = new HashSet<Drivant>());
			}
			destinations.add(drivant);
		}
		
		public void remove(DrivantObserver observer) {
			observers.remove(observer);
			if (!destinationMap.containsKey(observer)) {
				destinationMap.remove(observer);
			}
		}

		@SuppressWarnings("unchecked")
		public void emit(Drivant source, MMachineSpec emittable) {
			Iterator<DrivantObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				DrivantObserver cur = iObservers.next();
				Set<Drivant> destinations = destinationMap.get(cur);
				if (destinations.contains(source)) {
					cur.emit(source, emittable);
				}
			}
		}

		public void invalidated(Drivant source) {
			Iterator<DrivantObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				DrivantObserver cur = iObservers.next();
				Set<Drivant> destinations = destinationMap.get(cur);
				if (destinations.contains(source)) {
					cur.invalidated(source);
				}
			}
		}

		public void registered(Drivant source) {
			Iterator<DrivantObserver> iObservers = observers.iterator();
			while (iObservers.hasNext()) {
				DrivantObserver cur = iObservers.next();
				Set<Drivant> destinations = destinationMap.get(cur);
				if (destinations.contains(source)) {
					cur.registered(source);
				}
			}
		}
	}
	
	public Product product(Drivant target) {
		Product ret = (Product) _productPrototype.cloneProduct();
		if (target != null) {
			Parameters p = parameters();
			double dx = target.x() - this.x();
			double dy = target.y() - this.y();
			int direction = (int)p.direction;
			double vx = Avis.cos(direction);
			double vy = Avis.sin(direction);
			ret.inner = dx * vx + dy * vy; 
			ret.outer = dx * vy - dy * vx;
			ret.tangent = ret.inner!= 0 ? ret.outer / ret.inner
					                   : ret.outer > 0 ? Double.MAX_VALUE
					                		           : Double.MIN_VALUE;
			ret.distance = this.distance(target);
		} else {
			ret.inner = Double.MAX_VALUE;
			ret.outer = Double.MAX_VALUE;
			ret.tangent = Double.MAX_VALUE;
			ret.distance = Double.MAX_VALUE;
		}
		return ret;
	}

	public static class Product implements Cloneable {
		public double inner;
		public double outer;
		public double tangent = Double.NaN;
		public double distance = Double.NaN;
		Product cloneProduct() {
			Product ret = null;
			try {
				ret = (Product)clone();
			} catch (CloneNotSupportedException e) {
				throw new RuntimeException(e);
			}
			return ret;
		}
	}
	private static final Product _productPrototype = new Product();

	public double distance(Drivant another) {
		double dx = (this.x() - another.x());
		double dy = (this.y() - another.y());
		double r = Math.sqrt(dx * dx + dy * dy);
		return r;
	}

	public void beforeInteraction() {
	}

	public void afterInteraction() {
	}

	public void enableCollisionDetect() {
		collisionDetectEnabled = true;
	}

	public void disableCollisionDetect() {
		collisionDetectEnabled = false;
	}

	public boolean isCollisionDetectEnabled() {
		return collisionDetectEnabled;
	}
}
