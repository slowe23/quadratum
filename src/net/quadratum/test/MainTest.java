package net.quadratum.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;

import net.quadratum.ai.test.*;
import net.quadratum.core.*;
import net.quadratum.gui.*;
import net.quadratum.main.*;
import net.quadratum.network.*;

public class MainTest implements Main
{
	static int _port = 9600;
	public static void main(String[] args)
	{
		try
		{
			ServerThread server = new ServerThread(_port);
			server.start();
			// Make some players.
			Player player1 = new GUIPlayer();
			Player player2 = new TestAI_MTC();
			Player player3 = new TestAI_MTC();
			ArrayList<Piece> pieces = new ArrayList<Piece>();
			Block attackBlock = new Block(30);
			attackBlock._bonuses.put(Block.BonusType.ATTACK, 10);
			Piece lPiece = new Piece(100, -1, "L Block", "Provides +40 attack");
			lPiece._blocks.put(new MapPoint(0, 0), new Block(attackBlock));
			lPiece._blocks.put(new MapPoint(0, 1), new Block(attackBlock));
			lPiece._blocks.put(new MapPoint(0, 2), new Block(attackBlock));
			lPiece._blocks.put(new MapPoint(1, 2), new Block(attackBlock));
			pieces.add(lPiece);
			// Create a core.
			GameCore core = new GameCore(new MainTest(), "src/net/quadratum/test/test.qmap", new CheckWinner(), pieces);
			core.addPlayer(player1, "GUI Player", 5);
			core.addPlayer(player2, "AI Player", 5);
			createNetworkPlayer(player3, "Network AI Player", 5);
			for (Player p : server.stopListening()) {
				// XXX Massively hacky
				core.addPlayer(p, "Network AI Player", 5);
			}
			//core.addObserver(player3);
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
