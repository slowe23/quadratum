package net.quadratum.core;

import java.util.Map;

public interface Core {
	
	/**
	 * Adds a player to the game being run by this Core.
	 * @param p a player to be added
	 */
	void addPlayer(Player p);
	
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
	 * Callback that alerts the core that a chat message should be sent.
	 * @param id the ID of the player who sent the message
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
	 * Callback that alerts the core that a player is placing the given
	 * piece in the unit that is known by the given ID.
	 * @param id the ID of the player
	 * @param unitID the ID of the unit
	 * @param piece the piece that the player wishes to place in the
	 * given unit
	 * @return true if the update succeeded, false otherwise
	 */
	boolean updateUnit(int id, int unitID, Piece piece);
}
