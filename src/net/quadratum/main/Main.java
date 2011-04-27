package net.quadratum.main;

import java.util.ArrayList;

import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.ai.test.TestAI_MTC;

public class Main {
	public static void main(String[] args)
	{
		GUIPlayer player = new GUIPlayer();
		TestAI_MTC ai = new TestAI_MTC();
		GameCore core = new GameCore("null", new CheckWinner(), new ArrayList<Piece>());
		core.addPlayer(player, "Test Player", 10);
		core.addPlayer(ai, "AI Player", 10);
		core.start();
	}
}