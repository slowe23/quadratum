package net.quadratum.gui.test;

import net.quadratum.gui.GUIPlayer;
import net.quadratum.core.*;

import java.util.HashSet;

public class GUITests {

	public static void main(String[] args) {
		GUIPlayer pl = new GUIPlayer();
		Core c = new TestingCore();
		c.addPlayer(pl, "Test Player", 100);
		c.start();
	}
}