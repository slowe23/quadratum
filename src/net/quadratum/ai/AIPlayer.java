package net.quadratum.ai;

import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;

public abstract class AIPlayer implements Player {
	
	/** Core that this AIPlayer belongs to. */
	Core _core;
	/** Map terrain. */
	int[][] _terrain;
	/** Player ID. */
	int _id;
	/** Total number of players. */
	int _totalPlayers;
	
	/** Unit IDs */

	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		_terrain = mapData._terrain;
		_id = id;
		_totalPlayers = totalPlayers;
	}

	@Override
	public void updatePieces(List<Piece> pieces) { }

	@Override
	public void end(GameStats stats) { }

	@Override
	public void lost() { }

	@Override
	public void updateMapData(MapData mapData) { }

	@Override
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {
		if (lastAction._action == Action.ActionType.MOVE) { 
			// should actually be unit_created
			
		}
	}

	@Override
	public void chatMessage(int from, String message) { }

	@Override
	public void updateTurn(int turn) { }

}
