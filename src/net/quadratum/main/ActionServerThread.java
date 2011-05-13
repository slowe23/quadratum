package net.quadratum.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.List;

import net.quadratum.network.NetworkPlayer;

public class ActionServerThread extends ServerThread {

	private ActionListener _listener;
	private int _pCount = 0;
	
	public ActionServerThread(ActionListener al) {
		super();
		_listener = al;
	}
	
	public ActionServerThread(int port, ActionListener al) {
		super(port);
		_listener = al;
	}
	
	protected synchronized void addPlayer(Socket sock) {
		int n = getNumberOfConnections();
		super.addPlayer(sock);
		if(getNumberOfConnections() > n)
			_listener.actionPerformed(new ActionEvent(this, _pCount++, "PLAYER_ADDED"));
	}
	
	public synchronized NetworkPlayer getNewestPlayer() {
		if(getNumberOfConnections() > 0) {
			List<NetworkPlayer> players = getCurrentPlayers();
			return players.get(players.size()-1);
		}
		return null;
	}
	
	protected synchronized int socketClosed(Socket sock) {
		int i = super.socketClosed(sock);
		_listener.actionPerformed(new ActionEvent(this, i, "PLAYER_DISCONNECTED"));
		return i;
	}
}
