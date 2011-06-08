package example;

import java.awt.Color;
import oreactor.exceptions.OpenReactorException;
import mu64.Mu64Reactor;

public class DrawLine extends Mu64Reactor {
	public void run() throws OpenReactorException {
		graphicsplane().line(0, 0, 1023, 767, Color.white);
		exit();
	}
}
