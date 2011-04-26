package net.quadratum.network;

import java.util.Map;

import net.quadratum.core.Action.ActionType;
import net.quadratum.core.Core;
import net.quadratum.core.Player;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Unit;

public class VirtualCore implements Core {

	@Override
	public void addPlayer(Player p, String playerName, int maxUnits) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {

	}

	@Override
	public void ready(Player p) {
		// TODO protocol
		
	}

	@Override
	public void endTurn(Player p) {
		// TODO protocol
		
	}

	@Override
	public boolean unitAction(Player p, int unitID, MapPoint coords) {
		// TODO protocol
		return false;
	}

	@Override
	public Map<MapPoint, ActionType> getValidActions(Player p, int unitID) {
		// TODO protocol
		return null;
	}

	@Override
	public void quit(Player p) {
		// TODO protocol
		
	}

	@Override
	public void sendChatMessage(Player p, String message) {
		// TODO protocol
		
	}

	@Override
	public boolean placeUnit(Player p, MapPoint coords) {
		// TODO protocol
		return false;
	}

	@Override
	public int getRemainingUnits(Player p) {
		// TODO protocol
		return 0;
	}

	@Override
	public Unit getUnit(Player p, int unitID) {
		// TODO protocol
		return null;
	}

	@Override
	public boolean updateUnit(Player p, int unitID, int pieceID, MapPoint coords) {
		// TODO protocol
		return false;
	}
	
	@Override
	public String getPlayerName(int player) {
		// TODO protocol
		return null;
	}

	@Override
	public int getResources(Player p) {
		// TODO protocol
		return 0;
	}

}
