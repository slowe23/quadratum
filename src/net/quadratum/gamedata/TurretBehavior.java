package net.quadratum.gamedata;

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
		for(Entry<MapPoint, ActionType> entry : availableActions.entrySet())
			if(entry.getValue()==ActionType.ATTACK)
				return entry.getKey();

		return null;
	}
}