package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.Map;

import net.quadratum.ai.LevelAI;
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
		
		Core _core;
		Map<MapPoint, Integer> _units;
		Object _lockObject;

		public Level5AI() {
		}

		@Override
		public void createUnits(int id) {
			
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