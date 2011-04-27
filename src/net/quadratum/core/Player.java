package net.quadratum.core;

import java.util.List;
import java.util.Map;

public interface Player {

	/** Notifies the player that there is a new game. */
	public void start(Core core, MapData mapData, int id, int totalPlayers);
	
	/** Notifies the player of the pieces that are now available */
	public void updatePieces(List<Piece> pieces);
	
	/** Notifies the player that the game has ended. */
	public void end(GameStats stats);
	
	/** Notifies the player that he has lost. */
	public void lost();
	
	/** Updates the map data. Currently unused but should be supported for future flexibility */
	public void updateMapData(MapData mapData);
	
	/** Updates the position of units on the map. */
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction);
	
	/** Notifies the player of a chat message (can be from self). */
	public void chatMessage(int from, String message);
	
	/** Notifies the player that the turn has changed. */
	public void updateTurn(int turn);
}