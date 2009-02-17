package avis.motion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.TreeMap;

import avis.base.Avis;

/**
 * 複数の<code>MMachine</code>オブジェクトの動作を一貫した形式で一括して処理する機能を提供するクラス。
 * @author hukai
 */
public final class MotionController {
	private InteractionHandler interactionHandler = null;
	
	/**
	 * このオブジェクトが管理する<code>MMachine</code>オブジェクトのリスト。
	 */
	private List<MMachine> mmachines;
	/**
	 * このオブジェクトが管理する<code>MMachine</code>オブジェクトを一時的に保存するリスト。
	 * 変更が開始されてから、確定(<code>commit</code>)されるまでの間の一時的な状態を格納するのに使用する。
	 */
	private List<MMachine> tmpMMachines;
	/**
	 *  敵味方を識別するためのマップ
	 */
	private SortedMap<Group, List<MMachine>> groups;
	/**
	 * このクラスのオブジェクトを生成する。
	 */
	public MotionController() {
	}
	/**
	 * このオブジェクトを初期化する。
	 * このメソッドは、このオブジェクトの他の全てのメソッドの実行に先立って実行されている必要がある。
	 */
	public void init() {
        // ユーザコード
        tmpMMachines = new LinkedList<MMachine>();
        mmachines = new LinkedList<MMachine>();
        groups = new TreeMap<Group, List<MMachine>>();
        //Configuration.controller = this;
	}

	public void action() {
    	////
    	// 各キャラクタを、状態更新可能なように準備する。
    	prepareMMachines();
    	////
    	// 各キャラクタの移動を行う
    	performMMachines();
        ////
        // MMachineオブジェクト間の相互作用を実行
   		interactMMachines();
        
        ////
        // 今回のセッションで生じたオブジェクト状態の更新を反映
        commitMMachines();
	}
	
	public void update() {
        ////
        // 今回のセッションで新たに登録された一時領域のDrivantを、Drivant一覧に登録。
        registerNewMMachines();
	}
	
	public void prepareMMachines() {
    	Iterator<MMachine> iMMachines = mmachines.iterator();
    	while (iMMachines.hasNext()) {
    		MMachine cur = iMMachines.next();
    		cur.drivant().prepare();
    	}
    }
	
	public void performMMachines() {
    	Iterator<MMachine> iMMachines = mmachines.iterator();
        while (iMMachines.hasNext()) {
        	MMachine mmachine = iMMachines.next();
        	mmachine.drivant.perform();
        }
    }
	
	public void interactMMachines() {
    	Iterator<MMachine> iMMachines = mmachines.iterator();
        while (iMMachines.hasNext()) {
        	MMachine mmachine = iMMachines.next();
        	mmachine.drivant.beforeInteraction();
        }

        Iterator<Group> iGroups = groups.keySet().iterator();
    	while (iGroups.hasNext()) {
    		Group iGroup = iGroups.next();
    		Iterator<Group> jGroups = groups.tailMap(iGroup).keySet().iterator();
    		while (jGroups.hasNext()) {
    			Group jGroup = jGroups.next();
    			interaction(iGroup, jGroup);
    		}
    	}
    	
    	iMMachines = mmachines.iterator();
        while (iMMachines.hasNext()) {
        	MMachine mmachine = iMMachines.next();
        	mmachine.drivant.afterInteraction();
        }
    }

	protected void collision(Drivant d1, Drivant d2, double distance) {
		if (d1.isCollisionDetectEnabled() && d2.isCollisionDetectEnabled()) {
			if (interactionHandler.collides(d1, d2, distance)) {
				interactionHandler.handleCollision(d1, d2, distance);
			}
		}
	}

	protected void interaction(Group iGroup, Group jGroup) {
		if (interactionHandler == null) {
			return;
		}

		List<MMachine> group1 = groups.get(iGroup); 
		List<MMachine> group2 = groups.get(jGroup);
		
		boolean collides = iGroup.collides(jGroup);
		boolean interacts = iGroup.interacts(jGroup);
		if (collides || interacts) {
			ListIterator<MMachine> iGroup1 = group1.listIterator();
	    	while (iGroup1.hasNext()) {
	    		ListIterator<MMachine> iGroup2;
	    		MMachine mmachine1 = iGroup1.next();
	    		if (iGroup.equals(jGroup)) {
	    			iGroup2 = group2.listIterator(iGroup1.nextIndex());
	    		} else {
	    			iGroup2 = group2.listIterator();
	    		}
	    		while (iGroup2.hasNext()) {
	    			MMachine mmachine2 = iGroup2.next();
    				Drivant d1 = mmachine1.drivant();
    				Drivant d2 = mmachine2.drivant();
    				double distance = distance(d1, d2);
    				if (collides) {
    					collision(d1, d2, distance);
    				}
    				if (interacts) {
    					interact(d1, d2, distance);
    				}
	    		}
	    	}
		}
    }

	public void commitMMachines() {
    	Iterator<MMachine> iMMachines = mmachines.iterator();
    	while (iMMachines.hasNext()) {
    		MMachine cur = iMMachines.next();
    		try {
				cur.drivant().commit();
			} catch (MMachineStateException e) {
				e.printStackTrace();
			}
    	}
    }

	protected void registerNewMMachines() {
		if (tmpMMachines.size() > 0) {
        	Iterator<MMachine> iMMachines = tmpMMachines.iterator();
        	while (iMMachines.hasNext()) {
        		MMachine mmachine = iMMachines.next();

        		Drivant drivant = ((MMachine) mmachine).drivant();
    			
    			Group groupId = drivant.groupId();
    			if (groupId != null) {
    				List<MMachine> mmachinesInGroup= groups.get(groupId);
    				if (mmachinesInGroup == null) {
    					mmachinesInGroup = new LinkedList<MMachine>();
    					groups.put(groupId, mmachinesInGroup);
    				}
    				mmachinesInGroup.add(mmachine);
    			}
        		
        		mmachines.add(0, mmachine);
        	}
        	tmpMMachines.clear();
        }
	}
	
    public void scavengeMMachines() {
        Iterator<MMachine> j = mmachines.iterator();
        while (j.hasNext()) {
        	MMachine mmachine = j.next();
        	Drivant drivant = mmachine.drivant();
        	if (!drivant.isValid()) {
        		unregisterMMachine(mmachine);
        		j.remove();
        		continue;
        	}
        }
	}

    protected void unregisterMMachine(MMachine mmachine) {
    	Drivant drivant = mmachine.drivant();
		mmachine.sprite().dispose();
		Group groupId = drivant.groupId();
		if (groupId != null) {
			List<MMachine> group = groups.get(groupId);
			group.remove(mmachine);
		}
    }
	public List<MMachine> mmachines() {
		return mmachines;
	}
	public void add(MMachine mmachine) {
		tmpMMachines.add(mmachine);
	}
	
	protected void interact(Drivant d1, Drivant d2, double distance) {
		interactionHandler.handleInteraction(d1, d2, distance);
	}
	
	protected double distance(Drivant aDrivant, Drivant bDrivant) {
		return aDrivant.distance(bDrivant);
	}

	public void dump() {
		Avis.logger().debug("MotionController=<" + this + ">");
		Iterator<MMachine> iMMachines = mmachines.iterator();
		while (iMMachines.hasNext()) {
			Avis.logger().debug("cur=<" + iMMachines.next() + ">");
		}
	}
	
	public void interactionHandler(InteractionHandler handler) {
		this.interactionHandler = handler;
	}
}
