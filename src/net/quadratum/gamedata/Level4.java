package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.LinkedList;

import net.quadratum.ai.LevelAI;
import net.quadratum.ai.PathBehavior;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.WinCondition;
import net.quadratum.main.CheckWinner;
import net.quadratum.core.MapData;

public class Level4 implements Level
{
	public Level4() {}
	
	@Override
	public Player getAI()
	{
		return new Level4AI();
	}
	
	@Override
	public String getMap()
	{
		return new String("maps/level4.qmap");
	}
	
	@Override
	public int getMaxUnits()
	{
		return 4;
	}

	@Override
	public ArrayList<Piece>[] getPieces()
	{
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
		pieces[0] = DefaultPieces.getPieces();
		pieces[1] = DefaultPieces.getPieces();
		return pieces;
	}
	
	@Override
	public int getStartingResources()
	{
		return 1000;
	}
	
	@Override
	public WinCondition getWinCondition()
	{
		return new CheckWinner() {
			@Override
			public String getObjectives() {
				return "Defend your area from the incoming attackers!";
			}
		};
	}
	
	private class Level4AI extends LevelAI
	{
		public Level4AI()
		{
			super();
		}
		
		public void createUnits(MapData mapData, int id)
		{
			placeAttacker(new MapPoint(6, 0));
			placeAttacker(new MapPoint(6, 3));
			placeAttacker(new MapPoint(6, 6));
			placeAttacker(new MapPoint(0, 6));
			placeAttacker(new MapPoint(3, 6));
		}
		
		public void placeAttacker(MapPoint location)
		{
			LinkedList<MapPoint> target = new LinkedList<MapPoint>();
			target.add(new MapPoint(0, 0));
			
			int unit = _core.placeUnit(this, location, new String("Attacker"));
			registerUnit(unit, new PathBehavior(target, false, true));
			
			_core.updateUnit(this, unit, 4, new MapPoint(0, 0), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(0, 2), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(0, 4), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(0, 6), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(2, 0), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(4, 0), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 4, new MapPoint(6, 0), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 1, new MapPoint(4, 2), Piece.ROTATE_CW);
			_core.updateUnit(this, unit, 1, new MapPoint(7, 2), Piece.ROTATE_CW);
			_core.updateUnit(this, unit, 0, new MapPoint(2, 4), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 0, new MapPoint(6, 3), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 2, new MapPoint(2, 6), Piece.ROTATE_NONE);
			_core.updateUnit(this, unit, 7, new MapPoint(4, 4), Piece.ROTATE_NONE);
		}
	}
}
