package net.quadratum.test;

import java.util.ArrayList;

import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.main.CheckWinner;
import net.quadratum.main.Main;

public class TestMain2 implements Main {
	public static void main(String[] args)
	{
		Player player1 = new TestAI_MTC();
		Player player2 = new GUIPlayer();
		GameCore core = new GameCore(new TestMain2(), "null", new CheckWinner(), new ArrayList<Piece>());
		core.addPlayer(player1, "Test Player 1", 5);
		core.addPlayer(player2, "Test Player 2", 5);
		core.startGame();
	}

	@Override
	public void returnControl() {
		System.out.println("Done.");
		System.exit(0);
	}
}