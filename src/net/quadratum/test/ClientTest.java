package net.quadratum.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import net.quadratum.gui.GUIPlayer;
import net.quadratum.network.VirtualCore;

public class ClientTest {
	
	public static void main(String[] args) {
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		Socket sock = null;
		try {
			sock = new Socket(hostname,port);
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		
		VirtualCore vc = new VirtualCore(sock);
		vc.addPlayer(new GUIPlayer(), "Local Player", 5, 5000);
		vc.startGame();
		
	}
}
