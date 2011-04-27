package net.quadratum.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.quadratum.main.Main;

public class GameCore implements Core
{
	private Main _main;
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
	private OutputStream _log;
	
	/**
	 * Constructor for GameCore.
	 * @param map the map file name
	 * @param winCondition the win condition
	 */
	public GameCore(Main m, String map, WinCondition winCondition, ArrayList<Piece> pieces)
	{
		_main = m;
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
			log("Added player\n"
				+ "\tNumber: " + (_players.size() - 1) + "\n"
				+ "\tName: " + playerName + "\n"
				+ "\tMax Units: " + maxUnits, 1);
		}
		else
		{
			if(_started)
			{
				log("Tried to add a player but the game has already started", 2);
			}
			else
			{
				log("Tried to add more than the max number of players\n"
					+ "\tPlayers: " + _players.size() + "\n"
					+ "\tMax players: " + Constants.MAX_PLAYERS, 2);
			}
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
			// TODO add eception to throw
			log("Tried to start game with 0 players", 3);
			return;
		}
		if(!_started)
		{
			_started = true;
		}
		else
		{
			log("start() was called but the game had already started", 2);
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
		log("start() has been called, starting unit placement phase", 1);
		// Send a welcome message, can be removed
		sendChatMessage("Welcome to Quadratum!");
	}
	
