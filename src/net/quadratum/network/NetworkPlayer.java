package net.quadratum.network;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.Unit;
import net.quadratum.util.Serializer;

public class NetworkPlayer extends NetworkClient implements Player {
	
	/** Core that this Player belongs to. */
	Core _core;
	
	/** Player ID of this Player. */
	int _playerID;
	
	/** */
	boolean _done;
	
	public NetworkPlayer(Socket sock) {
		super(sock);
		
		_done = false;
	}
	
	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		_playerID = id;
		write("start\t"+Serializer.getEncodedString(mapData)+
				"\t"+id+"\t"+totalPlayers+"\n");
		// Start this thread, so it can listen for messages.
		new ReadThread().start();
	}
	
	@Override
	public void updatePieces(List<Piece> pieces) {
		// Copy to ArrayList because List does not implement Serializable.
		ArrayList<Piece> p = new ArrayList<Piece>(pieces);
		write("updatepieces\t"+Serializer.getEncodedString(p)+"\n");
	}
	
	@Override
	public void end(GameStats stats) {
		write("end\t"+Serializer.getEncodedString(stats)+"\n");
		_done = true;
	}

	@Override
	public void lost() {
		write("lost\n");
	}

	@Override
	public void turnStart() {
		write("turnstart\n");
	}

	@Override
	public void updateMapData(MapData mapData) {
		write("mapdata\t"+Serializer.getEncodedString(mapData)+"\n");
	}

	@Override
	public void updateMap(Map<MapPoint, Integer> units, Set<MapPoint> sight, Action lastAction) {
		// Copy these to HashCollections because the Map/Set interface does not
		// implement Serializable directly.
		HashMap<MapPoint,Integer> map = new HashMap<MapPoint,Integer>(units);
		HashSet<MapPoint> set = new HashSet<MapPoint>(sight);
		// Send the updated map, sight, and action.
		write("updatemap\t"+Serializer.getEncodedString(map)+"\t"
				+Serializer.getEncodedString(set)+"\t"
				+Serializer.getEncodedString(lastAction)+"\n");
	}
	
	@Override
	public void chatMessage(int from, String message) {
		write("chat\t"+from+"\t"+message+"\n");
	}
	
	@Override
	public void updateTurn(int turn) {
		write("updateturn\t"+turn+"\n");
	}
	
	@Override
	protected void process(String message) {
		String[] parts = message.split("\t");
		// Look at the first part of the incoming message
		if (parts[0].equals("ready")) {
			// The player is signaling that they are ready.
			_core.ready(this);
		} else if (parts[0].equals("endturn")) {
			// The player is signaling that their turn is over.
			_core.endTurn(this);
		} else if (parts[0].equals("unitaction")) {
			// The player has performed an action.
			boolean success = false;
			// protocol: unitaction \t id \t x \t y
			try {
				int id = Integer.parseInt(parts[1]);
				int x = Integer.parseInt(parts[2]);
				int y = Integer.parseInt(parts[3]);
				success = _core.unitAction(this, id, new MapPoint(x,y));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// send back whether or not it succeeded
			write("unitaction\t"+success+"\n");
		} else if (parts[0].equals("getvalidactions")) {
			// The player is requesting valid actions.
			int id = -1;
			// protocol: getvalidactions \t unitid
			try {
				id = Integer.parseInt(parts[1]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// Get the valid actions for this unit.
			HashMap<MapPoint, Action.ActionType> map = null;
			Map<MapPoint,Action.ActionType> valid = _core.getValidActions(this,id);
			if (valid != null) {
				map = new HashMap<MapPoint, Action.ActionType>(valid);
			}
			// Write the data...
			write("validactions\t"+id+"\t"+Serializer.getEncodedString(map)+"\n");
		} else if (parts[0].equals("quit")) {
			// The player has quit.
			_core.quit(this);
		} else if (parts[0].equals("chat")) {
			// The player is sending a chat message.
			// protocol: chat \t message
			_core.sendChatMessage(this,parts[1]);
		} else if (parts[0].equals("placeunit")) {
			// The player is placing a unit.
			int id = -1;
			// protocol: placeunit \t x \t y \t name
			try {
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				id = _core.placeUnit(this, new MapPoint(x,y), parts[3]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// send back whether or not it succeeded
			write("unitplaced\t"+id+"\n");
		} else if (parts[0].equals("getremainingunits")) {
			// The player wants to know how many units they have left to place.
			int remunits = _core.getRemainingUnits(this);
			// send back the remaining units count
			write("remainingunits\t"+remunits+"\n");
		} else if (parts[0].equals("getunit")) {
			// The player wants to get a particular unit.
			Unit u = null;
			int id = -1;
			// protocol: getunit \t id
			try {
				id = Integer.parseInt(parts[1]);
				u = _core.getUnit(this, id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// Write the unit back across the wire...
			write("unit\t"+id+"\t"+Serializer.getEncodedString(u)+"\n");
		} else if (parts[0].equals("updateunit")) {
			// The player wants to update a unit with a piece.
			boolean success = false;
			// protocol: updateunit \t unit ID \t piece ID \t x \t y
			try {
				int unitID = Integer.parseInt(parts[1]);
				int pieceID = Integer.parseInt(parts[2]);
				int x = Integer.parseInt(parts[3]);
				int y = Integer.parseInt(parts[4]);
				success = _core.updateUnit(this, unitID, pieceID, new MapPoint(x,y));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// send back whether it succeeded
			write("unitupdated\t"+success+"\n");
		} else if (parts[0].equals("getplayername")) {
			// The player wants to know the name of a given player.
			int id = -1;
			String name = "";
			// protocol: getplayername \t id
			try {
				id = Integer.parseInt(parts[1]);
				name = _core.getPlayerName(id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// send back the id and player name
			write("playername\t"+id+"\t"+name+"\n");
		} else if (parts[0].equals("getresources")) {
			// The player wants to know how many resources he has.
			int res = _core.getResources(this);
			// send back the amount of resources
			write("resources\t"+res+"\n");
		} else if (parts[0].equals("getobjectives")) {
			// The player wants to know the objectives.
			String objectives = _core.getObjectives(this);
			// send back the objectives, replacing all newlines with tabs
			write("objectives\t"+objectives.replaceAll("(\r)?\n","\t"));
		}
	}

	@Override
	protected boolean doneReading() {
		return _done;
	}
	
	@Override
	public void close() {
		_core.quit(this);
		super.close();
	}

}
