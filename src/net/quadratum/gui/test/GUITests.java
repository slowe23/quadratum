package net.quadratum.gui.test;

import net.quadratum.gui.GUIPlayer;
import net.quadratum.core.*;

import java.util.HashSet;

public class GUITests {

	public static void main(String[] args) {
		GUIPlayer pl = new GUIPlayer();
		pl.createWindow();
		pl.start(new TestingCore(), new MapData(createRandomTerrain(), new HashSet<MapPoint>()), 0, 1);
	}
	
	private static int[][] createRandomTerrain() {
		return new int[30][40];
	}
}