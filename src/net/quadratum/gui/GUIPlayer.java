package net.quadratum.gui;

import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;

public class GUIPlayer implements Player {
	private Core _core;
	private int _id;
	
	public final ChatHandler _chatHandler;
	public final GraphicsCoordinator _graphicsCoordinator;
	public final UnitHandler _unitHandler;
	public final GameplayHandler _gameplayHandler;
	
	public GUIPlayer() {
		_chatHandler = new ChatHandler(this);
		_graphicsCoordinator = new GraphicsCoordinator();
		_unitHandler = new UnitHandler();
		_gameplayHandler = new GameplayHandler(this);
		_gameplayHandler.setChatHandler(_chatHandler);
		_gameplayHandler.setUnitHandler(_unitHandler);
	}
	
	/**
	 * Notifies the player that there is a new game.
	 * @param core the game core
	 * @param id the ID for this Player
	 * @param mapData the MapData this game is using.
	 * @param totalPlayers the number of players in the game, including this one
	 */
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		GameWindow window = new GameWindow(this);
		window.setVisible(true);
		
		_core = core;
		_id = id;
		
		_gameplayHandler.start(core, mapData);
	}
	
	/**
	 * Notifies the player of the pieces that are now available
	 *
	 * @param pieces the Pieces that are available for use.
	 */
	public void updatePieces(List<Piece> pieces) {
		//TODO
	}
	
	/**
	 * Notifies the player that he has lost.
	 */
	public void lost() {
		//TODO
	}
	
	/**
	 * Notifies the player that the game has ended.
	 * @param stats Game stats for the game that was just played.
	 */
	public void end(GameStats stats) {
		//TODO
	}
	
	/**
	 * Notifies the player that it is his turn.
	 */
	public void turnStart() {
		//TODO don't remove this, Alex is using it
		
	}
	
	
	/**
	 * Notifies the player that the turn has changed.
	 * @param turn the player whose turn it is
	 */
	public void updateTurn(int id) {
		//TODO this should be used to change the GUI, not to notify the player
		//that it is his own turn.
	}
	
	/**
	 * Updates the map data
	 * @note Currently unused but should be supported for future flexibility
	 */
	public void updateMapData(MapData mapData) {
		//TODO
	}
	
	
	
	/**
	 * Updates the position of units on the map.
	 * @param units new positions of units.
	 * @param lastAction The action that caused this update
	 */
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		//TODO
	}
	
	/**
	 * Notifies the player of a chat message.
	 * @param from the ID of the player who this message was sent by.
	 * @param message the message that is being sent.
	 */
	public void chatMessage(int from, String message) {
		_chatHandler.getMessage(from, message);
	}

	
}