package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Unit {
		
	/** Width and height of the Unit. */
	int _size;
	/** Name of the Unit. */
	String _name;
	/** Pieces this Unit contains. */
	Map<Point, Piece> _pieces;
	/** The player who owns this Unit. */
	int _owner;
	/** Cached stats for this Unit. */
	Map<String,Integer> _stats;
	
	/**
	 * Copy constructor for Unit.
	 * @param unit the Unit to copy
	 */
	public Unit(Unit unit)
	{
		_stats = new HashMap<String, Integer>();
		_pieces = new HashMap<Point, Piece>();
		_size = unit._size;
		_name = new String(unit._name);
		_owner = unit._owner;
		for(String key : unit._stats.keySet())
		{
			_stats.put(new String(key), new Integer(unit._stats.get(key)));
		}
		for(Point key : unit._pieces.keySet())
		{
			_pieces.put(new Point(key), new Piece(unit._pieces.get(key)));
		}
	}
}
