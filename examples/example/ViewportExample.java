package example;

import mu64.Mu64Reactor;

public class ViewportExample extends Mu64Reactor {
	int i = 0;
	@Override
	public void run() {
		if (isFirstTime()) {
			graphicsplane().boxfill(0, 0, 1024, 1024, Pallette.DARKGREEN);
			for (int i = 0; i < 1024; i += 32) {
				graphicsplane().line(i, 0, i, 1024, Pallette.BLUE);
				graphicsplane().line(0, i, 1024, i, Pallette.RED);
			}
		} else {
			graphicsplane().viewport().i(256, i);
			graphicsplane().viewport().j(i, 256);
			graphicsplane().viewport().offset(i, i);
			if (i >= 255) {
				i = 0;
			}
			i++;
		}
	}
}
