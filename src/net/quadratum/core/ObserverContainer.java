package net.quadratum.core;

// Holds an observer
public class ObserverContainer
{
	public Player _p;
	public boolean _quit;
	public ObserverContainer(Player player)
	{
		_p = player;
		_quit = false;
	}
}