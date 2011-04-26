package net.quadratum.core;

public class PlayerInformation {

	/**
	 * Constructor for PlayerInformation.
	 * @param name the Player's name
	 * @param maxUnits the maximum number of units for this Player
	 */
	public PlayerInformation(String name, int maxUnits)
	{
		_name = new String(name); // Just to be safe
		_resources = 0;
		_ready = false;
		_quit = false;
		_lost = false;
		_maxUnits = maxUnits;
	}

	/** Number of resources a Player has. */
	public int _resources;

	/** Player's sercret id */
	public int _id;

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
