package net.quadratum.core;

import java.util.List;
import java.util.Map;

public interface Player {

	/**
	 * Notifies the player that there is a new game.
	 * @param core the game core
	 * @param id the ID for this Player.
	 * @param mapData the MapData this game is using.
	 * @param otherPlayers the number of other players in the Game.
	 * @param pieces the Pieces that are available for use.
	 */
	void start(Core core, int id, MapData mapData, int otherPlayers, List<Piece> pieces);
	
	/**
	 * Notifies the player that the game has ended.
	 * @param stats game stats for the game that was just played.
	 */
	void end(int[] stats);
	
	/**
	 * Notifies the player that he has lost.
	 */
	void lost();
	
	/**
	 * Notifies the player that their turn has started.
	 */
	void turnStart();
	
	/**
	 * Updates the position of units on the map.
	 * @param units new positions of units.
	 * @param resources new number of resources for this Player. 
	 * @param lastAction The
	 */
	void updateMap(Map<Point,Integer> units, int resources, Action lastAction);
	
	/**
	 * Notifies the player of a chat message.
	 * @param from the ID of the player who this message was sent by.
	 * @param message the message that is being sent.
	 */
	void chatMessage(int from, String message);
	
}
