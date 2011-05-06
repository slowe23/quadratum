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
		Block awesomeBlock = new Block(30);
		awesomeBlock._bonuses.put(Block.BonusType.ATTACK, 100);
		awesomeBlock._bonuses.put(Block.BonusType.RANGE, 100);
		awesomeBlock._bonuses.put(Block.BonusType.DEFENSE, 100);
		awesomeBlock._bonuses.put(Block.BonusType.SIGHT, 100);
		awesomeBlock._bonuses.put(Block.BonusType.MOVEMENT, 100);
		awesomeBlock._bonuses.put(Block.BonusType.WATER_MOVEMENT, 100);
		Piece awesomePiece = new Piece(10, -1, "Awesome Piece", "Provides awesome");
		awesomePiece._blocks.put(new MapPoint(0, 0), new Block(awesomeBlock));
		awesomePiece._blocks.put(new MapPoint(0, 1), new Block(awesomeBlock));
		awesomePiece._blocks.put(new MapPoint(1, 0), new Block(awesomeBlock));
		awesomePiece._blocks.put(new MapPoint(1, 1), new Block(awesomeBlock));
		pieces.add(awesomePiece);
		// core
		GameCore core = new GameCore(new MainTest2(),
				"maps/smalltest.qmap",
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