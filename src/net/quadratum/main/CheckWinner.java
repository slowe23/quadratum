package net.quadratum.main;

import net.quadratum.core.*;
import java.util.Map;

public class CheckWinner implements WinCondition
{
	public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation)
	{
		return false;
	}
    public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation)
	{
		return false;
	}
}