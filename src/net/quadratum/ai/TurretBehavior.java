package net.quadratum.ai;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

/**
 * Defines behavior for turret-type units
 *
 * The unit attacks nearby units if possible, otherwise it does nothing
 */
public class TurretBehavior extends AbstractBehavior {
	public MapPoint behave(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		if(isBad(location, availableActions, units))
			return null;
		
		Set<MapPoint> moves = new HashSet<MapPoint>(), attacks = new HashSet<MapPoint>();
		filter(availableActions, moves, attacks);
		
		return randomAction(attacks);
	}
}