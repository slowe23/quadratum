package net.quadratum.core;

import java.util.Map;

public interface WinCondition {

	/**
	 * Determines if a player has won the game.
	 * @param units The map of points to unit IDs for this player.
	 * @param playerInformation The information of the player who we are checking.
	 * @return true if the player has won, false otherwise.
	 */
	boolean hasPlayerWon(Map<Point,Integer> units, PlayerInformation playerInformation);
	
	/**
	 * Determines if a player has lost the game.
	 * @param units The map of points to unit IDs for this player.
	 * @param playerInformation The information of player who we are checking.
	 * @return true if the player has lost, false otherwise.
	 */
    boolean eliminatePlayer(Map<Point,Integer> units, PlayerInformation playerInformation);
}
