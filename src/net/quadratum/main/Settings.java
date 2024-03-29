package net.quadratum.main;

import net.quadratum.core.WinCondition;

public interface Settings {

	public boolean usingNetworkSettings();
	
	public boolean usingPresetMap();
	public String getPresetMap();
	public String getMap();
	
	//public boolean mapDimensionSet();
	public int getMapWidth();
	public int getMapHeight();
	
	public boolean isWaterAllowed();
	public boolean areBunkersAllowed();
	public boolean areMountainsAllowed();
	
	public int getStartingResources();
	public int getMaxUnits();
	
	public int numPlayers();
	public boolean areTurnsTimed();
	public int turnTimeLimit();
	public boolean durationSet();
	public int durationInTurns(); //perPlayer
	public boolean altWinCondition();
	public WinCondition getAltWinCon();
	public int getPort();
	public int getAiDifficulty();
}
