package siovanus;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import siovanus.drivant.AviatorDrivant;
import siovanus.drivant.SEmittableDrivant;
import siovanus.drivant.AviatorDrivant.AviatorParameters;
import siovanus.drivant.aviator.Aviator;
import siovanus.drivant.aviator.BomberAviator;
import siovanus.drivant.aviator.ChasingAviator;
import siovanus.drivant.aviator.GunAviator;
import siovanus.drivant.aviator.MobAviator;
import siovanus.drivant.aviator.SmartAviator;
import siovanus.drivant.aviator.UserAviator;
import siovanus.drivant.aviator.VesselAviator;
import siovanus.motion.AvengerMMachineSpec;
import siovanus.motion.B17MMachineSpec;
import siovanus.motion.B29MMachineSpec;
import siovanus.motion.F4UMMachineSpec;
import siovanus.motion.P51DMMachineSpec;
import siovanus.motion.USSNewJerseyGunMMachineSpec;
import siovanus.motion.USSNewJerseyMMachineSpec;
import siovanus.spec.SGageSpriteSpec;
import siovanus.sprite.SGageSprite;
import avis.base.AException;
import avis.base.Avis;
import avis.input.AInputDevice;
import avis.motion.Drivant;
import avis.motion.MMachine;
import avis.motion.MMachineSpec;
import avis.session.AScenario;
import avis.session.ASession;
import avis.video.AScreen;

public abstract class SScenario extends AScenario {
	public void handleCollision(Drivant d1, Drivant d2, double distance) {
		// from Aviator Drivant
		Avis.logger().debug("Collision detected:d1=<" + d1 + ">,d2=<" + d2 + ">");
		if (d1.isValid() && d2.isValid()) {
			_handleCollision(d1, d2, distance);
			_handleCollision(d2, d1, distance);
		}
		ssession.updateLife(((AviatorParameters)userDrivant.parameters()).life);
		ssession.updateScore(this.score);
	}

	private void _handleCollision(Drivant d1, Drivant d2, double distance) {
		if (d1 instanceof AviatorDrivant) {
			AviatorParameters p1 = (AviatorParameters) d1.parameters();
			if (--p1.life < 0) {
				//Configuration.controller.dump();
				d1.invalidate();
				this.score += calcScore(d1);
				if (d1.groupId() == SGroup.Enemy_Aerial || d1.groupId() == SGroup.Enemy_Surface) {
					if (d1.label() != null) {
						Integer s = scoreMap.get(d1.label());
						scoreMap.put(
								d1.label(), 
								s == null ? 1
										   : s.intValue() + 1
						);
					}
				}
				explosion(d1);
			}
		} else if (d1 instanceof SEmittableDrivant && d2 instanceof AviatorDrivant) {
			d1.invalidate();
			minorExplosion(d1);
		}
	}

	protected int calcScore(Drivant drivant) {
		if (drivant.groupId() == SGroup.Enemy_Aerial || drivant.groupId() == SGroup.Enemy_Surface) {
			Integer score = pointMap.get(drivant.label());
			return score != null ? score.intValue()
					              : 0;
		}
		return 0;
	}

	public boolean collides(Drivant d1, Drivant d2, double distance) {
		return distance < (d1.width() + d2.width() + d1.height() + d2.height()) / 2;
	}

	public void handleInteraction(Drivant d1, Drivant d2, double distance) {
		if (d1 instanceof AviatorDrivant && d2 instanceof AviatorDrivant) {
			((AviatorDrivant)d1).notify((AviatorDrivant)d2, distance);
			((AviatorDrivant)d2).notify((AviatorDrivant)d1, distance);
		}
	}

	private int score = 0;
	protected SSession ssession;
	protected AviatorDrivant userDrivant;
	private Map<String, Integer> pointMap;
	protected SortedMap<String, Integer> scoreMap;
	protected boolean passed;
	public static final String BGM_CHOPIN_OP39 = "bgm/chopin_op39.mid";
	public static final String BGM_CARMEN1_2 = "bgm/bizet_carmen_1_2.mid";
	public static final String BGM_LISZT143 = "bgm/liszt_s143.mid";
	public static final String BGM_WALKURENRITT = "bgm/wagner_walkurenritt.mid";
	public static final String BGM_IMPROMPTU_4 = "bgm/Schubert_Impromptus_D899_No_4.mid";
	public static final String BGM_QUARTET_10 = "bgm/Quartet_10_mvt3.MID";
	public static final String BGM_SYM9_1 = "bgm/Sym_9_First_Movement.mid";
	public static final String BGM_SYM9_4 = "bgm/Sym_9_Fourth_Movement.mid";
	public static final String BGM_BWV1068_2 = "bgm/jsbach_bwv1068_2.mid";
		
