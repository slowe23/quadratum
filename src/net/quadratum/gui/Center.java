package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.*;

public class Center {
	private Core _core;
	private GUIPlayer _player;
	
	private UnitsInfo _unitsInfo;
	private MapData _mapData;
	
	private MapPanel _mapPanel;
	private UnitInfoPanel _unitInfoPanel;
	private UnitImagePanel _unitImagePanel;
	private UnitPanel _unitPanel;
	private BuildPanel _buildPanel;
	
	public Center(GUIPlayer player, UnitsInfo unitsInfo, MapData mapData) {
		_player = player;
		_unitsInfo = unitsInfo;
		_mapData = mapData;
	}
	
	public void setComponents(MapPanel mapPanel, UnitInfoPanel unitInfoPanel, UnitImagePanel unitImagePanel, UnitPanel unitPanel, BuildPanel buildPanel) {
		_mapPanel = mapPanel;
		_unitInfoPanel = unitInfoPanel;
		_unitImagePanel = unitImagePanel;
		_unitPanel = unitPanel;
		_buildPanel = buildPanel;
	}
	
	public void start(Core core, MapData mapData) {
		_core = core;
		_mapData._terrain = mapData._terrain;
		_mapData._placementArea = mapData._placementArea;
		
		_unitsInfo.start(_core);
		_mapPanel.start();
		_unitInfoPanel.start(_core);
	}
	
	public void updateMapData(MapData mapData) {
		_mapData._terrain = mapData._terrain;
		_mapData._placementArea = mapData._placementArea;
		mapUpdated();
	}
	
	//Respond to a user's click on the given tile
	public void click(MapPoint point) {
		if(_unitsInfo.hasUnit(point)) {
			select(_unitsInfo.getUnit(point));
		} else {
			if(_mapData._placementArea!=null && _mapData._placementArea.contains(point)) {
				String newUnitName = "Unit "+(new Random().nextInt(Integer.MAX_VALUE));
				if(_core.placeUnit(_player, point, newUnitName)) {
					_mapData._placementArea.remove(point);
					mapUpdated();
					//TODO: deal with the aftermath?
				}
			} else
				deselect();
		}
	}
	
	//Respond to a user's click on the map display but off the map
	public void clickOut() {
		deselect();
	}
	
	public void placementDone() {
		_mapData._placementArea = null;
		mapUpdated();
		
		_core.ready(_player);
	}
	
	public void closing() {
		_core.quit(_player);
	}
	
	public void select(Unit u) {
		_unitsInfo.setSelected(u);
		unitsUpdated();
	}
	
	private void deselect() {
		_unitsInfo.setSelected();
		unitsUpdated();
	}
	
	private void unitsUpdated() {
		_mapPanel.unitsUpdated();
		_unitInfoPanel.unitsUpdated();
		_unitImagePanel.unitsUpdated();
		_unitPanel.unitsUpdated();
	}
	
	private void mapUpdated() {
		_mapPanel.mapUpdated();
	}
}