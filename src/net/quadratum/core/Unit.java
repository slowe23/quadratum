package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Unit {
		
	/** Width and height of the Unit. */
	int _size;
	/** Name of the Unit. */
	String _name;
	/** Blocks this Unit contains. */
	Map<Point, Block> _blocks;
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
		_blocks = new HashMap<Point, Block>();
		_size = unit._size;
		_name = new String(unit._name);
		_owner = unit._owner;
		for(String key : unit._stats.keySet())
		{
			_stats.put(new String(key), new Integer(unit._stats.get(key)));
		}
		for(Point key : unit._blocks.keySet())
		{
			_blocks.put(new Point(key), new Block(unit._blocks.get(key)));
		}
	}
}
