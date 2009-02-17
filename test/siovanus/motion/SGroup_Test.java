package siovanus.motion;

import avis.motion.Group;
import siovanus.SGroup;
import junit.framework.TestCase;

public class SGroup_Test extends TestCase {

	public void test_collides_EnemyAerial_EnemySurface() throws Exception {
		Group a = SGroup.Enemy_Aerial;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + "> collides b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + "> collides b=<" + b + ">", !b.collides(a));
	}

	public void test_collides_EnemyAerial_EnemyAerial() throws Exception {
		Group a = SGroup.Enemy_Aerial;
		Group b = SGroup.Enemy_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}

	public void test_collides_EnemySurface_EnemySurafacel() throws Exception {
		Group a = SGroup.Enemy_Surface;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}
	
	public void test_collides_PlayerAerial_PlayerSurface() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Player_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}

	public void test_collides_PlayerAerial_PlayerAerial() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Player_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}
	
	public void test_collides_PlayerSurface_PlayerSurface() throws Exception {
		Group a = SGroup.Player_Surface;
		Group b = SGroup.Player_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}

	public void test_collides_PlayerSurface_EnemySurface() throws Exception {
		Group a = SGroup.Player_Surface;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", b.collides(a));
	}

	public void test_collides_PlayerAerial_EnemyAeril() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Enemy_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", b.collides(a));
	}
	public void test_collides_PlayerAerial_EnemySurface() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}
	public void test_collides_PlayerSurface_EnemyAerial() throws Exception {
		Group a = SGroup.Player_Surface;
		Group b = SGroup.Enemy_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.collides(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.collides(a));
	}

	public void test_interacts_EnemyAerial_EnemySurface() throws Exception {
		Group a = SGroup.Enemy_Aerial;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + "> collides b=<" + b + ">", !a.interacts(b));
		assertTrue("a=<" + a + "> collides b=<" + b + ">", !b.interacts(a));
	}

	public void test_interacts_EnemyAerial_EnemyAerial() throws Exception {
		Group a = SGroup.Enemy_Aerial;
		Group b = SGroup.Enemy_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.interacts(a));
	}

	public void test_interacts_EnemySurface_EnemySurafacel() throws Exception {
		Group a = SGroup.Enemy_Surface;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.interacts(a));
	}
	
	public void test_interacts_PlayerAerial_PlayerSurface() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Player_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.interacts(a));
	}

	public void test_interacts_PlayerAerial_PlayerAerial() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Player_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.interacts(a));
	}
	
	public void test_interacts_PlayerSurface_PlayerSurface() throws Exception {
		Group a = SGroup.Player_Surface;
		Group b = SGroup.Player_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", !a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", !b.interacts(a));
	}

	public void test_interacts_PlayerSurface_EnemySurface() throws Exception {
		Group a = SGroup.Player_Surface;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", b.interacts(a));
	}

	public void test_interacts_PlayerAerial_EnemyAeril() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Enemy_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", b.interacts(a));
	}
	public void test_interacts_PlayerAerial_EnemySurface() throws Exception {
		Group a = SGroup.Player_Aerial;
		Group b = SGroup.Enemy_Surface;
		assertTrue("a=<" + a + ">, b=<" + b + ">", a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", b.interacts(a));
	}
	public void test_interacts_PlayerSurface_EnemyAerial() throws Exception {
		Group a = SGroup.Player_Surface;
		Group b = SGroup.Enemy_Aerial;
		assertTrue("a=<" + a + ">, b=<" + b + ">", a.interacts(b));
		assertTrue("a=<" + a + ">, b=<" + b + ">", b.interacts(a));
	}

}
