package net.quadratum.gui;

import java.util.List;
import java.util.Map;
import java.util.Random;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Unit;

public class Center {
	private Core _core;
	private GUIPlayer _player;
	
	private ChatHandler _chatHandler;
	
	private UnitsInfo _unitsInfo;
	private MapData _mapData;
	
	private MapPanel _mapPanel;
	private UnitInfoPanel _unitInfoPanel;
	private UnitImagePanel _unitImagePanel;
	private UnitPanel _unitPanel;
	private PiecesPanel _piecesPanel;
	
	public Center(GUIPlayer player, ChatHandler chatHandler, UnitsInfo unitsInfo, MapData mapData) {
		_player = player;
		_chatHandler = chatHandler;
		_unitsInfo = unitsInfo;
		_mapData = mapData;
	}
	
	public void setComponents(MapPanel mapPanel, UnitInfoPanel unitInfoPanel, UnitImagePanel unitImagePanel, UnitPanel unitPanel, PiecesPanel piecesPanel) {
		_mapPanel = mapPanel;
		_unitInfoPanel = unitInfoPanel;
		_unitImagePanel = unitImagePanel;
		_unitPanel = unitPanel;
		_piecesPanel = piecesPanel;
	}
	
	public void start(Core core, MapData mapData) {
		_core = core;
		_mapData._terrain = mapData._terrain;
		_mapData._placementArea = mapData._placementArea;
		
		_unitsInfo.start(_core);
		_mapPanel.start();
		_unitInfoPanel.start(_core);
		_chatHandler.start(_core);
	}
	
	public void updateMapData(MapData mapData) {
		_mapData._terrain = mapData._terrain;
		_mapData._placementArea = mapData._placementArea;
		mapUpdated();
	}
	
	public void setPieces(List<Piece> pieces) {
		try {
			_piecesPanel.setPieces(pieces);
		} catch(NullPointerException n) {
			System.out.println("This is a bug that needs fixing:");
			n.printStackTrace();
		}
	}
	
	public void update(Map<MapPoint, Integer> units, Action lastAction) {
		_unitsInfo.setUnits(units);
		unitsUpdated();
		_mapPanel.scrollTo(lastAction);
	}
	
	//Respond to a user's click on the given tile
	public void click(MapPoint point) {
		if(_unitsInfo.hasUnit(point)) {
			select(_unitsInfo.getUnit(point));
		} else {
			if(_mapData._placementArea!=null && _mapData._placementArea.contains(point)) {
				String newUnitName = "Unit "+(new Random().nextInt(Integer.MAX_VALUE));
				int newUnit;
				if((newUnit = _core.placeUnit(_player, point, newUnitName))!=-1) {
					_unitsInfo.addUnit(point, newUnit);
					_mapData._placementArea.remove(point);
					unitsUpdated();
					mapUpdated();
					if(_mapData._placementArea.size()==0 || _core.getRemainingUnits(_player)==0)
						placementDone();
				}
			} else if(_unitsInfo.getAvailableActions().containsKey(point)) {
				_core.unitAction(_player, _unitsInfo.getSelectedID(), point);
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