package net.quadratum.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.main.CheckWinner;
import net.quadratum.main.Main;
import net.quadratum.main.ServerThread;
import net.quadratum.network.VirtualCore;

public class TestNetworkMain1 implements Main {
	
	static int _port = 9600;

	public static void main(String[] args)
	{
		try {
			ServerThread server = new ServerThread(_port);
			server.start();
			// Make some players.
			Player player1 = new TestAI_MTC();
			Player player2 = new TestAI_MTC();
			// Create a core.
			GameCore core = new GameCore(new TestNetworkMain1(), "null", new CheckWinner(), new ArrayList<Piece>());
			core.addPlayer(player1, "Test Player 1", 5);
			createNetworkPlayer(player2, "Test Player 2", 5);
			for (Player p : server.stopListening()) {
				// XXX Massively hacky
				core.addPlayer(p, "Test Player 2", 5);
			}
			core.startGame();
			while(!core.done()) {}
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static void createNetworkPlayer(Player p, String name, int maxUnits) {
		try {
			Socket sock = new Socket("localhost",_port);
			VirtualCore vc = new VirtualCore(sock);
			vc.addPlayer(p, name, maxUnits);
			vc.startGame();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void returnControl() {
		System.out.println("Done.");
		
	}
}
