package net.quadratum.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

public class GameCore implements Core
{
	private int[][] _terrain;
	private ArrayList<HashSet<MapPoint>> _startingLocations;
	private ArrayList<Unit> _units;
	private ArrayList<UnitInformation> _unitInformation;
	private ArrayList<Player> _players;
	private ArrayList<PlayerInformation> _playerInformation;
	private ArrayList<Piece> _pieces;
	private WinCondition _winCondition;
	private int _turn; // -1 = not started, -2 = game over
	private boolean _started;
	private Object _chatLockObject, _turnLockObject;
	
	/**
	 * Constructor for GameCore.
	 * @param map the map file name
	 * @param winCondition the win condition
	 */
	public GameCore(String map, WinCondition winCondition, ArrayList<Piece> pieces)
	{
		_startingLocations = new ArrayList<HashSet<MapPoint>>();
		_units = new ArrayList<Unit>();
		_unitInformation = new ArrayList<UnitInformation>();
		_players = new ArrayList<Player>();
		_playerInformation = new ArrayList<PlayerInformation>();
		_pieces = pieces;
		_winCondition = winCondition;
		_turn = -1;
		_started = false;
		_chatLockObject = new Object();
		_turnLockObject = new Object();
		readMap(map);
	}
	
	/**
	 * Reads in the map.
	 * @param map the map file name
	 */
	// TODO Finish
	private void readMap(String map)
	{
		// TODO read real map data
		_terrain = new int[100][100];
		for(int i = 0; i < 100; i++)
		{
			for(int j = 0; j < 100; j++)
			{
				_terrain[i][j] = 0;
			}
		}
		HashSet<MapPoint> startingLocations = new HashSet<MapPoint>();
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				startingLocations.add(new MapPoint(i, j));
			}
		}
		_startingLocations.add(startingLocations);
		startingLocations = new HashSet<MapPoint>();
		for(int i = 90; i < 100; i++)
		{
			for(int j = 90; j < 100; j++)
			{
				startingLocations.add(new MapPoint(i, j));
			}
		}
		_startingLocations.add(startingLocations);
	}
	
	/**
	 * Adds a player to the game
	 * @param p the actual player
	 * @param playerName the name of the player
	 */
	public synchronized void addPlayer(Player p, String playerName, int maxUnits)
	{
		if(!_started && _players.size() < Constants.MAX_PLAYERS)
		{
			_players.add(p);
			_playerInformation.add(new PlayerInformation(new String(playerName), maxUnits));
		}
		else
		{
			// TODO log
		}
	}
		
	/**
	 * Gets a player's id from a reference.
	 * @param p the Player
	 * @return the id of that player
	 */
	private int getPlayerId(Player p)
	{
		for(int i = 0; i < _players.size(); i++)
		{
			if(_players.get(i) == p)
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Starts the game.
	 */
	public void start()
	{
		if(_players.size() == 0)
		{
			// TO DO: add exception to throw
		}
		if(!_started)
		{
			_started = true;
		}
		else
		{
			return;
		}
		MapData tempMap;
		ArrayList<Piece> tempPieces;
		PlayerStartThread playerStartThread;
		for(int i = 0; i < _players.size(); i++)
		{
			tempMap = new MapData(_terrain, _startingLocations.get(i));
			tempPieces = new ArrayList<Piece>();
			for(int j = 0; j < _pieces.size(); j++)
			{
				tempPieces.add(new Piece(_pieces.get(j)));
			}
			playerStartThread = new PlayerStartThread(_players.get(i), this, tempMap, i, _players.size());
			playerStartThread.start();
			_players.get(i).updatePieces(tempPieces);
		}
	}
	
	/**
	 * Callback for notifying the GameCode that a player is ready.
	 * @param p the Player
	 */
	public synchronized void ready(Player p)
	{
		if(_turn > -1)
		{
			return;
		}
		_playerInformation.get(getPlayerId(p))._ready = true;
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(!_playerInformation.get(i)._ready)
			{
				return;
			}
		}
		_turn = 0;
		_players.get(0).turnStart();
	}
	
	/**
	 * Callback for ending a turn.
	 * @param p the Player
	 */
	public void endTurn(Player p)
	{
		synchronized(_turnLockObject)
		{
			if(getPlayerId(p) == _turn)
			{
				nextTurn();
			}
		}
	}
	
	/**
	 * Performs a unit's action.
	 * @param p the player
	 * @param unitId the unit's id
	 * @param coords the point at which the action should be taken
	 * @return true if the action has been taken, false if otherwise
	 */
	public boolean unitAction(Player p, int unitId, MapPoint coords)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(_turn != player)
			{
				return false;
			}
			if(unitId < 0 || unitId >= _units.size())
			{
				return false;
			}
			if(_units.get(unitId)._owner != player)
			{
				return false;
			}
			coords = new MapPoint(coords);
			// TODO add real attacking
			HashSet<MapPoint> valid;
			MapPoint oldCoords = new MapPoint(_unitInformation.get(unitId)._position);
			if(getUnitAtPoint(coords) == -1) // Movement
			{
				if(_unitInformation.get(unitId)._hasMoved)
				{
					return false;
				}
				valid = getAreaForUnit(unitId, 0);
				if(!valid.contains(coords))
				{
					return false;
				}
				if(getUnitAtPoint(coords) != -1)
				{
					return false;
				}
				_unitInformation.get(unitId)._hasMoved = true;
				_unitInformation.get(unitId)._position = coords;
				updateMaps(new Action(Action.ActionType.MOVE, oldCoords, coords));
			}
			else // Attacking
			{
				if(_unitInformation.get(unitId)._hasAttacked)
				{
					return false;
				}
				valid = getAreaForUnit(unitId, 1);
				if(!valid.contains(coords))
				{
					return false;
				}
				int unit = getUnitAtPoint(coords);
				if(unit == -1)
				{
					return false;
				}
				if(_units.get(unit)._owner == player)
				{
					return false;
				}
				_unitInformation.get(unitId)._hasAttacked = true;
				_unitInformation.get(unit)._position = new MapPoint(-1, -1);
				updateMaps(new Action(Action.ActionType.UNIT_DIED, coords, coords));
			}
			return true;
		}
	}
	
	/**
	 * Calculates the valid actions for a given unit
	 * @param p the Player
	 * @param unitId the unit's id
	 * @return a map of MapPoints to Action.ActionTypes that represents what actions can be taken where
	 */
	public HashMap<MapPoint, Action.ActionType> getValidActions(Player p, int unitId)
	{
		int player = getPlayerId(p);
		if(unitId < 0 || unitId >= _units.size() || _units.get(unitId)._owner != player || _turn != player)
		{
			return null;
		}
		HashMap<MapPoint, Action.ActionType> actions = new HashMap<MapPoint, Action.ActionType>();
		if(_unitInformation.get(unitId)._hasMoved && _unitInformation.get(unitId)._hasAttacked)
		{
			return actions;
		}
		else
		{
			if(!_unitInformation.get(unitId)._hasMoved)
			{
				for(MapPoint point : getAreaForUnit(unitId, 0))
				{
					if(getUnitAtPoint(point) == -1)
					{
						actions.put(new MapPoint(point), Action.ActionType.MOVE);
					}
				}
			}
			if(!_unitInformation.get(unitId)._hasAttacked)
			{
				int unit;
				for(MapPoint point : getAreaForUnit(unitId, 1))
				{
					unit = getUnitAtPoint(point);
					if(unit != -1 && _units.get(unit)._owner != player)
					{
						actions.put(new MapPoint(point), Action.ActionType.ATTACK);
					}
				}
			}
			return actions;
		}
	}
	
	/**
	 * Gets a unit at a specific point.
	 * @param point the MapPoint to check
	 * @return the id of the unit, -1 if no unit exists
	 */
	private int getUnitAtPoint(MapPoint point)
	{
		for(int i = 0; i < _unitInformation.size(); i++)
		{
			if(_unitInformation.get(i)._position.equals(point))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Generates what the units player can see.
	 * @param player the player
	 * @return a map of MapPoints to unit ids
	 */
	private HashMap<MapPoint, Integer> generateMapForPlayer(int player)
	{
		HashMap<MapPoint, Integer> units = new HashMap<MapPoint, Integer>();
		HashSet<MapPoint> visible = getVisible(player);
		for(int i = 0; i < _unitInformation.size(); i++)
		{
			if(visible.contains(_unitInformation.get(i)._position))
			{
				units.put(new MapPoint(_unitInformation.get(i)._position), new Integer(i));
			}
		}
		return units;
	}
	
	/**
	 * Generates new map information and sends it to all players.
	 * @param action the last Action that occured
	 */
	private void updateMaps(Action action)
	{
		for(int i = 0; i < _players.size(); i++)
		{
			if(!_playerInformation.get(i)._quit)
			{
				if(getVisible(i).contains(action._dest))
				{
					// TODO create new _source for attacks that the player can't see
					_players.get(i).updateMap(generateMapForPlayer(i), new Action(action));
				}
				else
				{
					// TODO generate new _dest if player can see _source but not _dest (find the closest point in the visible area) 
					_players.get(i).updateMap(generateMapForPlayer(i), null);
				}
			}
		}
	}
	
	/**
	 * Callback for quitting a game.
	 * @param p the Player
	 */
	public void quit(Player p)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			_playerInformation.get(player)._lost = true;
			_playerInformation.get(player)._quit = true;
			if(_turn == player)
			{
				nextTurn();
			}
		}
	}
	
	/**
	 * Checks if someone has won/lost
	 * @return the id of the player that has won, -1 if no one has won
	 */
	private int checkWinLoss()
	{
		HashMap<MapPoint, Unit> units;
		for(int i = 0; i < _players.size(); i++)
		{
			units = new HashMap<MapPoint, Unit>();
			// Generate list of units
			for(int j = 0; j < _units.size(); j++)
			{
				if(_units.get(j)._owner == i)
				{
					units.put(new MapPoint(_unitInformation.get(j)._position), new Unit(_units.get(j)));
				}
			}
			if(_winCondition.hasPlayerWon(units, new PlayerInformation(_playerInformation.get(i))))
			{
				return i;
			}
			if(_winCondition.hasPlayerLost(units, new PlayerInformation(_playerInformation.get(i))))
			{
				_playerInformation.get(i)._lost = true;
				_players.get(i).lost();
			}
		}
		int notLost = 0;
		int winner = -1;
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(!_playerInformation.get(i)._lost && !_playerInformation.get(i)._quit)
			{
				notLost++;
				winner = i;
			}
		}
		if(notLost == 1)
		{
			return winner;
		}
		return -1;
	}
	
	/**
	 * Moves onto the next turn and calculates wins
	 */
	private void nextTurn()
	{
		int winner = checkWinLoss();
		if(winner != -1)
		{
			endGame(winner);
			return;
		}
		for(int i = 0; i < _players.size() - 1; i++)
		{
			_turn++;
			if(_turn == _players.size())
			{
				_turn = 0;
			}
			if(_playerInformation.get(_turn)._quit || _playerInformation.get(_turn)._lost)
			{
				continue;
			}
			for(int j = 0; j < _players.size(); j++)
			{
				_players.get(j).updateTurn(_turn);
			}
			// TODO add thread here
			TurnStartThread turnStartThread = new TurnStartThread(_players.get(_turn));
			turnStartThread.start();
			break;
		}
		// If we have reached here, the game should be over...
		log("Everyone has lost, ending game...", 1);
		endGame(-1);
	}
	
	/**
	 * Ends the game and sends stats.
	 * @param winner the winner of the game
	 */
	private void endGame(int winner)
	{
		_turn = -2;
		for(int i = 0; i < _players.size(); i++)
		{
			if(!_playerInformation.get(i)._quit)
			{
				_players.get(i).end(new GameStats());
			}
		}
		log("GAME OVER", 1);
		log("Player " + winner + " won!", 1);
	}
	
	/**
	 * Sends a chat message to all players from the system
	 * @param message the message to send
	 */
	private void sendChatMessage(String message)
	{
		synchronized(_chatLockObject)
		{
			for(int i = 0; i < _players.size(); i++)
			{
				_players.get(i).chatMessage(-1, new String(message));
			}
			log("System chat message", 1);
			log("\tMessage: " + message, 1);
		}
	}
	
	/**
	 * Sends a chat message to all players
	 * @param p the Player
	 * @param message the message to send
	 */
	public void sendChatMessage(Player p, String message)
	{
		if(message.length() > 0)
		{
			synchronized(_chatLockObject)
			{
				message = new String(message);
				int from = getPlayerId(p);
				for(int i = 0; i < _players.size(); i++)
				{
					_players.get(i).chatMessage(from, new String(message));
				}
				log("Chat message from player " + from, 1);
				log("\tMessage: " + message, 1);
			}
		}
		else
		{
			log("Player " + getPlayerId(p) + " tried to send an empty message", 1);
		}
	}
	
	/**
	 * Callback for placing a unit.
	 * @param p the Player
	 * @param coords the MapPoint at which the unit should be placed
	 * @param name the name of the unit
	 * @return true if the unit is placed sucessfully, false otherwise
	 */
	public boolean placeUnit(Player p, MapPoint coords, String name)
	{
		synchronized(_turnLockObject)
		{
			coords = new MapPoint(coords);
			int player = getPlayerId(p);
			if(_turn != -1)
			{
				return false;
			}
			if(getRemainingUnits(p) == 0)
			{
				return false;
			}
			if(getUnitAtPoint(coords) == -1 && _startingLocations.get(player).contains(coords))
			{
				_units.add(new Unit(new String(name), player));
				_unitInformation.add(new UnitInformation(coords));
				return true;
				// TODO add "brain block"
			}
			return false;
		}
	}
	
	/**
	 * Callback for updating a unit.
	 * @param p the Player
	 * @param unitId the id of the unit
	 * @param pieceId the id of the piece to add
	 * @param coords the coordinates in the unit to place the piece
	 * @return true if the piece is added sucessfuly, false otherwise
	 */
	public boolean updateUnit(Player p, int unitId, int pieceId, MapPoint coords)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			coords = new MapPoint(coords);
			if(_turn != -1 && _turn != player)
			{
				log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but it was not their turn", 2);
				log("\tTurn was: " + _turn, 2);
				return false;
				// TODO finish logging
			}
			if(pieceId < 0 || pieceId >= _pieces.size() || unitId < 0 || unitId >= _units.size())
			{
				log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but there was an invalid piece or unit id", 2);
				return false;
			}
			Piece piece = _pieces.get(pieceId);
			Unit unit = _units.get(unitId);
			if(unit._owner != player)
			{
				log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but they did not own the unit", 2);
				return false;
			}
			int resources = _playerInformation.get(player)._resources;
			if(piece._cost > resources)
			{
				log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but they have enough resources", 2);
				log("\tResources: " + resources, 2);
				return false;
			}
			for(MapPoint key : piece._blocks.keySet())
			{
				if(unit._blocks.containsKey(new MapPoint(coords._x + key._x, coords._y + key._y)))
				{
					log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but there was a collision", 2);
					log("\tCollision location on the unit: " + (new MapPoint(coords._x + key._x, coords._y + key._y)), 2);
					return false;
				}
				if((coords._x + key._x) < 0 || (coords._x + key._x) >= Constants.UNIT_SIZE && (coords._y + key._y) < 0 || (coords._y + key._y) >= Constants.UNIT_SIZE)
				{
					log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but piece went out of bounds", 2);
					log("\tLocation out of bounds: " + (new MapPoint(coords._x + key._x, coords._y + key._y)), 2);
					return false;
				}
			}
			for(MapPoint key : piece._blocks.keySet())
			{
				unit._blocks.put(new MapPoint(coords._x + key._x, coords._y + key._y), new Block(piece._blocks.get(key)));
			}
			log("Player " + player + "called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") ", 1);
			log("\tAnswer: true", 1);
			return true;
		}
	}
	
	/**
	 * Gets a player's name
	 * @param player the player's id
	 * @return the player's name
	 */
	public String getPlayerName(int player)
	{
		String name = new String(_playerInformation.get(player)._name);
		log("Player " + player + "called getPlayerName(player: " + player + ")", 1);
		log("\tAnswer: " + name, 1);
		return name;
	}
	
	/**
	 * Get's a player's resources.
	 * @param p the Player
	 * @return the Player's recourses
	 */
	public int getResources(Player p)
	{
		int resources = _playerInformation.get(getPlayerId(p))._resources;
		log("Player " + getPlayerId(p) + "called getResources()", 1);
		log("\tAnswer: " + resources, 1);
		return resources;
	}
	
	/**
	 * Returns information about a specific unit.
	 * @param p the Player who is requesting the information
	 * @param unitId the id of the unit
	 * @return a copy of the Unit
	 */
	public Unit getUnit(Player p, int unitId)
	{
		if(unitId >= _units.size() && unitId < 0)
		{
			log("Player " + getPlayerId(p) + "called getUnit(unitId: " + unitId + ") on a unit that did not exist", 2);
			return null;
		}
		Unit unit = new Unit(_units.get(unitId));
		if(!getVisible(getPlayerId(p)).contains(_unitInformation.get(unitId)._position))
		{
			log("Player " + getPlayerId(p) + "called getUnit(unitId: " + unitId + ") on a unit that did belong to them", 2);
			return null;
		}
		log("Player " + getPlayerId(p) + "called getUnit(unitId: " + unitId + ")", 1);
		log("\tAnswer: (success)", 1);
		return unit;
		
	}
	
	/**
	 * Gets the visible area for a player.
	 * @param p the Player
	 * @return the MapPoints that the player can see
	 */
	private HashSet<MapPoint> getVisible(int player)
	{
		HashSet<MapPoint> visible = new HashSet<MapPoint>();
		HashSet<MapPoint> unitVisible;
		for(int i = 0; i < _units.size(); i++)
		{
			if(_units.get(i)._owner == player)
			{
				unitVisible = getAreaForUnit(i, 2);
				for(MapPoint point : unitVisible)
				{
					visible.add(point);
				}
			}
		}
		log("getVisible(player: " + player + ")", 1);
		log("\tAnswer was: " + visible, 1);
		return visible;
	}
	
	/**
	 * Gets the action/visible area for a single unit.
	 * @param u the Unit
	 * @return the MapPoints that the unit can act upon/see
	 */
	// TODO add support for sight blocks and special movement blocks/terrain
	private HashSet<MapPoint> getAreaForUnit(int u, int type)
	{
		int radius;
		if(type == 0) // Movement area
		{
			radius = 3;
		}
		else if(type == 1) // Attack area
		{
			radius = 2;
		}
		else // Visible area
		{
			radius = 5;
		}
		UnitInformation info = _unitInformation.get(u);
		HashSet<MapPoint> visible = new HashSet<MapPoint>();
		for(int x = info._position._x - radius; x < (info._position._x + radius + 1); x++)
		{
			for(int y = info._position._y - radius; y < (info._position._y + radius + 1); y++)
			{
				if(x >= 0 && y >= 0 && x < _terrain.length && y < _terrain[0].length) // Check to make sure the point is on the board
				{
					if((Math.abs(info._position._x - x) + Math.abs(info._position._y - y)) < radius)
					{
						visible.add(new MapPoint(x, y));
					}
				}
			}
		}
		log("getAreaForUnit(u: " + u + ", type: " + type + ")", 1);
		log("\tAnswer was: " + visible, 1);
		return visible;
	}
	
	/**
	 * Returns the number of units left to build for a given player.
	 * @param p the Player we are checking
	 * @return the number of units the Player can build
	 */
	public int getRemainingUnits(Player p)
	{
		int built = 0;
		int player = getPlayerId(p);
		for(int i = 0; i < _units.size(); i++)
		{
			if(_units.get(i)._owner == player)
			{
				built++;
			}
		}
		int remaining = _playerInformation.get(player)._maxUnits - built;
		log("Player " + player + " called getRemainingUnits()", 1);
		log("\tAnswer was: " + remaining, 1);
		return remaining;
	}
	
	/**
	 * Logs a message.
	 * @param message the message to log
	 * @param level the severity level: 1 = debug, 2 = warning, 3 = error
	 */
	private void log(String message, int level)
	{
		if(Constants.DEBUG_LEVEL == -1)
		{
			return;
		}
		if(level > Constants.DEBUG_LEVEL)
		{
			if(level == 2)
			{
				System.out.print("WARNING: ");
			}
			else if(level == 3)
			{
				System.out.print("ERROR: ");
			}
			System.out.println(message);
		}
	}
}