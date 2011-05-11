package net.quadratum.gamedata;

import java.util.*;

import net.quadratum.core.*;
import net.quadratum.ai.*;

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
	
	private class Level2AI extends LevelAI {
		
		public Level2AI() {
			super();
		}
		
		public void createUnits(int id) {
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
			
			path = new LinkedList<MapPoint>(path);
			placeMover(new MapPoint(3, 0), path);
		}
		
		private void placeTurret(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Turret");
			if(unit!=-1) {
				registerUnit(unit, new TurretBehavior());
				
				_core.updateUnit(this, unit, 4, new MapPoint(6, 0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(6, 6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(0, 3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(0, 5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(3, 0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(2, 5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(5, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2), Piece.ROTATE_NONE);
			}
		}
		
		private void placeChaser(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Chaser");
			if(unit!=-1) {
				registerUnit(unit, new ChaseBehavior(true));
				
				_core.updateUnit(this, unit, 0, new MapPoint(5, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(1, 5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(5, 0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(4, 6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(1, 2), Piece.ROTATE_NONE);
			}
		}
		
		private void placePatroller(MapPoint location, Queue<MapPoint> patrol) {
			int unit = _core.placeUnit(this, location, "Patroller");
			if(unit!=-1) {
				registerUnit(unit, new PathBehavior(patrol, true, true));
				
				_core.updateUnit(this, unit, 0, new MapPoint(5, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(1, 5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(5, 0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(4, 6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(1, 2), Piece.ROTATE_NONE);
			}
		}
		
		private void placeMover(MapPoint location, Queue<MapPoint> path) {
			int unit = _core.placeUnit(this, location, "Pursuer");
			if(unit!=-1) {
				registerUnit(unit, new PathBehavior(path, false, false));
				
				_core.updateUnit(this, unit, 0, new MapPoint(5, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(1, 5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2, 4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(5, 0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(6, 2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(4, 6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(1, 2), Piece.ROTATE_NONE);
			}
		}
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