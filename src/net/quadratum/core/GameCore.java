package net.quadratum.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class GameCore implements Core
{
	private int[][] _terrain;
	private List<List<Point>> _startingLocations;
	private List<Unit> _units;
	private List<UnitInformation> _unitInformation;
	private List<Player> _players;
	private List<PlayerInformation> _playerInformation;
	private List<Pieces> _pieces;
	private WinCondition _winCondition;
	private int turn;
	
	/**
	 * Constructor
	 * @param map the map file name
	 * @param winCondition the win condition
	 */
	void GameCore(String map, WinCondition winCondition)
	{
		_units = new ArrayList<Unit>();
		_unitInformation = new ArrayList<UnitInformation>();
		_players = new ArrayList<Player>();
		_playerInformation = new ArrayList<PlayerInformation>();
		_pieces = new ArrayList<Piece>();
		_windCondition = winCondition;
		_turns = 
		
	}
	/**
	 * Reads in the map
	 * @param map the map file name
	 */
	private void readMap(String map)
	{
		// Read in the map and starting locations
	}
	/**
	 * Gets a random, unsued id
	 */
	private int getRandom()
	{
		Random r = new Random();
		int random = r.nextInt();
		boolean found = false;
		int l = _playerInformation.length();
		while(true)
		{
			for(int i = 0; i < l; i++)
			{
				if(_playerInformation.get(i).getID() == random)
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
	public void addPlayer(Player player, String playerName)
	{
		int id = getRandom();
		_players.add(player);
		_playerInformation.add(new PlayerInformation(id, playerName));
	}
	private Player getPlayer(int id)
	{
		int l = _playerInformation.length();
		for(int i = 0; i < l; i++)
		{
			if(_playerInformation.get(i).getId() == id)
			{
				return _players.get(i);
			}
		}
		return null;
	}
	void start()
	{
		int l = _players.length();
		MapData tempMap;
		List<Point> startingLocations, tempLocations;
		for(int i = 0; i < l; i++)
		{
			// Copy data so the player can't modify it
			startingLocations = new ArrayList<Point>();
			tempLocations = _startingLocations.get(i);
			for(int j = 0; j < tempLocations.length(); j++)
			{
				startingLocations.add(new Point(tempLocations.get(i).getX(), tempLocations.get(i).getY()));
			}
			tempMap = new MapData(terrain, startingLocations);
			// Need to copy pieces
			_players.get(i).start(_playerInformation.get(i).getId(), tempMap, l, pieces);
		}
	}
	void ready(int id); // Callback so players can let the game core know that they are ready to start the game (i.e. all of their units have been placed)
	void endTurn(int id); // Callback so players can let the game core know that their turn has ended
	boolean unitAction(int id, int unitId, Point coords); // Callback for unit actions (returns false for invalid actions) - if coords is an empty square, the unit moves, if coords contains a unit, the unit attacks
	Map<Point, ActionEnum> getValidActions(int id, int unitId); // Gets the valid actions for a unit, returns null if no possible actions
	void quit(int id); // Notifies the core that a player has quit - if the player is the main player, it notifies the main thread to end the game and to display itself again
	void sendChatMessage(int id, String message); // Callback for sending a chat message
	boolean placeUnit(int id, Point coords); // Callback for placing a unit
	boolean updateUnit(int id, int unitId, Piece newPiece); // Callback for updating a unit
	String getPlayerName(int player)
	{
		return new String(_playerInformation.get(player).getName());
	}
}