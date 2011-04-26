package net.quadratum.network;

import java.util.List;
import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.GameStats;
import net.quadratum.core.MapData;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;

public class VirtualPlayer implements Player {

	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {

	}

	@Override
	public void updatePieces(List<Piece> pieces) {

	}

	@Override
	public void end(GameStats stats) {

	}

	@Override
	public void lost() {

	}

	@Override
	public void turnStart() {

	}

	@Override
	public void updateMapData(MapData mapData) {

	}

	@Override
	public void updateMap(Map<MapPoint, Integer> units, Action lastAction) {

	}

	@Override
	public void chatMessage(int from, String message) {

	}

}
