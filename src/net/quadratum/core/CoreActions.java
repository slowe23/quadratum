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
	 * @param units a list of Units
	 * @param unitInformation a list of UnitInformation
	 * @param terrain the terrain
	 * @return a map of MapPoints to Action.ActionTypes that represents what actions can be taken where
	 */
	public static Map<MapPoint, Action.ActionType> getValidActions(int unit, int player, List<Unit> units, List<UnitInformation> unitInformation, int[][] terrain)
	{
		// Get the total sight area
		Set<MapPoint> totalSight = getTotalSightArea(player, units, unitInformation, terrain);
		
		Map<MapPoint, Action.ActionType> actions = new HashMap<MapPoint, Action.ActionType>();
		if(unitInformation.get(unit)._hasMoved && unitInformation.get(unit)._hasAttacked)
		{
			return actions;
		}
		else
		{
			if(!unitInformation.get(unit)._hasMoved)
			{
				for(MapPoint point : getAreaForUnit(unit, 0, units, unitInformation, terrain, totalSight))
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
				for(MapPoint point : getAreaForUnit(unit, 1, units, unitInformation, terrain, totalSight))
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
	 * @param sight all points this player can see (does not matter if finding sight)
	 * @return the MapPoints that the unit can act upon/see
	 */
	public static Set<MapPoint> getAreaForUnit(int unit, int type, List<Unit> units,
			List<UnitInformation> unitInformation, int[][] terrain, Set<MapPoint> sight)
	{
		int radius;
		UnitInformation info = unitInformation.get(unit);
		HashSet<MapPoint> area = new HashSet<MapPoint>();
		if(type == 0) // Movement area
		{
			Unit actionUnit = units.get(unit);
			radius = Constants.INITIAL_MOVE + actionUnit._stats.get(Block.BonusType.MOVEMENT) / Constants.MOVEMENT_MODIFIER;
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
			Queue<PointAndNum> queue = new LinkedList<PointAndNum>();
			PointAndNum temp;
			queue.add(new PointAndNum(new MapPoint(info._position._x + 1, info._position._y), radius));
			queue.add(new PointAndNum(new MapPoint(info._position._x - 1, info._position._y), radius));
			queue.add(new PointAndNum(new MapPoint(info._position._x, info._position._y + 1), radius));
			queue.add(new PointAndNum(new MapPoint(info._position._x, info._position._y - 1), radius));
			while(queue.size() > 0)
			{
				temp = queue.remove();
				if(!alreadyChecked.contains(temp._point))
				{
					alreadyChecked.add(temp._point);
					// Make sure the point is in the movement radius, in the map, and contains no unit
					if(temp._num > 0
							&& temp._point._x >= 0 && temp._point._y >= 0 && temp._point._x < terrain.length && temp._point._y < terrain[0].length
							&& getUnitAtPoint(temp._point, unitInformation) == -1 && sight.contains(temp._point)
							&& !TerrainConstants.isOfType(terrain[temp._point._x][temp._point._y], TerrainConstants.IMPASSABLE))
					{
						// Make sure the unit can only move over water if it has a water movement block
						if(TerrainConstants.isOfType(terrain[temp._point._x][temp._point._y], TerrainConstants.WATER))
						{
							if(canMoveOnWater)
							{
								area.add(new MapPoint(temp._point));
								queue.add(new PointAndNum(new MapPoint(temp._point._x + 1, temp._point._y), temp._num - 1));
								queue.add(new PointAndNum(new MapPoint(temp._point._x - 1, temp._point._y), temp._num - 1));
								queue.add(new PointAndNum(new MapPoint(temp._point._x, temp._point._y + 1), temp._num - 1));
								queue.add(new PointAndNum(new MapPoint(temp._point._x, temp._point._y - 1), temp._num - 1));
							}
						}
						else
						{
							area.add(new MapPoint(temp._point));
							queue.add(new PointAndNum(new MapPoint(temp._point._x + 1, temp._point._y), temp._num - 1));
							queue.add(new PointAndNum(new MapPoint(temp._point._x - 1, temp._point._y), temp._num - 1));
							queue.add(new PointAndNum(new MapPoint(temp._point._x, temp._point._y + 1), temp._num - 1));
							queue.add(new PointAndNum(new MapPoint(temp._point._x, temp._point._y - 1), temp._num - 1));
						}
					}
				}
			}
		}
		else
		{
			if(type == 1) // Attack area
			{
				radius = units.get(unit)._stats.get(Block.BonusType.RANGE) / Constants.ATTACK_RANGE_MODIFIER;
				if(TerrainConstants.isOfType(terrain[info._position._x][info._position._y], TerrainConstants.MOUNTAIN))
				{
					radius += Constants.MOUNTAIN_RANGE_BONUS;
				}
			}
			else // Visible area
			{
				radius = Constants.INITIAL_SIGHT + units.get(unit)._stats.get(Block.BonusType.SIGHT) / Constants.SIGHT_MODIFIER;
				;
			}
			for(int x = info._position._x - radius; x < (info._position._x + radius + 1); x++)
			{
				for(int y = info._position._y - radius; y < (info._position._y + radius + 1); y++)
				{
					// Check to make sure the point is on the board and in the radius
					if(x >= 0 && y >= 0 && x < terrain.length && y < terrain[0].length
							&& (Math.abs(info._position._x - x) + Math.abs(info._position._y - y)) <= radius)
					{
						MapPoint point = new MapPoint(x,y);
						if ((type == 1 && sight.contains(point)) || type != 1) {
							area.add(point);
						}
					}
				}
			}
		}
		return area;
	}
	
	/**
	 * Gets the total sight area for a player.
	 * @param player the player ID
	 * @param units the list of units
	 * @param unitInformation the list of unit information
	 * @param terrain the terrain
	 * @return a set containing all points this player can see
	 */
	public static Set<MapPoint> getTotalSightArea(int player, List<Unit> units, List<UnitInformation> unitInformation, int[][] terrain) {
		Set<MapPoint> totalSight = new HashSet<MapPoint>();
		for (int i = 0; i < units.size(); i++) {
			if (units.get(i)._owner == player && !unitInformation.get(i)._position.equals(new MapPoint(-1, -1))) {
				totalSight.addAll(getAreaForUnit(i,2,units,unitInformation,terrain,totalSight));
			}
		}
		return totalSight;
	}
	
	private static class PointAndNum
	{
		MapPoint _point;
		int _num;
		public PointAndNum(MapPoint point, int num)
		{
			_point = point;
			_num = num;
		}
	}
}