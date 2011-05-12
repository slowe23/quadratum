package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.Map;

import net.quadratum.ai.LevelAI;
import net.quadratum.ai.TurretBehavior;
import net.quadratum.core.Core;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level5 implements Level
{
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
		pieces[1] = DefaultPieces.getPieces();
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
			
			placePowerTurret(new MapPoint(22,1));
			placePowerTurret(new MapPoint(22,8));
			
			// Place beacons
			placeBeacon(new MapPoint(13,2));
			placeBeacon(new MapPoint(13,7));
			
			placeBeacon(new MapPoint(23,0));
			placeBeacon(new MapPoint(23,9));
		}
		
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
		
		private void placeBeacon(MapPoint location) {
			int unit = _core.placeUnit(this, location, "Beacon");
			if (unit != -1) {
				registerUnit(unit, new TurretBehavior());
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
	}

	class Level5WinCondition implements WinCondition {
		
		public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber) {
			return false;
		}
		
		public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber) {
			if (playerNumber == 1) {
				// If general is dead
			}
			return units.size() == 0;
		}
		
		public String getObjectives() {
			return new String("Kill the general!");
		}
	}
}