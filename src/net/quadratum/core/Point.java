package net.quadratum.core;

public class Point {
	
	/** The X coordinate of this Point. */
	private int _x;
	/** The Y coordinate of this Point. */
	private int _y;
	
	public Point(int x, int y) {
		_x = x;
		_y = y;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
}
