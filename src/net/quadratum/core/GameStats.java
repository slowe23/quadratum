package net.quadratum.core;

import java.io.Serializable;

//A class for storing information about a game
//TODO: Expand this to provide more detailed information
public class GameStats  implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 8087720658214992773L;
	
	/** The ID of the winning player. */
	public int _victor;
	
	/**
	 * Constructor for GameStats.
	 * @param victor the ID of the winning player.
	 */
	public GameStats(int victor) {
		_victor = victor;
	}
}
