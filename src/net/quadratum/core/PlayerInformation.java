package net.quadratum.core;

public class PlayerInformation {

	/**
	 * Constructor for PlayerInformation.
	 * @param id the player's secret id
	 * @param name the player's name
	 */
	public void PlayerInformation(int id, String name)
	{
		_id = id;
		_name = name;
		_resources = 0;
		_ready = false;
	}

	/** Number of resources a Player has. */
	int _resources;

	/** Player's sercret id */
	int _id;

	/** Name of the Player. */
	String _name;

	/** Is the player ready? */
	boolean _ready;
	
	/** Has the player quit? */
	boolean _quit;
}
