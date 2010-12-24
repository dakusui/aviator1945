package oreactor.video;

import java.awt.Graphics2D;

public class PatternPlane extends Plane {
	static class Pattern {
		public void render() {
			// TODO
		}
	}
	int[][] map;
	Pattern[] patterns;
	
	protected PatternPlane(Region physical, Region logical) {
		super(physical, logical);
	}

	public void put(int x, int y, int patternno) {
		this.map[x][y] = patternno;
	}
	
	public int get(int x, int y) {
		return this.map[x][y];
	}

	public void setPatterns(Pattern[] patterns) {
		this.patterns = patterns;
	}

	@Override
	protected void renderEngine(Graphics2D g) {
		// TODO Auto-generated method stub
		
	}
}
