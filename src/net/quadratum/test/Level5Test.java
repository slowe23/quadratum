package net.quadratum.test;

import net.quadratum.core.GameCore;
import net.quadratum.core.Player;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.main.Main;
import net.quadratum.gamedata.Level;
import net.quadratum.gamedata.Level3;
import net.quadratum.gamedata.Level5;

public class Level5Test implements Main {
	public static void main(String[] args)
	{
		Player player = new GUIPlayer();
		Level level = new Level5();
		GameCore core = new GameCore(new Level5Test(), level.getMap(),
				level.getWinCondition(), level.getPieces());
		core.addPlayer(player, "Local Player", level.getMaxUnits(), level.getStartingResources());
		core.addPlayer(level.getAI(), "AI Player", Integer.MAX_VALUE, Integer.MAX_VALUE);
		core.startGame();
	}

	@Override
	public void returnControl() {
		System.out.println("Done.");
		System.exit(0);
	}
}