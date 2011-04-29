package net.quadratum.core;

import java.util.HashMap;
import java.util.Map;

public class Piece {

	/** The Blocks that make up this Piece. */
	public Map<MapPoint,Block> _blocks;
	
	//Cached bounding box values
	private int[] _bounds;
	
	/** The cost of this Piece. */
	public int _cost;
	
	/** The maximum times a player can use this Piece. */
	public int _max;
	
	/** The name of the Piece. */
	public String _name;
	
	/** The description of the piece. */
	public String _description;
	
	/**
	 * Copy constructor for Piece.
	 * @param piece the Piece to copy
	 */
	public Piece(Piece piece) {
		this(piece._blocks, piece._cost, piece._max, piece._name, piece._description);
	}
	
	/** Explicit constructor */
	public Piece(Map<MapPoint, Block> blocks, int cost, int max, String name, String description) {
		_blocks = new HashMap<MapPoint, Block>();
		for(Map.Entry<MapPoint, Block> entry : blocks.entrySet()) {
			_blocks.put(new MapPoint(entry.getKey()), new Block(entry.getValue()));
		}
		
		_cost = cost;
		_max = max;
		_name = name;  //Strings are immutable
		_description = description;
	}
	
	/**
	 * Compute the minimum and maximum coordinates (relative to the piece's origin) of blocks in this piece
	 * Coordinates are stored in the following order: {min_x, min_y, max_x, max_y}
	 */
	public int[] getBounds() {
		if(_bounds==null) {
			_bounds = new int[4];
			boolean any = false;
			for(MapPoint m : _blocks.keySet()) {
				if(!any || m._x<_bounds[0])
					_bounds[0] = m._x;
				if(!any || m._y<_bounds[1])
					_bounds[1] = m._y;
				if(!any || m._x>_bounds[2])
					_bounds[2] = m._x;
				if(!any || m._y>_bounds[3])
					_bounds[3] = m._y;

				any = true;
			}	
		}
		
		return _bounds;
	}
	
	/**
	 * Used to convert this Piece into a String.
	 * @return a String representation of this Piece
	 */
	public String toString()
	{
		return _name + ": " + _description;
	}
}
