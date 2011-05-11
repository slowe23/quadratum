package net.quadratum.core;

// Represents a point with floating point numbers
public class RealPoint {
	/** The X coordinate of this Point. */
	public double _x;
	/** The Y coordinate of this Point. */
	public double _y;
	
	/**
	 * Constructor for MapPoint.
	 * @param x the x coordinate of the new MapPoint.
	 * @param y the y coordinate of the new MapPoint.
	 */
	public RealPoint(double x, double y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * The copy constructor for MapPoint.
	 * @param point the MapPoint to copy
	 */
	public RealPoint(RealPoint point)
	{
		_x = point._x;
		_y = point._y;
	}
	
	@Override
	public int hashCode() {
		long x = Double.doubleToLongBits(_x);
		long y = Double.doubleToLongBits(_y);
		long bits = x ^ y;
		return (int) ((bits >> 32 ^ bits) & 0xFFFF);
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof RealPoint && ((RealPoint) obj)._x == _x && ((RealPoint) obj)._y == _y);
	}
	
	@Override
	public String toString()
	{
		return "(" + _x + ", " + _y + ")";
	}
}