	public void explosion(Drivant drivant) {
    	ssession.explosion(this, drivant);
		ssession.soundEffect(SSession.SE_EXPLOSION);
	}

	public void minorExplosion(Drivant drivant) {
    	ssession.minorExplosion(this, drivant);
	}
    @Override
	public void init(ASession session) throws AException {
		this.ssession = (SSession)session;
		this.pointMap = new HashMap<String, Integer>();
		this.pointMap.put(null,                                0); 
		this.pointMap.put(AvengerMMachineSpec.LABEL,          100);
		this.pointMap.put(F4UMMachineSpec.LABEL,              200);
		this.pointMap.put(P51DMMachineSpec.LABEL,             500);
		this.pointMap.put(B17MMachineSpec.LABEL,              500);
		this.pointMap.put(B29MMachineSpec.LABEL,              800);
		this.pointMap.put(USSNewJerseyGunMMachineSpec.LABEL,  800);
		this.pointMap.put(USSNewJerseyMMachineSpec.LABEL,    9000);
		this.scoreMap = new TreeMap<String, Integer>();
	}

	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 */
	public void message(String message, int x, int y, Color color, float f) {
		ssession.message(message, x, y, color, f);
	}

	/**
	 * 指定されたメッセージをスプライトとして、画面上に描画する。
	 * @param message 表示するメッセージ
	 * @param x x座標
	 * @param y y座標
	 * @param c 描画色
	 * @param fontSize フォントのサイズ
	 * @param duration 表示する時間(単位:秒)
	 */
	public void message(String message, int x, int y, Color color, float f, int duration) {
		int ticks = 1000 * duration /  ssession.interval();
		ssession.message(message, x, y, color, f, ticks);
	}
	public void title(String message, int y, Color c, float fontSize,
			int durationToDisplay) {
		int ticks = 1000 * durationToDisplay /  ssession.interval();
		ssession.message(message, -1, y, c, fontSize,  ticks, Siovanus.SPRITESPEC_EFFECT_MESSAGE);
	}
	public void creditMessage(String message, int y, Color c, float fontSize,
			int durationToDisplay) {
		int ticks = 1000 * durationToDisplay /  ssession.interval();
		ssession.message(message, -1, y, c, fontSize,  ticks, Siovanus.SPRITESPEC_CREDIT_MESSAGE);
	}

    public void invalidated(Drivant drivant) {
		// does nothing
	}

	public void registered(Drivant drivant) {
		// does nothing
	}

	@SuppressWarnings("unchecked")
	public void emit(Drivant emitter, MMachineSpec emittable) {
		shot(emitter, emittable);
	}
	
	@SuppressWarnings("unchecked")
	protected void shot(Drivant emitter, MMachineSpec emittable) {
		MMachine mmachine = emittable.createMMachine(350, emitter.x(), emitter.y(), emitter.direction(), -1, emitter.groupId(), emitter);
		mmachine.drivant().addObserver(this);
	}

	public AInputDevice stick() {
		return ssession.stick();
	}

	public AviatorDrivant userDrivant() {
		return userDrivant;
	}

	protected void createNewDummy() {
		MMachine mmachine = Siovanus.dummySpec.createMMachine(900, 0, 0, 0, 0, SGroup.Enemy_Aerial, new ChasingAviator());
		mmachine.drivant().addObserver(this);
	}

	protected void createNewEnemy_Avenger(double x, double y,
			int direction, boolean smart, Aviator activatedAviator) {
		Aviator aviator;
		if (smart) {
			aviator = new SmartAviator();
		} else {
			if (activatedAviator != null) {
				aviator = new MobAviator(activatedAviator);
			} else {
				aviator = new MobAviator();
			}
		}
		MMachine mmachine = Siovanus.avengerSpec.createMMachine(500, x, y, direction, 1, SGroup.Enemy_Aerial, aviator);
		mmachine.drivant().addObserver(this);
	}
	protected void createNewEnemy_Avenger(double x, double y,
			int direction, boolean smart) {
		createNewEnemy_Avenger(x, y, direction, smart, null);
	}
	protected void createNewEnemy_Avenger(double x, double y,
			int direction) {
		createNewEnemy_Avenger(x, y, direction, false, null);
	}
	protected void createNewEnemy_Avenger(double x, double y,
			int direction, Aviator activatedAviator) {
		createNewEnemy_Avenger(x, y, direction, false, activatedAviator);
	}
	
	protected void createNewEnemy_P51D(double x, double y, int direction) {
		MMachine mmachine = Siovanus.p51dSpec.createMMachine(400, x, y, direction, 1, SGroup.Enemy_Aerial, new SmartAviator());
		mmachine.drivant().addObserver(this);
	}

