package net.quadratum.ai;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

/**
 * A class that defines a "path-following" behavior
 *
 */
public class PathBehavior extends AbstractBehavior {
	private Queue<MapPoint> _path;
	private boolean _loop;
	
	/** Whether the unit prioritizes attacking (aggressive) or path-following (not aggressive) */
	private boolean _aggressive;
	
	public PathBehavior(Queue<MapPoint> path, boolean loop, boolean aggressive) {
		_path = path;
		_loop = loop;
		_aggressive = aggressive;
	}
	
	public MapPoint behave(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		if(isBad(location, availableActions, units))
			return null;
		
		if(location.equals(_path.peek())) {  //Will be false if the queue is empty
			MapPoint rem = _path.remove();
			if(_loop)
				_path.add(rem);
		}
		
		
		Set<MapPoint> moves = new HashSet<MapPoint>(), attacks = new HashSet<MapPoint>();
		filter(availableActions, moves, attacks);
		
		MapPoint advance = null;
		
		MapPoint destination = _path.peek();
		if(destination!=null)
			advance = moveTowards(location, destination, moves);
		
		MapPoint attack = randomAction(attacks);
		
		if(_aggressive) {
			if(attack!=null)
				return attack;
			else
				return advance;
		} else {
			if(advance!=null)
				return advance;
			else
				return attack;
		}
	}
}