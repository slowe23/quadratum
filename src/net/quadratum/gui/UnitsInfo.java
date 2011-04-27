package net.quadratum.gui;

import net.quadratum.core.*;

import java.util.*;

public class UnitsInfo {
	private Core _core;
	private GUIPlayer _player;
	
	private Map<MapPoint, Integer> _pointMap;
	private Map<Integer, MapPoint> _idMap;
	private Map<Integer, Unit> _unitCache;
	
	private Integer _selectedID;
	
	public UnitsInfo(GUIPlayer player) {
		_player = player;
		
		_pointMap = new HashMap<MapPoint, Integer>();
		_idMap = new HashMap<Integer, MapPoint>();
		_unitCache = new HashMap<Integer, Unit>();
	}
	
	public void start(Core core) {
		_core = core;
	}
	
	public void setUnits(Map<MapPoint, Integer> units) {
		clearUnits();
		
		for(Map.Entry<MapPoint, Integer> entry : units.entrySet())
			addUnit(entry.getKey(), entry.getValue());
		
		if(_selectedID!=null && !(_idMap.containsKey(_selectedID))) {
			_selectedID = null;
		}
	}
	
	public void addUnit(MapPoint mapPoint, Integer id) {
		_pointMap.put(mapPoint, id);
		_idMap.put(id, mapPoint);
	}
	
	private void clearUnits() {
		_pointMap.clear();
		_idMap.clear();
		_unitCache.clear();
	}
	
	public int getSelectedID() {
		return _selectedID;
	}
	
	public Unit getSelected() {
		if(_selectedID==null)
			return null;
		else
			return getUnit(_selectedID);
	}
	
	public boolean isSelected(Unit u) {
		return _selectedID!=null && u._id == _selectedID;
	}
	
	public boolean hasUnit(MapPoint m) {
		return _pointMap.containsKey(m);
	}
	
	public int getID(MapPoint m) {
		return _pointMap.get(m);  //Will throw a NullPointerException if there is no unit at that location
	}
	
	public MapPoint getMapPoint(Unit u) {
		return getMapPoint(u._id);
	}
	
	public MapPoint getMapPoint(int id) {
		return _idMap.get(id);
	}
	
	public Unit getUnit(MapPoint m) {
		if(_pointMap.containsKey(m))
			return getUnit(_pointMap.get(m));
		else
			return null;
	}
	
	public Unit getUnit(int id) {
		if(!(_unitCache.containsKey(id)))
			_unitCache.put(id, _core.getUnit(_player, id));
		
		return _unitCache.get(id);
	}
	
	public void setSelected(int id) {
		_selectedID = id;
	}
	
	public void setSelected(Unit u) {
		if(u==null)
			_selectedID = null;
		else
			setSelected(u._id);
	}
	
	public void setSelected() {
		_selectedID = null;
	}
	
	public Map<MapPoint, Action.ActionType> getAvailableActions() {
		if(_selectedID==null)
			return new HashMap<MapPoint, Action.ActionType>();  //No actions available
		else
			return _core.getValidActions(_player, _selectedID);
	}
	
	//Gets a set of units belonging to the given player, ordered by unit ID
	public Set<Unit> getPlayerUnits(int playerID) {
		Set<Unit> playerUnits = new TreeSet<Unit>(new Comparator<Unit>() { public int compare(Unit u1, Unit u2) { return u1._id - u2._id; }});
		for(int id : _idMap.keySet()) {
			Unit unit = getUnit(id);
			if(unit._owner == playerID)
				playerUnits.add(unit);
		}
		return playerUnits;
	}
	
	public Set<MapPoint> getPoints() {
		return _pointMap.keySet();
	}
	
	//May not be needed, but just in case
	public Map<MapPoint, Unit> getUnits() {
		Map<MapPoint, Unit> units = new HashMap<MapPoint, Unit>();
		for(Map.Entry<MapPoint, Integer> entry : _pointMap.entrySet())
			units.put(entry.getKey(), getUnit(entry.getValue()));
		return units;
	}
}