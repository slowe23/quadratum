package net.quadratum.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;

import net.quadratum.core.Player;
import net.quadratum.network.NetworkPlayer;
import net.quadratum.network.PingThread;
import net.quadratum.network.Pingable;

/**
 * A thread that collects connections for the host.
 * @author Zircean
 *
 */
public class ServerThread extends Thread implements Pingable {

	/** List of connected sockets. */
	private List<Socket> _connectedSockets;
	/** List of conected players. */
	private List<NetworkPlayer> _connectedPlayers;
	
	/** Flag telling this ServerThread to keep listening for connections. */
	boolean _keepListening;
	/** Port this ServerThread listens on. */
	int _port = MainConstants.Defaults.PREFERRED_PORT;
	
	/**
	 * Constructor for ServerThread.
	 */
	public ServerThread() {
		_connectedSockets = new LinkedList<Socket>();
		_connectedPlayers = new LinkedList<NetworkPlayer>();
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
			return;
		}
		// Create a ping thread.
		new PingThread(this).start();
		// Listen loop
		while (_keepListening) {
			try {
				Socket sock = server.accept();
				addPlayer(sock);
			} catch (SocketTimeoutException e) {
				continue;
			} catch (IOException e) {
				return;
			} 
		}
		// We've stopped listening. Close the socket.
		try {
			server.close();
		} catch (IOException e) { }
	}
	
	/**
	 * Adds a player to the connected players list.
	 * @param sock the socket to make a player out of.
	 */
	private synchronized void addPlayer(Socket sock) {
		// Add the socket.
		_connectedSockets.add(sock);
		// Set the socket timeout.
		try {
			sock.setSoTimeout(MainConstants.SOCKET_TIMEOUT);
		} catch (SocketException e) {
			try {
				sock.close();
			} catch (IOException e1) { 
				// SCREW OFF
			}
		}
		_connectedPlayers.add(new NetworkPlayer(sock));
	}
	
	/**
	 * Gets a copy of the currently connected players list.
	 * @return a list of connected players
	 */
	public synchronized List<NetworkPlayer> getCurrentPlayers() {
		return new LinkedList<NetworkPlayer>(_connectedPlayers);
	}
	
	/**
	 * Kicks a player.
	 * @param p the player to kick
	 */
	public synchronized void kick(Player p) {
		int i = _connectedPlayers.indexOf(p);
		_connectedPlayers.remove(i);
		_connectedSockets.remove(i);
	}
	
	/**
	 * Returns the current number of connections.
	 * @return the number of connected players.
	 */
	public synchronized int getNumberOfConnections() {
		return _connectedPlayers.size();
	}
	
	/**
	 * Stops the ServerThread from listening and then gets the player list.
	 * @return the list of players that have connected.
	 */
	public synchronized List<NetworkPlayer> stopListening() {
		_keepListening = false;
		try {
			join();
		} catch (InterruptedException e) { }
		return _connectedPlayers;
	}
	
	@Override
	public synchronized void ping() {
		for (NetworkPlayer p : _connectedPlayers) {
			p.ping();
		}
	}

	@Override
	public synchronized boolean keepListening() {
		return _keepListening;
	}
	
	private synchronized void socketClosed(Socket sock) {
		int i = _connectedSockets.indexOf(sock);
		_connectedSockets.remove(i);
		_connectedPlayers.remove(i);
	}
	
	class ListenThread extends Thread {
		
		/** Socket to listen for pings on. */
		Socket _sock;
		
		ListenThread(Socket sock) {
			_sock = sock;
		}
		
		@Override
		public void run() {
			BufferedReader in = null;
			// Set up the reader
			try {
				in = new BufferedReader(
						new InputStreamReader(_sock.getInputStream()));
			} catch (IOException e) {
				socketClosed(_sock);
			}
			// Read loop
			while (_keepListening) {
				try {
					in.readLine();
				} catch (IOException e) {
					socketClosed(_sock);
				}
			}
		}
	}
}
