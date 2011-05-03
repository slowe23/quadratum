package net.quadratum.core;

import java.io.Serializable;

public class MapPoint implements Serializable {
	
	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = -3278468716332610867L;
	
	/** The X coordinate of this Point. */
	public int _x;
	/** The Y coordinate of this Point. */
	public int _y;
	
	/**
	 * Constructor for MapPoint.
	 * @param x the x coordinate of the new MapPoint.
	 * @param y the y coordinate of the new MapPoint.
	 */
	public MapPoint(int x, int y) {
		_x = x;
		_y = y;
	}
	
	/**
	 * The copy constructor for MapPoint.
	 * @param point the MapPoint to copy
	 */
	public MapPoint(MapPoint point)
	{
		_x = point._x;
		_y = point._y;
	}
	
	@Override
	public int hashCode() {
		return _x << 16 ^ _y;
	}
	
	@Override
	public boolean equals(Object obj) {
		return (obj instanceof MapPoint && ((MapPoint) obj)._x == _x && ((MapPoint) obj)._y == _y);
	}
	
	@Override
	public String toString()
	{
		return "(" + _x + ", " + _y + ")";
	}
}
