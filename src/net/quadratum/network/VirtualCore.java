package net.quadratum.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Action.ActionType;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.PlayerInformation;
import net.quadratum.core.Unit;
import net.quadratum.util.Serializer;

public class VirtualCore extends Thread implements Core {
	
	/** Socket that connects to the actual player. */
	Socket _sockToServer;
	/** Buffered writer to the player. */
	BufferedWriter _out;
	/** Buffered reader from the player. */
	BufferedReader _in;
	
	/** Local player. */
	Player _localPlayer;
	/** Local player's information. */
	PlayerInformation _localInfo;
	
	/** Nonlocal players. */
	List<VirtualPlayer> _virtualPlayers;
	/** Nonlocal player information. */
	List<PlayerInformation> _virtualInfo;
	
	/** Used for storage. */
	Map<String,String> _responses;
	
	public VirtualCore(Socket sock) {
		_sockToServer = sock;
		try {
			_out = new BufferedWriter(new OutputStreamWriter(_sockToServer.getOutputStream()));
			_in = new BufferedReader(new InputStreamReader(_sockToServer.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_responses = Collections.synchronizedMap(new HashMap<String,String>());		
	}

	@Override
	public void addPlayer(Player p, String playerName, int maxUnits) {
		if (p instanceof VirtualPlayer) {
			// A VirtualPlayer represents somebody on the actual game, but over the
			// network.
			_virtualPlayers.add((VirtualPlayer) p);
			_virtualInfo.add(new PlayerInformation(playerName, maxUnits));
		} else {
			// A VirtualCore should only have one local player associated.
			_localPlayer = p;
			_localInfo = new PlayerInformation(playerName, maxUnits);
		}
		
	}

	@Override
	public void start() {
		// The VirtualCore has no control over this.
	}

	@Override
	public void ready(Player p) {
		try {
			_out.write("ready\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endTurn(Player p) {
		try {
			_out.write("endturn\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean unitAction(Player p, int unitID, MapPoint coords) {
		try {
			_out.write("unitaction\t"+unitID+"\t"+coords._x+"\t"+coords._y+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <unitaction \t> success
		String[] s = getResponse("unitaction");
		boolean b = false;
		try {
			b = Boolean.parseBoolean(s[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return b;
	}

	@Override
	public Map<MapPoint, ActionType> getValidActions(Player p, int unitID) {
		try {
			_out.write("getvalidactions\t"+unitID+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <validactions \t> id \t mapobject
		String[] s = getResponse("validactions");
		return Serializer.<HashMap<MapPoint,ActionType>>getObject(s[2]);
	}

	@Override
	public void quit(Player p) {
		try {
			_out.write("quit\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendChatMessage(Player p, String message) {
		try {
			_out.write("chat\t"+message+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int placeUnit(Player p, MapPoint coords, String name) {
		try {
			_out.write("placeunit\t"+coords._x+"\t"+coords._y+"\t"+name+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <unitplaced \t> id
		String[] s = getResponse("unitplaced");
		int id = -1;
		try {
			id = Integer.parseInt(s[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public int getRemainingUnits(Player p) {
		// this should be cached
		try {
			_out.write("getremainingunits\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <remainingunits \t> remunits
		String[] s = getResponse("remainingunits");
		int i = 0;
		try {
			i = Integer.parseInt(s[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return i;
	}

	@Override
	public Unit getUnit(Player p, int unitID) {
		// this should be cached also
		try {
			_out.write("getunit\t"+unitID+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <unit \t> id \t unitobject
		String[] s = getResponse("unit");
		return (Unit) Serializer.getObject(s[1]);
	}

	@Override
	public boolean updateUnit(Player p, int unitID, int pieceID, MapPoint coords) {
		try {
			_out.write("updateunit\t"+unitID+"\t"+pieceID+"\t"+coords._x+"\t"+coords._y+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <unitupdated \t> success
		String[] s = getResponse("unitupdated");
		boolean b = false;
		try {
			b = Boolean.parseBoolean(s[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return b;
	}
	
	@Override
	public String getPlayerName(int player) {
		// trivial to cache
		try {
			_out.write("getplayername\t"+player+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <playername \t> id \t name
		String[] s = getResponse("playername");
		/*
		int id = 0;
		try {
			id = Integer.parseInt(s[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		*/
		return s[1];
	}

	@Override
	public int getResources(Player p) {
		try {
			_out.write("getresources\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// protocol: <resources \t> res
		String[] s = getResponse("resources");
		int i = 0;
		try {
			i = Integer.parseInt(s[0]);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return i;
	}
	
	/**
	 * Gets a response out of the map. Makes sure that no synchronization
	 * issues occur. Deletes the data of the response afterwards.
	 * @param resp the response's key
	 * @return the data provided in the response
	 */
	private String[] getResponse(String resp) {
		String[] s = null;
		boolean done = false;
		while (!done) {
			synchronized (_responses) {
				done = _responses.containsKey(resp);
				if (done) {
					s = _responses.remove(resp).split("\t");
				}
			}
		}
		return s;
	}
	
	/**
	 * Processes the message that is given.
	 * @param message a message from the host.
	 */
	private void process(String message) {
		String[] parts = message.split("\t");
		// Look at the first part of the incoming message
		if (parts[0].equals("start")) {
			// The game has started.
			int id = -1, numplayers = 1;
			// protocol: start \t mdobject \t id \t numplayers
			MapData md = Serializer.getObject(parts[1]);
			try {
				id = Integer.parseInt(parts[2]);
				numplayers = Integer.parseInt(parts[3]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			_localPlayer.start(this,md,id,numplayers);
		} else if (parts[0].equals("updatepieces")) {
			// The game is telling the player what pieces they are
			// allowed to have.
			// protocol: updatepieces \t listobject
			ArrayList<Piece> p = Serializer.getObject(parts[1]);
			_localPlayer.updatePieces(p);
		} else if (parts[0].equals("end")) {
			// The game has ended.
			// protocol: end \t statsobject
			_localPlayer.end(Serializer.<GameStats>getObject(parts[1]));
		} else if (parts[0].equals("lost")) {
			// This player has just lost.
			_localPlayer.lost();
		} else if (parts[0].equals("turnstart")) {
			// It is now this player's turn.
			_localPlayer.turnStart();
		} else if (parts[0].equals("mapdata")) {
			// The host is sending new map data.
			// protocol: mapdata \t mdobject
			_localPlayer.updateMapData(Serializer.<MapData>getObject(parts[1]));
		} else if (parts[0].equals("updatemap")) {
			// The host is sending new information about units and actions.
			// protocol: updatemap \t mapobject \t actobject
			HashMap<MapPoint,Integer> map = Serializer.getObject(parts[1]);
			Action act = Serializer.getObject(parts[2]);
			_localPlayer.updateMap(map,act);
		} else if (parts[0].equals("chat")) {
			// A chat message was sent.
			// protocol: chat \t id \t message
			try {
				int id = Integer.parseInt(parts[1]);
				_localPlayer.chatMessage(id, parts[2]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else if (parts[0].equals("updateturn")) {
			// The turn has changed.
			// protocol: updateturn \t turn
			try {
				int id = Integer.parseInt(parts[1]);
				_localPlayer.updateTurn(id);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		} else {
			// A response was sent. Put it in the response map.
			String resp = "";
			for (int i = 1; i < parts.length; i++) {
				resp += parts[i];
				if (i < parts.length - 1) {
					resp += "\t";
				}
			}
			_responses.put(parts[0],resp);
		}
	}
	
	/**
	 * Runs this VirtualCore.
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
