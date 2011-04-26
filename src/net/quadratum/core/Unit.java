package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Unit {
		
	/** Width and height of the Unit. */
	public int _size;
	/** Name of the Unit. */
	public String _name;
	/** Blocks this Unit contains. */
	public Map<MapPoint, Block> _blocks;
	/** The player who owns this Unit. */
	public int _owner;
	/** Cached stats for this Unit. */
	public Map<String,Integer> _stats;
	
	/**
	 * Copy constructor for Unit.
	 * @param unit the Unit to copy
	 */
	public Unit(Unit unit)
	{
		this(unit._size, unit._name, unit._blocks, unit._owner, unit._stats);
	}
	
	public Unit(int size, String name, Map<MapPoint, Block> blocks, int owner, Map<String, Integer> stats) {
		_size = size;
		_name = name;
		
		_blocks = new HashMap<MapPoint, Block>();
		for(MapPoint key : blocks.keySet())
			_blocks.put(new MapPoint(key), new Block(blocks.get(key)));
		
		_owner = owner;
		
		_stats = new HashMap<String, Integer>();
		for(String key : stats.keySet())
			_stats.put(key, new Integer(stats.get(key)));
	}
}
