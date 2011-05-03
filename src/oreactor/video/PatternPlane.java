package oreactor.video;

import java.awt.Graphics2D;

import oreactor.video.pattern.Pattern;

public class PatternPlane extends Plane {
	double patternwidth = 32;
	double patternheight = 32;
	int[][] map;
	Pattern[] patterns;
	int columns;
	int rows;
	
	protected PatternPlane(String name, double width, double height, double patternwidth, double patternheight, int numpatterns) {
		super(name, width, height);
		this.columns = (int)(width / patternwidth) + 1;
		this.rows = (int)(height / patternheight) + 1;
		this.map = new int[columns][rows];
		this.patterns = new Pattern[numpatterns];
	}

	public void put(int x, int y, int patternno) {
		this.map[x][y] = patternno;
	}
	
	public Pattern get(int x, int y) {
		Pattern ret = null;
		if (0 <= x && x < columns && 0 <= y && y < rows) {
			ret = patterns[this.map[x][y]];
		}
		return ret;
	}

	public void setPatterns(Pattern[] patterns) {
		this.patterns = patterns;
	}

	@Override
	protected void render_Protected(Graphics2D g) {
		for (int x = 0; x < this.columns; x++) {
			for (int y = 0; y < this.rows; y++) {
				Pattern p = get(x, y);
				if (p != null) {
					p.render(g, x, y, patternwidth, patternheight);
				}
			}
		}
	}
	
	public final double patternWidth() {
		return this.patternwidth;
	}
	
	public final double patternHeight() {
		return this.patternheight;
	}
	
	public int columns() {
		return this.columns;
	}
	
	public int rows() {
		return this.rows;
	}
}
