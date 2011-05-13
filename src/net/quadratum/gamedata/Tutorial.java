package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import net.quadratum.ai.AIPlayer;
import net.quadratum.ai.LevelAI;
import net.quadratum.core.Core;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Tutorial implements Level {
	
	public static TutorialAI TUTORIAL_AI = null;

	@Override
	public String getMap() {
		return "maps/tutorial.qmap";
	}

	@Override
	public int getStartingResources() {
		return 5000;
	}

	@Override
	public int getMaxUnits() {
		return 1;
	}

	@Override
	public Player getAI() {
		if (TUTORIAL_AI == null) {
			TUTORIAL_AI = new TutorialAI();
		}
		return TUTORIAL_AI;
	}

	@Override
	public WinCondition getWinCondition() {
		return new TutorialWinCondition((TutorialAI) getAI());
	}

	@Override
	public ArrayList<Piece>[] getPieces() {
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[]) new ArrayList[2];
		pieces[0] = new ArrayList<Piece>();
		pieces[1] = new ArrayList<Piece>();
		return pieces;
	}
	
	class TutorialAI extends AIPlayer {
		
		public TutorialAI() {
			super();
		}

		@Override
		public void start(Core core, MapData mapData, int id, int totalPlayers) {
			super.start(core,mapData,id,totalPlayers);
			_core.placeUnit(this, new MapPoint(11,7), "Enemy");
			_core.placeUnit(this, new MapPoint(11,8), "Enemy");
			_core.ready(this);
			giveDirections("Welcome to Quadratum! To begin, place a piece on the highlighted area, then click the \"Start Game\" button in the top right corner.");
		}
		
		/**
		 * Gives directions to the player through chat.
		 * @param message the directions to give
		 */
		public void giveDirections(String message) {
			_core.sendChatMessage(this, message);
		}
		
		/**
		 * Tells this AI to add the piece.
		 * @param p
		 */
		public void addPiece(Piece p) {
			((TutorialCore) _core).addPiece(p);
		}

		@Override
		public void turnStart() {
			_core.endTurn(this);
		}
	}
	
	class TutorialWinCondition implements WinCondition {
		
		TutorialAI _ai;
		
		boolean _placedUnit = false;
		boolean _movedToResources = false;
		boolean _movedToBunkers = false;
		boolean _killedEnemies = false;
		boolean _movedToMountains = false;
		boolean _movedToCorner = false;

		/**
		 * Creates a new TutorialWinCondition with the given player to
		 * call back to.
		 * @param ai
		 */
		public TutorialWinCondition(TutorialAI ai) {
			_ai = ai;
		}

		@Override
		public boolean hasPlayerWon(Map<MapPoint, Unit> units,
				PlayerInformation playerInformation, int playerNumber) {
			if (playerNumber == 0) {
				if (!_placedUnit) {
					if (units.size() > 0) {
						_ai.addPiece(new Piece(DefaultPieces.getPieces().get(1)));
						_ai.giveDirections("Good job! Now try adding some movement blocks to your unit and move him up to the gold diamond-shaped objects. I put the movement blocks in your blocks list for you.");
						_placedUnit = true;
					}
				} else if (!_movedToResources) {
					if (hasUnitInBounds(units,3,7,4,8)) {
						_ai.giveDirections("Very good! These gold squares are resources. Standing on them will give you resources every turn, which you spend to add blocks. Now try moving to the right to the gray shapes.");
						_movedToResources = true;
					}
				} else if (!_movedToBunkers) {
					if (hasUnitInBounds(units,8,7,9,8)) {
						_ai.addPiece(new Piece(DefaultPieces.getPieces().get(0)));
						_ai.addPiece(new Piece(DefaultPieces.getPieces().get(2)));
						_ai.giveDirections("This terrain type is a bunker. They give you a defensive boost. Make sure to make use of them in battle! I'm adding attack and range blocks to your list. Place some and kill the enemies!");
						_movedToBunkers = true;
					}
				} else if (!_killedEnemies) {
					// This is handled by the AI's side
				} else if (!_movedToMountains) {
					if (hasUnitInBounds(units,8,2,9,3)) {
						_ai.addPiece(new Piece(DefaultPieces.getPieces().get(5)));
						_ai.giveDirections("This terrain is a mountain. You get a range bonus when you are on top of one of these. I added a water movement block to your list, so use it to cross the moat in the top right.");
						_movedToMountains = true;
					}
				} else if (!_movedToCorner) {
					if (hasUnitInBounds(units,13,0,13,0) || hasUnitInBounds(units,14,0,14,0)
							|| hasUnitInBounds(units,14,1,14,1)) {
						_ai.giveDirections("Congratulations! You completed the tutorial. Try playing the campaign next!");
						_movedToCorner = true;
					}
				} else if (_movedToCorner) {
					return true;
				}
			} else if (playerNumber == 1) {
				if (!_killedEnemies) {
					if (units.size() == 0) {
						_ai.giveDirections("Nice job! Trying moving around the water to the brown triangular spaces...");
						_killedEnemies = true;
					}
				}
			}
			return false;
		}

		@Override
		public boolean hasPlayerLost(Map<MapPoint, Unit> units,
				PlayerInformation playerInformation, int playerNumber) {
			return false;
		}

		@Override
		public String getObjectives() {
			// TODO make this not suck
			return "Follow directions!";
		}
		
		/**
		 * Tests to see if a unit in the given map is in the given bounds.
		 * @param units the map of units
		 * @param lx the lower x-coordinate
		 * @param ly the lower y-coordinate
		 * @param ux the upper x-coordinate
		 * @param uy the upper y-coordinate
		 * @return whether the map contains a unit between the locations given.
		 */
		private boolean hasUnitInBounds(Map<MapPoint, Unit> units, 
				int lx, int ly, int ux, int uy) {
			for (MapPoint p : units.keySet()) {
				if (p._x >= lx && p._x <= ux && p._y >= ly && p._y <= uy) {
					return true;
				}
			}
			return false;
		}
		
	}
}
