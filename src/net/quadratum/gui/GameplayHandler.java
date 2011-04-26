package net.quadratum.gui;

import net.quadratum.core.*;

public interface GameplayHandler {
	public void setMapPanel(MapPanel mapPanel);
	/*public void setUnitImagePanel(UnitImagePanel unitImagePanel);
	public void setUnitInfoArea(JTextArea unitInfo);
	public void setMyTabbedPanel(MyTabbedPanel myTabbedPanel);
	public void setUnitPanel(UnitPanel unitPanel);
	public void setBuildPanel(BuildPanel buildPanel);
	public void setObjectivesArea(JTextArea objectives);*/
	
	/**
	 * Notifies the appropriate components that a new game has started
	 */
	public void startGame(MapData m);
	
	/**
	 * Selects the given unit and notifies the appropriate components
	 */
	//public void selectUnit(Unit u);
}