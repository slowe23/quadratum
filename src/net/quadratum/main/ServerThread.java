package net.quadratum.main;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.quadratum.core.Player;
import net.quadratum.network.NetworkPlayer;
import net.quadratum.network.PingThread;
import net.quadratum.network.Pingable;

public class ServerThread extends Thread implements Pingable {

	/** List of connected sockets. */
	private List<Socket> _connectedSockets;
	/** List of conected players. */
	private List<NetworkPlayer> _connectedPlayers;
	
	/** List of output writers. */
	private List<Writer> _outs;
	
	/** Flag telling this ServerThread to keep listening for connections. */
	boolean _keepListening;
	/** Port this ServerThread listens on. */
	int _port = MainConstants.Defaults.PREFERRED_PORT;
	
	public ServerThread() {
		_connectedSockets = new LinkedList<Socket>();
		_connectedPlayers = new LinkedList<NetworkPlayer>();
		
		_outs = new LinkedList<Writer>();
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
	 * @param sock the socket to make a player out of.
	 */
	private synchronized void addPlayer(Socket sock) {
		// Add the socket.
		_connectedSockets.add(sock);
		// Add the output stream.
		try {
			_outs.add(new BufferedWriter(new OutputStreamWriter(sock.getOutputStream())));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return _connectedPlayers;
	}
	
	@Override
	public synchronized void ping() {
		for (NetworkPlayer p : _connectedPlayers) {
			p.ping();
		}
	}

	@Override
	public boolean keepListening() {
		return _keepListening;
	}
}
