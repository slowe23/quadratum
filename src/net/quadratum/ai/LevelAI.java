package net.quadratum.ai;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;

/**
 * A class that defines a simple, general level AI
 *
 * Units are controlled with "behaviors" which are set in the createUnits method
 */
public abstract class LevelAI implements Player {
	protected Core _core;
	
	private Map<Integer, UnitBehavior> _behaviors;

	private Map<MapPoint, Unit> _units;
	private boolean _unitsChanged;
	private final Object _unitsLock;
	
	public LevelAI() {
		_unitsLock = new Object();
	}
	
	public final void start(Core core, MapData mapData, int id, int otherPlayers) {
		_core = core;
		
		_behaviors = new HashMap<Integer, UnitBehavior>();
		
		createUnits(id);
		
		_core.ready(this);
	}
	
	protected final void registerUnit(int unitID, UnitBehavior behavior) {
		_behaviors.put(unitID, behavior);
	}
	
	public abstract void createUnits(int id);
	
	public final void updatePieces(List<Piece> pieces) {}
	
	public final void end(GameStats stats) {}
	
	public final void lost() {}
	
	public final void turnStart() {
		boolean refresh;
		
		Map<MapPoint, Unit> units;
		do {
			synchronized(_unitsLock) {
				units = _units;
				refresh = _unitsChanged = false;;
			}
			for(Iterator<Entry<MapPoint, Unit>> iterator = units.entrySet().iterator(); !refresh && iterator.hasNext(); ) {
				Entry<MapPoint, Unit> entry = iterator.next();
				int unit = entry.getValue()._id;
				UnitBehavior unitBehavior = _behaviors.get(unit);
				if(unitBehavior!=null) {
					MapPoint act = unitBehavior.behave(entry.getKey(), _core.getValidActions(this, unit), units);
					if(act!=null)
						_core.unitAction(this, unit, act);
				}
				synchronized(_unitsLock) {
					refresh = _unitsChanged;
				}
			}
		} while(refresh);
		
		_core.endTurn(this);
	}
	
	public final void updateMapData(MapData mapData) { }
	
	public final void updateMap(Map<MapPoint,Integer> units, Set<MapPoint> sight, Action lastAction) {
		synchronized(_unitsLock) {
			_units = new HashMap<MapPoint, Unit>();
			for(Entry<MapPoint, Integer> entry : units.entrySet()) {
				Unit u = _core.getUnit(this, entry.getValue());
				if(u!=null)
					_units.put(entry.getKey(), u);
			}
			_unitsChanged = true;
		}
	}
	
	public final void chatMessage(int from, String message) { }
	
	public final void updateTurn(int id) { }
}