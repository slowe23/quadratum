package net.quadratum.network;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.MapData;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.Point;

public class NetworkPlayer implements Player {
	
	/** Core that this Player belongs to. */
	Core _core;
	
	/** Player ID of this Player. */
	int _playerID;
	
	/** Socket that connects to the actual player. */
	Socket _sockToPlayer;

	@Override
	public void start(Core core, int id, MapData mapData, int otherPlayers, List<Piece> pieces) {
		_core = core;
		_playerID = id;
		
		// TODO protocol

	}

	@Override
	public void end(int[] stats) {
		// TODO protocol
		
		// We can close the socket now because the game is over.
		try {
			_sockToPlayer.close();
		} catch (IOException e) {
			// TODO error message?
			e.printStackTrace();
		}
	}

	@Override
	public void lost() {
		// TODO protocol
	}

	@Override
	public void turnStart() {
		// TODO protocol
	}

	@Override
	public void updateMap(Map<Point, Integer> units, int resources, Action lastAction) {
		// TODO protocol
	}

	@Override
	public void chatMessage(int from, String message) {
		// TODO protocol
	}

}
