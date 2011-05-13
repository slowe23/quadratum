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
 * You can also specify a distance past which the unit will not chase.
 */
public class ChaseBehavior extends AbstractBehavior {
	private boolean _wander;
	private int _maxDistance;
	
	public ChaseBehavior(boolean wander) {
		this(wander, Integer.MAX_VALUE);
	}
	
	public ChaseBehavior(boolean wander, int distance) {
		_wander = wander;
		_maxDistance = distance;
	}
	
	public MapPoint behave(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		if(isBad(location, availableActions, units))
			return null;
		
		MapPoint closestEnemy = findClosestEnemy(location, units);
		
		if(closestEnemy==null) {
			if(_wander)
				return randomAction(availableActions.keySet());
			else
				return null;
		}
		
		Set<MapPoint> moves = new HashSet<MapPoint>(), attacks = new HashSet<MapPoint>();
		filter(availableActions, moves, attacks);
		
		MapPoint advance;
		if (taxicabDistance(location, closestEnemy) <= _maxDistance) {
			advance = moveTowards(location, closestEnemy, moves);
		} else {
			advance = null;
		}
		
		if(advance==null)
			return (attacks.contains(closestEnemy) ? closestEnemy : randomAction(attacks));
		else
			return advance;
	}
}