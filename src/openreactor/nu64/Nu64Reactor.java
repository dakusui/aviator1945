package openreactor.nu64;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oreactor.annotations.ExtensionPoint;
import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;
import oreactor.io.ResourceLoader.MidiData;
import oreactor.io.ResourceLoader.SoundData;
import oreactor.io.ResourceLoader;
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
	private Context context;
	public static abstract class Action {
		public static final Action NullAction = new Action() {
			@Override
			public void perform() {
			}
		};
		protected Action next = this;
		public abstract void perform() throws OpenReactorException;
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
		ret.getResourceLoader().addMonitor(this);
		ret.getResourceLoader().addMonitor(ret.getSoundEngine());
		ret.getResourceLoader().addMonitor(ret.getMusicEngine());
		return ret;
	}

	protected GraphicsPlane graphicsplane() {
		return graphicsplane("graphics");
	}
	   
	protected GraphicsPlane graphicsplane(String name) {
		GraphicsPlane ret = null; 
		VideoEngine ve = context.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof GraphicsPlane && p.name().equals(name)) {
				ret = (GraphicsPlane)p;
			}
		}
		return ret;
	}
	
	protected PatternPlane patternplane() {
		return patternplane("pattern");
	}
	
	protected SpritePlane spriteplane(String name) {
		SpritePlane ret = null; 
		VideoEngine ve = context.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof SpritePlane && p.name().equals(name)) {
				ret = (SpritePlane)p;
			}
		}
		return ret;
	}

	protected SpritePlane spriteplane() {
		return spriteplane("sprite");
	}
	
	protected PatternPlane patternplane(String name) {
		PatternPlane ret = null; 
		VideoEngine ve = context.getVideoEngine();
		Screen s = ve.screen();
		List<Plane> planes = s.planes();
		for (Plane p : planes) {
			if (p instanceof PatternPlane && p.name().equals(name)) {
				ret = (PatternPlane)p;
			}
		}
		return ret;
	}
	
	private InputDevice joystick() {
		return context.getJoystickEngine().devices().get(0);
	}
	
	protected final Stick stick() {
		Stick ret = joystick().stick();
		return ret;
	}
	
	protected final boolean trigger() {
		return trigger(Trigger.ANY);
	}
	protected final boolean trigger(Trigger t) {
		boolean ret = joystick().trigger(t);
		return ret;
	}
	@Override
	public void numPatterns(int numPatterns) {
		logger().debug("Loading " + numPatterns + " patterns.");
	}
	
	@Override
	public void patternLoaded(Pattern pattern) {
		patterns.put(pattern.num(), pattern);
		logger().debug("  Pattern:<" + pattern.num() + "> is loaded.");
	}
	
	@Override
	public void numSpriteSpecs(int numSpriteSpecs) {
		logger().debug("Loading " + numSpriteSpecs + " sprite specs.");
	}
	
	@Override
	public void spriteSpecLoaded(SpriteSpec spriteSpec) {
		spriteSpecs.put(spriteSpec.name(), spriteSpec);
		logger().debug("  SpriteSpec:<" + spriteSpec.name() + "> is loaded.");
	}

	@ExtensionPoint
	protected Action action() {
		return Action.NullAction;
	}

	@Override
	protected final void run(Context c) throws OpenReactorException {
		this.currentContext(c);
		try {
			run();
		} finally {
			this.currentContext(null);
		}
	}
	
	@ExtensionPoint
	protected void run() throws OpenReactorException {
		this.action.perform();
		this.action = this.action.next();
		if (this.action == null) {
			exit();
		}
	}
	
	private void currentContext(Context context) {
		this.context = context;
	}

	public void playwave(String soundclipName) throws OpenReactorException {
		context.getSoundEngine().player(soundclipName).start();
	}

	public void playmidi(String midiclipName) throws OpenReactorException {
		context.getMusicEngine().player(midiclipName).play();
	}
	

	@Override
	public void numSoundClips(int numSoundClips) {
		logger().debug("Loading " + numSoundClips + " soud clips.");
	}

	@Override
	public void soundClipLoaded(String name, SoundData soundData) {
		logger().debug("  Sound data:<" + soundData.resourceUrl() + "> is loaded as '" + name + "'.");
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

	protected Context context() {
		return context;
	}
	
	protected void loadConfig(String url) throws OpenReactorException {
		ResourceLoader loader = context().getResourceLoader();
		loader.loadConfigFromUrl(url);
	}
}
