package oreactor.video;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class GraphicsPlane extends Plane {
	BufferedImage image;
	Graphics2D g;

	protected GraphicsPlane(Region physical, Region logical) {
		super(physical, logical);
	}

	public void line(double x1, double y1, double x2, double y2, Color c) {
		this.assertProgrammableState();
		g.setColor(c);
		g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
	}

	public void box(double x, double y, double w, double h, Color c) {
		this.assertProgrammableState();
		g.setColor(c);
		g.drawRect((int)x, (int)y, (int)w, (int)h);
	}

	public void boxfill(double x, double y, double w, double h, Color c) {
		this.assertProgrammableState();
		g.setColor(c);
		g.fillRect((int)x, (int)y, (int)w, (int)h);
	}

	public void oval(double x, double y, double w, double h, Color c) {
		this.assertProgrammableState();
		g.drawOval((int)x, (int)y, (int)w, (int)h);
	}

	public void filloval(double x, double y, double w, double h, Color c) {
		this.assertProgrammableState();
		g.drawOval((int)x, (int)y, (int)w, (int)h);
	}

	public void pset(double x, double y, Color c) {
		this.assertProgrammableState();
		this.image.setRGB((int)x, (int)y, c.getRGB());
	}

	public void put(Image image, double x, double y, double hratio,
			double vratio) {
		this.assertProgrammableState();
		g.drawImage(image, (int)x, (int)y, null);
	}

	public Color point(double x, double y) {
		this.assertProgrammableState();
		return new Color(this.image.getRGB((int)x, (int)y));
	}

	public void print(String msg, double x, double y) {
		this.assertProgrammableState();
		this.g.drawString(msg, (int)x, (int)y);

	}

	public void clear() {
		this.assertProgrammableState();
		this.g.clearRect(0, 0, this.image.getWidth(), this.image.getHeight());
	}

	public void paint(double x1, double y1, Color c, Color b) {
		this.assertProgrammableState();
		// TODO
	}

	public void color(Color foreground, Color background) {
		this.assertProgrammableState();
		// TODO
	}

	@Override
	protected void renderEngine(Graphics2D g) {
		// TODO Auto-generated method stub

	}
}
