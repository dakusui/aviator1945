package avis.video;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.DisplayMode;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import siovanus.Siovanus;
import avis.base.AException;
import avis.base.Avis;
import avis.base.Configuration;
import avis.input.ACompositeDevice;
import avis.input.AInputDevice;
import avis.input.AJoypadDevice;
import avis.input.AKeyboardDevice;
import avis.input.APS3JoystickDevice;
import avis.spec.ASpriteSpec;
import avis.spec.ASpriteSpecManager;
import avis.sprite.AMessageSprite;
import avis.sprite.ASprite;
import avis.sprite.ASpriteException;
import avis.sprite.IASpriteObserver;


public class AScreen extends JFrame implements IAPlaneObserver, IASpriteObserver {
	private static final String FOREGROUND_PLANE = "FOREGROUND_PLANE";
	private static final String BACKGROUND_PLANE = "BACKGROUND_PLANE";
	protected APlane foregroundPlane = null;
	protected APlane backgroundPlane = null;
	
    /**
     * <code>serialVersionUID</code>:シリアルバージョン
     */
    private static final long serialVersionUID = -4359479588246032825L;
    /**
     * オフスクリーンバッファ
     */
    protected Image    bImage;
    /**
     * 描画プレーン
     */
    protected List<APlane> planes;

    /**
     * ジョイスティックオブジェクト
     */
    protected AInputDevice stick;
    protected Graphics2D graphics2d;
    protected String bgImageResourceName;
    protected List<ASpriteSpec> specs;
    protected int messagePriority = 9999;
    protected ASpriteSpecManager spriteSpecManager;
    protected boolean closed;
    private GraphicsDevice device;
    private BufferStrategy bufferStrategy = null;
    
    public AScreen(int width, int height, ASpriteSpecManager manager) {
        super();
        this.planes = new LinkedList<APlane>();
        this.specs = new LinkedList<ASpriteSpec>();
        this.spriteSpecManager = manager;
        this.closed = false;
        setSize(width, height);
        setResizable(false);
        setVisible(false);
        
        addWindowListener(new WindowAdapter() {
        	
			@Override
        	public void windowClosing(WindowEvent e) {
        		closed = true;
        	}
        });
        Avis.logger().info("width=" + width + ", height=" + height);
    }

    public boolean isClosed() {
    	return closed;
    }
    
    public APlane createPlane(String name, int priority, Image backgroundImage) throws ASpriteException {
		AViewport viewport = new AViewport(this);
		ABackground background = null;
		background = new ABackground(backgroundImage, viewport);
		APlane newPlane = new APlane(name, viewport, background, this.getWidth(), this.getHeight(), priority, this);
		planes.add(newPlane);
		return newPlane;
    }

    public List<APlane> planes() {
    	return planes;
    }

