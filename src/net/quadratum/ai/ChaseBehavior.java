package net.quadratum.ai;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

/**
 * A class that defines a "chasing" behavior for mobile guards
 *
 * The unit in question will target the closest enemy and attempt to move towards it.
 * If it cannot move any closer to its target, it will attempt to attack it
 * If it cannot attack its target, but can attack some other unit (e.g. for line-of-sight reasons) it will attack that unit
 * If there are no enemies in sight and wandering is enabled, the unit will make a random move
 */
public class ChaseBehavior extends AbstractBehavior {
	private boolean _wander;
	
	public ChaseBehavior(boolean wander) {
		_wander = wander;
	}
	
	public MapPoint behave(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		if(isBad(location, availableActions, units))
			return null;
		
		Unit me = units.get(location);
		int friends = me._owner;
		
		MapPoint closestEnemy = null;
		int closestDist = 0;
		
		for(Entry<MapPoint, Unit> entry : units.entrySet()) {
			MapPoint p = entry.getKey();
			Unit u = entry.getValue();
			if(u._owner!=friends) {
				int dist = taxicabDistance(p, location);
				if(closestEnemy==null || dist<closestDist) {
					closestEnemy = p;
					closestDist = dist;
				}
			}
		}
		
		if(closestEnemy==null) {
			if(_wander)
				return randomAction(availableActions.keySet());
			else
				return null;
		}
		
		Set<MapPoint> moves = new HashSet<MapPoint>(), attacks = new HashSet<MapPoint>();
		filter(availableActions, moves, attacks);
		
		MapPoint advance = moveTowards(location, closestEnemy, moves);
		
		if(advance==null)
			return (attacks.contains(closestEnemy) ? closestEnemy : randomAction(attacks));
		else
			return advance;
	}
}