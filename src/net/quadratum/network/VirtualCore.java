package net.quadratum.network;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Map;

import net.quadratum.core.Action.ActionType;
import net.quadratum.core.Core;
import net.quadratum.core.Player;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Unit;

public class VirtualCore implements Core {
	
	/** Socket that connects to the actual player. */
	Socket _sockToServer;
	/** Buffered writer to the player. */
	BufferedWriter _out;
	/** Buffered reader from the player. */
	BufferedReader _in;
	
	public VirtualCore(Socket sock) {
		_sockToServer = sock;
		try {
			_out = new BufferedWriter(new OutputStreamWriter(_sockToServer.getOutputStream()));
			_in = new BufferedReader(new InputStreamReader(_sockToServer.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void addPlayer(Player p, String playerName, int maxUnits) {
		// TODO Auto-generated method stub
		
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
		// TODO get return val
		return false;
	}

	@Override
	public Map<MapPoint, ActionType> getValidActions(Player p, int unitID) {
		// TODO protocol/caching
		return null;
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
	public boolean placeUnit(Player p, MapPoint coords) {
		try {
			_out.write("placeunit\t"+coords._x+"\t"+coords._y+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO get return val
		return false;
	}

	@Override
	public int getRemainingUnits(Player p) {
		// this should be cached
		try {
			_out.write("getremainingunits\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO get return val
		return 0;
	}

	@Override
	public Unit getUnit(Player p, int unitID) {
		// this should be cached also
		try {
			_out.write("getunit\t"+unitID+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO get return val
		return null;
	}

	@Override
	public boolean updateUnit(Player p, int unitID, int pieceID, MapPoint coords) {
		try {
			_out.write("updateunit\t"+unitID+"\t"+pieceID+"\t"+coords._x+"\t"+coords._y+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO get return val
		return false;
	}
	
	@Override
	public String getPlayerName(int player) {
		// trivial to cache
		try {
			_out.write("getplayername\t"+player+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO get return val
		return null;
	}

	@Override
	public int getResources(Player p) {
		try {
			_out.write("getresources\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		// TODO get return val
		return 0;
	}

}
