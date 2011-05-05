package net.quadratum.gui.test;

import net.quadratum.core.Core;
import net.quadratum.gui.GUIPlayer;

public class GUITests {

	public static void main(String[] args) {
		GUIPlayer pl = new GUIPlayer();
		Core c = new TestingCore();
		c.addPlayer(pl, "Test Player", 100, 100);
		c.startGame();
	}
}