	/**
	 * Callback for notifying the GameCode that a player is ready.
	 * @param p the Player
	 */
	public synchronized void ready(Player p)
	{
		int player = getPlayerId(p);
		if(_turn != -1)
		{
			log("Player " + player + " called ready() but the game had already started", 2);
			return;
		}
		_playerInformation.get(player)._ready = true;
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(!_playerInformation.get(i)._ready && !_playerInformation.get(i)._quit)
			{
				log("Player " + player + " called ready()", 1);
				return;
			}
		}
		log("Player " + player + " called ready() and every other player was ready, starting game", 1);
		updateMaps(new Action(Action.ActionType.GAME_START, new MapPoint(-1, -1), new MapPoint(-1, -1)));
		nextTurn();
	}
	
	/**
	 * Callback for ending a turn.
	 * @param p the Player
	 */
	public void endTurn(Player p)
	{
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(player == _turn)
			{
				log("Player " + player + " called endTurn(), moving on to next turn", 1);
				nextTurn();
			}
			else
			{
				log("Player " + player + " called endTurn() but it was not their turn", 2);
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
	// TODO finish
	public boolean unitAction(Player p, int unitId, MapPoint coords)
	{
		// TODO logging in this function and then all functions above
		synchronized(_turnLockObject)
		{
			int player = getPlayerId(p);
			if(_turn != player)
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but it was not their turn\n"
					+ "\tTurn: " + _turn, 2);
				return false;
			}
			if(unitId < 0 || unitId >= _units.size())
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but they used an invalid unitId", 2);
				return false;
			}
			int owner = _units.get(unitId)._owner;
			if(owner != player)
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but they did not own the unit\n"
					+ "\tOwner: " + owner, 2);
				return false;
			}
			if(_unitInformation.get(unitId)._position.equals(new MapPoint(-1, -1)))
			{
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but the unit was dead", 2);
				return false;
			}
			coords = new MapPoint(coords);
			// TODO add real attacking
			HashSet<MapPoint> valid;
			MapPoint oldCoords = new MapPoint(_unitInformation.get(unitId)._position);
			int unit = getUnitAtPoint(coords);
			if(unit == -1) // Movement
			{
				if(_unitInformation.get(unitId)._hasMoved)
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but the unit had already moved", 2);
					return false;
				}
				valid = getAreaForUnit(unitId, 0);
				if(!valid.contains(coords))
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but did provide a valid movement coordinate\n"
						+ "\tValid: " + valid, 2);
					return false;
				}
				_unitInformation.get(unitId)._hasMoved = true;
				_unitInformation.get(unitId)._position = coords;
				updateMaps(new Action(Action.ActionType.MOVE, oldCoords, coords));
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ")\n"
					+ "\tAction taken: move\n"
					+ "\tFrom: " + oldCoords, 1);
			}
			else // Attacking
			{
				if(_unitInformation.get(unitId)._hasAttacked)
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but the unit had already attacked", 2);
					return false;
				}
				valid = getAreaForUnit(unitId, 1);
				if(!valid.contains(coords))
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but did provide a valid attack coordinate\n"
						+ "\tValid: " + valid, 2);
					return false;
				}
				if(_units.get(unit)._owner == player)
				{
					log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ") but tried to attack a unit that they owned\n"
						+ "\tUnit at coords: " + unit, 2);
					return false;
				}
				_unitInformation.get(unitId)._hasAttacked = true;
				_unitInformation.get(unit)._position = new MapPoint(-1, -1);
				updateMaps(new Action(Action.ActionType.UNIT_DIED, coords, coords));
				log("Player " + player + " called unitAction(unitId: " + unitId + ", coords: " + coords + ")\n"
					+ "\tAction taken: attack\n"
					+ "\tFrom: " + oldCoords, 1);
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
	// TODO finish
	// TODO add support for different types of terrain
	public HashMap<MapPoint, Action.ActionType> getValidActions(Player p, int unitId)
	{
		int player = getPlayerId(p);
		if(unitId < 0 || unitId >= _units.size() || _units.get(unitId)._owner != player || _turn != player)
		{
			String toLog;
			toLog = "Player " + player + " called getValidActions(unitId: " + unitId + ") but they did not own the unit, it was an invalid unit, or it was not their turn\n";
			if(unitId >= 0 && unitId < _units.size())
			{
				toLog += "\tOwner: " + _units.get(unitId)._owner + "\n";
			}
			else
			{
				toLog += "\tOwner: (none)\n";
			}
			toLog += "\tTurn: " + _turn + "\n";
			toLog += "\tAnswer: null";
			log(toLog, 2);
			return null;
		}
		if(_unitInformation.get(unitId)._position.equals(new MapPoint(-1, -1)))
		{
			log("Player " + player + " called getValidActions(unitId: " + unitId + ") but the unit was dead\n"
				+ "\tAnswer: null", 2);
			return null;
		}
		HashMap<MapPoint, Action.ActionType> actions = new HashMap<MapPoint, Action.ActionType>();
		if(_unitInformation.get(unitId)._hasMoved && _unitInformation.get(unitId)._hasAttacked)
		{
			log("Player " + player + " called getValidActions(unitId: " + unitId + ") but the unit had already attacked/moved\n"
				+ "\tAnswer: (empty map)", 1);
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
			log("Player " + player + " called getValidActions(unitId: " + unitId + ")\n"
				+ "\tAnswer: " + actions, 1);
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
		log("Looking for unit at point " + point, 1);
		for(int i = 0; i < _unitInformation.size(); i++)
		{
			if(_unitInformation.get(i)._position.equals(point))
			{
				log("\tUnit " + i + " found", 1);
				return i;
			}
		}
		log("\tNo unit found", 1);
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
		log("\tGenerated map for player " + player + ": " + units, 1);
		return units;
	}
	
	/**
	 * Generates new map information and sends it to all players.
	 * @param action the last Action that occured
	 */
	private void updateMaps(Action action)
	{
		log("Updating maps for all players", 1);
		for(int i = 0; i < _players.size(); i++)
		{
			if(!_playerInformation.get(i)._quit)
			{
				if(getVisible(i).contains(action._dest) || action._action == Action.ActionType.GAME_START)
				{
					// TODO create new _source for attacks that the player can't see
					// Though this may actually be a good thing that they can see where the attack comes from
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
			log("Player " + player + " has quit", 1);
			_playerInformation.get(player)._lost = true;
			_playerInformation.get(player)._quit = true;
			if(_turn == player)
			{
				log("\tMoving on to next turn", 1);
				nextTurn();
			}
			else
			{
				log("Player " + player + " has quit", 1);
				int winner = checkWinLoss();
				if(winner != -1)
				{
					log("Player " + winner + " has won the game, ending...", 1);
					endGame(winner);
					return;
				}
			}
		}
	}
	
	/**
	 * Checks if someone has won/lost
	 * @return the id of the player that has won, -1 if no one has won
	 */
	private int checkWinLoss()
	{
		log("Checking for winners/losers...", 1);
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
				log("Player " + i + " has lost", 1);
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
		log("No one has won", 1);
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
			log("Player " + winner + " has won the game, ending...", 1);
			endGame(winner);
			return;
		}
		for(int i = 0; i < _unitInformation.size(); i++)
		{
			_unitInformation.get(i)._hasMoved = false;
			_unitInformation.get(i)._hasAttacked = false;
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
			TurnStartThread turnStartThread = new TurnStartThread(_players.get(_turn));
			turnStartThread.start();
			log("Start of turn for player " + _turn, 1);
			return;
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
				_players.get(i).end(new GameStats(winner));
			}
		}
		log("GAME OVER\n"
			+ "Player " + winner + " won!", 1);
		// Return control to the main class.
		_main.returnControl();
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
			log("System chat message\n"
				+ "\tMessage: " + message, 1);
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
				log("Chat message from player " + from + "\n"
					+ "\tMessage: " + message, 1);
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
	// TODO finish
	public int placeUnit(Player p, MapPoint coords, String name)
	{
		synchronized(_turnLockObject)
		{
			coords = new MapPoint(coords);
			int player = getPlayerId(p);
			if(_turn != -1)
			{
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ") but the game had started\n"
					+ "\tTurn: " + _turn + "\n"
					+ "\tAnswer: -1", 2);
				return -1;
			}
			if(getRemainingUnits(p) == 0)
			{
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ") but did not have any units remaining to be placed\n"
					+ "\tAnswer: false", 2);
				return -1;
			}
			if(getUnitAtPoint(coords) == -1 && _startingLocations.get(player).contains(coords))
			{
				_units.add(new Unit(new String(name), player, _units.size()));
				_unitInformation.add(new UnitInformation(coords));
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ")\n"
					+ "\tAnswer: " + (_units.size() - 1), 1);
				return _units.size() - 1;
				// TODO add "brain block"
			}
			else
			{
				log("Player " + player + " called placeUnit(coords: " + coords + ", name: " + name + ") but tried to place a unit in an invalid location\n"
					+ "\tUnit at point: " + getUnitAtPoint(coords) + "\n"
					+ "\tAnswer: -1", 2);
				return -1;
			}
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
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but it was not their turn\n"
					+ "\tTurn was: " + _turn + "\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			if(pieceId < 0 || pieceId >= _pieces.size() || unitId < 0 || unitId >= _units.size())
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but there was an invalid piece or unit id\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			Piece piece = _pieces.get(pieceId);
			Unit unit = _units.get(unitId);
			if(unit._owner != player)
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but they did not own the unit\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			int resources = _playerInformation.get(player)._resources;
			if(piece._cost > resources)
			{
				log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but they have enough resources\n"
					+ "\tResources: " + resources + "\n"
					+ "\tAnswer: false", 2);
				return false;
			}
			for(MapPoint key : piece._blocks.keySet())
			{
				if(unit._blocks.containsKey(new MapPoint(coords._x + key._x, coords._y + key._y)))
				{
					log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but there was a collision\n"
						+ "\tCollision location on the unit: " + (new MapPoint(coords._x + key._x, coords._y + key._y)) + "\n"
						+ "\tAnswer: false", 2);
					return false;
				}
				if((coords._x + key._x) < 0 || (coords._x + key._x) >= Constants.UNIT_SIZE && (coords._y + key._y) < 0 || (coords._y + key._y) >= Constants.UNIT_SIZE)
				{
					log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ") but piece went out of bounds\n"
						+ "\tLocation out of bounds: " + (new MapPoint(coords._x + key._x, coords._y + key._y)) + "\n"
						+ "\tAnswer: false", 2);
				}
			}
			for(MapPoint key : piece._blocks.keySet())
			{
				unit._blocks.put(new MapPoint(coords._x + key._x, coords._y + key._y), new Block(piece._blocks.get(key)));
			}
			log("Player " + player + " called updateUnit(unitId: " + unitId + ", pieceId: " + pieceId + ", coords: " + coords + ")\n"
				+ "\tAnswer: true", 1);
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
		String name;
		if(player == -1)
		{
			name = "System Message";
		}
		else
		{
			name = new String(_playerInformation.get(player)._name);
		}
		log("Player " + player + " called getPlayerName(player: " + player + ")\n"
			+ "\tAnswer: " + name, 1);
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
		log("Player " + getPlayerId(p) + " called getResources()\n"
			+ "\tAnswer: " + resources, 1);
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
			log("Player " + getPlayerId(p) + " called getUnit(unitId: " + unitId + ") on a unit that did not exist", 2);
			return null;
		}
		Unit unit = new Unit(_units.get(unitId));
		if(!getVisible(getPlayerId(p)).contains(_unitInformation.get(unitId)._position))
		{
			log("Player " + getPlayerId(p) + " called getUnit(unitId: " + unitId + ") on a unit that did belong to them", 2);
			return null;
		}
		log("Player " + getPlayerId(p) + " called getUnit(unitId: " + unitId + ")\n"
			+ "\tAnswer: (success)", 1);
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
		/*log("getVisible(player: " + player + ")\n"
			+ "\tAnswer was: " + visible, 1);*/
			// TODO add back in when we are generating less data
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
			radius = 100; // TODO change when not testing
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
		log("getAreaForUnit(u: " + u + ", type: " + type + ")\n"
			+ "\tAnswer was: " + visible, 1);
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
		log("Player " + player + " called getRemainingUnits()\n"
			+ "\tAnswer was: " + remaining, 1);
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