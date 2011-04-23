package net.quadratum.core;

import java.util.Map;

public class Unit {
	
	/** Width and height of the Unit. */
	int _size;
	/** Name of the Unit. */
	String _name;
	/** Pieces this Unit contains. */
	Map<Point, Piece> _pieces;
	/** The Player who owns this Unit. */
	Player _owner;
	/** Cached stats for this Unit. */
	Map<String,Integer> _stats;
	
}
