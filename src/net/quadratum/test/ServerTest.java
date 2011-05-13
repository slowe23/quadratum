package net.quadratum.test;

import net.quadratum.core.GameCore;
import net.quadratum.core.Player;
import net.quadratum.gamedata.DefaultPieces;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.main.CheckWinner;
import net.quadratum.main.Main;
import net.quadratum.main.ServerThread;

public class ServerTest implements Main {
	static int _port = 9600;
	public static void main(String[] args)
	{
		try
		{
			ServerThread server = new ServerThread(_port);
			server.start();
			// Make some players.
			Player player1 = new GUIPlayer();
			// Create a core.
			GameCore core = new GameCore(new QuadratumNetworkTest(), "maps/smalltest.qmap", 
					new CheckWinner(), DefaultPieces.getPieces());
			core.addPlayer(player1, "Host Player", 5, 5000);
			while (server.getNumberOfConnections() < 1) { }
			for (Player p : server.stopListening()) {
				// XXX Massively hacky
				core.addPlayer(p, "Network Player", 5, 5000);
			}
			core.startGame();
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	@Override
	public void returnControl() {
		System.out.println("Done.");
		
	}
}
