package net.quadratum.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Unit;

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
		// TODO protocol
		
	}
	
	@Override
	public void updatePieces(List<Piece> pieces) {
		try {
			_out.write("updatepieces");
			for (Piece p : pieces) {
				// find out some concise way to represent a piece and write it out
			}
			_out.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void end(GameStats stats) {
		// TODO GameStats is currently empty, but when it gets
		// some meat, send that over
		try {
			_out.write("end\n");
			_sockToPlayer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void lost() {
		try {
			_out.write("lost\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void turnStart() {
		try {
			_out.write("turnStart\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMapData(MapData mapData) {
		// TODO protocol
		
	}

	@Override
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		// TODO protocol
		
	}
	
	@Override
	public void chatMessage(int from, String message) {
		try {
			_out.write("chat\t"+from+"\t"+message+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Process the message that is given.
	 * @param message the message sent from the client.
	 */
	private void process(String message) {
		String[] parts = message.split("\t");
		// Look at the first part of the incoming message
		if (parts[0].equals("ready")) {
			// The player is signalling that they are ready.
			_core.ready(this);
		} else if (parts[0].equals("endturn")) {
			// The player is signalling that their turn is over.
			_core.endTurn(this);
		} else if (parts[0].equals("unitaction")) {
			// The player has performed an action.
			// protocol: unitaction \t id \t x \t y
			try {
				int id = Integer.parseInt(parts[1]);
				int x = Integer.parseInt(parts[2]);
				int y = Integer.parseInt(parts[3]);
				boolean success = _core.unitAction(this, id, new MapPoint(x,y));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// TODO send back value of success
		} else if (parts[0].equals("getvalidactions")) {
			// The player is requesting valid actions.
			// This might be left out, since the VC should
			// cache this information and a new cache should
			// be sent if an invalid move is made.
		} else if (parts[0].equals("quit")) {
			// The player has quit.
			_core.quit(this);
		} else if (parts[0].equals("chat")) {
			// The player is sending a chat message.
			// protocol: chat \t message
			_core.sendChatMessage(this,parts[1]);
		} else if (parts[0].equals("placeunit")) {
			// The player is placing a unit.
			// protocol: placeunit \t x \t y
			try {
				int x = Integer.parseInt(parts[1]);
				int y = Integer.parseInt(parts[2]);
				boolean success = _core.placeUnit(this, new MapPoint(x,y));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// TODO send back value of success
		} else if (parts[0].equals("getremainingunits")) {
			// The player wants to know how many units they have left to place.
			int remunits = _core.getRemainingUnits(this);
			// TODO send back value of res
		} else if (parts[0].equals("getunit")) {
			// The player wants to get a particular unit.
			// protocol: getunit \t id
			try {
				int id = Integer.parseInt(parts[1]);
				Unit u = _core.getUnit(this, id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// TODO send back value of u
		} else if (parts[0].equals("updateunit")) {
			// The player wants to update a unit with a piece.
			// protocol: updateunit \t unit ID \t piece ID \t x \t y
			try {
				int unitID = Integer.parseInt(parts[1]);
				int pieceID = Integer.parseInt(parts[2]);
				int x = Integer.parseInt(parts[3]);
				int y = Integer.parseInt(parts[4]);
				boolean success = _core.updateUnit(this, unitID, pieceID, new MapPoint(x,y));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// TODO send back value of success
		} else if (parts[0].equals("getplayername")) {
			// The player wants to know the name of a given player.
			// protocol: getplayername \t id
			try {
				int id = Integer.parseInt(parts[1]);
				String name = _core.getPlayerName(id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			// TODO send back value of name
		} else if (parts[0].equals("getresources")) {
			// The player wants to know how many resources he has.
			int res = _core.getResources(this);
			// TODO return value of res
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
				process(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
