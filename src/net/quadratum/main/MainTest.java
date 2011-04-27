package net.quadratum.main;

import net.quadratum.gui.*;
import net.quadratum.core.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Main {
	public static void main(String[] args)
	{
		GUIPlayer player = new GUIPlayer();
		GameCore core = new GameCore("null", new CheckWinner(), new ArrayList<Piece>());
		core.addPlayer(player, "Test Player", 10);
		core.start();
	}
}