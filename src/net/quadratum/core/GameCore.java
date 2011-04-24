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
	private int getPlayerID(int id)
	{
		for(int i = 0; i < _playerInformation.size(); i++)
		{
			return i;
		}
		return -1;
	}
	
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
			
			// TO DO: copy terrain
			startingLocations = new HashSet<Point>();
			tempLocations = _startingLocations.get(i);
			for(int j = 0; j < tempLocations.size(); j++)
			{
				startingLocations.add(new Point(tempLocations.get(i).getX(), tempLocations.get(i).getY()));
			}
			tempMap = new MapData(_terrain, startingLocations);
			// TO DO: copy pieces
			_players.get(i).start(this, _playerInformation.get(i)._id, tempMap, _players.size(), _pieces);
		}
	}
	
	public void ready(int id)
	{
		_playerInformation.get(getPlayerID(id))._ready = true;
		// TO DO: Check to see if everyone is ready, and, if so, start the game
	}
	public void endTurn(int id) {} // Callback so players can let the game core know that their turn has ended
	public boolean unitAction(int id, int unitId, Point coords) { return true; } // Callback for unit actions (returns false for invalid actions) - if coords is an empty square, the unit moves, if coords contains a unit, the unit attacks
	public HashMap<Point, Action.ActionType> getValidActions(int id, int unitId) { return null; } // Gets the valid actions for a unit, returns null if no possible actions
	public void quit(int id)
	{
		int player = getPlayerID(id);
		_playerInformation.get(player)._lost = true;
		if(_turn == player)
		{
			nextTurn();
		}
	}
	
	/**
	 * Moves onto the next turn and calculates wins
	 */
	private void nextTurn()
	{
		// TO DO: implement next turn and check for wins/losses and end game
	}
	
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
		int from = getPlayerID(id);
		for(int i = 0; i < _players.size(); i++)
		{
			_players.get(i).chatMessage(from, new String(message));
		}
	}
	public boolean placeUnit(int id, Point coords) { return true; } // Callback for placing a unit
	public boolean updateUnit(int id, int unitID, Piece newPiece) { return true; } // Callback for updating a unit
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