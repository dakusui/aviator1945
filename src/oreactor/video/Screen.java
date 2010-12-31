package oreactor.video;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.VolatileImage;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;

import oreactor.core.Settings;
import oreactor.core.Settings.RenderingMode;
import oreactor.core.Util;
import oreactor.exceptions.OpenReactorRuntimeException;

public class Screen extends JFrame {
	public static enum PlaneType {
		GRAPHICS {
			@Override
			public Plane createPlane(Screen screen, String name) {
				return screen.createGraphicsPlane(name);
			}
		}, 
		PATTTERN {
			@Override
			public Plane createPlane(Screen screen, String name) {
				return screen.createPatternPlane(name);
			}
		}, 
		SPRITE {
			@Override
			public Plane createPlane(Screen screen, String name) {
				return screen.createSpritePlane(name);
			}
		};
		public abstract Plane createPlane(Screen screen, String name);
	}
	public static class PlaneInfo {
		private String name;
		private PlaneType type;
		private Region region;
		
		public PlaneInfo(String name, PlaneType type, Region region) {
			this.name = name;
			this.type = type;
			this.region = region;
		}
		
		public String name() {
			return this.name;
		}
		
		public PlaneType type() {
			return this.type;
		}
		
		public Region region() {
			return this.region;
		}
	}
	
	static enum Strategy {
		VOLATILE {
			@Override
			public void paint(Screen s, Graphics g) {
		    	VolatileImage vImg = (VolatileImage) s.bImage;
		    	if (vImg != null) {
			    	do {
			    		int returnCode = vImg.validate(s.getGraphicsConfiguration());
			    		if (returnCode == VolatileImage.IMAGE_RESTORED) {
			    			// Contents need to be restored
			    			this.renderOffScreen(s, (Graphics2D) g);      // restore contents
			    		} else if (returnCode == VolatileImage.IMAGE_INCOMPATIBLE) {
			    			// old vImg doesn't work with new GraphicsConfig; re-create it
			    			vImg = s.createVolatileImage(s.getWidth(), s.getHeight());
			    			this.renderOffScreen(s, (Graphics2D) g);
			    		}
			    		g.drawImage(vImg, 0, 0, s);
			    	} while (vImg.contentsLost());
		    	} else {
		    		Util.logger().debug("volatile image is currently <null>");
		    	}				
			}
			
			@Override
			public void renderOffScreen(Screen s, Graphics2D gg) {
				if (s.bufferStrategy == null) {
					VolatileImage vImg = (VolatileImage) s.bImage;
					do {
						////
						// 1. VolatileImageの状態を確認し、無効であれば再作成
						if (vImg.validate(s.getGraphicsConfiguration()) ==
							VolatileImage.IMAGE_INCOMPATIBLE) {
							// old vImg doesn't work with new GraphicsConfig; re-create it
							vImg = s.createVolatileImage(s.getWidth(), s.getHeight());
						}
				    	////
				    	// 2. 各表示平面をオフスクリーンバッファへ描画
						s.graphics2d = vImg.createGraphics();
						s.renderPlanes(s.graphics2d);
				    	s.graphics2d.dispose();
					} while (vImg.contentsLost());
				} else {
					s.renderPlanes(gg);
				}
			}
			@Override
			public void render(Screen s) {
	    		// video mode -> full screen
	    		Graphics2D gg = (Graphics2D) s.bufferStrategy.getDrawGraphics();
	        	////
	    		// 1. Render off screen buffer
	    		try {
	    			this.renderOffScreen(s, gg);
	    		} finally {
	    			gg.dispose();
	    		}
	    		
	    		// Flip screen
	            if (!s.bufferStrategy.contentsLost()) {
	                s.bufferStrategy.show();
	            } else {
	                Toolkit.getDefaultToolkit().sync();
	            }	
			}

		},
		BUFFERED {
			@Override
			public void paint(Screen s, Graphics g) {
		    	g.drawImage(s.bImage, 0, 0, s.getWidth(), s.getHeight(), s);
			}

			@Override
			public void render(Screen s) {
	            ////
				// 1. オフスクリーンへの画面の描画
	        	this.renderOffScreen(s, s.graphics2d);
	        	////
	        	// 2. 画面への更新
	    		Graphics g = s.getGraphics();
	    		try {
	    			s.paint(g);
	    		} finally {
	    			g.dispose();
	    		}				
			}

			@Override
			public void renderOffScreen(Screen s, Graphics2D gg) {
				s.renderPlanes(gg);
			}
		};
		abstract void renderOffScreen(Screen s, Graphics2D gg);
		abstract void render(Screen s);
		abstract void paint(Screen s, Graphics g);
		static Strategy figureOut(RenderingMode rm) {
			if (rm == RenderingMode.BUFFERED) {
				return Strategy.BUFFERED;
			} else if (rm == RenderingMode.VOLATILE){
				return Strategy.VOLATILE;
			} else {
				throw new OpenReactorRuntimeException("Unknown mode:<" + rm + "> is given.");
			}
			
		}
	}
	
	/**
	 * A serial version UID.
	 */
	private static final long serialVersionUID = -7060260135859428693L;

	private BufferStrategy bufferStrategy = null;
	
    protected Graphics2D graphics2d;
    
    protected Image    bImage;

	private Settings settings;

	private Strategy strategy;
	
	private Region logical;
	private Region physical;
	private List<Plane> planes;

	private boolean closed;
	
    public Screen(Settings settings) {
    	this.settings = settings;
    	this.strategy = Strategy.figureOut(this.settings.renderingMode());
		this.planes = new LinkedList<Plane>();
		this.closed = false;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closed = true;
			}
		});
    }
    
    private void renderPlanes(Graphics2D g) {
		for (Plane p: this.planes) {
			p.render(g);
		}		
	}

	public void paint(Graphics g) {
    	this.strategy.paint(this, g);
    }

    public void render() {
    	this.strategy.render(this);
    }

	GraphicsPlane createGraphicsPlane(String name) {
		GraphicsPlane ret = new GraphicsPlane(name, physical, logical);
		return ret;
	}
	
	SpritePlane createSpritePlane(String name) {
		SpritePlane ret = new SpritePlane(name, physical, logical);
		return ret;
	}
	
	PatternPlane createPatternPlane(String name) {
		PatternPlane ret = new PatternPlane(name, physical, logical);
		return ret;
	}

	public void createPlane(PlaneInfo info) {
		this.planes.add(info.type().createPlane(this, info.name()));
	}
	public void prepare() {
		for (Plane p: this.planes) {
			p.prepare();
		}		
	}

	public void finish() {
		List<Plane> rev = new LinkedList<Plane>();
		rev.addAll(this.planes);
		Collections.reverse(rev);
		for (Plane p: rev) {
			p.finish();
		}	
	}

	public Iterator<Plane> planes() {
		return this.planes.iterator();
	}

	public boolean isClosed() {
		return this.closed;
	}
}
