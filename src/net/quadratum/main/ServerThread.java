package net.quadratum.main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import net.quadratum.core.Player;
import net.quadratum.network.NetworkPlayer;

public class ServerThread extends Thread {

	/** List of conected players. */
	private List<Player> _connectedPlayers;
	/** Flag telling this ServerThread to keep listening for connections. */
	boolean _keepListening;
	/** Port this ServerThread listens on. */
	int _port = MainConstants.Defaults.PREFERRED_PORT;
	
	public ServerThread() {
		_connectedPlayers = new ArrayList<Player>();
	}
	
	public ServerThread(int port) {
		this();
		_port = port;
	}
	
	/**
	 * Runs this ServerThread. The ServerThread will listen for incoming
	 * connections and make NetworkPlayers out of them. 
	 */
	@Override
	public void run() {
		_keepListening = true;
		ServerSocket server;
		// Create the ServerSocket that everything will connect to.
		try {
			server = new ServerSocket(_port);
			server.setSoTimeout(1000);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		// Listen loop
		while (_keepListening) {
			try {
				Socket sock = server.accept();
				addPlayer(sock);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		// We've stopped listening. Close the socket.
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a player to the connected players list.
	 * @param p
	 */
	private synchronized void addPlayer(Socket sock) {
		_connectedPlayers.add(new NetworkPlayer(sock));
	}
	
	/**
	 * Stops the ServerThread from listening and then gets the player list.
	 * @return the list of players that have connected.
	 */
	public synchronized List<Player> stopListening() {
		_keepListening = false;
		try {
			join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return _connectedPlayers;
	}
}
