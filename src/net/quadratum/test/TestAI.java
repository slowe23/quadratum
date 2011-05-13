package net.quadratum.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.core.*;

class TestAI implements Player
{
	Core _core;
	int _turn;
	int _unit;
	boolean _messageReceived;
	
	public void start(Core core, MapData mapData, int id, int totalPlayers)
	{
		_core = core;
		_turn = 0;
		_messageReceived = false;
		
		if(id != 0)
		{
			fail("Recieved incorrect player id");
		}
		// Test unit/block placement
		
		if(_core.placeUnit(this, new MapPoint(-1, -1), "test") != -1)
		{
			fail("Was able to place unit outside map");
		}
		if(_core.placeUnit(this, new MapPoint(5, 5), "test") != -1)
		{
			fail("Was able to place unit outside placement area");
		}
		boolean unitPlaced = true;
		_unit = _core.placeUnit(this, new MapPoint(0, 0), "test");
		if(_unit == -1)
		{
			fail("Was not able to place unit");
			unitPlaced = false;
		}
		if(unitPlaced && _core.placeUnit(this, new MapPoint(0, 1), "test") != -1)
		{
			fail("Was able to place more than the maximum number of units");
		}
		if(_core.updateUnit(this, _unit, 0, new MapPoint(-1, -1), 0))
		{
			fail("Was able to place piece outside unit bounds");
		}
		if(_core.updateUnit(this, _unit, 0, new MapPoint(8, 8), 0))
		{
			fail("Was able to place piece outside unit bounds");
		}
		if(_core.updateUnit(this, _unit, 0, new MapPoint(4, 4), 0))
		{
			fail("Was able to place piece on top of unit heart");
		}
		if(!_core.updateUnit(this, _unit, 0, new MapPoint(0, 0), 0))
		{
			fail("Was not able to place piece");
		}
		if(_core.updateUnit(this, _unit, 0, new MapPoint(0, 0), 0))
		{
			fail("Was able to place piece on top of another piece");
		}
		_core.ready(this);
	}
	
	public void updatePieces(List<Piece> pieces)
	{
		if(pieces.size() != 2)
		{
			fail("Received incorrect number of pieces");
		}
	}
	
	public void lost()
	{
		fail("Lost the game incorrectly");
	}
	
	public void turnStart()
	{
		// Turn 1 - try to move unit on top of water (also check getValidActions) - then add water movement block and try to add another block
		// Turn 2 - try to move onto water
		// Turn 3 - try to move onto impassable terrain then attack enemy unit
		// Any turns past that mean that something has gone wrong
		_turn++;
		switch(_turn)
		{
			case 1:
				if(_core.getValidActions(this, _unit).size() != 1)
				{
					fail("Incorrect number of valid moves");
				}
				if(_core.unitAction(this, _unit, new MapPoint(-1, -1)))
				{
					fail("Unit was able to move outside of map");
				}
				if(_core.getValidActions(this, _unit).containsKey(new MapPoint(1, 0)))
				{
					fail("Unit was told it was able to move on water");
				}
				if(_core.unitAction(this, _unit, new MapPoint(0, 2)))
				{
					fail("Unit was able to move outside of movement area");
				}
				if(!_core.updateUnit(this, _unit, 1, new MapPoint(1, 1), 0))
				{
					fail("Was not able to add water block");
				}
				if(_core.getValidActions(this, _unit) != null)
				{
					fail("Unit still had actions after a block had been placed");
				}
				if(_unit == 0)
				{
					if(_core.getUnit(this, 1) != null)
					{
						fail("Was able to get information of unit that could not be seen");
					}
				}
				else
				{
					if(_core.getUnit(this, 0) != null)
					{
						fail("Was able to get information of unit that could not be seen");
					}
				}
				if(_core.getUnit(this, -1) != null)
				{
					fail("Was able to get information of unit that did not exist");
				}
				if(_core.getUnit(this, 3) != null)
				{
					fail("Was able to get information of unit that did not exist");
				}
				_core.sendChatMessage(this, "test1");
				_core.endTurn(this);
			break;
			case 2:
				if(!_messageReceived)
				{
					fail("Never received message from the network AI player");
				}
				if(!_core.unitAction(this, _unit, new MapPoint(1, 0)))
				{
					fail("Unit failed to walk on water");
				}
				_core.endTurn(this);
			break;
			case 3:
				if(!_core.unitAction(this, _unit, new MapPoint(2, 0)))
				{
					fail("Unit could not attack");
				}
				_core.endTurn(this);
			break;
			default:
				fail("Too many turns have occurred");
				_core.quit(this);
			break;
		}
	}
	
	public void updateMapData(MapData mapData) {}
	
	public void updateMap(Map<MapPoint, Integer> units, Set<MapPoint> sight, Action lastAction) {}

	public void chatMessage(int from, String message)
	{
		if(from == 1)
		{
			if(!message.equals("test2"))
			{
				fail("Incorrect message receieved");
			}
			if(_turn != 1)
			{
				fail("Messge receieved on incorrect turn");
			}
			_messageReceived = true;
		}
	}
	
	public void updateTurn(int id) {}
	
	public void end(GameStats stats)
	{
		if(stats._victor != 0 || !stats._victorName.equals("AI Player"))
		{
			fail("Recieved incorrect end game stats");
		}
	}
	
	public void fail(String message)
	{
		System.out.println("TEST FAIL FROM TEST AI: " + message + " (turn: " + _turn + ")");
	}
}