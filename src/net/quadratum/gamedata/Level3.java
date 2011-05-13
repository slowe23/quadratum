package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import net.quadratum.ai.ChaseBehavior;
import net.quadratum.ai.LevelAI;
import net.quadratum.ai.PathBehavior;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.TerrainConstants;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level3 implements Level {
	
	public Level3() {}

	public String getMap() {
		return new String("maps/level3.qmap");
	}
	
	public int getStartingResources() {
		return 1500;
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
			// Place rangers
			placeRanger(new MapPoint(18,10),false);
			placeRanger(new MapPoint(19,11),true);
			// Place soldier
			placeSoldier(new MapPoint(19,10));
			// Place tank
			placeTank(new MapPoint(19,9));
			placeTank(new MapPoint(20,9));
		}
		
		/**
		 * Places a scout-type unit.
		 * @param location the starting point of this scout
		 * @param patrol the patrol loop this scout should cover
		 */
		private void placeScout(MapPoint location, Queue<MapPoint> patrol) {
			int unit = _core.placeUnit(this, location, new String("Scout"));
			if (unit != -1) {
				registerUnit(unit, new PathBehavior(patrol,true,true));
				_core.updateUnit(this, unit, 6, new MapPoint(1,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(5,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(0,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(3,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(1,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(5,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(0,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(3,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(5,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,5), Piece.ROTATE_NONE);
			}
		}
		
		/**
		 * Places a ranger-type unit.
		 * @param location the starting point of this ranger
		 * @param sniper if true, the ranger will pick a random mountain, move
		 * towards it, and stay there
		 */
		private void placeRanger(MapPoint location, boolean sniper) {
			int unit = _core.placeUnit(this, location, new String("Ranger"));
			if (unit != -1) {
				if (sniper) {
					final MapPoint p = getRandomLocationOfType(TerrainConstants.MOUNTAIN);
					if (p != null) {
						registerUnit(unit, new PathBehavior(new LinkedList<MapPoint>() {
							{ add(p); }},false,true));
					} else {
						registerUnit(unit, new ChaseBehavior(true));
					}
				} else {
					registerUnit(unit, new ChaseBehavior(true));
				}
				_core.updateUnit(this, unit, 6, new MapPoint(0,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(5,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(2,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(6,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(0,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(5,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 5, new MapPoint(7,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(0,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(1,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(5,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(3,6), Piece.ROTATE_NONE);
			}
		}
		
		/**
		 * Places a soldier-type unit.
		 * @param location the starting location of this soldier
		 */
		private void placeSoldier(MapPoint location) {
			int unit = _core.placeUnit(this, location, new String("Soldier"));
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(true));
				_core.updateUnit(this, unit, 0, new MapPoint(1,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(3,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(6,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(0,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 5, new MapPoint(5,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(6,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(0,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(6,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(0,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(2,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(4,6), Piece.ROTATE_NONE);
			}
		}
		
		/**
		 * Places a tank-type unit
		 * @param location the starting location of the tank
		 */
		private void placeTank(MapPoint location) {
			int unit = _core.placeUnit(this, location, new String("Tank"));
			if (unit != -1) {
				final MapPoint p = getRandomLocationOfType(TerrainConstants.BUNKER);
				if (p != null) {
					registerUnit(unit, new PathBehavior(new LinkedList<MapPoint>() {
						{ add(p); }},false,true));
				} else {
					registerUnit(unit, new ChaseBehavior(true));
				}
				_core.updateUnit(this, unit, 4, new MapPoint(0,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(2,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(4,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(1,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(5,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(6,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(0,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(0,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(2,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(4,6), Piece.ROTATE_NONE);
			}
		}
		
		/**
		 * Gets a random map point of the specified type.
		 * @param type a terrain type
		 * @return a MapPoint to a random location of the given type, or null.
		 */
		private MapPoint getRandomLocationOfType(int type) {
			Set<MapPoint> spots = new HashSet<MapPoint>();
			for (int i = 0; i < _terrain.length; i++) {
				for (int j = 0; j < _terrain[0].length; j++) {
					if (TerrainConstants.isOfType(_terrain[i][j],type)) {
						spots.add(new MapPoint(i,j));
					}
				}
			}
			int i = (int)(Math.random()*spots.size());
			for (MapPoint p : spots) {
				if (i-- == 0) {
					return p;
				}
			}
			return null;
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
