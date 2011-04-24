package net.quadratum.core;

import java.util.Map;

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
	/** Has the unit moved this turn? */
	boolean _hasMoved;
	/** Has the unit attacked this turn? */
	boolean _hasAttacked;
}
