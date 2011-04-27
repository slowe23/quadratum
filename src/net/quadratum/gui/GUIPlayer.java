package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
	 * Notifies the player that their turn has started.
	 */
	public void turnStart() {
		//TODO
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