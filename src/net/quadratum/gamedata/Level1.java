package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level1 implements Level
{
	public Level1() {}

	public String getMap()
	{
		return new String("maps/level1.qmap");
	}

	public int getStartingResources()
	{
		return 300;
	}

	public int getMaxUnits()
	{
		return 5;
	}

	public Player getAI()
	{
		return new Level1AI();
	}

	public WinCondition getWinCondition()
	{
		return new Level1WinCondition();
	}

	public ArrayList<Piece> getPieces()
	{
		return DefaultPieces.getPieces();
	}

	class Level1AI implements Player
	{
		Core _core;
		Map<MapPoint, Integer> _units;

		public Level1AI() {}

		public void start(Core core, MapData mapData, int id, int totalPlayers)
		{
			_core = core;
			_units = new HashMap<MapPoint, Integer>();
			_core.placeUnit(this, new MapPoint(19, 4), new String("King"));
			_core.placeUnit(this, new MapPoint(19, 5), new String("Queen"));
			_core.placeUnit(this, new MapPoint(16, 3), new String("Royal Guard"));
			_core.placeUnit(this, new MapPoint(16, 6), new String("Royal Guard"));
			_core.placeUnit(this, new MapPoint(10, 1), new String("Defender"));
			_core.placeUnit(this, new MapPoint(10, 8), new String("Defender"));
			_core.placeUnit(this, new MapPoint(12, 3), new String("Defender"));
			_core.placeUnit(this, new MapPoint(12, 6), new String("Defender"));
			_core.placeUnit(this, new MapPoint(13, 3), new String("Defender"));
			_core.placeUnit(this, new MapPoint(13, 6), new String("Defender"));
			_core.ready(this);
		}

		public void updatePieces(List<Piece> pieces) {}

		public void end(GameStats stats) {}

		public void lost() {}

		public void turnStart()
		{
			_core.endTurn(this);
		}

		public void updateMapData(MapData mapData) {}

		public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {}

		public void chatMessage(int from, String message) {}

		public void updateTurn(int id) {}
	}

	class Level1WinCondition implements WinCondition
	{
		public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
		{
			return false;
		}
		
		public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
		{
			if(playerNumber == 1)
			{
				if(!units.containsKey(new MapPoint(19, 4)) && !units.containsKey(new MapPoint(19, 5)))
				{
					return true;
				}
			}
			if(units.size() == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		public String getObjectives()
		{
			return new String("Kill the enemy king and queen!");
		}
	}
}