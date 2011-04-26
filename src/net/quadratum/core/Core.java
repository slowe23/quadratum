package net.quadratum.core;

import java.util.Map;

public interface Core {
	
	/**
	 * Adds a player to the game being run by this Core.
	 * @param p a player to be added
	 * @param playerName the player to be added
	 */
	void addPlayer(Player p, String playerName);
	
	/**
	 * Starts the game.
	 */
	void start();
	
	/**
	 * Callback that alerts the core when a player is ready to start the game,
	 * that is, this is called by a player once all of their units have been
	 * placed.
	 * @param id the ID of the player who is now ready
	 */
	void ready(int id);
	
	/**
	 * Callback which alerts the core when a player is done with their turn.
	 * @param id the ID of the player who is now done
	 */
	void endTurn(int id);
	
	/**
	 * Callback which lets the core know that a player wants to make a unit
	 * take a particular action.
	 * @param id the ID of the player
	 * @param unitID the ID of the unit
	 * @param coords the point on the map which is the destination of the action,
	 * the action itself will be inferred from this and the map
	 * @return true if the action succeeded, false if the action was invalid.
	 */
	boolean unitAction(int id, int unitID, Point coords);
	
	/**
	 * Gets the valid actions for the given player's given unit.
	 * @param id the ID of the player
	 * @param unitID the ID of the unit
	 * @return a Map of locations to the action that can be taken at that location,
	 * or null if the given unit has already acted.
	 */
	Map<Point,Action.ActionType> getValidActions(int id, int unitID);
	
	/**
	 * Callback that alerts the core that a player has left the game.
	 * @param id the ID of the player
	 */
	void quit(int id);
	
	/**
	 * Callback that alerts the core that a chat message should be sent
	 * @param id the ID of the player who sent the message - can be this
	 * @param message the message that is being sent
	 */
	void sendChatMessage(int id, String message);
	
	/**
	 * Callback that alerts the core that a player is placing a unit.
	 * @param id the ID of the player
	 * @param coords the coordinates at which a player is placing the unit
	 * @return true if the placement succeeded, false otherwise
	 */
	boolean placeUnit(int id, Point coords);
	
	/**
	 * Returns the number of units left to place for the given player
	 * @param id The player id
	 */
	int getRemainingUnits(int id);
	
	/**
	 * Returns the unit with the given ID
	 * @param id The player's id
	 * @param id The unit in question's id
	 */
	Unit getUnit(int id, int unitID);
	
	/**
	 * Callback that alerts the core that a player is placing the given
	 * piece in the unit that is known by the given ID.
	 * @param id the ID of the player
	 * @param unitID the ID of the unit
	 * @param pieceID the ID of the piece that the player wishes to place in the
	 * given unit
	 * @param coords The coordinates in the unit where the piece should be placed
	 * @return true if the update succeeded, false otherwise
	 */
	boolean updateUnit(int id, int unitID, int pieceID, Point coords);
	
	/**
	 * Returns the name of a player
	 * @param player the id of the player (i.e. 1-n where n is the number of players, not the player's secret id)
	 */
	String getPlayerName(int player);
	
	/**
	 * Get's a player's resources.
	 * @param id the secret id
	 */
	int getResources(int id);
	
	/**
	 * Gets the cost of a given piece for a given player
	 * @param id the Player's id
	 * @param pieceID the ID of the piece in question
	 * @param unitID the ID of the unit in which to place the piece
	 */
	int getPieceCost(int id, int pieceID, int unitID);
	
	/**
	 * Returns whether the given piece may be placed in the given unit of the given player
	 *
	 * @param id the Player's id
	 * @param pieceID the ID of the piece in question
	 * @param unitID the ID of the unit in which to place the piece
	 */
	boolean isAvailable(int id, int pieceID, int unitID);
}
