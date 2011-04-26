package net.quadratum.gui;

import net.quadratum.core.Unit;
import net.quadratum.core.MapPoint;

import java.util.Map;

public class UnitHandler {
	private Map<MapPoint, Unit> _units;
	private Unit _selectedUnit;
	
	private UnitImagePanel _unitImagePanel;
	private MapPanel _mapPanel;
	
	public UnitHandler() { }
	
	public Map<MapPoint, Unit> getUnits() {
		return _units;
	}
	
	public Unit getSelectedUnit() {
		return _selectedUnit;
	}
	
	public void setUnits(Map<MapPoint, Unit> units) {
		_units = units;
	}
	
	public void setSelectedUnit(Unit unit) {
		_selectedUnit = unit;
	}
}