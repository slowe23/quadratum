package net.quadratum.ai.test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.ai.AIPlayer;
import net.quadratum.core.Action.ActionType;
import net.quadratum.core.Core;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;

public class TestAI_MTC extends AIPlayer {
	
	// This test AI moves its units to the center and attacks things whenever it can.
	
	/** The center of the map. */
	MapPoint _center;
	
	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		super.start(core, mapData, id, totalPlayers);
		Set<MapPoint> locs = mapData._placementArea;
		
		_center = new MapPoint(_terrain.length/2,_terrain[0].length/2);
		
		// Unit placement
		int rand, unitID;
		while (_core.getRemainingUnits(this) > 0) {
			rand = (int)(Math.random()*locs.size());
			MapPoint placement = null;
			for (Iterator<MapPoint> iter = locs.iterator(); iter.hasNext(); 
					placement = iter.next()) {
				if (rand == 0) {
					iter.remove();
					break;
				}
				rand--;
			}
			// place the unit... counts backwards?
			unitID = _core.placeUnit(this, placement, "Dude #"+_core.getRemainingUnits(this));
			
			if (unitID != -1) {
				_unitIDs.add(unitID);
			}
		}
		_core.ready(this);
	}

	@Override
	public void turnStart() {
		// Copy the list to avoid possible concurrent modification
		// when updateMap is called
		List<Integer> idsCopy = new LinkedList<Integer>(_unitIDs);
		for (int id : idsCopy) {
			// Give it a little time to think.
			try {
				Thread.sleep(TestAIConstants.THINK_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Get the valid actions.
			Map<MapPoint,ActionType> map = _core.getValidActions(this,id);
			if (map == null) {
				continue;
			}
			// Get all the possible move locations.
			Set<MapPoint> possibleActions = new HashSet<MapPoint>();
			for (MapPoint point : map.keySet()) {
				if (map.get(point) == ActionType.MOVE) {
					possibleActions.add(point);
				}
			}
			// Find the movement with the minimum distance to the center.
			int minDist = Integer.MAX_VALUE, dist;
			MapPoint closest = null;
			for (MapPoint point : possibleActions) {
				dist = distance(point,_center);
				if (dist < minDist) {
					closest = point;
					minDist = dist;
				}
			}
			// Move the unit.
			_core.unitAction(this,id,closest);
			// We need to refresh the valid actions.
			map = _core.getValidActions(this,id);
			// Try to attack something.
			possibleActions = new HashSet<MapPoint>();
			for (MapPoint point : map.keySet()) {
				if (map.get(point) == ActionType.ATTACK) {
					_core.unitAction(this,id,point);
					// After we have attacked, we can't do it again.
					break;
				}
			}
		}
		// Turn is done.
		_core.endTurn(this);
	}
	
	/** Calculates the Manhattan distance between two points. */
	private int distance(MapPoint mp1, MapPoint mp2) {
		return Math.abs(mp1._x-mp2._x) + Math.abs(mp1._y-mp2._y);
	}

}
