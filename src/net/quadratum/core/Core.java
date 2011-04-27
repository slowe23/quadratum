package net.quadratum.core;

import java.util.Map;

public interface Core {
	
	/**
	 * Adds a player to the game being run by this Core.
	 * @param p a player to be added
	 * @param playerName the player to be added
	 * @param maxUnits the maximum number of units this palyer can add
	 */
	public void addPlayer(Player p, String playerName, int maxUnits);
	
	/**
	 * Starts the game.
	 */
	public void start();
	
	/**
	 * Callback that alerts the core when a player is ready to start the game,
	 * that is, this is called by a player once all of their units have been
	 * placed.
	 * @param p the Player itself who is now ready
	 */
	public void ready(Player p);
	
	/**
	 * Callback which alerts the core when a player is done with their turn.
	 * @param p the Player itself who is now done
	 */
	public void endTurn(Player p);
	
	/**
	 * Callback which lets the core know that a player wants to make a unit
	 * take a particular action.
	 * @param p the Player itself
	 * @param unitID the ID of the unit
	 * @param coords the point on the map which is the destination of the action,
	 * the action itself will be inferred from this and the map
	 * @return true if the action succeeded, false if the action was invalid.
	 */
	public boolean unitAction(Player p, int unitID, MapPoint coords);
	
	/**
	 * Gets the valid actions for the given player's given unit.
	 * @param p the Player itself
	 * @param unitID the ID of the unit
	 * @return a Map of locations to the action that can be taken at that location,
	 * or null if the given unit has already acted.
	 */
	public Map<MapPoint,Action.ActionType> getValidActions(Player p, int unitID);
	
	/**
	 * Callback that alerts the core that a player has left the game.
	 * @param p the Player itself
	 */
	public void quit(Player p);
	
	/**
	 * Callback that alerts the core that a chat message should be sent
	 * @param p the Player itself who sent the message - can be this
	 * @param message the message that is being sent
	 */
	public void sendChatMessage(Player p, String message);
	
	/**
	 * Callback that alerts the core that a player is placing a unit.
	 * @param p the Player itself
	 * @param coords the coordinates at which a player is placing the unit
	 * @param name the name of the Unit
	 * @return -1 if the unit could not be place, the unit id otherwise
	 */
	int placeUnit(Player p, MapPoint coords, String name);
	
	/**
	 * Returns the number of units left to place for the given player
	 * @param p the Player itself
	 */
	public int getRemainingUnits(Player p);
	
	/**
	 * Returns the unit with the given ID
	 * @param p the Player itself
	 * @param unitID The unit in question's id
	 */
	public Unit getUnit(Player p, int unitID);
	
	/**
	 * Callback that alerts the core that a player is placing the given
	 * piece in the unit that is known by the given ID.
	 * @param p the Player itself
	 * @param unitID the ID of the unit
	 * @param pieceID the ID of the piece that the player wishes to place in the
	 * given unit
	 * @param coords The coordinates in the unit where the piece should be placed
	 * @return true if the update succeeded, false otherwise
	 */
	public boolean updateUnit(Player p, int unitID, int pieceID, MapPoint coords);
	
	/**
	 * Returns the name of a player
	 * @param player the id of the player (i.e. 1-n where n is the number of players, not the player's secret id)
	 */
	public String getPlayerName(int player);
	
	/**
	 * Get's a player's resources.
	 * @param p the Player itself
	 */
	public int getResources(Player p);
}
