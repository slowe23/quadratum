package net.quadratum.core;

// Used to call run() without blocking
public class TurnStartThread extends Thread
{
	/** Player whose turn this thread needs to start. */
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