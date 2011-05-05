package net.quadratum.gui;

import java.util.*;

import net.quadratum.core.*;

public class UnitsData {
	private Core _core;
	private GUIPlayer _player;
	
	private final Map<MapPoint, Unit> _pointMap;
	private final Map<Integer, MapPoint> _idMap;
	
	private Integer _selectedID;
	
	public UnitsData(GUIPlayer player) {
		_player = player;
		
		_pointMap = new HashMap<MapPoint, Unit>();
		_idMap = new HashMap<Integer, MapPoint>();
	}
	
	public void start(Core core) {
		_core = core;
	}
	
	public void setUnits(Map<MapPoint, Integer> units) {
		clearUnits();
		
		for(Map.Entry<MapPoint, Integer> entry : units.entrySet())
			addUnit(entry.getKey(), entry.getValue(), false);
		
		if(_selectedID!=null && !(_idMap.containsKey(_selectedID)))  //If the selected unit no longer exists, deselect
			_selectedID = null;
	}
	
	private void clearUnits() {
		_pointMap.clear();
		_idMap.clear();
	}
	
	public void addUnit(MapPoint mapPoint, Integer id, boolean select) {
		Unit u = _core.getUnit(_player, id);
		if(u!=null) {  //To avoid a certain potential synchronization problem
			_pointMap.put(mapPoint, u);
			_idMap.put(id, mapPoint);
			if(select)
				_selectedID = id;
		} else
			if(select)
				_selectedID = null;
	}
	
	public Integer getSelectedID() {
		return _selectedID;
	}
	
	public Unit getSelectedUnit() {
		if(_selectedID==null)
			return null;
		
		MapPoint sMP = _idMap.get(_selectedID);
		if(sMP==null)
			return null;
		
		return _pointMap.get(sMP);
	}
	
	public MapPoint getSelectedLocation() {
		if(_selectedID!=null)
			return _idMap.get(_selectedID);
		else
			return null;
	}
	
	public MapPoint getMapPoint(Unit u) {
		if(u==null)
			return null;
		else
			return getMapPoint(u._id);
	}
	
	public MapPoint getMapPoint(int id) {
		return _idMap.get(id);
	}
	
	public Unit getUnit(MapPoint p) {
		if(p==null)
			return null;
		else
			return _pointMap.get(p);  //May be null as well
	}
	
	//Returns the unit with the given ID, or null if no such unit exists
	private Unit getUnit(int id) {
		if(_idMap.containsKey(id))
			return _pointMap.get(_idMap.get(id));
		else
			return null;
	}
	
	public void setSelected(int id) {
		_selectedID = id;
	}
	
	public void setSelected(Unit u) {
		if(u==null)
			deselect();
		else
			setSelected(u._id);
	}
	
	public void deselect() {
		_selectedID = null;
	}
	
	//Returns a map of actions of the selected unit, or null if something fails
	public Map<MapPoint, Action.ActionType> getSelectedActions() {
		if(_selectedID==null)
			return null;
		else {
			Unit selectedUnit = getUnit(_selectedID);
			if(selectedUnit!=null && selectedUnit._owner==_player.getID())
				return _core.getValidActions(_player, _selectedID);
			else
				return null;
		}
	}
	
	//Gets a list of the player's units, sorted in ascending order of unit ID
	public List<Unit> getPlayerUnits() {
		List<Unit> playerUnits = new ArrayList<Unit>();
		for(int id : _idMap.keySet()) {
			Unit unit = getUnit(id);
			if(unit._owner == _player.getID())
				playerUnits.add(unit);
		}

		Collections.sort(playerUnits, new Comparator<Unit>() { public int compare(Unit u1, Unit u2) { return u1._id - u2._id; }});
		
		return playerUnits;
	}
	
	//Gets a map of all the units, mapped to their locations
	public Map<MapPoint, Unit> getAllUnits() {
		Map<MapPoint, Unit> units = new HashMap<MapPoint, Unit>();
		for(Map.Entry<MapPoint, Unit> entry : _pointMap.entrySet())
			units.put(entry.getKey(), entry.getValue());
		return units;
		
	}
}