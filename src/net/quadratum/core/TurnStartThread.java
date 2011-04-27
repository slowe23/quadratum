package net.quadratum.core;

import java.lang.Thread;

public class TurnStartThread extends Thread
{
	Player _player;
		
	/**
	 * Constructor for TurnStartThread.
	 * @param player the player
	 */
	public TurnStartThread(Player player)
	{
		_player = player;
	}
	
	/**
	 * Runs the thread.
	 */
	public void run()
	{
		_player.turnStart();
	}
}