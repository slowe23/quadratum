package net.quadratum.gamedata;

import java.util.*;
import java.util.Map.Entry;

import net.quadratum.core.*;
import net.quadratum.core.Action.ActionType;

public class Level2 implements Level {
	public Level2() {}
	
	public String getMap() {
		return new String("maps/level2.qmap");
	}
	
	public int getStartingResources() {
		return 2000;
	}
	
	public int getMaxUnits() {
		return 5;
	}
	
	public Player getAI() {
		return new Level2AI();
	}
	
	public WinCondition getWinCondition() {
		return new Level2WinCondition();
	}
	
	public ArrayList<Piece>[] getPieces() {
		ArrayList<Piece> dPieces = DefaultPieces.getPieces();
		
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
		
		pieces[0] = new ArrayList<Piece>(dPieces);
		pieces[0].remove(5);  //No water movement for the human player
		
		pieces[1] = new ArrayList<Piece>(dPieces);
		
		return pieces;
	}
	
	private class Level2AI implements Player {
		private Core _core;
		private Map<Integer, UnitBehavior> _behaviors;
		private Map<MapPoint, Unit> _units;
		private boolean _unitsChanged;
		private final Object _unitsLock;
		
		public Level2AI() {
			_unitsLock = new Object();
		}
		
		public void start(Core core, MapData mapData, int id, int totalPlayers) {
			_core = core;
			
			_behaviors = new HashMap<Integer, UnitBehavior>();
			
			placeTurret(new MapPoint(4, 3));
			placeTurret(new MapPoint(5, 2));
			placeTurret(new MapPoint(10, 7));
			
			Queue<MapPoint> patrol = new LinkedList<MapPoint>();
			patrol.add(new MapPoint(2, 6));
			patrol.add(new MapPoint(5,9)); 
			placePatroller(new MapPoint(4, 7), patrol);
			
			placeChaser(new MapPoint(9, 4));
			
			Queue<MapPoint> path = new LinkedList<MapPoint>();
			path.add(new MapPoint(13, 1));
			path.add(new MapPoint(0, 11));
			placeMover(new MapPoint(0, 0), path);
			
			_core.ready(this);
		}
		
		private void placeTurret(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Turret");
			if(unit!=-1) {
				_behaviors.put(unit, new TurretBehavior());
				
				_core.updateUnit(this, unit, 4, new MapPoint(6, 0));
				_core.updateUnit(this, unit, 4, new MapPoint(6, 6));
				_core.updateUnit(this, unit, 0, new MapPoint(0, 3));
				_core.updateUnit(this, unit, 0, new MapPoint(0, 5));
				_core.updateUnit(this, unit, 2, new MapPoint(3, 0));
				_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
				_core.updateUnit(this, unit, 3, new MapPoint(5, 4));
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2));
			}
		}
		
		private void placeChaser(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Chaser");
			if(unit!=-1) {
				_behaviors.put(unit, new ChaseBehavior(true));
				
				_core.updateUnit(this, unit, 0, new MapPoint(5, 4));
				_core.updateUnit(this, unit, 1, new MapPoint(1, 5));
				_core.updateUnit(this, unit, 1, new MapPoint(2, 4));
				_core.updateUnit(this, unit, 3, new MapPoint(5, 0));
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2));
				_core.updateUnit(this, unit, 4, new MapPoint(4, 6));
				_core.updateUnit(this, unit, 6, new MapPoint(1, 2));
			}
		}
		
		private void placePatroller(MapPoint location, Queue<MapPoint> patrol) {
			int unit = _core.placeUnit(this, location, "Patroller");
			if(unit!=-1) {
				_behaviors.put(unit, new PathBehavior(patrol, true, true));
				
				_core.updateUnit(this, unit, 0, new MapPoint(5, 4));
				_core.updateUnit(this, unit, 1, new MapPoint(1, 5));
				_core.updateUnit(this, unit, 1, new MapPoint(2, 4));
				_core.updateUnit(this, unit, 3, new MapPoint(5, 0));
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2));
				_core.updateUnit(this, unit, 4, new MapPoint(4, 6));
				_core.updateUnit(this, unit, 6, new MapPoint(1, 2));
			}
		}
		
		private void placeMover(MapPoint location, Queue<MapPoint> path) {
			int unit = _core.placeUnit(this, location, "Pursuer");
			if(unit!=-1) {
				_behaviors.put(unit, new PathBehavior(path, false, false));
				
				_core.updateUnit(this, unit, 0, new MapPoint(5, 4));
				_core.updateUnit(this, unit, 1, new MapPoint(1, 5));
				_core.updateUnit(this, unit, 1, new MapPoint(2, 4));
				_core.updateUnit(this, unit, 3, new MapPoint(5, 0));
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2));
				_core.updateUnit(this, unit, 4, new MapPoint(4, 6));
				_core.updateUnit(this, unit, 6, new MapPoint(1, 2));
			}
		}
		
		public void updatePieces(List<Piece> pieces) {}
		
		public void end(GameStats stats) {}
		
		public void lost() {}
		
		public void turnStart() {
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
		
		public void updateMapData(MapData mapData) { }
		
		public void updateMap(Map<MapPoint,Integer> units, Set<MapPoint> sight, Action lastAction) {
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
		
		public void chatMessage(int from, String message) { }
		
		public void updateTurn(int id) { }
	}
	
	private class Level2WinCondition implements WinCondition {
		private static final int RESOURCES = 2000;
		
		public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber) {
			return playerNumber==0 && playerInformation._resources >RESOURCES;
		}
		
		public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber) {
			return units.size() == 0;
		}
		
		public String getObjectives() {
			return "Accumulate more than "+RESOURCES+" resources.";
		}
	}
}