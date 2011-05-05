package oreactor.video.pattern;

import java.awt.Graphics2D;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.Plane;

public class PatternPlane extends Plane {
	double patternwidth = 16;
	double patternheight = 16;
	int[][] map;
	Pattern[] patterns;
	int columns;
	int rows;
	
	public PatternPlane(String name, double width, double height, double patternwidth, double patternheight, int numpatterns) {
		super(name, width, height);
		this.patternwidth = patternwidth;
		this.patternheight = patternheight;
		this.columns = (int)(width / patternwidth) + 1;
		this.rows = (int)(height / patternheight) + 1;
		this.map = new int[columns][rows];
		this.patterns = new Pattern[numpatterns];
	}

	public void put(int x, int y, int patternno) throws OpenReactorException {
		if (x < 0 || x >= this.columns) {
			ExceptionThrower.throwParameterException("Parameter 'x' must be larger or equal to 0 and less than " + this.columns);
		}
		if (y < 0 || y >= this.rows) {
			ExceptionThrower.throwParameterException("Parameter 'y' must be larger or equal to 0 and less than " + this.rows);
		}
		if (patternno < 0 || patternno >= this.patterns.length) {
			ExceptionThrower.throwParameterException("Parameter 'patternno' must be greater or equal to 0 and less than " + this.patterns.length + ".");
		}
		
		this.map[x][y] = patternno;
	}
	
	public void reset(int x, int y) throws OpenReactorException {
		if (x < 0 || x >= this.columns) {
			ExceptionThrower.throwParameterException("Parameter 'x' must be larger or equal to 0 and less than " + this.columns);
		}
		if (y < 0 || y >= this.rows) {
			ExceptionThrower.throwParameterException("Parameter 'y' must be larger or equal to 0 and less than " + this.rows);
		}
		this.map[x][y] = -1;
	}

	public Pattern get(int x, int y) throws OpenReactorException {
		if (x < 0 || x >= this.columns) {
			ExceptionThrower.throwParameterException("Parameter 'x' must be larger or equal to 0 and less than " + this.columns);
		}
		if (y < 0 || y >= this.rows) {
			ExceptionThrower.throwParameterException("Parameter 'y' must be larger or equal to 0 and less than " + this.rows);
		}
		Pattern ret = null;
		if (0 <= x && x < columns && 0 <= y && y < rows) {
			int patternno = this.map[x][y];
			if (patternno >= 0) { 
				ret = patterns[patternno];
			}
		}
		return ret;
	}

	public void bind(Pattern p) throws OpenReactorException {
		if (p == null) {
			ExceptionThrower.throwParameterException("Parameter 'p' must not be null.");
		}
		int patternno = p.id();
		if (patternno < 0 || patternno >= this.patterns.length) {
			ExceptionThrower.throwParameterException("Parameter 'p''s id must be greater or equal to 0 and less than " + this.patterns.length + ".");
		}
		this.patterns[patternno] = p;
	}
	
	@Override
	protected void render_Protected(Graphics2D g) throws OpenReactorException {
		for (int x = 0; x < this.columns; x++) {
			for (int y = 0; y < this.rows; y++) {
				Pattern p = get(x, y);
				if (p != null) {
					p.render(g, x * patternwidth, y * patternheight, patternwidth, patternheight);
				}
			}
		}
	}
	
	void dump() {
		for (int y = 0; y < this.rows; y++) {
			for (int x = 0; x < this.columns; x++) {
				String s = (s = ("00" + this.map[x][y])).substring(s.length() - 2);
				System.out.print(s);
				if (x < this.columns -1) {
					System.out.print(":");
				}
			}
			System.out.println();
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
