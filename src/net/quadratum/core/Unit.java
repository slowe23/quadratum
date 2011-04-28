package net.quadratum.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Unit implements Serializable {
		
	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 8546407195685197582L;
	
	/** Width and height of the Unit. */
	public int _size;
	
	/** Name of the Unit. */
	public String _name;
	
	/** Unit ID - stays constant over the whole game, is used to identify/refer to a unit */
	public int _id;
	
	/** Blocks this Unit contains. */
	public Map<MapPoint, Block> _blocks;
	
	/** The player who owns this Unit. */
	public int _owner;
	
	/** Cached stats for this Unit. */
	public Map<Block.BonusType, Integer> _stats;
	
	/**
	 * Constructor for Unit.
	 */
	public Unit(String name, int owner, int id)
	{
		_id = id;
		_name = new String(name);
		_blocks = new HashMap<MapPoint, Block>();
		_stats = new HashMap<Block.BonusType, Integer>();
		_owner = owner;
		_size = Constants.UNIT_SIZE;
	}
	
	/**
	 * Copy constructor for Unit.
	 * @param unit the Unit to copy
	 */
	public Unit(Unit unit)
	{
		_stats = new HashMap<Block.BonusType, Integer>();
		_blocks = new HashMap<MapPoint, Block>();
		_size = unit._size;
		_name = new String(unit._name);
		_owner = unit._owner;
		_id = unit._id;
		for(Block.BonusType key : unit._stats.keySet())
		{
			_stats.put(key, new Integer(unit._stats.get(key)));
		}
		for(MapPoint key : unit._blocks.keySet())
		{
			_blocks.put(new MapPoint(key), new Block(unit._blocks.get(key)));
		}
	}
	
	/** Tests whether the two objects represent the same unit */
	public boolean equals(Object o) {
		return (o instanceof Unit) && (_id == ((Unit)o)._id);
	}
	
	public int hashCode() {
		return new Integer(_id).hashCode();
	}
}
