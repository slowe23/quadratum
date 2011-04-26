package net.quadratum.gui.test;

import net.quadratum.gui.GUIPlayer;

public class GUITests {

	public static void main(String[] args) {
		GUIPlayer pl = new GUIPlayer();
		pl.createWindow();
		pl.start(new TestingCore(), null, 0, 1);
	}
}