package net.quadratum.core;

public class PlayerInformation {

	/**
	 * Constructor for PlayerInformation.
	 * @param name the Player's name
	 * @param maxUnits the maximum number of units for this Player
	 */
	PlayerInformation(String name, int maxUnits)
	{
		_name = new String(name); // Just to be safe
		_resources = 0;
		_ready = false;
		_quit = false;
		_lost = false;
		_maxUnits = maxUnits;
	}
	
	/**
	 * Copy contructor for PlayerInformation.
	 * @param pi the PlayerInformation to copy
	 */
	public PlayerInformation(PlayerInformation pi)
	{
		_resources = pi._resources;
		_name = new String(pi._name);
		_ready = pi._ready;
		_quit = pi._quit;
		_lost = pi._lost;
		_maxUnits = pi._maxUnits;
	}

	/** Number of resources a Player has. */
	int _resources;

	/** Name of the Player. */
	String _name;

	/** Is the player ready? */
	boolean _ready;
	
	/** Has the player quit? */
	boolean _quit;
	
	/** Has the player lost? */
	boolean _lost;
	
	/** How many units the Player can build. */
	int _maxUnits;
}
