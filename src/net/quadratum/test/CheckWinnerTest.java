package net.quadratum.test;

import java.util.Map;

import net.quadratum.core.MapPoint;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class CheckWinnerTest implements WinCondition
{
	public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
	{
		return false;
	}
	
	public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
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
    
    public String toString()
    {
    	return "Default - eliminate opponents";
    }

	public String getObjectives()
	{
		return new String("Kill all enemy units!");
	}
}