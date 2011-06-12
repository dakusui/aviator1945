package example;

import java.awt.Color;

import mu64.Mu64Reactor;
import oreactor.exceptions.OpenReactorException;

public class DrawLine extends Mu64Reactor {
	public void run() throws OpenReactorException {
		graphicsplane().line(0, 0, 1023, 767, Color.white);
		exit();
	}
}
