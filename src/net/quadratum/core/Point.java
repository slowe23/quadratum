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
	
	/**
	 * The copy constructor for Point.
	 * @param point the Point to copy
	 */
	public Point(Point point)
	{
		_x = point.getX();
		_y = point.getY();
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
}
