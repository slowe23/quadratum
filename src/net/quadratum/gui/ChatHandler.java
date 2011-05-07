package net.quadratum.gui;

import net.quadratum.core.Core;

public class ChatHandler {
	private GUIPlayer _player;  //The nominal sender of messages
	private Core _core;
	
	private ChatPanel _chat;
	
	public ChatHandler(GUIPlayer player) {
		_player = player;
	}
	
	public void setChatPanel(ChatPanel chien) {
		_chat = chien;
	}
	
	public void start(Core core) {
		_core = core;
		_chat.start();
	}
	
	public void incomingMessage(int id, String message) {
		if(_chat != null)
		{
			_chat.addMessage(id, message);
		}
	}
	
	public void outgoingMessage(String message) {
		_core.sendChatMessage(_player, message);
	}
	
	public String getPlayerName(int id) {
		return _core.getPlayerName(id);
	}
}