package avis.video;

import java.awt.Dimension;
import java.awt.Toolkit;

import avis.base.AException;
import avis.base.AResourceObserver;
import avis.base.Avis;
import avis.spec.ASpriteSpecManager;
import avis.sprite.ASpriteException;

public class AVideoEngine {
	protected static AVideoEngine instance = null;
	protected AScreen screen;
	private ASpriteSpecManager spriteSpecManager;
	private int width;
	private int height;
	protected AVideoEngine() {
	}
	AResourceObserver observer = new AResourceObserver() {
		public void endLoading(String resourceName) {
		}
		public void startLoading(String resourceName) {
		}
	};
	
	public static AVideoEngine instance() {
		synchronized (AVideoEngine.class) {
			if (instance == null) {
				instance = new AVideoEngine();
			}
		}
		return instance;
	}

	public void resourceObserver(AResourceObserver observer) {
		this.observer = observer;
	}
	public void size(int width, int height) {
		this.width = width;
		this.height = height;
	}
	public synchronized AScreen getScreen() throws ASpriteException, AException {
		if (screen == null) {
			screen = createScreen(width, height);
			screen.setSize(width, height);
		}
		return screen;
	}
	
	public synchronized ASpriteSpecManager getSpriteSpecManager() {
		if (spriteSpecManager == null) {
			spriteSpecManager = createSpriteSpecManager(observer);
		}
		return spriteSpecManager;
	}
	
	private ASpriteSpecManager createSpriteSpecManager(AResourceObserver observer) {
		ASpriteSpecManager ret = new ASpriteSpecManager(observer); 
		return ret;
	}

	protected Dimension getDesktopSize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		return screenSize;
	}

	protected AScreen createScreen(int width, int height) throws ASpriteException, AException {
		AScreen screen;
		Dimension desktopSize = getDesktopSize();
		screen = new AScreen(width, height, getSpriteSpecManager());
		Avis.logger().info("Setup: desktop width = <" + desktopSize.width + ">");
		Avis.logger().info("Setup: desktop height = <" + desktopSize.height+ ">");
		screen.setLocation(desktopSize.width / 2 - width / 2, desktopSize.height / 2 - height / 2);
		screen.init();
		return screen;
	}

	public void terminate() {
		if (screen != null) {
			screen.terminate();
		}
		instance = null;
	}

}
