package net.quadratum.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

// Represents a single piece
public class Piece implements Serializable {
	
	public static final int ROTATE_NONE = 0;
	public static final int ROTATE_CW = 1;  //90° clockwise rotation
	public static final int ROTATE_180 = 2;  //180° rotation
	public static final int ROTATE_CCW = 3;  //90° counterclockwise rotation

	/**
	 * Serialization UID
	 */
	private static final long serialVersionUID = -6946404104013633398L;

	/** The Blocks that make up this Piece. */
	private Map<MapPoint, Block> _blocks;
	
	/** Cached bounding box values */
	private int[] _bounds;
	
	/** Bounds lock */
	private final transient Object _boundsLock = new Object();
	
	/** The cost of this Piece. */
	public int _cost;
	
	/** The name of the Piece. */
	public String _name;
	
	/** The description of the piece. */
	public String _description;
	
	
	/**
	 * Constructor for Piece.
	 * @param cost the cost of the Piece
	 * @param max the max times a player can use this Piece
	 * @param name the name of the Piece
	 * @param description the description of the Piece
	 */
	public Piece(int cost, String name, String description)
	{
		_cost = cost;
		_name = new String(name);
		_description = new String(description);
		_blocks = new HashMap<MapPoint, Block>();
	}
	
	
	/**
	 * Copy constructor for Piece.
	 * @param piece the Piece to copy
	 */
	public Piece(Piece piece) {
		this(piece._blocks, piece._cost, piece._name, piece._description);
	}
	
	/** Explicit constructor */
	public Piece(Map<MapPoint, Block> blocks, int cost, String name, String description) {
		_blocks = new HashMap<MapPoint, Block>();
		for(Map.Entry<MapPoint, Block> entry : blocks.entrySet()) {
			_blocks.put(new MapPoint(entry.getKey()), new Block(entry.getValue()));
		}
		_cost = cost;
		_name = name;  //Strings are immutable
		_description = description;
	}
	
	/**
	 * Adds a block to this Piece
	 * @param p
	 * @param b
	 * @return whether or not there was a block already in the specified position
	 */
	public boolean addBlock(MapPoint p, Block b) {
		synchronized(_boundsLock) {
			_bounds = null;
			if (_blocks.containsKey(p)) {
				return false;
			} else {
				_blocks.put(p,b);
				return true;
			}			
		}
	}
	
	/**
	 * Compute the minimum and maximum coordinates (relative to the piece's origin) of blocks in this piece
	 * Coordinates are stored in the following order: {min_x, min_y, max_x, max_y}
	 */
	public int[] getBounds(int rotation) {
		int lx, ly, ux, uy;
		
		synchronized(_boundsLock) {
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
			lx = _bounds[0]; 
			ly = _bounds[1]; 
			ux = _bounds[2]; 
			uy = _bounds[3];
		}
		
		int temp;
		for (int i = 0; i < rotation; i++) {
			temp = lx;
			lx = -uy;
			uy = ux;
			ux = -ly;
			ly = temp;
		}
		
		return new int[] {lx,ly,ux,uy};
	}
	
	/**
	 * Returns a new map of blocks rotated by the amount given
	 * @param rotation the number of clockwise 90 degree turns to make
	 * @return 
	 */
	public Map<MapPoint,Block> getRotatedBlocks(int rotation) {
		Map<MapPoint,Block> map = new HashMap<MapPoint,Block>();
		int x, y, temp;
		// Rotate the blocks in a new map
		for (MapPoint p : _blocks.keySet()) {
			x = p._x;
			y = p._y;
			// This is inverted because y goes down
			for (int i = 0; i < rotation; i++) {
				temp = x;
				x = -y;
				y = temp;
			}
			map.put(new MapPoint(x,y),_blocks.get(p));
		}
		return map;
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
