package example;

public class DrawLine extends mu64.Mu64Reactor {
	public void run() throws ExitMu64 {
		graphicsplane().clear();
		graphicsplane().line(0, 0, 1023, 767, Pallette.WHITE);
		exit();
	}
}
