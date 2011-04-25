package net.quadratum.core;

import java.util.List;
import java.util.Map;

public interface Player {

	/**
	 * Notifies the player that there is a new game.
	 * @param core the game core
	 * @param id the ID for this Player
	 * @param mapData the MapData this game is using.
	 * @param totalPlayers the number of players in the game, including this one
	 */
	public void start(Core core, MapData mapData, int id, int totalPlayers);
	
	/**
	 * Notifies the player of the pieces that are now available
	 *
	 * @param pieces the Pieces that are available for use.
	 */
	public void updatePieces(List<Piece> pieces);
	
	/**
	 * Notifies the player that the game has ended.
	 * @param stats Game stats for the game that was just played.
	 */
	public void end(GameStats stats);
	
	/**
	 * Notifies the player that he has lost.
	 */
	public void lost();
	
	/**
	 * Notifies the player that their turn has started.
	 */
	public void turnStart();
	
	/**
	 * Updates the map data
	 * @note Currently unused but should be supported for future flexibility
	 */
	public void updateMapData(MapData mapData);
	
	/**
	 * Updates the position of units on the map.
	 * @param units new positions of units.
	 * @param lastAction The action that caused this update
	 */
	public void updateMap(Map<Point, Integer> units, Action lastAction);
	
	/**
	 * Notifies the player of a chat message.
	 * @param from the ID of the player who this message was sent by.
	 * @param message the message that is being sent.
	 */
	public void chatMessage(int from, String message);	
}