	public void init() throws AException, ASpriteException {
        try {
			if (Configuration.videoMode == Configuration.VideoMode.NORMAL) {
				Avis.logger().info("normal video mode");
				device = null;
				setVisible(true);
				if (Configuration.renderingMode == Configuration.RenderingMode.VOLATILE) {
					bImage = createVolatileImage((int)(getWidth() * 1.00), (int)(getHeight() * 1.00));
				} else {
					bImage = createImage((int)(getWidth() * 1.00), (int)(getHeight() * 1.00));
				}
		        graphics2d = (Graphics2D) bImage.getGraphics();
			} else {
				int w = getWidth();
				int h = getHeight();
				setVisible(false);
		        GraphicsEnvironment ge = GraphicsEnvironment
		        .getLocalGraphicsEnvironment();
		        device = ge.getDefaultScreenDevice();
		        Avis.logger().statistics("Available vram=<" + device.getAvailableAcceleratedMemory() + ">");
		        if (device.isFullScreenSupported()) {
		        	Avis.logger().info("go fullscreen");
				    setUndecorated(true); // タイトルバー・ボーダー非表示
				
				    // 必要ならマウスカーソルを消す
				    Image cursorImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
				    Graphics g;
				    (g = cursorImage.getGraphics()).clearRect(0, 0, 1, 1);
				    g.dispose();
				    Cursor cursor = Toolkit.getDefaultToolkit().createCustomCursor(
				    		cursorImage,
				            new Point(), "");
				    setCursor(cursor);
				    // フルスクリーン化！
				    device.setFullScreenWindow(this);
				    // ディスプレイモードの変更はフルスクリーン化後
				    // 変更可能なディスプレイモードしか使えない
				    // 640x480,800x600,1024x768の32bitあたりが妥当
				    DisplayMode[] modes = device.getDisplayModes();
				    int d = DisplayMode.BIT_DEPTH_MULTI;
				    int r = DisplayMode.REFRESH_RATE_UNKNOWN;
				    DisplayMode modeChosen = null;
				    
				    for (int i = 0; i < modes.length; i++) {
				    	Avis.logger().debug("mode=<" + modes[i].getWidth() +"," + modes[i].getHeight() + "," + modes[i].getBitDepth() + "," + modes[i].getRefreshRate() + ">");
				    	if (modes[i].getWidth() >= w && modes[i].getHeight() >= h && modes[i].getBitDepth() >= d && modes[i].getRefreshRate() >= r) {
				    		if (modeChosen == null || (modeChosen.getWidth() >= modes[i].getWidth() && modeChosen.getHeight() >= modes[i].getHeight() && modeChosen.getBitDepth() <=modes[i].getBitDepth() && modeChosen.getRefreshRate() <= modes[i].getRefreshRate() )) {
				    			modeChosen = modes[i];
				    		}
				    	}
				    }
				    if (device.isDisplayChangeSupported()) {
				    	Avis.logger().info("width=<" + w + ">");
				    	Avis.logger().info("height=<" + h + ">");
				    	if (modeChosen != null) {
				            device.setDisplayMode(modeChosen);
						    createBufferStrategy(2);
						    bufferStrategy = getBufferStrategy();
					    	Avis.logger().statistics("mode(width,height,depth,refresh rate)=<" + modeChosen.getWidth() + "," + modeChosen.getHeight() + "," + modeChosen.getBitDepth() + "," + modeChosen.getRefreshRate() + ">");
				    	} else {
				    		Avis.logger().info("There is no applicable screen mode.");
				    	}
				    } else {
				    	terminate();
				    }
		        	Avis.logger().info("full screen supported=<" + device.isFullScreenSupported() + ">");
		        	Avis.logger().info("display change supported=<" + device.isDisplayChangeSupported() + ">"); 
		        } else {
		        	Avis.logger().info("not able to go fullscreen");
		        	Avis.logger().info("full screen supported=<" + device.isFullScreenSupported() + ">");
		        	Avis.logger().info("display change supported=<" + device.isDisplayChangeSupported() + ">"); 
		        	device = null;
		        }
			}
		} finally {
			if (graphics2d == null && Configuration.renderingMode == Configuration.RenderingMode.BUFFERED) {
				setVisible(true);
			    this.toFront();
		        bImage = createImage((int)(getWidth() * 1.00), (int)(getHeight() * 1.00));
				graphics2d = (Graphics2D) bImage.getGraphics();
				if (bufferStrategy != null) {
					bufferStrategy.dispose();
					bufferStrategy = null;
				}
			}
			if (Configuration.renderingMode == Configuration.RenderingMode.VOLATILE) {
				setVisible(true);
				bImage = createVolatileImage((int)(getWidth() * 1.00), (int)(getHeight() * 1.00));
				graphics2d = (Graphics2D) bImage.getGraphics();
				Avis.logger().debug("Wndows mode is selected.");
			}
			Avis.logger().info("Device setup:bImage=<" + bImage + ">, graphics2d=<" + graphics2d + ">, bufferStrategy=<" + bufferStrategy + ">");
		}

		if (bufferStrategy != null) {
			Avis.logger().statistics("Window mode=<Full screen mode>");
		} else {
			Avis.logger().statistics("Window mode=<Normal window mode>");
		}
		if (Configuration.renderingMode == Configuration.RenderingMode.VOLATILE) {
			Avis.logger().statistics("Rendering mode=<Volatile image>");
		} else {
			Avis.logger().statistics("Rendering mode=<Buffered image>");
		}
        Avis.logger().info("Setup: bImage=<" + bImage + ">");
        stick = new ACompositeDevice();
        if (Configuration.joystickMode != Configuration.JoystickMode.DISABLED) {
	        try {
	        	Avis.logger().info("System property java.library.path=<" + System.getProperty("java.library.path") + ">");
	        	if (Configuration.joystickMode == Configuration.JoystickMode.JOYPAD) {
	        		((ACompositeDevice)stick).add(new AJoypadDevice());
	        	} else if (Configuration.joystickMode == Configuration.JoystickMode.PS3) {
	        		((ACompositeDevice)stick).add(new APS3JoystickDevice());
	        	}
			} catch (IOException e) {
				Avis.logger().info("No joystick found");
			}
        }
		AKeyboardDevice keyboardDevice;
		((ACompositeDevice)stick).add(keyboardDevice = new AKeyboardDevice());
        addKeyListener(keyboardDevice);

		backgroundPlane = loadPlaneFromResource(BACKGROUND_PLANE, 0, this.bgImageResourceName);
		backgroundPlane.viewport().zoom(1.0);
		
		foregroundPlane = createPlane(FOREGROUND_PLANE, 10000, null);
	}
	
