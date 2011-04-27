package net.quadratum.gui;

import net.quadratum.core.Core;

public class ChatHandler {
	private GUIPlayer _player;
	private Core _core;
	
	private ChatPanel _chat;
	
	public ChatHandler(GUIPlayer player) {
		_player = player;
	}
	
	public void setChatPanel(ChatPanel chien) {
		if(_chat==null)
			_chat = chien;
		else
			throw new RuntimeException("ChatHandler.setChatPanel(ChatPanel) should only be called once.");
	}
	
	public void start(Core core) {
		_core = core;
		_chat.start();
	}
	
	public void sendMessage(String message) {
		if(_core!=null)
			_core.sendChatMessage(_player, message);
	}
	
	public void getMessage(int id, String message) {
		if(_chat!=null) {
			_chat.addMessage(id, message);
			_chat.repaint();
		}
	}
	
	public void sendInternalMessage(String message) {
		if(_chat!=null) {
			_chat.addMessage(message);
			_chat.repaint();
		}
	}
	
	public String getPlayerName(int id) {
		if(_core!=null)
			return _core.getPlayerName(id);
		else
			return "";
	}
}