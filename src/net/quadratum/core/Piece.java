package net.quadratum.core;

import java.util.Map;
import java.util.HashMap;

public class Piece {
	
	/** The Blocks that make up this Piece. */
	Map<Point,Block> _blocks;
	
	/**
	 * Copy constructor for Piece.
	 * @param piece the Piece to copy
	 */
	public Piece(Piece piece)
	{
		_blocks = new HashMap<Point, Block>();
		for(Point key : piece._blocks.keySet())
		{
			_blocks.put(new Point(key), new Block(piece._blocks.get(key)));
		}
	}
}
