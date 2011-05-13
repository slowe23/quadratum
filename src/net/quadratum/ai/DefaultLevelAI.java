package net.quadratum.ai;

import net.quadratum.core.*;

import java.util.*;

/** A level AI suitable for quick play games, etc. */
public class DefaultLevelAI extends LevelAI {
	
	public DefaultLevelAI() {
	}
	
	public void createUnits(MapData mapData, int id) {
		Random r = new Random();
		
		int unit = 0;
		
		while(_core.getRemainingUnits(this)>0 && mapData._placementArea!=null && mapData._placementArea.size()>0) {
			MapPoint location = null;
			
			int n = r.nextInt(mapData._placementArea.size());
			for(MapPoint m : mapData._placementArea) {
				if(n--==0) {
					location = m;
					break;
				}
			}
			
			if(unit%4==1) {
				if(placeScout(location))
					unit++;
			} else {
				if(placeChaser(location))
					unit++;
			}
			
			mapData._placementArea.remove(location);
		}
	
		// So the AI will make fun of you
		new ChatThread().start();
	}
	
	private boolean placeChaser(MapPoint location) {
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

			return true;
		} else
			return false;
	}
	
	private boolean placeScout(MapPoint location) {
		int unit = _core.placeUnit(this, location, "Scout");
		if(unit!=-1) {
			registerUnit(unit, new ScoutBehavior());
			
			_core.updateUnit(this, unit, 0, new MapPoint(1, 0), Piece.ROTATE_CW);
			_core.updateUnit(this, unit, 1, new MapPoint(2, 3), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 1, new MapPoint(1, 4), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 1, new MapPoint(0, 5), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 1, new MapPoint(4, 5), Piece.ROTATE_180);
			_core.updateUnit(this, unit, 1, new MapPoint(2, 6), Piece.ROTATE_CCW);
			_core.updateUnit(this, unit, 2, new MapPoint(0, 0), Piece.ROTATE_CW);
			_core.updateUnit(this, unit, 3, new MapPoint(2, 1), Piece.ROTATE_CCW);
			_core.updateUnit(this, unit, 3, new MapPoint(5, 0), Piece.ROTATE_180);
			_core.updateUnit(this, unit, 3, new MapPoint(5, 2), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(6, 4), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(6, 6), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 5, new MapPoint(7, 0), Piece.ROTATE_NONE);
			
			return true;
		} else
			return false;
	}
	
	private class ChatThread extends Thread {
		
		private final String[] INSULTS = {
		"LOL, you suck!",
		"What the hell was that?",
		"You call that a move?",
		"Is this the best you puny humans can do?!",
		"Stupid meatsack!"
		};
		
		public void run() {
			
			while (!hasEnded()) {
				try {
					Thread.sleep((int)(Math.random()*20+5)*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				_core.sendChatMessage(DefaultLevelAI.this,
									  INSULTS[(int)(Math.random()*INSULTS.length)]);
			}
		}
	}
}