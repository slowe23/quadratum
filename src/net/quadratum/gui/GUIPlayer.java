package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.*;
import java.awt.Color;

public class GUIPlayer implements Player, UnitHolder {
	private Core _core;
	private ChatPanel _chat;
	
	private int _id;
	
	public static void main(String[] args) {
		GUIPlayer pl = new GUIPlayer();
		pl.createWindow();
		pl.start(new TestingCore(), null, 0, 1);
	}
	
	public GUIPlayer() {
	}
	
	public void createWindow() {
		GameWindow window = new GameWindow(this);
		_chat = window.getChatPanel();
		window.setVisible(true);
	}
	
	/**
	 * Notifies the player that there is a new game.
	 * @param core the game core
	 * @param id the ID for this Player
	 * @param mapData the MapData this game is using.
	 * @param totalPlayers the number of players in the game, including this one
	 */
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		
		_id = id;
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
		if(_chat!=null)
			_chat.addMessage(from, message);
	}
	
	public void sendChatMessage(String message) {
		if(_core!=null)
			_core.sendChatMessage(this, message);
	}
	
	public String getPlayerName(int i) {
		return _core.getPlayerName(i);
	}
	
	public Color getPlayerColor(int i) {
		return Color.red;  //TODO: something
	}
	
	public Map<MapPoint, Unit> getUnits() {
		Map<MapPoint, Unit> units = new HashMap<MapPoint, Unit>();
		
		///TODO:  something
		
		return units;
	}
	
	public Unit getSelectedUnit() {
		//TODO
		
		return null;
	}
}