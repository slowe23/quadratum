package net.quadratum.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

class GameCore implements Core
{
	private int[][] _terrain;
	private ArrayList<HashSet<MapPoint>> _startingLocations;
	private ArrayList<Unit> _units;
	private ArrayList<UnitInformation> _unitInformation;
	private ArrayList<Player> _players;
	private ArrayList<PlayerInformation> _playerInformation;
	private ArrayList<Piece> _pieces;
	private WinCondition _winCondition;
	private int _turn;
	private boolean _started;
	
	/**
	 * Constructor for GameCore.
	 * @param map the map file name
	 * @param winCondition the win condition
	 */
	public GameCore(String map, WinCondition winCondition)
	{
		_startingLocations = new ArrayList<HashSet<MapPoint>>();
		_units = new ArrayList<Unit>();
		_unitInformation = new ArrayList<UnitInformation>();
		_players = new ArrayList<Player>();
		_playerInformation = new ArrayList<PlayerInformation>();
		_pieces = new ArrayList<Piece>();
		_winCondition = winCondition;
		_turn = -1;
		_started = false;
	}
	
	/**
	 * Reads in the map.
	 * @param map the map file name
	 */
	private void readMap(String map)
	{
		// Read in the map and starting locations
	}
	
	/**
	 * Gets a random, unused id.
	 * @return returns the new id
	 */
	private int getRandom()
	{
		Random r = new Random();
		int random = r.nextInt();
		boolean found = false;
		while(true)
		{
			for(int i = 0; i < _playerInformation.size(); i++)
			{
				if(_playerInformation.get(i)._id == random)
				{
					found = true;
				}
			}
			if(found == false)
			{
				return random;
			}
			else
			{
				random++;
				found = false;
			}
		}
	}
	
	/**
	 * Adds a player to the game
	 * @param player the actual player
	 * @param playerName the name of the player
	 */
	public void addPlayer(Player player, String playerName, int maxUnits)
	{
		if(_started == false)
		{
			_players.add(player);
			_playerInformation.add(new PlayerInformation(playerName, maxUnits));
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
		if(_started == false)
		{
			_started = true;
		}
		else
		{
			return;
		}
		MapData tempMap;
		ArrayList<Piece> tempPieces;
		for(int i = 0; i < _players.size(); i++)
		{
			tempMap = new MapData(_terrain, _startingLocations.get(i));
			tempPieces = new ArrayList<Piece>();
			for(int j = 0; j < _pieces.size(); j++)
			{
				tempPieces.add(new Piece(_pieces.get(j)));
			}
			_players.get(i).start(this, tempMap, i, _players.size());
			_players.get(i).updatePieces(_pieces);
		}
	}
	
	/**
	 * Callback for notifying the GameCode that a player is ready.
	 * @param id the secret id
	 */
	public void ready(Player p)
	{
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
	 * @param id the secret id
	 */
	public void endTurn(Player p)
	{
		if(getPlayerId(p) == _turn)
		{
			nextTurn();
		}
	}
	
	/**
	 * Performs a unit's action.
	 * @param id the secret it
	 * @param unitId the unit's id
	 * @param coords the point at which the action should be taken
	 * @return true if the action has been taken, false if otherwise
	 */
	public boolean unitAction(Player p, int unitId, MapPoint coords) { return true; }
	
	/**
	 * Calculates the valid actions for a given unit
	 * @param id the secret id
	 * @param unitId the unit's id
	 * @return a map of MapPoints to Action.ActionTypes that represents what actions can be taken where
	 */
	public HashMap<MapPoint, Action.ActionType> getValidActions(Player p, int unitId) { return null; }
	
	/**
	 * Generates what the player can see.
	 * @param id the non-secret id
	 * @return a map of MapPoints to unit ids
	 */
	private HashMap<MapPoint, Integer> generateMapForPlayer(Player p) { return null; }
	
	/**
	 * Generates new map information and sends it to all players.
	 */
	private void updateMaps() {}
	
	/**
	 * Callback for quitting a game.
	 * @param id the secret id
	 */
	public void quit(Player p)
	{
		int player = getPlayerId(p);
		_playerInformation.get(player)._lost = true;
		_playerInformation.get(player)._quit = true;
		if(_turn == player)
		{
			nextTurn();
		}
	}
	
	/**
	 * Checks if someone has won/lost
	 */
	private void checkWinLoss() {}
	
	/**
	 * Moves onto the next turn and calculates wins
	 */
	private void nextTurn()
	{
		checkWinLoss();
		for(int i = 0; i < _players.size() - 1; i++)
		{
			_turn++;
			if(_turn == _players.size())
			{
				_turn = 0;
			}
			if(!_playerInformation.get(_turn)._quit && !_playerInformation.get(_turn)._lost)
			{
				return;
			}
			_players.get(_turn).turnStart();
		}
		// If we have reached here, the game should be over...
		endGame(-1);
	}
	
	/**
	 * Ends the game and sends stats.
	 * @param winner the winner of the game
	 */
	private void endGame(int winner) {}
	
	/**
	 * Sends a chat message to all players from the system
	 * @param message the message to send
	 */
	private void sendChatMessage(String message)
	{
		for(int i = 0; i < _players.size(); i++)
		{
			_players.get(i).chatMessage(-1, new String(message));
		}
	}
	
	/**
	 * Sends a chat message to all players
	 * @param id the secret id
	 * @param message the message to send
	 */
	public void sendChatMessage(Player p, String message)
	{
		int from = getPlayerId(p);
		for(int i = 0; i < _players.size(); i++)
		{
			_players.get(i).chatMessage(from, new String(message));
		}
	}
	
	/**
	 * Callback for placing a unit.
	 * @param id the secret id
	 * @param coords the MapPoint at which the unit should be placed
	 * @return true if the unit is placed sucessfully, false otherwise
	 */
	public boolean placeUnit(Player p, MapPoint coords) { return true; } // Callback for placing a unit
	
	/**
	 * Callback for updating a unit.
	 * @param id the secret id
	 * @param unitId the id of the unit
	 * @param pieceId the id of the piece to add
	 * @param coords the coordinates in the unit to place the piece
	 * @return true if the piece is added sucessfuly, false otherwise
	 */
	public boolean updateUnit(Player p, int unitId, int pieceId, MapPoint coords) { return true; } // Callback for updating a unit
	
	/**
	 * Gets a player's name
	 * @param player the player's id
	 * @return the player's name
	 */
	public String getPlayerName(int player)
	{
		return new String(_playerInformation.get(player)._name);
	}
	
	/**
	 * Get's a player's resources.
	 * @param id the secret id
	 */
	public int getResources(Player p)
	{
		return _playerInformation.get(getPlayerId(p))._resources;
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
			return null;
		}
		Unit unit = new Unit(_units.get(unitId));
		if(true) // TODO check to see if the unit is visible to the player
		{
			return null;
		}
		return unit;
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
		return _playerInformation.get(player)._maxUnits - built;
	}
}