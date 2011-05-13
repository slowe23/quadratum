package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

import net.quadratum.ai.ChaseBehavior;
import net.quadratum.ai.LevelAI;
import net.quadratum.ai.PathBehavior;
import net.quadratum.ai.TurretBehavior;
import net.quadratum.core.Block;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level5 implements Level {
	
	int _numberOfDefaultPieces;
	
	public Level5() {}

	public String getMap() {
		return new String("maps/level5.qmap");
	}

	public int getStartingResources() {
		return 1500;
	}

	public int getMaxUnits() {
		return 5;
	}

	public Player getAI() {
		return new Level5AI();
	}

	public WinCondition getWinCondition() {
		return new Level5WinCondition();
	}

	public ArrayList<Piece>[] getPieces() {
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
		pieces[0] = DefaultPieces.getPieces();
		pieces[1] = getAIPieces();
		return pieces;
	}
	
	private ArrayList<Piece> getAIPieces() {
		ArrayList<Piece> pieces = DefaultPieces.getPieces();
		_numberOfDefaultPieces = pieces.size();
		
		// These are hax blocks used by the General.
		
		// Attack/range block
		Block attackrangeBlock = new Block(120);
		attackrangeBlock._bonuses.put(Block.BonusType.ATTACK, 36);
		attackrangeBlock._bonuses.put(Block.BonusType.RANGE, 12);
		
		// Sight block
		Block sightBlock = new Block(130);
		sightBlock._bonuses.put(Block.BonusType.SIGHT, 60);
		
		// Movement block
		Block movementBlock = new Block(150);
		movementBlock._bonuses.put(Block.BonusType.MOVEMENT, 120);
		
		// Defense block
		Block defenseBlock = new Block(200);
		defenseBlock._bonuses.put(Block.BonusType.DEFENSE, 40);
		
		// Attack/range piece - +360 attack, +1 range
		Piece attackrangePiece = new Piece(10000, "Hax Attax", "Provides hax");
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4 - i; j++) {
				attackrangePiece.addBlock(new MapPoint(i,j), attackrangeBlock);
			}
		}
		pieces.add(attackrangePiece);
		
		// Sight piece - +1 sight
		Piece sightPiece = new Piece(10000, "Hax Sight", "Provides hax");
		sightPiece.addBlock(new MapPoint(0,0), sightBlock);
		sightPiece.addBlock(new MapPoint(1,0), sightBlock);
		pieces.add(sightPiece);
		
		// Movement piece - +1 movement
		Piece movementPiece = new Piece(10000, "Hax Moves", "Provides hax");
		movementPiece.addBlock(new MapPoint(0,0), movementBlock);
		pieces.add(movementPiece);
		
		// Defense piece - +80 defense
		Piece defensePiece = new Piece(10000, "Hax Tank", "Provides hax");
		defensePiece.addBlock(new MapPoint(0,0), defenseBlock);
		defensePiece.addBlock(new MapPoint(1,0), defenseBlock);
		pieces.add(defensePiece);
		
		return pieces;
	}
	
	class Level5AI extends LevelAI {

		public Level5AI() {
			super();
		}

		@Override
		public void createUnits(int id) {
			// Place turrets
			placeTurret(new MapPoint(13,0));
			placeTurret(new MapPoint(13,9));
			
			placeTurret(new MapPoint(27,0));
			placeTurret(new MapPoint(27,9));
			
			placeTurret(new MapPoint(32,0));
			placeTurret(new MapPoint(32,9));
			placeTurret(new MapPoint(33,1));
			placeTurret(new MapPoint(33,8));
			
			// Place power turrets
			placePowerTurret(new MapPoint(22,1));
			placePowerTurret(new MapPoint(22,8));
			
			placePowerTurret(new MapPoint(33,2));
			placePowerTurret(new MapPoint(33,7));
			
			placePowerTurret(new MapPoint(42,4));
			placePowerTurret(new MapPoint(42,5));
			
			// Place beacons
			placeBeacon(new MapPoint(13,2));
			placeBeacon(new MapPoint(13,7));
			
			placeBeacon(new MapPoint(23,0));
			placeBeacon(new MapPoint(23,9));
			
			// Place defensive beacon
			placeDefensiveBeacon(new MapPoint(34,4));
			placeDefensiveBeacon(new MapPoint(34,5));
			
			placeDefensiveBeacon(new MapPoint(43,4));
			placeDefensiveBeacon(new MapPoint(43,5));
			
			// Place attackers
			placeAttacker(new MapPoint(28,4));
			placeAttacker(new MapPoint(28,5));
			
			// Place runners
			placeRunner(new MapPoint(27,4));
			placeRunner(new MapPoint(27,5));
			
			// Place patrols
			placePatrol(new MapPoint(19,4), new MapPoint(2,4));
			placePatrol(new MapPoint(19,5), new MapPoint(2,5));
			
			// Place bodyguard
			placeBodyguard(new MapPoint(47,5));
			
			// Place general
			placeGeneral(new MapPoint(48,4));
		}
		
		/**
		 * Places a turret.
		 * @param location the location at which to place a turret
		 */
		private void placeTurret(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Turret");
			if (unit != -1) {
				registerUnit(unit, new TurretBehavior());
				_core.updateUnit(this, unit, 4, new MapPoint(1,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(4,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(5,1), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 4, new MapPoint(6,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(1,2), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(3,2), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(2,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,4), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 4, new MapPoint(0,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(6,5), Piece.ROTATE_CCW);
				_core.updateUnit(this, unit, 2, new MapPoint(2,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(5,6), Piece.ROTATE_NONE);
				
			}
		}
		
		/**
		 * Places a power turret. Power turrets have very high attack.
		 * @param location the location at which to place a turret
		 */
		private void placePowerTurret(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Turret");
			if (unit != -1) {
				registerUnit(unit, new TurretBehavior());
				_core.updateUnit(this, unit, 2, new MapPoint(1,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(4,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 4, new MapPoint(5,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(4,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(3,2), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(6,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(0,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(2,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,4), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 4, new MapPoint(1,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(5,6), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 2, new MapPoint(6,7), Piece.ROTATE_CCW);
				
			}
		}
		
		/**
		 * Places a beacon. Beacons have a high sight range.
		 * @param location the location at which to place a beacon
		 */
		private void placeBeacon(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Beacon");
			if (unit != -1) {
				registerUnit(unit, new TurretBehavior());
				placeBeaconBlocks(unit);
			}
		}
		
		/**
		 * Places a defensive beacon. A defensive beacon has more defense
		 * than a normal beacon.
		 * @param location
		 */
		private void placeDefensiveBeacon(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Beacon");
			if (unit != -1) {
				registerUnit(unit, new TurretBehavior());
				placeBeaconBlocks(unit);
				_core.updateUnit(this, unit, 4, new MapPoint(0,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(0,5), Piece.ROTATE_NONE);
			}
		}
		
		/**
		 * Places an attacker. Attackers chase and have high attack power.
		 * @param location the location at which to place an attacker
		 */
		private void placeAttacker(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Attacker");
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(false));
				_core.updateUnit(this, unit, 4, new MapPoint(0,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(4,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(4,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(7,1), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 4, new MapPoint(0,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 5, new MapPoint(2,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(6,3), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(0,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(5,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(4,5), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 4, new MapPoint(0,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(5,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(2,7), Piece.ROTATE_CCW);
				
			}
		}
		
		/**
		 * Places a runner. Runners chase and have high movement range, but also
		 * wander when no enemy is in sight.
		 * @param location the location at which to place a runner
		 */
		private void placeRunner(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Runner");
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(true));
				_core.updateUnit(this, unit, 4, new MapPoint(0,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 2, new MapPoint(3,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(5,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(0,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(5,2), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 1, new MapPoint(7,2), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 1, new MapPoint(7,3), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 4, new MapPoint(0,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 3, new MapPoint(2,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(6,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 4, new MapPoint(0,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(4,6), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 1, new MapPoint(7,6), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 2, new MapPoint(4,7), Piece.ROTATE_CCW);
			}
		}
		
		/**
		 * Places a patrol unit. Patrol units pace back and forth between their start
		 * spot and the given other location, and attack on sight.
		 * @param location the point at which to place a patrol
		 * @param toGo the extent of the patrol's search
		 */
		private void placePatrol(final MapPoint location, final MapPoint toGo) {
			int unit = _core.placeUnit(this, location, "Patroller");
			if (unit != -1) {
				registerUnit(unit, new PathBehavior(new LinkedList<MapPoint>() {
					{ add(toGo); add(location); }},true,true));
				_core.updateUnit(this, unit, 2, new MapPoint(1,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(3,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(6,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(4,1), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 1, new MapPoint(2,3), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 7, new MapPoint(6,3), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 7, new MapPoint(1,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(5,4), Piece.ROTATE_CCW);
				_core.updateUnit(this, unit, 0, new MapPoint(0,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 0, new MapPoint(5,5), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 0, new MapPoint(6,6), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 2, new MapPoint(6,7), Piece.ROTATE_CCW);
			}
		}
		
		/**
		 * Places a bodyguard.
		 * @param location the location to place this bodyguard.
		 */
		private void placeBodyguard(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Bodyguard");
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(false));
				_core.updateUnit(this, unit, 1, new MapPoint(0,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(1,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 1, new MapPoint(7,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 6, new MapPoint(7,1), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 8, new MapPoint(3,3), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 8, new MapPoint(4,3), Piece.ROTATE_CCW);
				_core.updateUnit(this, unit, 8, new MapPoint(3,4), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, 8, new MapPoint(4,4), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, 6, new MapPoint(0,6), Piece.ROTATE_CCW);
				_core.updateUnit(this, unit, 1, new MapPoint(0,7), Piece.ROTATE_CCW);
				_core.updateUnit(this, unit, 6, new MapPoint(6,7), Piece.ROTATE_180);
				_core.updateUnit(this, unit, 1, new MapPoint(7,7), Piece.ROTATE_180);
			}
		}
		
		/**
		 * Places the general.
		 * @param location the location at which to place the general
		 */
		private void placeGeneral(MapPoint location) {
			int unit = _core.placeUnit(this, location, "General");
			if (unit != -1) {
				registerUnit(unit, new ChaseBehavior(false));
				// Hax stuff
				int attack = _numberOfDefaultPieces;
				int sight = _numberOfDefaultPieces+1;
				int movement = _numberOfDefaultPieces+2;
				int defense = _numberOfDefaultPieces+3;
				_core.updateUnit(this, unit, attack, new MapPoint(0,0), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, attack, new MapPoint(7,0), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, sight, new MapPoint(3,1), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, movement, new MapPoint(2,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, defense, new MapPoint(3,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, movement, new MapPoint(5,2), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, sight, new MapPoint(1,3), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, defense, new MapPoint(2,3), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, defense, new MapPoint(5,3), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, sight, new MapPoint(6,3), Piece.ROTATE_CW);
				_core.updateUnit(this, unit, movement, new MapPoint(2,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, defense, new MapPoint(3,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, movement, new MapPoint(5,5), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, sight, new MapPoint(3,6), Piece.ROTATE_NONE);
				_core.updateUnit(this, unit, attack, new MapPoint(0,7), Piece.ROTATE_CCW);
				_core.updateUnit(this, unit, attack, new MapPoint(7,7), Piece.ROTATE_180);
			}
		}
		
		/**
		 * Places the blocks that a standard beacon contains.
		 * @param unit the unit to place blocks in
		 */
		private void placeBeaconBlocks(int unit) {
			_core.updateUnit(this, unit, 4, new MapPoint(2,1), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(4,1), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 3, new MapPoint(7,2), Piece.ROTATE_180);
			_core.updateUnit(this, unit, 4, new MapPoint(1,3), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 3, new MapPoint(5,3), Piece.ROTATE_CCW);
			_core.updateUnit(this, unit, 3, new MapPoint(7,4), Piece.ROTATE_CW);
			_core.updateUnit(this, unit, 4, new MapPoint(2,5), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(4,5), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 3, new MapPoint(7,7), Piece.ROTATE_180);
		}
	}

	class Level5WinCondition implements WinCondition {
		
		public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber) {
			return false;
		}
		
		public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber) {
			if (playerNumber == 1) {
				boolean generalAlive = false;
				for (Unit u : units.values()) {
					if (u._name.equals("General")) {
						generalAlive = true;
						break;
					}
				}
				if (!generalAlive) {
					return true;
				}
			}
			return units.size() == 0;
		}
		
		public String getObjectives() {
			return new String("Kill the general!");
		}
	}
}