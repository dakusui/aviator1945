package openreactor.nu64;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oreactor.annotations.ExtensionPoint;
import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader;
import oreactor.io.ResourceLoader.MidiData;
import oreactor.io.ResourceLoader.SoundData;
import oreactor.io.ResourceMonitor;
import oreactor.joystick.InputDevice;
import oreactor.joystick.JoystickEngine.Stick;
import oreactor.joystick.JoystickEngine.Trigger;
import oreactor.video.Plane;
import oreactor.video.PlaneDesc;
import oreactor.video.Screen;
import oreactor.video.VideoEngine;
import oreactor.video.graphics.GraphicsPlane;
import oreactor.video.pattern.Pattern;
import oreactor.video.pattern.PatternPlane;
import oreactor.video.sprite.SpritePlane;
import oreactor.video.sprite.SpriteSpec;

public class Nu64Reactor extends Reactor implements ResourceMonitor {
	public static abstract class Action {
		public static final Action NullAction = new Action() {
			@Override
			public void perform(Context c) {
			}
		};
		protected Action next = this;
		public abstract void perform(Context c) throws OpenReactorException;
		public Action next() {
			return next ;
		}
	}
	
	protected Map<String, SpriteSpec> spriteSpecs = new HashMap<String, SpriteSpec>();
	protected Map<Integer, Pattern> patterns = new HashMap<Integer, Pattern>();
	protected Action action = null;
	
	@Override
	protected Settings loadSettings() throws OpenReactorException {
		Settings settings = super.loadSettings();
		double w = 1024;
		double h = 768;
		double pw = patternWidth();
		double ph = patternHeight();
		{
			PlaneDesc desc = new PlaneDesc("graphics", PlaneDesc.Type.Graphics);
			desc.width(w);
			desc.height(h);
			settings.addPlaneDesc(desc);
		}
		{
			PlaneDesc desc = new PlaneDesc("pattern", PlaneDesc.Type.Pattern);
			desc.width(w);
			desc.height(h);
			desc.put(PlaneDesc.PATTERNWIDTH_KEY, pw);
			desc.put(PlaneDesc.PATTERNHEIGHT_KEY, ph);
			settings.addPlaneDesc(desc);
		}
		{
			PlaneDesc desc = new PlaneDesc("sprite", PlaneDesc.Type.Sprite);
			desc.width(w);
			desc.height(h);
			settings.addPlaneDesc(desc);
		}
		return settings;
	}

	@Override 
	protected Context initialize(Settings settings) throws OpenReactorException {
		Context ret = super.initialize(settings);
		this.action = action();
		((Nu64ResourceLoader)ret.getResourceLoader()).addMonitor(this);
		((Nu64ResourceLoader)ret.getResourceLoader()).addMonitor(ret.getSoundEngine());
		((Nu64ResourceLoader)ret.getResourceLoader()).addMonitor(ret.getMusicEngine());
		return ret;
	}

	protected GraphicsPlane graphicsplane(Context c) {
		return graphicsplane(c, "graphics");
	}
	   
	protected GraphicsPlane graphicsplane(Context c, String name) {
		GraphicsPlane ret = null; 
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof GraphicsPlane && p.name().equals(name)) {
				ret = (GraphicsPlane)p;
			}
		}
		return ret;
	}
	
	protected PatternPlane patternplane(Context c) {
		return patternplane(c, "pattern");
	}
	
	protected SpritePlane spriteplane(Context c, String name) {
		SpritePlane ret = null; 
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof SpritePlane && p.name().equals(name)) {
				ret = (SpritePlane)p;
			}
		}
		return ret;
	}

	protected SpritePlane spriteplane(Context c) {
		return spriteplane(c, "sprite");
	}
	
	protected PatternPlane patternplane(Context c, String name) {
		PatternPlane ret = null; 
		VideoEngine ve = c.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof PatternPlane && p.name().equals(name)) {
				ret = (PatternPlane)p;
			}
		}
		return ret;
	}
	
	private InputDevice joystick(Context c) {
		return c.getJoystickEngine().devices().get(0);
	}
	
	protected final Stick stick(Context c) {
		Stick ret = joystick(c).stick();
		return ret;
	}
	
	protected final boolean trigger(Context c) {
		return trigger(c, Trigger.ANY);
	}
	protected final boolean trigger(Context c, Trigger t) {
		boolean ret = joystick(c).trigger(t);
		return ret;
	}
	@Override
	public void numPatterns(int numPatterns) {
		// does nothing
		System.err.println("Loading " + numPatterns + " patterns.");
	}
	
	@Override
	public void patternLoaded(Pattern pattern) {
		patterns.put(pattern.num(), pattern);
		System.err.println("  Pattern:<" + pattern.num() + "> is loaded.");
	}
	
	@Override
	public void numSpriteSpecs(int numSpriteSpecs) {
		// does nothing
		System.err.println("Loading " + numSpriteSpecs + " sprite specs.");
	}
	
	@Override
	public void spriteSpecLoaded(SpriteSpec spriteSpec) {
		spriteSpecs.put(spriteSpec.name(), spriteSpec);
		System.err.println("  SpriteSpec:<" + spriteSpec.name() + "> is loaded.");
	}

	@ExtensionPoint
	protected Action action() {
		return Action.NullAction;
	}

	@Override
	@ExtensionPoint
	protected void run(Context c) throws OpenReactorException {
		this.action.perform(c);
		this.action = this.action.next();
		if (this.action == null) {
			exit();
		}
	}
	
	public Class<? extends ResourceLoader> resourceLoaderClass() {
		return Nu64ResourceLoader.class;
	}

	public void playwave(Context c, String name) throws OpenReactorException {
		c.getSoundEngine().player(name).start();
	}

	@Override
	public void numSoundClips(int numSoundClips) {
		System.err.println("Loading " + numSoundClips + " soud clips.");
	}

	@Override
	public void soundClipLoaded(String name, SoundData soundData) {
		System.err.println("  Sound data:<" + soundData.resourceUrl() + "> is loaded as '" + name + "'.");
	}

	@Override
	public void numMidiClips(int numClips) {
	}

	@Override
	public void midiClipLoaded(String name, MidiData midiData) {
	}
	
	@ExtensionPoint
	public int patternWidth() {
		return 32;
	}
	@ExtensionPoint
	public int patternHeight() {
		return 32;
	}
}
