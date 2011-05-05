package net.quadratum.core;

public class UnitInformation
{
	/** Has the unit moved this turn? */
	public boolean _hasMoved;
	/** Has the unit attacked this turn? */
	boolean _hasAttacked;
	/** The position of the unit. */
	MapPoint _position;
	
	/**
	 * Constructor for UnitInformation.
	 * @param position the position of the unit
	 */
	public UnitInformation(MapPoint position)
	{
		_position = new MapPoint(position);
		_hasMoved = false;
		_hasAttacked = false;
	}
}