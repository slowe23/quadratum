package net.quadratum.gui;

import java.util.*;

import net.quadratum.core.*;

/** A class for storing data about the units currently visible to the player */
public class UnitsData {
	private Core _core;
	private GUIPlayer _player;
	
	private final Map<MapPoint, Unit> _pointMap;
	private final Map<Integer, MapPoint> _idMap;
	
	private Set<MapPoint> _sight;
	
	private Integer _selectedID;
	
	public UnitsData(GUIPlayer player) {
		_player = player;
		
		_pointMap = new HashMap<MapPoint, Unit>();
		_idMap = new HashMap<Integer, MapPoint>();
	}
	
	/** Passes a reference to the core, for querying units */
	public void start(Core core) {
		_core = core;
	}
	
	/** Sets the units */
	public synchronized void setUnits(Map<MapPoint, Integer> units) {
		clearUnits();
		
		for(Map.Entry<MapPoint, Integer> entry : units.entrySet())
			addUnit(entry.getKey(), entry.getValue(), false);
		
		if(_selectedID!=null && !(_idMap.containsKey(_selectedID)))  //If the selected unit no longer exists, deselect
			_selectedID = null;
	}
	
	/** Sets the sight region */
	public synchronized void setSight(Set<MapPoint> sight) {
		_sight = sight;
	}
	
	/** Gets the sight region */
	public synchronized Set<MapPoint> getSight() {
		return _sight;
	}
	
	/** Clears the current unit data */
	private synchronized void clearUnits() {
		_pointMap.clear();
		_idMap.clear();
	}
	
	/**
	 * Adds a single unit with the given location and id
	 *
	 * @param mapPoint The location of the unit to add
	 * @param id The id of the unit to add
	 * @param select Whether to select the newly-added unit
	 */
	public synchronized void addUnit(MapPoint mapPoint, Integer id, boolean select) {
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
	
	/** Re-queries the core for information about the given unit */
	public synchronized void refreshUnit(int id) {
		MapPoint m = _idMap.get(id);
		if(m!=null) {
			Unit u =  _core.getUnit(_player, id);
			if(u!=null) {
				_pointMap.put(m, u);
				_idMap.put(id, m);
			}
		}
	}
	
	/** Gets the id of the selected unit (null if no unit is selected) */
	public synchronized Integer getSelectedID() {
		return _selectedID;
	}
	
	/** Gets the selected unit (null if none is selected) */
	public synchronized Unit getSelectedUnit() {
		if(_selectedID==null)
			return null;
		
		MapPoint sMP = _idMap.get(_selectedID);
		if(sMP==null)
			return null;
		
		return _pointMap.get(sMP);
	}
	
	/** Gets the locaton of the selected unit (null if no unit is selected) */
	public synchronized MapPoint getSelectedLocation() {
		if(_selectedID!=null)
			return _idMap.get(_selectedID);
		else
			return null;
	}
	
	/** Gets the location corresponding to the given unit */
	public synchronized MapPoint getMapPoint(Unit u) {
		if(u==null)
			return null;
		else
			return getMapPoint(u._id);
	}
	
	/** Gets the location corresponding to the unit with the given id */
	public synchronized MapPoint getMapPoint(int id) {
		return _idMap.get(id);
	}
	
	/** Gets the unit at the given location */
	public synchronized Unit getUnit(MapPoint p) {
		if(p==null)
			return null;
		else
			return _pointMap.get(p);  //May be null as well
	}
	
	/** Returns the unit with the given ID, or null if no such unit exists */
	private synchronized Unit getUnit(int id) {
		if(_idMap.containsKey(id))
			return _pointMap.get(_idMap.get(id));
		else
			return null;
	}
	
	/** Sets the selected unit to the unit with the given ID */
	public synchronized void setSelected(int id) {
		_selectedID = id;
	}
	
	/** Sets the selected unit to the given unit */
	public synchronized void setSelected(Unit u) {
		if(u==null)
			deselect();
		else
			setSelected(u._id);
	}
	
	/** Sets the selected unit to null */
	public synchronized void deselect() {
		_selectedID = null;
	}
	
	/** Returns a map of actions currently available to the selected unit, or null if something fails */
	public synchronized Map<MapPoint, Action.ActionType> getSelectedActions() {
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
	
	/** Gets a list of the player's units, sorted in ascending order of unit ID */
	public synchronized List<Unit> getPlayerUnits() {
		List<Unit> playerUnits = new ArrayList<Unit>();
		for(int id : _idMap.keySet()) {
			Unit unit = getUnit(id);
			if(unit._owner == _player.getID())
				playerUnits.add(unit);
		}

		Collections.sort(playerUnits, new Comparator<Unit>() { public int compare(Unit u1, Unit u2) { return u1._id - u2._id; }});
		
		return playerUnits;
	}
	
	/** Gets a map of all the units where each unit's location is a key with the unit as the corresponding value */
	public synchronized Map<MapPoint, Unit> getAllUnits() {
		Map<MapPoint, Unit> units = new HashMap<MapPoint, Unit>();
		for(Map.Entry<MapPoint, Unit> entry : _pointMap.entrySet())
			units.put(entry.getKey(), entry.getValue());
		return units;
		
	}
}