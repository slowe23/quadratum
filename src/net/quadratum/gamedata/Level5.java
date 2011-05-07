package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class Level5 implements Level
{
	public Level5() {}

	public String getMap()
	{
		return new String("maps/level5.qmap");
	}

	public int getStartingResources()
	{
		return 1500;
	}

	public int getMaxUnits()
	{
		return 5;
	}

	public Player getAI()
	{
		return new Level5AI();
	}

	public WinCondition getWinCondition()
	{
		return new Level5WinCondition();
	}

	public ArrayList<Piece>[] getPieces()
	{
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
		pieces[0] = DefaultPieces.getPieces();
		pieces[1] = DefaultPieces.getPieces();
		return pieces;
	}
	
	class Level5AI implements Player
	{
		Core _core;
		Map<MapPoint, Integer> _units;
		Object _lockObject;

		public Level5AI()
		{
			_lockObject = new Object();
		}

		public void start(Core core, MapData mapData, int id, int totalPlayers)
		{
			_core = core;
			_core.ready(this);
		}

		public void updatePieces(List<Piece> pieces) {}

		public void end(GameStats stats) {}

		public void lost() {}

		public void turnStart()
		{
			synchronized(_lockObject)
			{
				Map<MapPoint, Action.ActionType> valid;
				Set<MapPoint> keys = _units.keySet();
				for(MapPoint key : keys)
				{
					valid = _core.getValidActions(this, _units.get(key).intValue());
					if(valid == null)
					{
						continue;
					}
					for(MapPoint point : valid.keySet())
					{
						if(valid.get(point) == Action.ActionType.ATTACK)
						{
							_core.unitAction(this, _units.get(key), point);
							continue;
						}
					}
				}
				_core.endTurn(this);
			}
		}

		public void updateMapData(MapData mapData) {}

		public void updateMap(Map<MapPoint,Integer> units, Set<MapPoint> sight, Action lastAction)
		{
			synchronized(_lockObject)
			{
				_units = new HashMap<MapPoint, Integer>();
				for(MapPoint key : units.keySet())
				{
					if(_core.getUnit(this, units.get(key).intValue())._owner == 1)
					{
						_units.put(new MapPoint(key), new Integer(units.get(key)));
					}
				}
			}
		}

		public void chatMessage(int from, String message) {}

		public void updateTurn(int id) {}
	}

	class Level5WinCondition implements WinCondition
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
		
		public String getObjectives()
		{
			return new String("Kill all enemy units");
		}
	}
}