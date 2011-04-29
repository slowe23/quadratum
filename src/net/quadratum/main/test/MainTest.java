package net.quadratum.main.test;

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
			Player player1 = new TestAI_MTC();
			Player player2 = new TestAI_MTC();
			Player player3 = new GUIPlayer();
			HashSet<Integer> observers = new HashSet<Integer>();
			observers.add(new Integer(2));
			// Create a core.
			GameCore core = new GameCore(new MainTest(), "null", new CheckWinner(), new ArrayList<Piece>(), observers);
			core.addPlayer(player1, "AI Player", 5);
			createNetworkPlayer(player2, "Network AI Player", 5);
			for (Player p : server.stopListening()) {
				// XXX Massively hacky
				core.addPlayer(p, "Network AI Player", 5);
			}
			core.addPlayer(player3, "GUI Player", 1);
			core.startGame();
			try
			{
				Thread.sleep(1000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			core.sendChatMessage(player3, "Place a unit for the game to start!");
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
