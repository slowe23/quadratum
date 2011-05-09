package net.quadratum.core;

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