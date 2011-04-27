package net.quadratum.core;


public class PlayerStartThread extends Thread
{
	Player _player;
	GameCore _gameCore;
	MapData _mapData;
	int _playerId, _players;
	
	/**
	 * Constructor for PlayerStartThread.
	 * @param player the player
	 * @param gameCore the game core
	 * @param mapData the map data
	 * @param playerId the player's id
	 * @param players the number of players
	 */
	public PlayerStartThread(Player player, GameCore gameCore, MapData mapData, int playerId, int players)
	{
		_player = player;
		_gameCore = gameCore;
		_mapData = mapData;
		_playerId = playerId;
		_players = players;
	}
	
	/**
	 * Runs the thread.
	 */
	public void run()
	{
		_player.start(_gameCore, _mapData, _playerId, _players);
	}
}