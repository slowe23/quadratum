package net.quadratum.network;

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

/**
 * A class that represents a player connected to the host on one of the client's
 * sides. Probably not necessary...
 * @author Zircean
 *
 */
public class VirtualPlayer implements Player {

	@Override
	public void start(Core core, MapData mapData, int id, int totalPlayers) {}

	@Override
	public void updatePieces(List<Piece> pieces) {}

	@Override
	public void end(GameStats stats) {}

	@Override
	public void lost() {}

	@Override
	public void turnStart() {}

	@Override
	public void updateMapData(MapData mapData) {}

	@Override
	public void updateMap(Map<MapPoint, Integer> units, Set<MapPoint> sight, Action lastAction) {}

	@Override
	public void chatMessage(int from, String message) {}

	@Override
	public void updateTurn(int turn) {}
}