	protected void createNewEnemy_F4U(double x, double y, int direction) {
		createNewEnemy_F4U(x, y, direction, false, null);
	}
	protected void createNewEnemy_F4U(double x, double y, int direction, boolean smart) {
		createNewEnemy_F4U(x, y, direction, smart, null);
	}
	protected void createNewEnemy_F4U(double x, double y, int direction, boolean smart, Aviator activatedAviator) {
		Aviator aviator;
		if (smart) {
			aviator = new SmartAviator();
		} else {
			if (activatedAviator != null) {
				aviator = new MobAviator(activatedAviator);
			} else {
				aviator = new MobAviator();
			}
		}
		MMachine mmachine = Siovanus.f4uSpec.createMMachine(400, x, y, direction, 1, SGroup.Enemy_Aerial, aviator);
		mmachine.drivant().addObserver(this);
	}

	protected Drivant createNewEnemy_USSNewJersey(double x, double y) {
		MMachine mmachine = Siovanus.ussNewJerseySpec.createMMachine(100, x, y, 64, 1, SGroup.Enemy_Surface, new VesselAviator());
		mmachine.drivant().addObserver(this);
		return mmachine.drivant();
	}

	void createNewEnemy_USSNewJersey_Gun(Drivant parent, double r, int th, int direction, int rightEnd, int leftEnd) {
		MMachine mmachine = Siovanus.ussNewJerseyGunSpec.createMMachine(150, 0, 0, direction, 1, SGroup.Enemy_Surface, new GunAviator(parent, r, th, rightEnd, leftEnd));
		mmachine.drivant().addObserver(this);
	}

	protected void createNewEnemy_B17(double x, double y,
			int direction) {
		MMachine mmachine = Siovanus.b17Spec.createMMachine(400, x, y, direction, 1, SGroup.Enemy_Aerial, new MobAviator(new BomberAviator()));
		mmachine.drivant().addObserver(this);
	}

	protected void createNewEnemy_B29(double x, double y,
			int direction) {
		MMachine mmachine = Siovanus.b29Spec.createMMachine(400, x, y, direction, 1, SGroup.Enemy_Aerial, new MobAviator(new BomberAviator()));
		mmachine.drivant().addObserver(this);
	}

	protected void createNewFriend_Zero(double x, double y, int direction) {
		MMachine mmachine = Siovanus.zeroSpec.createMMachine(400, x, y, direction, 1, SGroup.Player_Aerial, new SmartAviator());
		((AviatorDrivant)mmachine.drivant()).target(userDrivant);
		mmachine.drivant().addObserver(this);
	}

	protected void createNewUser_Ki84(double x, double y, int direction) {
		Aviator userAviator = createUserAviator();
		MMachine mmachine = Siovanus.ki84Spec.createMMachine(900, x, y, direction, 1, SGroup.Player_Aerial, userAviator);
		userDrivant = (AviatorDrivant) mmachine.drivant();
		userDrivant.addObserver(this);
		mmachine.drivant().addObserver(this);
		ssession.userDrivantCreated(userDrivant);
		
		{
			AScreen screen = ssession.screen();
			SGageSpriteSpec p = new SGageSpriteSpec(userAviator);
			SGageSprite gage = (SGageSprite) p.createSprite(1000);
			screen.foregroundPlane().attach(gage);
			gage.put(screen.getWidth() - p.width() - 20, screen.getHeight() - 192 - p.height() - 30);
			gage.visible(true);
		}
		
	}

	protected Aviator createUserAviator() {
		UserAviator userAviator = new UserAviator(stick());
		return userAviator;
	}
	protected void createNewFriend_Ki84(double x, double y, int direction) {
		MMachine mmachine = Siovanus.ki84Spec.createMMachine(900, x, y, direction, 1, SGroup.Player_Aerial, new SmartAviator());
		((AviatorDrivant)mmachine.drivant()).target(userDrivant);
		mmachine.drivant().addObserver(this);
	}

	public abstract String bgm();

	@Override
	public String backgroundImageResource() {
		return "image/background/bg00.bmp";
	}
	
	@Override
	public AScenario next() {
		SScenario ret = (SScenario) super.next();
		if (ret != null) {
			ret.score(this.score());
		}
		return ret;
	}
		

	public int score() {
		return score;
	}
	
	public void score(int score) {
		this.score = score;
	}

	public ASession session() {
		return ssession;
	}
	
	@Override
	public void reset() {
		score(0);
		scoreMap.clear();
	}

	public double initialX() {
		return 512;
	}

	public double initialDirection() {
		return 64;
	}

	public double initialY() {
		return -600;
	}

	public abstract String getBackgroundResourceName();

	public boolean isPassed() {
		return passed;
	}

	public abstract void instructionAfterScenario() ;
}
