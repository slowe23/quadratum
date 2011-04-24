package net.quadratum.network;

import java.util.Map;

import net.quadratum.core.Action;
import net.quadratum.core.Core;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.Point;

public class VirtualCore implements Core {

	@Override
	public void addPlayer(Player p, String playerName) {


	}

	@Override
	public void start() {


	}

	@Override
	public void ready(int id) {


	}

	@Override
	public void endTurn(int id) {


	}

	@Override
	public boolean unitAction(int id, int unitID, Point coords) {

		return false;
	}

	@Override
	public Map<Point, Action.ActionType> getValidActions(int id, int unitID) {

		return null;
	}

	@Override
	public void quit(int id) {


	}

	@Override
	public void sendChatMessage(int id, String message) {


	}

	@Override
	public boolean placeUnit(int id, Point coords) {

		return false;
	}

	@Override
	public boolean updateUnit(int id, int unitID, Piece piece) {

		return false;
	}

	@Override
	public String getPlayerName(int player) {

		return null;
	}

}
