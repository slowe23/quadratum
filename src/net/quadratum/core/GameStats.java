package net.quadratum.core;

import java.io.Serializable;

//A class for storing information about a game
//TODO: Expand this to provide more detailed information
public class GameStats  implements Serializable {
	public int _victor;  //The ID of the winning player

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = 8087720658214992773L;
	
	public GameStats(int victor) {
		_victor = victor;
	}
}
