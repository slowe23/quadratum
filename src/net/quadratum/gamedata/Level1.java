package net.quadratum.gamedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.ai.LevelAI;
import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.core.WinCondition;

public class Level1 implements Level
{
	public Level1() {}

	public String getMap()
	{
		return new String("maps/level1.qmap");
	}
	
	public int getStartingResources()
	{
		return 2000;
	}
	
	public int getMaxUnits()
	{
		return 8;
	}
	
	public Player getAI()
	{
		return new Level1AI();
	}
	
	public WinCondition getWinCondition()
	{
		return new Level1WinCondition();
	}
	
	public ArrayList<Piece>[] getPieces()
	{
		ArrayList<Piece>[] pieces = (ArrayList<Piece>[])new ArrayList[2];
		pieces[0] = DefaultPieces.getPieces();
		pieces[1] = DefaultPieces.getPieces();
		return pieces;
	}
	
	private class Level1AI extends LevelAI
	{
		public Level1AI()
		{
			super();
		}
		
		public void createUnits(int id)
		{
			
		}
		public void start(Core core, MapData mapData, int id, int totalPlayers)
		{
			_core = core;
			int unit;
			
			// Place king
			unit = _core.placeUnit(this, new MapPoint(19, 4), new String("King"));
			for(int i = 0; i < 7; i += 2)
			{
				for(int j = 0; j < 7; j += 2)
				{
					_core.updateUnit(this, unit, 4, new MapPoint(i, j));
				}
			}
			
			// Place queen
			unit = _core.placeUnit(this, new MapPoint(19, 5), new String("Queen"));
			for(int i = 0; i < 7; i += 2)
			{
				for(int j = 0; j < 7; j += 2)
				{
					_core.updateUnit(this, unit, 4, new MapPoint(i, j));
				}
			}
			
			// Add royal guard
			placeRoyalGuard(new MapPoint(18, 3));
			placeRoyalGuard(new MapPoint(18, 6));
			
			// Place defenders
			placeDefender(new MapPoint(10, 1));
			placeDefender(new MapPoint(10, 8));
			placeDefender(new MapPoint(12, 3));
			placeDefender(new MapPoint(12, 6));
			placeDefender(new MapPoint(13, 3));
			placeDefender(new MapPoint(13, 6));
			_core.ready(this);
		}
		
		// Places a royal guard
		private void placeRoyalGuard(MapPoint location)
		{
			int unit = _core.placeUnit(this, location, new String("Royal Guard"));
			if(unit == -1)
			{
				return;
			}
			registerUnit(unit, new TurretBehavior());
			for(int i = 0; i < 7; i += 2)
			{
				_core.updateUnit(this, unit, 4, new MapPoint(0, i));
			}
			_core.updateUnit(this, unit, 2, new MapPoint(2, 0));
			_core.updateUnit(this, unit, 2, new MapPoint(2, 5));
			_core.updateUnit(this, unit, 0, new MapPoint(6, 0));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 1));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 3));
			_core.updateUnit(this, unit, 0, new MapPoint(5, 5));
		}
		
		// Places a defender
		private void placeDefender(MapPoint location)
		{
			int unit = _core.placeUnit(this, location, new String("Defender"));
			if(unit == -1)
			{
				return;
			}
			registerUnit(unit, new TurretBehavior());
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

	private class Level1WinCondition implements WinCondition
	{
		public boolean hasPlayerWon(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
		{
			return false;
		}
		
		public boolean hasPlayerLost(Map<MapPoint, Unit> units, PlayerInformation playerInformation, int playerNumber)
		{
			if(playerNumber == 1)
			{
				if(!units.containsKey(new MapPoint(19, 4)) && !units.containsKey(new MapPoint(19, 5)))
				{
					return true;
				}
			}
			if(units.size() == 0)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		
		public String getObjectives()
		{
			return new String("Kill the enemy king and queen!");
		}
	}
}