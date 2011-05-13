package net.quadratum.gui;

import java.util.*;

import net.quadratum.core.*;

/** A class implementing the Player interface that serves as the connection between the core and the GUI */
public class GUIPlayer implements Player {
	private boolean _isPlayer;
	
	private int _id;
	
	public final DrawingMethods _drawingMethods;
	
	public final MapData _mapData;
	public final UnitsData _unitsData;
	private int _unitNumber;
	
	private final ChatHandler _chat;  //Manages chat stuff
	
	private MapPanel _map;  //Map and minimap display
	private UnitInfoPanel _selectedInfo;  //Displays info for the selected unit
	private UnitImagePanel _selectedImage;  //Displays an image of the selected unit
	
	private UnitsPanel _units;  //Displays the player's units
	private PiecesPanel _pieces;  //Displays the available pieces
	private ButtonsPanel _buttonsPanel;  //Displays some stuff
	private ObjectivesPanel _objectives;  //Game objectives
	
	private final GameWindow _gameWindow;
	
	private Core _core;
	
	private boolean _ready;
	private final Object _readyLock;
	
	public GUIPlayer() {
		this(true);
	}
	
	public GUIPlayer(boolean isPlayer) {
		_isPlayer = isPlayer;
		
		_drawingMethods = new DrawingMethods();
		
		_mapData = new MapData();
		_unitsData = new UnitsData(this);
		
		_unitNumber = 1;
		
		_chat = new ChatHandler(this);
		
		_gameWindow = new GameWindow(this, _chat);
		
		_ready = false;
		_readyLock = new Object();
	}
	
	/** Returns true if this GUIPlayer is a player, false if it is an observer */
	public boolean isPlayer() {
		return _isPlayer;
	}
	
	/** Gives the GUI player references to the various components that receive updates */
	public void setStuff(MapPanel map, UnitInfoPanel selectedInfo, UnitImagePanel selectedImage, UnitsPanel units, PiecesPanel pieces, ButtonsPanel buttons, ObjectivesPanel objectives) {
		_map = map;
		_selectedInfo = selectedInfo;
		_selectedImage = selectedImage;
		_units = units;
		_pieces = pieces;
		_buttonsPanel = buttons;
		_objectives = objectives;
	}
	
	/** Notifies the player that there is a new game. */
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_id = id;
		_core = core;
		
		_mapData._terrain = mapData._terrain;
		_mapData._placementArea = mapData._placementArea;
		
		_unitsData.start(_core);
		
		_chat.start(_core);
		
		_selectedInfo.start(_core);
		
		if(_isPlayer) {
			_buttonsPanel.start(_core.getRemainingUnits(this));
			_objectives.setText(_core.getObjectives(this));
		}
		
		_gameWindow.setVisible(true);
		
		synchronized(_readyLock) {
			_ready = true;
			_readyLock.notifyAll();
		}
		
