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
		Object _lockObject;

		public Level1AI()
		{
			_lockObject = new Object();
			_units = new HashMap<MapPoint, Integer>();
		}

		public void start(Core core, MapData mapData, int id, int totalPlayers)
		{
			_core = core;
			int unit;

			// Place kings
			unit = _core.placeUnit(this, new MapPoint(19, 4), new String("King"));
			for(int i = 0; i < 7; i += 2)
			{
				for(int j = 0; j < 7; j += 2)
				{
					_core.updateUnit(this, unit, 4, new MapPoint(i, j));
				}
			}

			// Place queen
			unit = _core.placeUnit(this, new MapPoint(19, 5), new String("Queen"));
			for(int i = 0; i < 7; i += 2)
			{
				for(int j = 0; j < 7; j += 2)
				{
					_core.updateUnit(this, unit, 4, new MapPoint(i, j));
				}
			}

			// Add royal guard
			unit = _core.placeUnit(this, new MapPoint(16, 3), new String("Royal Guard"));
			for(int i = 0; i < 7; i += 2)
			{
				_core.updateUnit(this, unit, 4, new MapPoint(0, i));
			}
			_core.updateUnit(this, unit, 2, new MapPoint(2, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 0, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 5));
			
			// Add royal guard
			unit = _core.placeUnit(this, new MapPoint(16, 6), new String("Royal Guard"));
			for(int i = 0; i < 7; i += 2)
			{
				_core.updateUnit(this, unit, 4, new MapPoint(0, i));
			}
			_core.updateUnit(this, unit, 2, new MapPoint(2, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 0, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 5));
			
			// Place defender
			unit = _core.placeUnit(this, new MapPoint(10, 1), new String("Defender"));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
			_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
			_core.updateUnit(this, unit, 3, new MapPoint(6, 2));

			// Place defender
			unit = _core.placeUnit(this, new MapPoint(10, 8), new String("Defender"));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
			_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
			_core.updateUnit(this, unit, 3, new MapPoint(6, 2));

			// Place defender
			unit = _core.placeUnit(this, new MapPoint(12, 3), new String("Defender"));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
			_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
			_core.updateUnit(this, unit, 3, new MapPoint(6, 2));

			// Place defender
			unit = _core.placeUnit(this, new MapPoint(12, 6), new String("Defender"));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
			_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
			_core.updateUnit(this, unit, 3, new MapPoint(6, 2));

			// Place defender
			unit = _core.placeUnit(this, new MapPoint(13, 3), new String("Defender"));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
			_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
			_core.updateUnit(this, unit, 3, new MapPoint(6, 2));

			// Place defender
			unit = _core.placeUnit(this, new MapPoint(13, 6), new String("Defender"));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
			_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
			_core.updateUnit(this, unit, 3, new MapPoint(6, 2));

			_core.ready(this);
		}

		public void updatePieces(List<Piece> pieces) {}

		public void end(GameStats stats) {}

		public void lost() {}

		public void turnStart()
		{
			synchronized(_units)
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

		public void updateMap(Map<MapPoint, Integer> units, Action lastAction)
		{
			synchronized(_units)
			{
				_units.clear();
				for(MapPoint key : units.keySet())
				{
					if(_core.getUnit(this, units.get(key).intValue())._owner == 1)
					{
						_units.put(key, units.get(key));
					}
				}
			}
		}

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