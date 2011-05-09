package net.quadratum.ai;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

public abstract class AbstractBehavior implements UnitBehavior {
	protected static boolean isBad(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		return location==null || availableActions==null || units==null;
	}
	
	protected static int taxicabDistance(MapPoint p1, MapPoint p2) {
		return Math.abs(p1._x-p2._x)+Math.abs(p1._y-p2._y);
	}
	
	protected static void filter(Map<MapPoint, ActionType> actions, Set<MapPoint> moves, Set<MapPoint> attacks) {
		for(Entry<MapPoint, ActionType> entry : actions.entrySet()) {
			ActionType a = entry.getValue();
			MapPoint p = entry.getKey();
			if(a==ActionType.MOVE)
				moves.add(p);
			else if(a==ActionType.ATTACK)
				attacks.add(p);
		}
	}
	
	protected static MapPoint moveTowards(MapPoint location, MapPoint destination, Set<MapPoint> moves) {
		Set<MapPoint> bestMoves = new HashSet<MapPoint>();
		int currDist = taxicabDistance(location, destination);
		int bestDist = currDist;
		for(MapPoint p : moves) {
			int dist = taxicabDistance(p, destination);
			if(dist<=bestDist) {
				if(dist<bestDist)
					bestMoves.clear();
				bestMoves.add(p);
				bestDist = dist;
			}
		}
		
		return randomAction(bestMoves, (bestDist==currDist));
	}
	
	protected static MapPoint randomAction(Set<MapPoint> actions) {
		return randomAction(actions, false);
	}
	
	protected static MapPoint randomAction(Set<MapPoint> actions, boolean includeDoNothing) {
		if(actions.size()>0) {
			int r = new Random().nextInt(actions.size()+(includeDoNothing?1:0));
			for(MapPoint m : actions)
				if(r--==0)
					return m;
		}
		
		return null;
	}
}