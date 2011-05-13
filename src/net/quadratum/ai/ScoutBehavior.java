package net.quadratum.ai;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;
import java.util.*;

public class ScoutBehavior extends AbstractBehavior {
	
	public ScoutBehavior() {
	}
	
	public MapPoint behave(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		Set<MapPoint> moves = new HashSet<MapPoint>(), attacks = new HashSet<MapPoint>();
		filter(availableActions, moves, attacks);
		
		MapPoint pt = randomAction(moves);
		if(pt!=null)
			return pt;
		else
			return randomAction(attacks);
	}
}