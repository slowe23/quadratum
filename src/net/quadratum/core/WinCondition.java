package net.quadratum.core;

import java.util.Map;

// Defines how win/loss conditions should be implemented
public interface WinCondition {

	/**
	 * Determines if a player has won the game.
	 * @param units The map of points to units for this player.
	 * @param playerInformation The information of the player who we are checking.
	 * @param playerNumber the number of the player
	 * @return true if the player has won, false otherwise.
	 */
	boolean hasPlayerWon(Map<MapPoint,Unit> units, PlayerInformation playerInformation, int playerNumber);
	
	/**
	 * Determines if a player has lost the game.
	 * @param units The map of points to units for this player.
	 * @param playerInformation The information of player who we are checking.
	 * @param playerNumber the number of the player
	 * @return true if the player has lost, false otherwise.
	 */
    boolean hasPlayerLost(Map<MapPoint,Unit> units, PlayerInformation playerInformation, int playerNumber);

    /**
     * Returns the objectives.
     * @return the objectives
     */
    public String getObjectives();
}
