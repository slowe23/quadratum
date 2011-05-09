package net.quadratum.gamedata;

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
			if(_wander && availableActions.size()>0) {
				int r = new Random().nextInt(availableActions.size());
				for(MapPoint m : availableActions.keySet())
					if(r--==0)
						return m;
			}
			
			return null;
		}
		
		MapPoint bestMove = null;
		MapPoint attack = (availableActions.containsKey(closestEnemy) ? closestEnemy : null);
		for(Entry<MapPoint, ActionType> entry : availableActions.entrySet()) {
			ActionType a = entry.getValue();
			MapPoint p = entry.getKey();
			if(a==ActionType.MOVE) {
				int dist = taxicabDistance(p, closestEnemy);
				if(dist<closestDist) {
					bestMove = p;
					closestDist = dist;
				}
			} else if(a==ActionType.ATTACK) {
				if(attack==null)
					attack = p;
			}
		}
		
		if(bestMove==null)
			return attack;
		else
			return bestMove;
	}
}