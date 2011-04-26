package net.quadratum.gui;

import net.quadratum.core.Unit;
import net.quadratum.core.MapPoint;

import java.util.Map;

public interface UnitHandler {
	public Map<MapPoint, Unit> getUnits();
	public Unit getSelectedUnit();
}