package net.quadratum.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.Unit;
import net.quadratum.util.Serializer;

public class NetworkPlayer extends Thread implements Player {
	
	/** Core that this Player belongs to. */
	Core _core;
	
	/** Player ID of this Player. */
	int _playerID;
	
	/** Socket that connects to the actual player. */
	Socket _sockToPlayer;
	/** Buffered writer to the player. */
	BufferedWriter _out;
	/** Buffered reader from the player. */
	BufferedReader _in;
	
	public NetworkPlayer(Socket sock) {
		_sockToPlayer = sock;
		try {
			_out = new BufferedWriter(new OutputStreamWriter(_sockToPlayer.getOutputStream()));
			_in = new BufferedReader(new InputStreamReader(_sockToPlayer.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		_playerID = id;
		write("start\t"+Serializer.getEncodedString(mapData)+
				"\t"+id+"\t"+totalPlayers+"\n");
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
		try {
			_sockToPlayer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		// Copy this to a HashMap because the Map interface does not
		// implement Serializable directly.
		HashMap<MapPoint,Integer> map = new HashMap<MapPoint,Integer>(units);
		write("updatemap\t"+Serializer.getEncodedString(map)+"\t"
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
	
	/**
	 * Process the message that is given.
	 * @param message the message sent from the client.
	 */
	private void process(String message) {
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
			HashMap<MapPoint, Action.ActionType> map = 
				new HashMap<MapPoint, Action.ActionType>(
						_core.getValidActions(this,id));
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
		}
	}
	
	/**
	 * Writes to the socket.
	 * @param s the string to write.
	 */
	private void write(String s) {
		try {
			_out.write(s);
			_out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Runs this NetworkPlayer.
	 */
	public void run() {
		boolean threadRunning = true;
		
		String line;
		try {
			while (threadRunning && (line = _in.readLine()) != null) {
				if (_sockToPlayer.isClosed()) {
					// Player has disconnected, quit.
					_core.quit(this);
					break;
				}
				process(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