	public APlane loadPlaneFromResource(String planeName, int priority, String resourceName) throws AException, ASpriteException {
    	InputStream is = 
    		resourceName != null ? Avis.openUrl(resourceName)
    				             : null;
    	try {
    		return loadPlane(planeName, priority, is);
    	} catch (AException e) {
    		e.printStackTrace();
    		throw e;
    	} finally {
    		if (is != null) {
    			Avis.closeStream(is);
    		}
    	}
	}
	
	public APlane loadPlane(String planeName, int priority, InputStream is) throws AException, ASpriteException {
        Image image = is != null ? Avis.readImage(is)
        		                 : null;
        
        APlane ret = createPlane(planeName, priority, image);
        return ret;
    }
	
	// rendering to the image
	void renderOffscreen(Graphics2D gg) {
		if (bufferStrategy == null && Configuration.renderingMode == Configuration.RenderingMode.VOLATILE) {
			VolatileImage vImg = (VolatileImage) bImage;
			do {
				////
				// 1. VolatileImageの状態を確認し、無効であれば再作成
				if (vImg.validate(getGraphicsConfiguration()) ==
					VolatileImage.IMAGE_INCOMPATIBLE)
				{
					// old vImg doesn't work with new GraphicsConfig; re-create it
					vImg = createVolatileImage(getWidth(), getHeight());
				}
		    	////
		    	// 2. 各表示平面をオフスクリーンバッファへ描画
				graphics2d = vImg.createGraphics();
		    	Iterator<APlane> iPlanes = planes.iterator();
		    	while (iPlanes.hasNext()) {
		    		APlane cur = iPlanes.next();
		    		cur.paint(graphics2d);
		    	}
		    	graphics2d.dispose();
			} while (vImg.contentsLost());
		} else {
	    	Iterator<APlane> iPlanes = planes.iterator();
	    	while (iPlanes.hasNext()) {
	    		APlane cur = iPlanes.next();
	    		cur.paint(gg);
	    	}
		}
	}

