package net.quadratum.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

// Represents a single player in the game (can be implemented by GUIs, AIs, etc.)
public interface Player {

	/** Notifies the player that there is a new game. */
	public void start(Core core, MapData mapData, int id, int totalPlayers);
	
	/** Notifies the player of the pieces that are now available */
	public void updatePieces(List<Piece> pieces);
	
	/** Notifies the player that the game has ended. */
	public void end(GameStats stats);
	
	/** Notifies the player that he has lost. */
	public void lost();
	
	/**
	 * Notifies the player that their turn has started.
	 */
	public void turnStart();
	
	/**
	 * Updates the map data. Currently unused but should be supported for future flexibility.
	 */
	public void updateMapData(MapData mapData);
	
	/** Updates the position of units and sight on the map. */
	public void updateMap(Map<MapPoint, Integer> units, Set<MapPoint> sight, Action lastAction);
	
	/** Notifies the player of a chat message (can be from self). */
	public void chatMessage(int from, String message);
	
	/**
	 * Notifies the player that the turn has changed.
	 * @param id the ID of the player whose turn it now is
	 */
	public void updateTurn(int id);
}