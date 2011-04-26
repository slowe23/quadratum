package net.quadratum.gui;

import net.quadratum.core.*;

public class GameplayHandler {
	private GUIPlayer _player;
	private Core _core;
	
	private MapPanel _mapPanel;
	private MinimapPanel _minimapPanel;
	private ChatHandler _chatHandler;
	
	public GameplayHandler(GUIPlayer player) {
		_player = player;
	}
	
	public void setMapPanel(MapPanel mapPanel) {
		if(_mapPanel==null) {
			_mapPanel = mapPanel;
			_minimapPanel = mapPanel.getMinimapPanel();
		} else
			throw new RuntimeException("Map panel can only be set once.");
	}
	
	public void setChatHandler(ChatHandler chatHandler) {
		if(_chatHandler==null)
			_chatHandler = chatHandler;
		else
			throw new RuntimeException("Chat handler can only be set once.");
	}
	
	public boolean isReady() {
		return _mapPanel!=null && _minimapPanel!=null && _chatHandler!=null;
	}
	
	public void start(Core c, MapData m) {
		_core = c;
		
		if(!isReady())
			throw new RuntimeException("Started GameplayHandler without setting all necessary attributes.");
		
		_chatHandler.sendInternalMessage("Game started.");
		
		_mapPanel.start(m);
		_mapPanel.repaint();
		_minimapPanel.repaint();
	}
}