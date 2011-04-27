package net.quadratum.main;

import java.util.ArrayList;

import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.gui.GUIPlayer;

public class Main {
	public static void main(String[] args)
	{
		GUIPlayer player = new GUIPlayer();
		GameCore core = new GameCore("null", new CheckWinner(), new ArrayList<Piece>());
		core.addPlayer(player, "Test Player", 10);
		core.start();
	}
}