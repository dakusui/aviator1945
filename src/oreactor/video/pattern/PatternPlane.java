package oreactor.video.pattern;

import java.awt.Graphics2D;
import java.awt.Polygon;

import oreactor.exceptions.ExceptionThrower;
import oreactor.exceptions.OpenReactorException;
import oreactor.video.Plane;
import oreactor.video.Viewport;
import oreactor.video.Viewport.Vector;

public class PatternPlane extends Plane {
	double patternwidth = -1;
	double patternheight = -1;
	int[][] map;
	Pattern[] patterns;
	int columns;
	int rows;
	private Polygon viewportPolygon;
	private double minx;
	private double miny;
	private double maxx;
	private double maxy;
	private boolean viewportChanged;
	
	public PatternPlane(String name, double width, double height, double patternwidth, double patternheight, int numpatterns,  Viewport viewport) {
		super(name, width, height, viewport);
		this.patternwidth = patternwidth;
		this.patternheight = patternheight;
		this.columns = (int)(width / patternwidth) + (width % patternwidth == 0 ? 0 : 1);
		this.rows = (int)(height / patternheight) + (height % patternheight == 0 ? 0 : 1);
		this.map = new int[columns][rows];
		this.patterns = new Pattern[numpatterns];
		this.updateViewport(viewport);
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
			ExceptionThrower.throwParameterException("Parameter 'x' must be larger or equal to 0 and less than " + this.columns + ":x=" + x);
		}
		if (y < 0 || y >= this.rows) {
			ExceptionThrower.throwParameterException("Parameter 'y' must be larger or equal to 0 and less than " + this.rows + ":y=" + y);
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
		int patternno = p.num();
		if (patternno < 0 || patternno >= this.patterns.length) {
			ExceptionThrower.throwParameterException("Parameter 'p''s id must be greater or equal to 0 and less than " + this.patterns.length + ".");
		}
		this.patterns[patternno] = p;
	}
	
	@Override
	protected void render_Protected(Graphics2D g) throws OpenReactorException {
		double x, y;
		Pattern p;
		if (viewportChanged) {
			updateViewport(viewport);
		}
		for (int r = Math.max(0, figureoutRow(miny)) ; r < Math.min(this.rows, figureoutRow(maxy)); r++) {
			y = this.patternheight * r;
			for (int c = Math.max(0, figureoutColumn(minx)) ; c < Math.min(this.columns, figureoutColumn(maxx)); c++) {
				x = this.patternwidth * c;
				p = get(c, r);
				if (p != null) {
					if (viewportPolygon.intersects(x, y, patternwidth, patternheight)) {
						p.render(g, x, y);
					}
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

	private void updateViewport(Viewport viewport) {
		Vector i = viewport.i();
		Vector j = viewport.j();
		Vector offset = viewport.offset();
		Vector[] tmp = new Vector[]{offset, offset.add(i), offset.add(i).add(j), offset.add(j)};
		viewportPolygon= new Polygon();
		minx = Double.MAX_VALUE;
		miny = Double.MAX_VALUE;
		maxx = Double.MIN_VALUE;
		maxy = Double.MIN_VALUE;
		double vx, vy;
		for (Vector v : tmp) {
			// Since this method is very performance intensive, I'm doing 2 things in one loop.
			// 1. calculating max/min x, y of the viewport.
			vx = v.x();
			vy = v.y();
			if (minx > vx ) minx = vx; 
			if (miny > vy ) miny = vy; 
			if (maxx < vx ) maxx = vx; 
			if (maxy < vy ) maxy = vy;
			// 2. composing a polygon object
			viewportPolygon.addPoint((int)vx, (int)vy);
		}
		viewportChanged = false;
	}
	
	@Override
	public void viewportChanged(Viewport viewport) {
		viewportChanged = true;
	}
	
	private int figureoutRow(double y) {
		return (int) (y / patternheight) + (y % patternheight == 0 ? 0 : 1);
	}
	private int figureoutColumn(double x) {
		return (int) (x / patternwidth) + (x % patternwidth == 0 ? 0 : 1);
	}
}
