package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.Map;

public interface UnitHolder {
	public Map<MapPoint, Unit> getUnits();
	public Unit getSelectedUnit();
}