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
	
	public boolean equals(Object obj)
	{
		return obj instanceof ObserverContainer && ((ObserverContainer) obj)._p == _p;
	}
	
	public int hashCode()
	{
		return _p.hashCode();
	}
}