package mu64;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mu64.motion.MotionEngine;
import mu64.motion.MotionProvider;
import oreactor.annotations.ExtensionPoint;
import oreactor.core.Context;
import oreactor.core.Reactor;
import oreactor.core.Settings;
import oreactor.exceptions.OpenReactorException;
import oreactor.exceptions.OpenReactorExitException;
import oreactor.io.ResourceLoader;
import oreactor.io.ResourceMonitor;
import oreactor.io.ResourceLoader.MidiData;
import oreactor.io.ResourceLoader.SoundData;
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

public class Mu64Reactor extends Reactor implements ResourceMonitor {
	public static abstract class Action {
		public static final Action NullAction = new Action() {
			@Override
			public void perform() {
			}
		};
		protected Action next = this;
		public Action next() {
			return next ;
		}
		public abstract void perform() throws OpenReactorException;
	}
	private Context context;
	
	private boolean firstTime = true;
	private MotionEngine motionEngine;
	private Map<Integer, Pattern> patterns = new HashMap<Integer, Pattern>();
	private boolean running = true;
	private Map<String, SpriteSpec> spriteSpecs = new HashMap<String, SpriteSpec>();

	private int ticks = 0;

	protected Action action = null;
	
	@Override
	public void midiClipLoaded(String name, MidiData midiData) {
	}

	@Override
	public void numMidiClips(int numClips) {
	}

	@Override
	public void numPatterns(int numPatterns) {
		logger().debug("Loading " + numPatterns + " patterns.");
	}
	@Override
	public void numSoundClips(int numSoundClips) {
		logger().debug("Loading " + numSoundClips + " soud clips.");
	}
	
	@Override
	public void numSpriteSpecs(int numSpriteSpecs) {
		logger().debug("Loading " + numSpriteSpecs + " sprite specs.");
	}
	
	@ExtensionPoint
	public int patternHeight() {
		return 32;
	}

	@Override
	public void patternLoaded(Pattern pattern) {
		try {
			patternplane().bind(pattern);
			patterns.put(pattern.num(), pattern);
		} catch (OpenReactorException e) {
			logger().warn("Failed to bind a pattern:<" + pattern + ">");
		}
		logger().debug("  Pattern:<" + pattern.num() + "> is loaded.");
	}

	@ExtensionPoint
	public int patternWidth() {
		return 32;
	}
	
	public void playmidi(String midiclipName) throws OpenReactorException {
		context.getMusicEngine().player(midiclipName).play();
	}
	
	public void playwave(String soundclipName) throws OpenReactorException {
		context.getSoundEngine().player(soundclipName).start();
	}
	
	@Override
	public void soundClipLoaded(String name, SoundData soundData) {
		logger().debug("  Sound data:<" + soundData.resourceUrl() + "> is loaded as '" + name + "'.");
	}
	
	public SpritePlane spriteplane() {
		return spriteplane("sprite");
	}
	public SpriteSpec spritespec(String name) {
		return spriteSpecs.get(name);
		
	}
	@Override
	public void spriteSpecLoaded(SpriteSpec spriteSpec) {
		spriteSpecs.put(spriteSpec.name(), spriteSpec);
		logger().debug("  SpriteSpec:<" + spriteSpec.name() + "> is loaded.");
	}
	private void currentContext(Context context) {
		this.context = context;
	}
	private InputDevice joystick() {
		return context.getJoystickEngine().devices().get(0);
	}
	
	@ExtensionPoint
	protected Action action() {
		return Action.NullAction;
	}
	
	protected Context context() {
		return context;
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

	@Override 
	protected Context initialize(Settings settings) throws OpenReactorException {
		Context ret = super.initialize(settings);
		this.action = action();
		this.motionEngine = new MotionEngine(this);
		this.motionEngine.initialize(ret);
		this.motionEngine.setProvider(newMotionProvider());
		ret.getResourceLoader().addMonitor(this);
		ret.getResourceLoader().addMonitor(ret.getSoundEngine());
		ret.getResourceLoader().addMonitor(ret.getMusicEngine());
		return ret;
	}

	protected final boolean isFirstTime() {
		return this.firstTime;
	}
	
	protected void loadConfig(String url) throws OpenReactorException {
		ResourceLoader loader = context().getResourceLoader();
		loader.loadConfigFromUrl(url);
	}
	
	@Override
	protected Settings loadSettings() throws OpenReactorException {
		Settings settings = super.loadSettings();
		double pw = patternWidth();
		double ph = patternHeight();
		{
			PlaneDesc desc = new PlaneDesc("graphics", PlaneDesc.Type.Graphics);
			desc.width(this.graphicsplaneWidth());
			desc.height(this.graphicsplaneHeight());
			this.addPlaneDesc(desc);
		}
		{
			PlaneDesc desc = new PlaneDesc("pattern", PlaneDesc.Type.Pattern);
			desc.width(this.patternplaneWidth());
			desc.height(this.patternplaneHeight());
			desc.put(PlaneDesc.PATTERNWIDTH_KEY, pw);
			desc.put(PlaneDesc.PATTERNHEIGHT_KEY, ph);
			this.addPlaneDesc(desc);
		}
		{
			PlaneDesc desc = new PlaneDesc("sprite", PlaneDesc.Type.Sprite);
			// Actually, sprite plane does not recognize its width and height.
			// But I set them to the same values the enclosing screen because
			// I just felt unconfortable to set them to 0 ;-).
			desc.width(this.screenWidth());
			desc.height(this.screenHeight());
			this.addPlaneDesc(desc);
		}
		return settings;
	}

	private int graphicsplaneWidth() {
		return screenWidth();
	}

	private double graphicsplaneHeight() {
		return screenHeight();
	}

	protected MotionProvider motionProvider() {
		return this.motionEngine.getMotionProvider();
	}

	@ExtensionPoint
	protected MotionProvider newMotionProvider() throws OpenReactorException {
		return null;
	}
	

	protected void parseConfig(String config) throws OpenReactorException {
		ResourceLoader loader = context().getResourceLoader();
		loader.loadConfigFromString(config);
	}

	protected PatternPlane patternplane() {
		return patternplane("pattern");
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

	@ExtensionPoint
	protected void run() throws OpenReactorException {
		this.action.perform();
		this.action = this.action.next();
		if (this.action == null) {
			exit();
		}
	}
	
	@Override
	protected final void run(Context c) throws OpenReactorException {
		this.currentContext(c);
		motionEngine.prepare();
		try {
			if (running) {
				motionEngine.run();
				run();
			}
			if (trigger(Trigger.START)) {
				quit();
			}
		} catch (OpenReactorExitException e) {
			running = false;
		} finally {
			motionEngine.finish();
			this.currentContext(null);
			this.firstTime = false;
			this.ticks ++;
		}
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

	protected final Stick stick() {
		Stick ret = joystick().stick();
		return ret;
	}
	
	@Override
	protected void terminate(Context c) throws OpenReactorException {
		this.motionEngine.terminate(c);
		super.terminate(c);
	}
	
	protected final int ticks() {
		return this.ticks;
	}
	
	protected final boolean trigger() {
		return trigger(Trigger.ANY);
	}
	
	protected final boolean trigger(Trigger t) {
		boolean ret = joystick().trigger(t);
		return ret;
	}
	
	protected int patternplaneWidth() {
		return screenWidth();
	}
	
	protected int patternplaneHeight() {
		return screenHeight();
	}
}
