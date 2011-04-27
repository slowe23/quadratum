package net.quadratum.ai;

import java.util.ArrayList;
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
	protected Core _core;
	/** Map terrain. */
	protected int[][] _terrain;
	/** Player ID. */
	protected int _id;
	/** Total number of players. */
	protected int _totalPlayers;
	
	/** Unit IDs */
	List<Integer> _unitIDs;

	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		_terrain = mapData._terrain;
		_id = id;
		_totalPlayers = totalPlayers;
		
		_unitIDs = new ArrayList<Integer>();
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
		// AIs should keep track of their own units.
		if (lastAction._action == Action.ActionType.UNIT_CREATED) {
			_unitIDs.clear();
			for (int i : units.values()) {
				if (_core.getUnit(this,i) != null) {
					// this AI owns this unit
					_unitIDs.add(i);
				}
			}
		}
	}

	@Override
	public void chatMessage(int from, String message) { }

	@Override
	public void updateTurn(int turn) { }

}
