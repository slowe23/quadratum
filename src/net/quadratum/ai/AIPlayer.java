package net.quadratum.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
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
	protected List<Integer> _unitIDs;

	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {
		_core = core;
		_terrain = mapData._terrain;
		_id = id;
		_totalPlayers = totalPlayers;
		
		_unitIDs = new LinkedList<Integer>();
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
		if (lastAction._action == Action.ActionType.UNIT_DIED) {
			int id = -1;
			Collection<Integer> allIDs = units.values();
			for (Iterator<Integer> iter = _unitIDs.iterator(); iter.hasNext(); 
					id = iter.next()) {
				if (id != -1 && !allIDs.contains(id)) {
					iter.remove();
				}
			}
		}
	}

	@Override
	public void chatMessage(int from, String message) { }

	@Override
	public void updateTurn(int turn) { }

}
