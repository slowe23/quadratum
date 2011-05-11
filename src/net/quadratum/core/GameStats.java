package net.quadratum.core;

import java.io.Serializable;

// A class for storing information about a game
public class GameStats  implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 8087720658214992773L;
	
	/** The ID of the winning player. */
	public final int _victor;
	public final String _victorName;
	
	/**
	 * Constructor for GameStats.
	 * @param victor the ID of the winning player.
	 */
	public GameStats(int vID, String vName) {
		_victor = vID;
		_victorName = vName;
	}
	
	public String toString() {
		return "Winner: "+_victorName;
	}
}