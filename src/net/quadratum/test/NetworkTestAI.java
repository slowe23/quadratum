package net.quadratum.test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.core.*;

class NetworkTestAI implements Player
{
	Core _core;
	int _turn;
	int _unita, _unitb;
	boolean _messageReceived, _unitDied;
	
	public void start(Core core, MapData mapData, int id, int totalPlayers)
	{
		_core = core;
		_turn = 0;
		_messageReceived = false;
		_unitDied = false;
		
		if(id != 1)
		{
			fail("Recieved incorrect player id");
		}
		
		_unita = _core.placeUnit(this, new MapPoint(9, 0), "testa");
		_unitb = _core.placeUnit(this, new MapPoint(9, 1), "testb");
		
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 0), 0);	
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 1), 0);	
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 2), 0);	
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 3), 0);	
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 4), 0);	
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 5), 0);	
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 6), 0);
		_core.updateUnit(this, _unita, 0, new MapPoint(7, 7), 0);
		_core.updateUnit(this, _unita, 1, new MapPoint(6, 0), 0);

		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 0), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 1), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 2), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 3), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 4), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 5), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 6), 0);
		_core.updateUnit(this, _unitb, 0, new MapPoint(7, 7), 0);
		
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
		// Turn 1 - move unit b to resources, move unit a into position to be attacked
		// Turn 2 - make sure message has been recieved and resources have increaed by 10, move unit b to mountain, see if other unit is in range
		// Turn 3 - make sure unit has died, quit game
		// Any turns past that mean that something has gone wrong
		_turn++;
		switch(_turn)
		{
			case 1:
				if(!_messageReceived)
				{
					fail("Never recieved message from the other player");
				}
				if(!_core.unitAction(this, _unitb, new MapPoint(8, 3)))
				{
					fail("Could not move unit b to resources");
				}
				if(!_core.unitAction(this, _unita, new MapPoint(2, 0)))
				{
					fail("Could not move unit a into position");
				}
				_core.sendChatMessage(this, "test2");
				_core.endTurn(this);
			break;
			case 2:
				if(_core.getResources(this) != 10)
				{
					fail("Resources are incorrect");
				}
				if(!_core.unitAction(this, _unitb, new MapPoint(7, 3)))
				{
					fail("Unit b could not move to mountain");
				}
				if(!_core.getValidActions(this, _unitb).containsKey(new MapPoint(1, 0)))
				{
					fail("Unit b cannot attack other unit");
				}
				if(_core.getValidActions(this, _unitb).get(new MapPoint(1, 0)) != Action.ActionType.ATTACK)
				{
					fail("Invalid action type");
				}
				_core.endTurn(this);
			break;
			case 3:
				if(!_unitDied)
				{
					fail("Unit a was never killed");
				}
				_core.quit(this);
			break;
			default:
				fail("Too many turns have occurred");
				_core.quit(this);
			break;
		}
	}
	
	public void updateMapData(MapData mapData) {}
	
	public void updateMap(Map<MapPoint, Integer> units, Set<MapPoint> sight, Action lastAction)
	{
		if(lastAction._action == Action.ActionType.UNIT_DIED)
		{
			if(!lastAction._source.equals(new MapPoint(2, 0)) || !lastAction._dest.equals(new MapPoint(2, 0)))
			{
				fail("Invalid action start/end");
			}
			_unitDied = true;
		}
	}

	public void chatMessage(int from, String message)
	{
		if(from == 0)
		{
			if(!message.equals("test1"))
			{
				fail("Incorrect message receieved");
			}
			if(_turn != 0)
			{
				fail("Message receieved on incorrect turn");
			}
			_messageReceived = true;
		}
	}
	
	public void updateTurn(int id) {}
	
	public void end(GameStats stats)
	{
		fail("Recieved end game stats");
	}
	
	public void fail(String message)
	{
		System.out.println("TEST FAIL FROM NETWORK TEST AI: " + message + " (turn: " + _turn + ")");
	}
}