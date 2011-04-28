package net.quadratum.network;

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

public class VirtualCore extends NetworkClient implements Core {
	
	/** Local player. */
	Player _localPlayer;
	/** Local player's information. */
	PlayerInformation _localInfo;
	
	/** True if the game is over. */
	boolean _done;
	
	/** Nonlocal players. */
	List<VirtualPlayer> _virtualPlayers;
	/** Nonlocal player information. */
	// Should actual ID be stored in here?
	List<PlayerInformation> _virtualInfo;
	
	/** Used for response storage. */
	Map<String,String> _responses;
	
	// CACHEING ----------------------------
	/** Maps number to player name. */
	Map<Integer,String> _playerNames;
	
	public VirtualCore(Socket sock) {
		super(sock);
		
		_done = false;
		
		_responses = Collections.synchronizedMap(new HashMap<String,String>());
		
		_playerNames = new HashMap<Integer,String>();
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
	public void startGame() {
		// Start this thread, so it can listen for messages.
		new ReadThread().start();
	}

	@Override
	public void ready(Player p) {
		write("ready\n");
	}

	@Override
	public void endTurn(Player p) {
		write("endturn\n");
	}

	@Override
	public boolean unitAction(Player p, int unitID, MapPoint coords) {
		write("unitaction\t"+unitID+"\t"+coords._x+"\t"+coords._y+"\n");
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
		write("getvalidactions\t"+unitID+"\n");
		// protocol: <validactions \t> id \t mapobject
		String[] s = getResponse("validactions");
		return Serializer.<HashMap<MapPoint,ActionType>>getObject(s[1]);
	}

	@Override
	public void quit(Player p) {
		write("quit\n");
	}

	@Override
	public void sendChatMessage(Player p, String message) {
		write("chat\t"+message+"\n");
	}

	@Override
	public int placeUnit(Player p, MapPoint coords, String name) {
		write("placeunit\t"+coords._x+"\t"+coords._y+"\t"+name+"\n");
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
		write("getremainingunits\n");
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
		write("getunit\t"+unitID+"\n");
		// protocol: <unit \t> id \t unitobject
		String[] s = getResponse("unit");
		return Serializer.<Unit>getObject(s[1]);
	}

	@Override
	public boolean updateUnit(Player p, int unitID, int pieceID, MapPoint coords) {
		write("updateunit\t"+unitID+"\t"+pieceID+"\t"+coords._x+"\t"+coords._y+"\n");
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
		if (_playerNames.containsKey(player)) {
			return _playerNames.get(player);
		} else {
			// trivial to cache
			write("getplayername\t"+player+"\n");
			// protocol: <playername \t> id \t name
			String[] s = getResponse("playername");
			_playerNames.put(player,s[1]);
			return s[1];
		}
	}

	@Override
	public int getResources(Player p) {
		write("getresources\n");
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
	
	@Override
	public boolean done() {
		return _done;
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
	@Override
	protected void process(String message) {
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
			_done = true;
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

	@Override
	protected boolean doneReading() {
		return _done;
	}
}
