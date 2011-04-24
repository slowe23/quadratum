package net.quadratum.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;

class GameCore implements Core
{
	private int[][] _terrain;
	private ArrayList<ArrayList<Point>> _startingLocations;
	private ArrayList<Unit> _units;
	private ArrayList<Player> _players;
	private ArrayList<PlayerInformation> _playerInformation;
	private ArrayList<Piece> _pieces;
	private WinCondition _winCondition;
	private int _turn;
	private boolean _started, _allReady;
	
	/**
	 * Constructor for GameCore.
	 * @param map the map file name
	 * @param winCondition the win condition
	 */
	public void GameCore(String map, WinCondition winCondition)
	{
		_units = new ArrayList<Unit>();
		_players = new ArrayList<Player>();
		_playerInformation = new ArrayList<PlayerInformation>();
		_pieces = new ArrayList<Piece>();
		_winCondition = winCondition;
		_turn = 0;
		_started = false;
		_allReady = false;
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
	public void addPlayer(Player player, String playerName)
	{
		if(_started == false)
		{
			int id = getRandom();
			_players.add(player);
			_playerInformation.add(new PlayerInformation(id, playerName));
		}
	}
	
	/**
	 * Gets a player by their secret id.
	 * @param id the player's secret id
	 * @return the instance of that player
	 */
	private Player getPlayer(int id)
	{
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(_playerInformation.get(i)._id == id)
			{
				return _players.get(i);
			}
		}
		return null;
	}
	
	/**
	 * Gets a player's id by their secret id.
	 * @param id the player's secret id
	 * @return the id of that player
	 */
	private int getPlayerId(int id)
	{
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			return i;
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
		HashSet<Point> startingLocations;
		ArrayList<Point> tempLocations;
		for(int i = 0; i < _players.size(); i++)
		{
			// Copy data so the player can't modify it
			
			// TO DO copy terrain
			startingLocations = new HashSet<Point>();
			tempLocations = _startingLocations.get(i);
			for(int j = 0; j < tempLocations.size(); j++)
			{
				startingLocations.add(new Point(tempLocations.get(i).getX(), tempLocations.get(i).getY()));
			}
			tempMap = new MapData(_terrain, startingLocations);
			// TODO copy pieces
			_players.get(i).start(this, _playerInformation.get(i)._id, tempMap, _players.size(), _pieces);
		}
	}
	
	/**
	 * Callback for notifying the GameCode that a player is ready.
	 * @param id the secret id
	 */
	public void ready(int id)
	{
		_playerInformation.get(getPlayerId(id))._ready = true;
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			if(!_playerInformation.get(i)._ready)
			{
				return;
			}
		}
		_players.get(0).turnStart();
	}
	
	/**
	 * Callback for ending a turn.
	 * @param id the secret id
	 */
	public void endTurn(int id)
	{
		if(getPlayerId(id) == _turn)
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
	public boolean unitAction(int id, int unitId, Point coords) { return true; }
	
	/**
	 * Calculates the valid actions for a given unit
	 * @param id the secret id
	 * @param unitId the unit's id
	 * @return a map of Points to Action.ActionTypes that represents what actions can be taken where
	 */
	public HashMap<Point, Action.ActionType> getValidActions(int id, int unitId) { return null; }
	
	/**
	 * Generates what the player can see.
	 * @param id the non-secret id
	 * @return a map of Points to unit ids
	 */
	private HashMap<Point, Integer> generateMapForPlayer(int id) { return null; }
	
	/**
	 * Generates new map information and sends it to all players.
	 */
	private void updateMaps() {}
	
	/**
	 * Callback for quitting a game.
	 * @param id the secret id
	 */
	public void quit(int id)
	{
		int player = getPlayerId(id);
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
		// TODO implement next turn and check for wins/losses and end game
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
	public void sendChatMessage(int id, String message)
	{
		int from = getPlayerId(id);
		for(int i = 0; i < _players.size(); i++)
		{
			_players.get(i).chatMessage(from, new String(message));
		}
	}
	
	/**
	 * Callback for placing a unit.
	 * @param id the secret id
	 * @param coords the Point at which the unit should be placed
	 * @return true if the unit is placed sucessfully, false otherwise
	 */
	public boolean placeUnit(int id, Point coords) { return true; } // Callback for placing a unit
	
	/**
	 * Callback for updating a unit.
	 * @param id the secret id
	 * @param unitId the id of the unit
	 * @param newPiece the piece to add to the unit
	 * @return true if the piece is added sucessfuly, false otherwise
	 */
	public boolean updateUnit(int id, int unitId, Piece newPiece) { return true; } // Callback for updating a unit
	
	/**
	 * Gets a player's name
	 * @param player the player's non-secret id
	 * @return the player's name
	 */
	public String getPlayerName(int player)
	{
		return new String(_playerInformation.get(player)._name);
	}
}