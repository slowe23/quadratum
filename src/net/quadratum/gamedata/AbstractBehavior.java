package net.quadratum.gamedata;

import java.util.*;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

public abstract class AbstractBehavior implements UnitBehavior {
	protected static boolean isBad(MapPoint location, Map<MapPoint, ActionType> availableActions, Map<MapPoint, Unit> units) {
		return location==null || availableActions==null || units==null;
	}
	protected static int taxicabDistance(MapPoint p1, MapPoint p2) {
		return Math.abs(p1._x-p2._x)+Math.abs(p1._y-p2._y);
	}
}