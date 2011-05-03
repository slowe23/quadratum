package net.quadratum.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	// TODO finish
	// TODO add support for different types of terrain
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
				for(MapPoint point : getAreaForUnit(unit, 0, unitInformation, terrain))
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
				for(MapPoint point : getAreaForUnit(unit, 1, unitInformation, terrain))
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
	 * @param unitInformation a list of UnitInformation
	 * @param terrain the terrain
	 * @return the MapPoints that the unit can act upon/see
	 */
	// TODO add support for sight blocks and special movement blocks/terrain
	public static Set<MapPoint> getAreaForUnit(int unit, int type, List<UnitInformation> unitInformation, int[][] terrain)
	{
		int radius;
		if(type == 0) // Movement area
		{
			radius = 3;
		}
		else if(type == 1) // Attack area
		{
			radius = 2;
		}
		else // Visible area
		{
			radius = 60; // TODO change when not testing
		}
		UnitInformation info = unitInformation.get(unit);
		HashSet<MapPoint> area = new HashSet<MapPoint>();
		for(int x = info._position._x - radius; x < (info._position._x + radius + 1); x++)
		{
			for(int y = info._position._y - radius; y < (info._position._y + radius + 1); y++)
			{
				if(x >= 0 && y >= 0 && x < terrain.length && y < terrain[0].length) // Check to make sure the point is on the board
				{
					if((Math.abs(info._position._x - x) + Math.abs(info._position._y - y)) < radius)
					{
						area.add(new MapPoint(x, y));
					}
				}
			}
		}
		return area;
	}
}