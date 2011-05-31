package zero;

import oreactor.exceptions.OpenReactorException;
import oreactor.joystick.JoystickEngine.Stick;
import oreactor.video.sprite.ImageSpriteSpec.RenderingParameters;
import oreactor.video.sprite.Sprite;
import openreactor.mu64.Mu64Reactor;

public class ZeroReactor extends Mu64Reactor {
	public static final String config = 
	"{"+
    "    'spritespecs':{"+
    "        'zero':{"+
    "            'class':'oreactor.video.sprite.ImageSpriteSpec', "+
    "            'params':{" +
    "            'images':[" +
    "                'zero/images/01.gif'," +
    "                'zero/images/02.gif'," +
    "                'zero/images/03.gif'," +
    "                'zero/images/04.gif'," +
    "                'zero/images/05.gif'," +
    "                'zero/images/06.gif'," +
    "                'zero/images/07.gif'," +
    "                'zero/images/08.gif'," +
    "                'zero/images/09.gif'," +
    "                'zero/images/10.gif'," +
    "                'zero/images/11.gif'," +
    "                'zero/images/12.gif'," +
    "                'zero/images/13.gif'," +
    "                'zero/images/14.gif'," +
    "                'zero/images/15.gif'," +
    "                'zero/images/16.gif'," +
    "                'zero/images/17.gif'," +
    "                'zero/images/18.gif'," +
    "                'zero/images/19.gif'," +
    "                'zero/images/20.gif'," +
    "                'zero/images/21.gif'," +
    "                'zero/images/22.gif'," +
    "                'zero/images/23.gif'," +
    "                'zero/images/24.gif'," +
    "                'zero/images/25.gif'," +
    "                'zero/images/26.gif'," +
    "                'zero/images/27.gif'," +
    "                'zero/images/28.gif'," +
    "                'zero/images/29.gif'," +
    "                'zero/images/30.gif'," +
    "                'zero/images/31.gif'," +
    "                'zero/images/32.gif'," +
    "            ]," +
    "        }," +
    "        'hresolution':64," +
    "        'vresolution':64, " +
    "   }" +
    "}," +
    "'patterns':[" +
    "]," +
    "'midiclips':{" +
    "}," +
    "'soundclips':{" +
    "}," +
    "}" +
	"";
	
	Sprite s = null;

	private int pnum;
	
	
	@Override
	public void run() throws OpenReactorException{
		if (isFirstTime()) {
			parseConfig(config);
			s = spriteplane().createSprite(spritespec("zero"));
		} else {
			s.put(512, 384, 0);
			Stick o = stick();
			RenderingParameters p = (RenderingParameters) s.renderingParameters();
			pnum += o.x();
			pnum = pnum < 0 ? 32 + pnum
					        : pnum;
			pnum = pnum % 32;
			p.patternno(pnum);
		}
	}
}
