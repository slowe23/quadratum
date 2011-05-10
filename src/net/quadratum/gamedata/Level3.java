package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import net.quadratum.ai.ChaseBehavior;
import net.quadratum.ai.LevelAI;
import net.quadratum.ai.PathBehavior;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level3 implements Level {
	
	public Level3() {}

	public String getMap() {
		return new String("maps/level3.qmap");
	}
	
	public int getStartingResources() {
		return 1000;
	}
	
	public int getMaxUnits() {
		return 4;
	}
	
	public Player getAI() {
		return new Level3AI();
	}
	
	public WinCondition getWinCondition() {
		return new Level3WinCondition();
	}
	
	public ArrayList<Piece>[] getPieces() {
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
		pieces[0] = new ArrayList<Piece>(DefaultPieces.getPieces());
		pieces[0].remove(5); // No water movement
		pieces[1] = DefaultPieces.getPieces();
		return pieces;
	}
	
	class Level3AI extends LevelAI {
		
		public Level3AI() {
			super();
		}
		
		public void createUnits(int id) {
			// Scout path
			Queue<MapPoint> patrol = new LinkedList<MapPoint>();
			patrol.add(new MapPoint(9,9));
			patrol.add(new MapPoint(9,10));
			patrol.add(new MapPoint(10,10));
			patrol.add(new MapPoint(15,6));
			patrol.add(new MapPoint(19,10));
			patrol.add(new MapPoint(15,11));
			
			placeScout(new MapPoint(18,9),patrol);
			placeRanger(new MapPoint(18,10));
			placeSoldier(new MapPoint(19,10));
			placeTank(new MapPoint(19,9),new MapPoint(18,8));
		}
		
		// Places a scout
		private void placeScout(MapPoint location, Queue<MapPoint> patrol) {
			int unit = _core.placeUnit(this, location, new String("Scout"));
			if (unit != -1) {
				registerUnit(unit, new PathBehavior(patrol,true,true));
				_core.updateUnit(this, unit, 6, new MapPoint(1,0));
				_core.updateUnit(this, unit, 1, new MapPoint(5,0));
				_core.updateUnit(this, unit, 0, new MapPoint(6,0));
				_core.updateUnit(this, unit, 3, new MapPoint(0,1));
				_core.updateUnit(this, unit, 4, new MapPoint(3,1));
				_core.updateUnit(this, unit, 3, new MapPoint(1,3));
				_core.updateUnit(this, unit, 4, new MapPoint(5,3));
				_core.updateUnit(this, unit, 1, new MapPoint(0,5));
				_core.updateUnit(this, unit, 1, new MapPoint(2,5));
				_core.updateUnit(this, unit, 4, new MapPoint(3,5));
				_core.updateUnit(this, unit, 1, new MapPoint(5,5));
				_core.updateUnit(this, unit, 0, new MapPoint(6,5));
			}
		}
		
		// Places a ranger
		private void placeRanger(MapPoint location) {
			int unit = _core.placeUnit(this, location, new String("Ranger"));
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(true));
				_core.updateUnit(this, unit, 6, new MapPoint(0,0));
				_core.updateUnit(this, unit, 2, new MapPoint(5,0));
				_core.updateUnit(this, unit, 2, new MapPoint(2,1));
				_core.updateUnit(this, unit, 4, new MapPoint(6,2));
				_core.updateUnit(this, unit, 4, new MapPoint(0,3));
				_core.updateUnit(this, unit, 1, new MapPoint(2,3));
				_core.updateUnit(this, unit, 0, new MapPoint(5,3));
				_core.updateUnit(this, unit, 5, new MapPoint(7,4));
				_core.updateUnit(this, unit, 1, new MapPoint(0,5));
				_core.updateUnit(this, unit, 0, new MapPoint(1,5));
				_core.updateUnit(this, unit, 0, new MapPoint(5,5));
				_core.updateUnit(this, unit, 4, new MapPoint(3,6));
			}
		}
		
		// Places a soldier
		private void placeSoldier(MapPoint location) {
			int unit = _core.placeUnit(this, location, new String("Soldier"));
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(true));
				_core.updateUnit(this, unit, 0, new MapPoint(1,0));
				_core.updateUnit(this, unit, 2, new MapPoint(3,0));
				_core.updateUnit(this, unit, 4, new MapPoint(6,0));
				_core.updateUnit(this, unit, 0, new MapPoint(0,1));
				_core.updateUnit(this, unit, 5, new MapPoint(5,2));
				_core.updateUnit(this, unit, 4, new MapPoint(6,2));
				_core.updateUnit(this, unit, 0, new MapPoint(0,3));
				_core.updateUnit(this, unit, 1, new MapPoint(2,3));
				_core.updateUnit(this, unit, 4, new MapPoint(6,4));
				_core.updateUnit(this, unit, 0, new MapPoint(0,5));
				_core.updateUnit(this, unit, 4, new MapPoint(2,6));
				_core.updateUnit(this, unit, 6, new MapPoint(4,6));
			}
		}
		
		// Places a tank
		private void placeTank(MapPoint location, final MapPoint locationToStop) {
			int unit = _core.placeUnit(this, location, new String("Tank"));
			if (unit != -1) {
				registerUnit(unit, new PathBehavior(new LinkedList<MapPoint>() {
					{ add(locationToStop); }},false,true));
				_core.updateUnit(this, unit, 4, new MapPoint(0,0));
				_core.updateUnit(this, unit, 4, new MapPoint(2,0));
				_core.updateUnit(this, unit, 0, new MapPoint(4,0));
				_core.updateUnit(this, unit, 0, new MapPoint(6,0));
				_core.updateUnit(this, unit, 3, new MapPoint(1,2));
				_core.updateUnit(this, unit, 1, new MapPoint(5,3));
				_core.updateUnit(this, unit, 4, new MapPoint(6,3));
				_core.updateUnit(this, unit, 2, new MapPoint(0,4));
				_core.updateUnit(this, unit, 4, new MapPoint(0,6));
				_core.updateUnit(this, unit, 4, new MapPoint(2,6));
				_core.updateUnit(this, unit, 6, new MapPoint(4,6));
			}
		}
	}

	class Level3WinCondition implements WinCondition {
		public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
		{
			return false;
		}
		
		public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
		{
			return units.size() == 0;
		}
		
		public String getObjectives()
		{
			return new String("Kill the enemy units!");
		}
	}
}
