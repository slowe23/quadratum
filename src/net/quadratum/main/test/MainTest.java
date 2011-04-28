package net.quadratum.main.test;

import java.util.ArrayList;

import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.main.Main;

public class MainTest implements Main {
	
	public static void main(String[] args)
	{
		GUIPlayer player = new GUIPlayer();
		TestAI_MTC ai = new TestAI_MTC();
		GameCore core = new GameCore(new MainTest(), "null", new CheckWinnerTest(), new ArrayList<Piece>());
		core.addPlayer(ai, "AI Player", 10);
		core.addPlayer(player, "Test Player", 10);
		core.start();
	}

	@Override
	public void returnControl() {
		System.out.println("Done.");
	}
}