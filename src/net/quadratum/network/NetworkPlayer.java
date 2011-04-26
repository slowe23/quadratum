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

public class NetworkPlayer implements Player {
	
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

}
