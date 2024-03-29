package net.quadratum.main;

import java.util.Map;

import net.quadratum.core.MapPoint;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class CheckWinner implements WinCondition
{
	@Override
	public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
	{
		return false;
	}
	
	@Override
    public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
	{
		return units.size() == 0;
	}

	public String getObjectives()
	{
		return new String("Kill all enemy units!");
	}
}