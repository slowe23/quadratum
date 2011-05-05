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
		
		_center = new MapPoint(_terrain.length/2,_terrain[0].length/2);
		
		// Unit placement
		int rand, unitID;
		MapPoint placement;
		while (_core.getRemainingUnits(this) > 0) {
			rand = (int)(Math.random()*mapData._placementArea.size());
			placement = null;
			for (MapPoint point : mapData._placementArea) {
				if (rand == 0) {
					placement = point;
					break;
				}
				rand--;
			}
			// We didn't find anything... this should never happen.
			if (placement == null) {
				throw new IllegalStateException("Unable to find a placement point");
			}
			// place the unit... counts backwards?
			unitID = _core.placeUnit(this, placement, "Dude #"+_core.getRemainingUnits(this));
			
			if (unitID != -1) {
				_unitIDs.add(unitID);
			}
		}
		_core.ready(this);
		
		// So the AI will make fun of you
		new ChatThread().start();
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
			if (closest != null) {
				_core.unitAction(this,id,closest);
			}
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
	
	/** 
	 * Calculates the Manhattan distance between two points.
	 * @param mp1 the first point
	 * @param mp2 the second point
	 * @return the Manhattan distance, given by taking the sum of the differences
	 * of the respective x and y coordinates
	 */
	private int distance(MapPoint mp1, MapPoint mp2) {
		return Math.abs(mp1._x-mp2._x) + Math.abs(mp1._y-mp2._y);
	}
	
	class ChatThread extends Thread {
		
		final String[] INSULTS = {
			"LOL, you suck!",
			"What the hell was that?",
			"You call that a move?",
			"Is this the best you puny humans can do?!",
			"Stupid meatsack!"
		};
		
		public void run() {
			while (!_ended) {
				try {
					Thread.sleep((int)(Math.random()*20+5)*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				_core.sendChatMessage(TestAI_MTC.this,
					INSULTS[(int)(Math.random()*INSULTS.length)]);
			}
		}
	}

}
