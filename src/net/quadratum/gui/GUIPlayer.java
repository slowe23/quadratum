//TODO: placing units during placement phase

package net.quadratum.gui;

import java.util.*;

import net.quadratum.core.*;

public class GUIPlayer implements Player {
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
	
	private final GameWindow _gameWindow;
	
	private Core _core;
	
	private boolean _ready;
	private final Object _readyLock;
	
	public GUIPlayer() {
		_drawingMethods = new DrawingMethods();
		
		_mapData = new MapData();
		_unitsData = new UnitsData(this);
		
		_unitNumber = 1;
		
		_chat = new ChatHandler(this);
		
		_gameWindow = new GameWindow(this, _chat);
		
		_ready = false;
		_readyLock = new Object();
	}
	
	public void setStuff(MapPanel map, UnitInfoPanel selectedInfo, UnitImagePanel selectedImage, UnitsPanel units, PiecesPanel pieces, ButtonsPanel buttons) {
		_map = map;
		_selectedInfo = selectedInfo;
		_selectedImage = selectedImage;
		_units = units;
		_pieces = pieces;
		_buttonsPanel = buttons;
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
		
		_buttonsPanel.start(_core.getRemainingUnits(this));
		
		synchronized(_readyLock) {
			_ready = true;
			_readyLock.notifyAll();
		}
		
		_gameWindow.setVisible(true);
		
		resourcesUpdated();
		mapUpdated();
		unitsUpdated();
		_map.centerAtPlacementArea();
	}
	
	public int getID() {
		return _id;
	}
	
	/** Notifies the player of the pieces that are now available */
	public void updatePieces(List<Piece> pieces) {
		blockUntilReady();
		
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
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		blockUntilReady();
		
		synchronized(_unitsData) {
			_unitsData.setUnits(units);
		}
		
		unitsUpdated();
		_map.scrollTo(lastAction, false);
		_map.repaintBoth();
		resourcesUpdated();
	}
	
	/** Notifies the player that their turn has started. */
	public void turnStart() {
		//Do nothing because this is completely redundant (see updateTurn, below)
	}
	
	/** Notifies the player that the turn has changed. */
	public void updateTurn(int turn) {
		blockUntilReady();
		
		if(turn==_id) {
			_chat.incomingMessage(-1, "Your turn.");
			selectionUpdated();
			_map.scrollTo(_unitsData.getSelectedLocation(), false);
			_map.repaintBoth();
		} else {
			_chat.incomingMessage(-1, _core.getPlayerName(turn)+"'s turn.");
			selectionUpdated();  //The available actions may change with the turn
		}
		_buttonsPanel.turn(turn==_id);
		resourcesUpdated();
	}
	
	/** Notifies the player that he has lost. */
	public void lost() {
		blockUntilReady();
		
		_chat.incomingMessage(-1, "You have lost.");
		
		//TODO: stuff?
	}
	
	/** Notifies the player that the game has ended. */
	public void end(GameStats stats) {
		blockUntilReady();
		
		_chat.incomingMessage(-1, "The game has ended.");

		//TODO: stuff?
	}
	
	/** Notifies the player of a chat message (can be from self). */
	public void chatMessage(int from, String message) {
		blockUntilReady();
		
		_chat.incomingMessage(from, message);
	}
	
	public void placePiece(Unit unit, int ind, MapPoint pos) {
		blockUntilReady();
		
		if(_core.updateUnit(this, unit._id, ind, pos)) {
			_unitsData.refreshUnit(unit._id);
			resourcesUpdated();
			unitsUpdated();
		}
	}
	
	public void click(MapPoint point) {
		blockUntilReady();
		
		synchronized(_mapData) {
			synchronized(_unitsData) {
				Map<MapPoint, Action.ActionType> selActions = _unitsData.getSelectedActions();
				if(selActions!=null && selActions.containsKey(point)) {
					_core.unitAction(this, _unitsData.getSelectedID(), point);
					if(_unitsData.getUnit(point)!=null)
						unitsUpdated();
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
	
	public void clickOut() {
		blockUntilReady();
		
		synchronized(_unitsData) {
			_unitsData.deselect();
			selectionUpdated();
		}
	}
	
	public void placementDone() {
		blockUntilReady();
		
		synchronized(_mapData) {
			_mapData._placementArea = null;
			placementUpdated();
			
			_core.ready(this);
			resourcesUpdated();
			_buttonsPanel.gameStart();
		}
	}
	
	public void turnDone() {
		_core.endTurn(this);
	}
	
	public void closing() {
		blockUntilReady();
		
		_core.quit(this);
	}
	
	public void selectUnit(Unit u) {
		blockUntilReady();
		
		synchronized(_unitsData) {
			_unitsData.setSelected(u);
			selectionUpdated();
			_map.scrollTo(_unitsData.getSelectedLocation(), false);
		}
	}
	
	private void resourcesUpdated() {
		blockUntilReady();
		
		_buttonsPanel.updateResources(_core.getResources(this));
		_pieces.updateResources(_core.getResources(this));
	}
	
	private void placementUpdated() {
		blockUntilReady();
		
		_map.placementUpdated();
	}
	
	private void mapUpdated() {
		blockUntilReady();
		
		_map.mapUpdated();
	}
	
	private void selectionUpdated() {
		blockUntilReady();
		
		_map.selectionUpdated();
		_selectedInfo.selectionUpdated();
		_selectedImage.selectionUpdated();
		_units.selectionUpdated();
	}
	
	private void unitsUpdated() {
		blockUntilReady();
		
		_map.unitsUpdated();
		_selectedInfo.selectionUpdated();
		_selectedImage.selectionUpdated();
		_units.unitsUpdated();
	}
	
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