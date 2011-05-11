package net.quadratum.ai;

import java.util.*;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

public interface UnitBehavior {
	/**
	 * Returns the unit's desired action given the specified circumstances
	 * May return null to signify that the unit should wait
	 */
	public MapPoint behave(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units);
}