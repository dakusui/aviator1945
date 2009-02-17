package siovanus.motion;

import siovanus.SGroup;
import avis.motion.Drivant;
import avis.motion.InteractionHandler;
import avis.motion.MMachine;
import avis.motion.MotionController;
import junit.framework.TestCase;

public class MotionController_Test extends TestCase {
	MMachine_Driver driver;
	MMachine mmachine1;
	MMachine mmachine2;
	
	MotionController controller;
	Drivant drivant2;
	Drivant drivant1;
	
	@Override
	public void setUp() {
		driver = new MMachine_Driver();
		mmachine1 =  driver.createTestMMachine_01();
		mmachine2 =  driver.createTestMMachine_02();
		
		drivant1 = mmachine1.drivant();
		drivant2 = mmachine2.drivant();
		driver.drivant1TimesCollisionDetected = 0;
		driver.drivant2TimesCollisionDetected = 0;
		
		controller = new MotionController();
		controller.interactionHandler(new InteractionHandler() {
			public boolean collides(Drivant d1, Drivant d2, double distance) {
				return true;
			}

			public void handleCollision(Drivant d1, Drivant d2, double distance) {
				driver.drivant1CollisionDetected = true;
				driver.drivant2CollisionDetected = true;
				driver.drivant1TimesCollisionDetected++;
				driver.drivant2TimesCollisionDetected++;
			}

			public void handleInteraction(Drivant d1, Drivant d2,
					double distance) {
			}
			
		});
		controller.init();		
	}

	public void test_collision_01() {
		drivant1.parameters().groupId = null;
		drivant2.parameters().groupId = null;
		
		controller.add(mmachine1);
		controller.add(mmachine2);

		controller.update();
		controller.action();

		assertTrue(!driver.drivant1CollisionDetected);
		assertTrue(!driver.drivant2CollisionDetected);
	}

	public void test_collision_02() {
		drivant1.parameters().groupId = SGroup.Enemy_Aerial;
		drivant1.commit();
		drivant2.parameters().groupId = SGroup.Player_Aerial;
		drivant2.commit();
		controller.add(mmachine1);
		controller.add(mmachine2);
		
		controller.update();
		controller.action();
		
		assertTrue(driver.drivant1CollisionDetected);
		assertEquals(1, driver.drivant1TimesCollisionDetected);
		assertTrue(driver.drivant2CollisionDetected);
		assertEquals(1, driver.drivant2TimesCollisionDetected);
	}

	public void test_collision_03() {
		drivant1.parameters().groupId = SGroup.Enemy_Aerial;
		drivant1.commit();
		drivant2.parameters().groupId = null;
		drivant2.commit();
		controller.add(mmachine1);
		controller.add(mmachine2);
		
		controller.update();
		controller.action();
		
		assertTrue(!driver.drivant1CollisionDetected);
		assertTrue(!driver.drivant2CollisionDetected);
	}
	
	public void test_collision_04() {
		drivant1.parameters().groupId = null;
		drivant1.commit();
		drivant2.parameters().groupId = SGroup.Player_Aerial;
		drivant2.commit();
		controller.add(mmachine1);
		controller.add(mmachine2);
		
		controller.update();
		controller.action();
		
		assertTrue(!driver.drivant1CollisionDetected);
		assertTrue(!driver.drivant2CollisionDetected);
	}
	
	public void test_collision_05() {
		drivant1.parameters().groupId = SGroup.Enemy_Surface;
		drivant1.commit();
		drivant2.parameters().groupId = SGroup.Player_Surface;
		drivant2.commit();
		controller.add(mmachine1);
		controller.add(mmachine2);
		
		controller.update();
		controller.action();
		
		assertTrue(driver.drivant1CollisionDetected);
		assertEquals(1, driver.drivant1TimesCollisionDetected);
		assertTrue(driver.drivant2CollisionDetected);
		assertEquals(1, driver.drivant2TimesCollisionDetected);
	}
}
