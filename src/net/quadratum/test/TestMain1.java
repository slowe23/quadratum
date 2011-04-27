package net.quadratum.test;

import java.util.ArrayList;

import net.quadratum.ai.AIPlayer;
import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.main.CheckWinner;

public class TestMain1 {
	public static void main(String[] args)
	{
		AIPlayer player1 = new TestAI_MTC();
		AIPlayer player2 = new TestAI_MTC();
		GameCore core = new GameCore("null", new CheckWinner(), new ArrayList<Piece>());
		core.addPlayer(player1, "Test Player 1", 5);
		core.addPlayer(player2, "Test Player 2", 5);
		core.start();
	}
}