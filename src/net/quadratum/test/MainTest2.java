package net.quadratum.test;

import java.util.ArrayList;

import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.Block;
import net.quadratum.core.GameCore;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.main.CheckWinner;
import net.quadratum.main.Main;

public class MainTest2 implements Main {
	public static void main(String[] args)
	{
		Player player1 = new TestAI_MTC();
		Player player2 = new GUIPlayer();
		// piece
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		Block attackBlock = new Block(30);
		attackBlock._bonuses.put(Block.BonusType.ATTACK, 10);
		Piece lPiece = new Piece(10, -1, "L Block", "Provides +40 attack");
		lPiece._blocks.put(new MapPoint(0, 0), new Block(attackBlock));
		lPiece._blocks.put(new MapPoint(0, 1), new Block(attackBlock));
		lPiece._blocks.put(new MapPoint(0, 2), new Block(attackBlock));
		lPiece._blocks.put(new MapPoint(1, 2), new Block(attackBlock));
		pieces.add(lPiece);
		// core
		GameCore core = new GameCore(new MainTest2(),
				"src/net/quadratum/test/smalltest.qmap",
				new CheckWinner(), pieces);
		core.addPlayer(player1, "Test Player 1", 5, 100);
		core.addPlayer(player2, "Test Player 2", 5, 100);
		core.startGame();
	}

	@Override
	public void returnControl() {
		System.out.println("Done.");
		System.exit(0);
	}
}