package net.quadratum.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class CoreActions
{
	/**
	 * Calculates the valid actions for a given unit
	 * @param unitId the unit's id
	 * @param unitInformation a list of UnitInformation
	 * @param terrain the terrain
	 * @return a map of MapPoints to Action.ActionTypes that represents what actions can be taken where
	 */
	public static Map<MapPoint, Action.ActionType> getValidActions(int unit, int player, List<Unit> units, List<UnitInformation> unitInformation, int[][] terrain)
	{
		HashMap<MapPoint, Action.ActionType> actions = new HashMap<MapPoint, Action.ActionType>();
		if(unitInformation.get(unit)._hasMoved && unitInformation.get(unit)._hasAttacked)
		{
			return actions;
		}
		else
		{
			if(!unitInformation.get(unit)._hasMoved)
			{
				for(MapPoint point : getAreaForUnit(unit, 0, units, unitInformation, terrain))
				{
					if(getUnitAtPoint(point, unitInformation) == -1)
					{
						actions.put(new MapPoint(point), Action.ActionType.MOVE);
					}
				}
			}
			if(!unitInformation.get(unit)._hasAttacked)
			{
				int otherUnit;
				for(MapPoint point : getAreaForUnit(unit, 1, units, unitInformation, terrain))
				{
					otherUnit = getUnitAtPoint(point, unitInformation);
					if(otherUnit != -1 && units.get(otherUnit)._owner != player)
					{
						actions.put(new MapPoint(point), Action.ActionType.ATTACK);
					}
				}
			}
			return actions;
		}
	}
	
	/**
	 * Gets a unit at a specific point.
	 * @param point the MapPoint to check
	 * @param unitInformation a list of UnitInformation
	 * @return the id of the unit, -1 if no unit exists
	 */
	public static int getUnitAtPoint(MapPoint point, List<UnitInformation> unitInformation)
	{
		for(int i = 0; i < unitInformation.size(); i++)
		{
			if(unitInformation.get(i)._position.equals(point))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Gets the action/visible area for a single unit.
	 * @param unit the unit
	 * @param type the type of area to get: 0 = movement, 1 = attack, 2 = sight
	 * @param units a list of Units
	 * @param unitInformation a list of UnitInformation
	 * @param terrain the terrain
	 * @return the MapPoints that the unit can act upon/see
	 */
	public static Set<MapPoint> getAreaForUnit(int unit, int type, List<Unit> units, List<UnitInformation> unitInformation, int[][] terrain)
	{
		int radius;
		UnitInformation info = unitInformation.get(unit);
		HashSet<MapPoint> area = new HashSet<MapPoint>();
		int sightRadius = Constants.INITIAL_SIGHT + units.get(unit)._stats.get(Block.BonusType.SIGHT) / 120;
		if(type == 0) // Movement area
		{
			Unit actionUnit = units.get(unit);
			radius = Constants.INITIAL_MOVE + actionUnit._stats.get(Block.BonusType.MOVEMENT) / 120;
			if(radius > sightRadius)
			{
				radius = sightRadius;
			}
			boolean canMoveOnWater;
			if(actionUnit._stats.get(Block.BonusType.WATER_MOVEMENT).intValue() > 0)
			{
				canMoveOnWater = true;
			}
			else
			{
				canMoveOnWater = false;
			}
			HashSet<MapPoint> alreadyChecked = new HashSet<MapPoint>();
			Queue<MapPoint> queue = new LinkedList<MapPoint>();
			MapPoint temp;
			queue.add(new MapPoint(info._position._x + 1, info._position._y));
			queue.add(new MapPoint(info._position._x - 1, info._position._y));
			queue.add(new MapPoint(info._position._x, info._position._y + 1));
			queue.add(new MapPoint(info._position._x, info._position._y - 1));
			while(queue.size() > 0)
			{
				temp = new MapPoint(queue.remove());
				if(!alreadyChecked.contains(temp))
				{
					alreadyChecked.add(temp);
					// Make sure the point is in the movement radius
					if((Math.abs(info._position._x - temp._x) + Math.abs(info._position._y - temp._y)) <= radius)
					{
						// Make sure the coordinate is in the map
						if(temp._x >= 0 && temp._y >= 0 && temp._x < terrain.length && temp._y < terrain[0].length)
						{
							// Make sure there's no unit at that point
							if(getUnitAtPoint(temp, unitInformation) == -1)
							{
								// Make sure the unit can only move over water if it has a water movement block
								if(TerrainConstants.isOfType(terrain[temp._x][temp._y], TerrainConstants.WATER))
								{
									if(canMoveOnWater)
									{
										area.add(temp);
										queue.add(new MapPoint(temp._x + 1, temp._y));
										queue.add(new MapPoint(temp._x - 1, temp._y));
										queue.add(new MapPoint(temp._x, temp._y + 1));
										queue.add(new MapPoint(temp._x, temp._y - 1));
									}
								}
								else
								{
									area.add(temp);
									queue.add(new MapPoint(temp._x + 1, temp._y));
									queue.add(new MapPoint(temp._x - 1, temp._y));
									queue.add(new MapPoint(temp._x, temp._y + 1));
									queue.add(new MapPoint(temp._x, temp._y - 1));
								}
							}
						}
					}
				}
			}
		}
		else
		{
			if(type == 1) // Attack area
			{
				if(TerrainConstants.isOfType(terrain[info._position._x][info._position._y], TerrainConstants.MOUNTAIN))
				{
					radius = 3 + units.get(unit)._stats.get(Block.BonusType.RANGE) / 120;
				}
				else
				{
					radius = units.get(unit)._stats.get(Block.BonusType.RANGE) / 120;
				}
				if(radius > sightRadius)
				{
					radius = sightRadius;
				}
			}
			else // Visible area
			{
				radius = sightRadius;
			}
			for(int x = info._position._x - radius; x < (info._position._x + radius + 1); x++)
			{
				for(int y = info._position._y - radius; y < (info._position._y + radius + 1); y++)
				{
					if(x >= 0 && y >= 0 && x < terrain.length && y < terrain[0].length) // Check to make sure the point is on the board
					{
						if((Math.abs(info._position._x - x) + Math.abs(info._position._y - y)) <= radius)
						{
							area.add(new MapPoint(x, y));
						}
					}
				}
			}
		}
		return area;
	}
}