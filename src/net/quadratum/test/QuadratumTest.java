package net.quadratum.test;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import net.quadratum.core.*;
import net.quadratum.main.*;
import net.quadratum.network.*;
import net.quadratum.gui.*;

public class QuadratumTest implements Main
{
	static int _port = 9600;
	public static void main(String[] args)
	{
		try
		{
			// Make some players.
			Player player1 = new TestAI();
			Player player2 = new NetworkTestAI();
			ArrayList<Piece> pieces = new ArrayList<Piece>();
			Block awesomeBlock = new Block(10);
			awesomeBlock._bonuses.put(Block.BonusType.ATTACK, 1000);
			awesomeBlock._bonuses.put(Block.BonusType.RANGE, Constants.ATTACK_RANGE_MODIFIER);
			awesomeBlock._bonuses.put(Block.BonusType.SIGHT, Constants.SIGHT_MODIFIER * 5);
			awesomeBlock._bonuses.put(Block.BonusType.MOVEMENT, Constants.MOVEMENT_MODIFIER);
			Piece awesomePiece = new Piece(10, "Awesome Piece", "Provides awesome");
			awesomePiece.addBlock(new MapPoint(0, 0), new Block(awesomeBlock));
			pieces.add(awesomePiece);
			
			Block waterBlock = new Block(10);
			waterBlock._bonuses.put(Block.BonusType.WATER_MOVEMENT, 1);
			Piece waterPiece = new Piece(10, "Water Block", "Provides water movement");
			waterPiece.addBlock(new MapPoint(0, 0), new Block(waterBlock));
			pieces.add(waterPiece);
			// Create a core.
			GameCore core = new GameCore(new QuadratumTest(), "maps/minitest.qmap", new CheckWinner(), pieces);
			core.addPlayer(player1, "AI Player", 1, 20);
			core.addPlayer(player2, "Network AI Player", 2, 150);
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