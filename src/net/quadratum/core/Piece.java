package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Piece {
	
	/** The Blocks that make up this Piece. */
	Map<MapPoint,Block> _blocks;
	
	/** The cost of this Piece. */
	int _cost;
	
	/** The maximum times a player can use this Piece. */
	int _max;
	
	/** The name of the Piece. */
	String _name;
	
	/** The description of the piece. */
	String _description;
	
	/**
	 * Copy constructor for Piece.
	 * @param piece the Piece to copy
	 */
	public Piece(Piece piece)
	{
		_blocks = new HashMap<MapPoint, Block>();
		for(MapPoint key : piece._blocks.keySet())
		{
			_blocks.put(new MapPoint(key), new Block(piece._blocks.get(key)));
		}
		_cost = piece._cost;
		_max = piece._max;
		_name = new String(piece._name);
		_description = new String(piece._description);
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
