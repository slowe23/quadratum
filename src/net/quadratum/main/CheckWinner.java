package net.quadratum.main;

import java.util.Map;

import net.quadratum.core.MapPoint;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class CheckWinner implements WinCondition
{
	public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation)
	{
		return false;
	}
	
    public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation)
	{
		if(units.size() == 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}