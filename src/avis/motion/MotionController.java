package avis.motion;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedMap;
import java.util.TreeMap;

import avis.base.Avis;

/**
 * ������<code>MMachine</code>�I�u�W�F�N�g�̓������т����`���ňꊇ���ď�������@�\��񋟂���N���X�B
 * @author hukai
 */
public final class MotionController {
	private InteractionHandler interactionHandler = null;
	
	/**
	 * ���̃I�u�W�F�N�g���Ǘ�����<code>MMachine</code>�I�u�W�F�N�g�̃��X�g�B
	 */
	private List<MMachine> mmachines;
	/**
	 * ���̃I�u�W�F�N�g���Ǘ�����<code>MMachine</code>�I�u�W�F�N�g���ꎞ�I�ɕۑ����郊�X�g�B
	 * �ύX���J�n����Ă���A�m��(<code>commit</code>)�����܂ł̊Ԃ̈ꎞ�I�ȏ�Ԃ��i�[����̂Ɏg�p����B
	 */
	private List<MMachine> tmpMMachines;
	/**
	 *  �G���������ʂ��邽�߂̃}�b�v
	 */
	private SortedMap<Group, List<MMachine>> groups;
	/**
	 * ���̃N���X�̃I�u�W�F�N�g�𐶐�����B
	 */
	public MotionController() {
	}
	/**
	 * ���̃I�u�W�F�N�g������������B
	 * ���̃��\�b�h�́A���̃I�u�W�F�N�g�̑��̑S�Ẵ��\�b�h�̎��s�ɐ旧���Ď��s����Ă���K�v������B
	 */
	public void init() {
        // ���[�U�R�[�h
        tmpMMachines = new LinkedList<MMachine>();
        mmachines = new LinkedList<MMachine>();
        groups = new TreeMap<Group, List<MMachine>>();
        //Configuration.controller = this;
	}

	public void action() {
    	////
    	// �e�L�����N�^���A��ԍX�V�\�Ȃ悤�ɏ�������B
    	prepareMMachines();
    	////
    	// �e�L�����N�^�̈ړ����s��
    	performMMachines();
        ////
        // MMachine�I�u�W�F�N�g�Ԃ̑��ݍ�p�����s
   		interactMMachines();
        
        ////
        // ����̃Z�b�V�����Ő������I�u�W�F�N�g��Ԃ̍X�V�𔽉f
        commitMMachines();
	}
	
	public void update() {
        ////
        // ����̃Z�b�V�����ŐV���ɓo�^���ꂽ�ꎞ�̈��Drivant���ADrivant�ꗗ�ɓo�^�B
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
