package net.quadratum.test;

import net.quadratum.core.GameCore;
import net.quadratum.core.Player;
import net.quadratum.gamedata.Level;
import net.quadratum.gamedata.Tutorial;
import net.quadratum.gamedata.TutorialCore;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.main.Main;

public class TutorialTest implements Main {
	public static void main(String[] args)
	{
		Player player = new GUIPlayer();
		Level level = new Tutorial();
		GameCore core = new TutorialCore(new TutorialTest(), level.getMap(),
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