package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.JTextArea;

public class GameplayHandler {
	public static final int PHASE_WAIT = 0, PHASE_PLACE = 1, PHASE_GAME = 2, PHASE_STATS = 3;
	
	private int _phase;
	
	private GUIPlayer _player;
	private Core _core;
	
	private MapPanel _mapPanel;
	private ChatHandler _chatHandler;
	private UnitHandler _unitHandler;
	private JTextArea _unitInfoArea;
	private UnitImagePanel _unitImagePanel;
	private UnitPanel _unitPanel;
	
	public GameplayHandler(GUIPlayer player) {
		_phase = PHASE_WAIT;
		
		_player = player;
	}
	
	public void setMapPanel(MapPanel mapPanel) {
		_mapPanel = mapPanel;
	}
	
	public void setChatHandler(ChatHandler chatHandler) {
		_chatHandler = chatHandler;
	}
	
	public void setUnitHandler(UnitHandler unitHandler) {
		_unitHandler = unitHandler;
	}
	
	public void setUnitInfoArea(JTextArea unitInfoArea) {
		_unitInfoArea = unitInfoArea;
	}
	
	public void setUnitImagePanel(UnitImagePanel unitImagePanel) {
		_unitImagePanel = unitImagePanel;
	}
	
	public void setUnitPanel(UnitPanel unitPanel) {
		_unitPanel = unitPanel;
	}
	
	public void start(Core c, MapData m) {
		_phase = PHASE_PLACE;
		
		_core = c;
		
		_chatHandler.start(c);
		_chatHandler.sendInternalMessage("Game started.");
		
		_unitInfoArea.setText(CM.getUnitDescription(null));
		_unitInfoArea.repaint();
		
		_mapPanel.start(m);
		_mapPanel.repaintBoth();
	}
	
	public void select(Unit u) {
		if(_phase==PHASE_GAME)
			select(u, _unitHandler.getUnitLocation(u));
	}
	
	public void select(Unit u, MapPoint point) {
		if(_phase==PHASE_GAME) {
			_unitHandler.setSelectedUnit(u);
			_unitInfoArea.setText(CM.getUnitDescription(u));

			if(point!=null)
				_mapPanel.scrollTo(point);
			
			_mapPanel.repaintBoth();
			_unitInfoArea.repaint();
			_unitImagePanel.repaint();
			_unitPanel.repaint();
		}
	}
	
	public int getPhase() {
		return _phase;
	}
}