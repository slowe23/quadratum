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
	private UnitsInfo _unitsInfo;
	private MapData _mapData;
	
	private ChatHandler _chatHandler;
	private Center _center;
	
	private DrawingMethods _drawingMethods;
	
	private int _id;
	
	public GUIPlayer() {
		_unitsInfo = new UnitsInfo(this);
		_mapData = new MapData();
		
		_chatHandler = new ChatHandler(this);
		_center = new Center(this, _chatHandler, _unitsInfo, _mapData);
		
		_drawingMethods = new DrawingMethods();
	}
	
	/** Notifies the player that there is a new game. */
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		GameWindow window = new GameWindow(this, _center, _chatHandler, _drawingMethods, _mapData, _unitsInfo);
		window.setVisible(true);
		
		_id = id;
		
		_center.start(core, mapData);
	}
	
	public int getID() {
		return _id;
	}
	
	/** Notifies the player of the pieces that are now available */
	public void updatePieces(List<Piece> pieces) {
		_center.setPieces(pieces);
	}
	
	/** Updates the map data. Currently unused but should be supported for future flexibility */
	public void updateMapData(MapData mapData) {
		_center.updateMapData(mapData);
	}
	
	/** Updates the position of units on the map. */
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		_center.update(units, lastAction);
	}
	
	/** Notifies the player that their turn has started. */
	public void turnStart() {
		//TODO
	}
	
	/** Notifies the player that the turn has changed. */
	public void updateTurn(int turn) {
		//TODO
	}
	
	/** Notifies the player that he has lost. */
	public void lost() {
		//TODO
	}
	
	/** Notifies the player that the game has ended. */
	public void end(GameStats stats) {
		//TODO
	}
	
	/** Notifies the player of a chat message (can be from self). */
	public void chatMessage(int from, String message) {
		_chatHandler.incomingMessage(from, message);
	}

	
}