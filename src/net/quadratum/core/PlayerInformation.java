package net.quadratum.core;

// Holds information about a single player that we don't want to let other players know about
public class PlayerInformation {

	/**
	 * Constructor for PlayerInformation.
	 * @param name the Player's name
	 * @param maxUnits the maximum number of units for this Player
	 * @param startingResources the starting resources for this player
	 */
	public PlayerInformation(String name, int maxUnits, int startingResources)
	{
		_name = new String(name); // Just to be safe
		_resources = startingResources;
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
	public int _resources;

	/** Name of the Player. */
	public String _name;

	/** Is the player ready? */
	public boolean _ready;
	
	/** Has the player quit? */
	public boolean _quit;
	
	/** Has the player lost? */
	public boolean _lost;
	
	/** How many units the Player can build. */
	public int _maxUnits;
}