    public void paint(Graphics g) {
		if (Configuration.renderingMode == Configuration.RenderingMode.VOLATILE) {
	    	VolatileImage vImg = (VolatileImage) bImage;
	    	if (vImg != null) {
		    	do {
		    		int returnCode = vImg.validate(getGraphicsConfiguration());
		    		if (returnCode == VolatileImage.IMAGE_RESTORED) {
		    			// Contents need to be restored
		    			renderOffscreen((Graphics2D) g);      // restore contents
		    		} else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
		    			// old vImg doesn't work with new GraphicsConfig; re-create it
		    			vImg = createVolatileImage(getWidth(), getHeight());
		    			renderOffscreen((Graphics2D) g);
		    		}
		    		g.drawImage(vImg, 0, 0, this);
		    	} while (vImg.contentsLost());
	    	} else {
	    		Avis.logger().debug("volatile image is currently <null>");
	    	}
		} else {
	    	g.drawImage(bImage, 0, 0, this.getWidth(), this.getHeight(), this);
		}
    }

    public void render() {
    	if (bufferStrategy == null) {
        	////
        	// 1. オフスクリーンへの画面の描画
        	renderOffscreen(graphics2d);
        	////
        	// 2. 画面への更新
    		Graphics g = getGraphics();
    		try {
    			paint(g);
    		} finally {
    			g.dispose();
    		}
    	} else {
    		// video mode -> full screen
    		Graphics2D gg = (Graphics2D) bufferStrategy.getDrawGraphics();
        	////
        	// 1. オフスクリーンへの画面の描画
    		try {
    			renderOffscreen(gg);
    		} finally {
    			gg.dispose();
    		}
    		
    		// オフスクリーンバッファの切り替え
            if (!bufferStrategy.contentsLost()) {
                bufferStrategy.show();
            } else {
            	Avis.logger().info("BufferStrategy Contents Lost");
                Toolkit.getDefaultToolkit().sync();
            }
    	}
    }
    
    public AInputDevice stick() {
        return stick;
    }

	public APlane backgroundPlane() {
		return backgroundPlane;
	}
	public APlane foregroundPlane() {
		return this.foregroundPlane;
	}

	public void clear() {
		Iterator<APlane> iPlanes = planes.iterator();
		while (iPlanes.hasNext()) {
			APlane cur = iPlanes.next();
			cur.clear();
		}
	}
	
	public void priorityChanged(APlane plane) {
		Collections.sort(planes);
	}

	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 * @param durationToDisplay 表示する時間(単位:ticks)
	 * @return メッセージの表示に使用したスプライト
	 */
	public AMessageSprite message(String message, int x, int y, Color c, float fontSize, int durationToDisplay) {
		return message(message, x, y, c, fontSize, durationToDisplay, Siovanus.SPRITESPEC_DEFAULT_MESSAGE);
	}
	
	public AMessageSprite message(String message, int x, int y, Color c, float fontSize, int durationToDisplay, String messageSpecName) {
		AMessageSprite ret = null;
		try {
			ret = (AMessageSprite) createSprite(foregroundPlane, messageSpecName, messagePriority );
			if (fontSize != -1) {
				ret.size(fontSize);
			}
			ret.plane(foregroundPlane);
			ret.color(c);
			ret.lifetime(durationToDisplay);
			ret.message(message);
			ret.put(x, y);
		} catch (AException e) {
			// AMessageSpriteの表示で例外が送出されることはないため、なにもしないで続行
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 * @param durationToDisplay 表示する時間(単位:ticks)
	 * @return
	 */
	public AMessageSprite message(String message, int x, int y, Color c, float fontSize) {
		return message(message, x, y, c, fontSize, -1);
	}


	public ASprite createSprite(
			APlane plane, 
			String spriteSpecName,
			int priority
			) throws AException {
		Avis.logger().info("Creating sprite from spec named=<" + spriteSpecName + ">");
		ASprite ret = plane.createSprite(spriteSpecManager.getSpriteSpec(spriteSpecName), priority);
		ret.visible(true);
		return ret;
	}

	public void disposed(ASprite sprite) {
		if (sprite instanceof AMessageSprite) {
			sprite.spec();
			specs.remove(sprite);
		}
	}

	public void setVisible(ASprite sprite, boolean visible) {
		// does nothing
	}


	public void priorityChanged(ASprite sprite) {
		// does nothing
	}
	
	public void terminate() {
		if (device != null) {
			Window w = device.getFullScreenWindow();
			if (w != null) {
				w.dispose();
			}
			device.setFullScreenWindow(null);
		}
	}
}