		resourcesUpdated();
		mapUpdated();
		unitsUpdated();
		_map.centerAtPlacementArea();
		_map.repaintBoth();
	}
	
	public int getID() {
		return _id;
	}
	
	/** Notifies the player of the pieces that are now available */
	public void updatePieces(List<Piece> pieces) {
		blockUntilReady();
		
		if(_isPlayer)
			_pieces.setPieces(pieces);
	}
	
	/** Updates the map data. Currently unused but should be supported for future flexibility */
	public void updateMapData(MapData mapData) {
		blockUntilReady();
		
		synchronized(_mapData) {
			_mapData._terrain = mapData._terrain;
			_mapData._placementArea = mapData._placementArea;
		}
		
		mapUpdated();
	}
	
	/** Updates the position of units on the map. */
	public void updateMap(Map<MapPoint, Integer> units, Set<MapPoint> sight, Action lastAction) {
		blockUntilReady();
		
		synchronized(_unitsData) {
			_unitsData.setUnits(units);
			_unitsData.setSight(sight);
		}
		
		unitsUpdated();
		_map.scrollTo(lastAction, false);
		_map.repaintBoth();
		resourcesUpdated();
	}
	
	/** Notifies the player that their turn has started. */
	public void turnStart() {
		blockUntilReady();
		
		resourcesUpdated();
		
		_map.scrollTo(_unitsData.getSelectedLocation(), false);
		_map.repaintBoth();
	}
	
	/** Notifies the player that the turn has changed. */
	public void updateTurn(int turn) {
		blockUntilReady();
		
		if(turn==_id)
			_chat.incomingMessage(-1, "Your turn.");
		else
			_chat.incomingMessage(-1, _core.getPlayerName(turn)+"'s turn.");

		selectionUpdated();
		if(_isPlayer) {
			_buttonsPanel.turn(turn==_id);
			_objectives.setText(_core.getObjectives(this));
		}
	}
	
	/** Notifies the player that he has lost. */
	public void lost() {
		blockUntilReady();
		
		_chat.incomingMessage(-1, "You have lost.");
		if(_isPlayer)
			_buttonsPanel.lost();
	}
	
	/** Notifies the player that the game has ended. */
	public void end(GameStats stats) {
		_gameWindow.end(stats);
	}
	
	/** Notifies the player of a chat message (can be from self). */
	public void chatMessage(int from, String message) {
		blockUntilReady();
		
		_chat.incomingMessage(from, message);
	}
	
	/** Sends a message to the core to place a piece in the given unit */
	public void placePiece(Unit unit, int ind, int rotation, MapPoint pos) {
		blockUntilReady();
		
		if(_core.updateUnit(this, unit._id, ind, pos, rotation)) {
			_unitsData.refreshUnit(unit._id);
			unitsUpdated();
			resourcesUpdated();
		}
	}
	
	/** Handles a click on the map by placing a unit, selecting or deselecting, or performing an action, as appropriate */
	public void click(MapPoint point) {
		blockUntilReady();
		
		synchronized(_mapData) {
			synchronized(_unitsData) {
				Map<MapPoint, Action.ActionType> selActions = _unitsData.getSelectedActions();
				if(selActions!=null && selActions.containsKey(point)) {
					_core.unitAction(this, _unitsData.getSelectedID(), point);
				} else {
					Unit clicked = _unitsData.getUnit(point);
					if(clicked!=null) {
						selectUnit(clicked);
					} else {
						if(_mapData._placementArea!=null && _mapData._placementArea.contains(point)) {
							String newUnitName = "Unit "+_unitNumber;
							int newUnitID = _core.placeUnit(this, point, newUnitName);
							if(newUnitID!=-1) {
								_unitNumber++;
								
								_unitsData.addUnit(point, newUnitID, true);
								_mapData._placementArea.remove(point);
								
								unitsUpdated();
								placementUpdated();
								
								if(_isPlayer)
									_buttonsPanel.updateToPlace(_core.getRemainingUnits(this));
							}
						} else {
							_unitsData.deselect();
							selectionUpdated();
						}
					}
				}
			}
		}
	}
	
	/** Handles a click off the map by deselecting the selected unit */
	public void clickOut() {
		blockUntilReady();
		
		synchronized(_unitsData) {
			_unitsData.deselect();
			selectionUpdated();
		}
	}
	
	/** Sends out notifications that the placement phase of the game has ended */
	public void placementDone() {
		blockUntilReady();
		
		synchronized(_mapData) {
			_mapData._placementArea = null;
			placementUpdated();
			
			_core.ready(this);
			resourcesUpdated();
			
			if(_isPlayer)
				_buttonsPanel.gameStart();
		}
	}
	
	/** Called when the turn is done */
	public void turnDone() {
		_core.endTurn(this);
	}
	
	/** Called when the game window is closing */
	public void closing() {
		blockUntilReady();
		
		_core.quit(this);
	}
	
	/** Called when the player has forfeited the game */
	public void forfeit() {
		blockUntilReady();
		
		_core.quit(this);
		_gameWindow.setVisible(false);
	}
	
	/** Called in order to select a given unit */
	public void selectUnit(Unit u) {
		blockUntilReady();
		
		synchronized(_unitsData) {
			_unitsData.setSelected(u);
			selectionUpdated();
			_map.scrollTo(_unitsData.getSelectedLocation(), false);
		}
	}
	
	/** Sends out resource updated notifications */
	private void resourcesUpdated() {
		blockUntilReady();
		
		if(_isPlayer) {
			_buttonsPanel.updateResources(_core.getResources(this));
			_pieces.updateResources(_core.getResources(this));
		}
	}
	
	/** Sends out placement updated notifications */
	private void placementUpdated() {
		blockUntilReady();
		
		_map.placementUpdated();
	}
	
	/** Sends out map updated notifications */
	private void mapUpdated() {
		blockUntilReady();
		
		_map.mapUpdated();
	}
	
	/** Sends out selection updated notifications */
	private void selectionUpdated() {
		blockUntilReady();
		
		_map.selectionUpdated();
		_selectedInfo.selectionUpdated();
		_selectedImage.selectionUpdated();
		if(_isPlayer)
			_units.selectionUpdated();
	}
	
	/** Sends out unit updated notifications */
	private void unitsUpdated() {
		blockUntilReady();
		
		_map.unitsUpdated();
		_selectedInfo.selectionUpdated();
		_selectedImage.selectionUpdated();
		if(_isPlayer)
			_units.unitsUpdated();
	}
	
	/** Blocks until the start method has finished setting things up */
	private void blockUntilReady() {
		synchronized(_readyLock) {
			while(!_ready) {
				try {
					_readyLock.wait();
				} catch (InterruptedException e) {}
			}
		}
	}
}