package net.quadratum.core;

// Used by GameCore to store information aobut units
public class UnitInformation
{
	/** Has the unit moved this turn? */
	public boolean _hasMoved;
	/** Has the unit attacked this turn? */
	public boolean _hasAttacked;
	/** The position of the unit. */
	public MapPoint _position;
	/** Has the unit been updated this turn? */
	public boolean _updated;
	
	/**
	 * Constructor for UnitInformation.
	 * @param position the position of the unit
	 */
	public UnitInformation(MapPoint position)
	{
		_position = new MapPoint(position);
		_hasMoved = false;
		_hasAttacked = false;
		_updated = false;
	}
}