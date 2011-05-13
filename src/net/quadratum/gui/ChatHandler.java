package net.quadratum.gui;

import net.quadratum.core.Core;

/** A class for handling messages between the GUIPlayer and the ChatPanel */
public class ChatHandler {
	private GUIPlayer _player;  //The nominal sender of messages
	private Core _core;
	
	private ChatPanel _chat;
	
	public ChatHandler(GUIPlayer player) {
		_player = player;
	}
	
	/** Provides a chat panel associated to this ChatHandler */
	public void setChatPanel(ChatPanel chien) {
		_chat = chien;
	}
	
	/** Provides a core for this ChatHandler and starts the chat panel */
	public void start(Core core) {
		_core = core;
		_chat.start();
	}
	
	/** Sends a message from the core or GUI player to the chat panel */
	public void incomingMessage(int id, String message) {
		_chat.addMessage(id, message);
	}
	
	/** Sends a message from the chat panel to the core */
	public void outgoingMessage(String message) {
		_core.sendChatMessage(_player, message);
	}
	
	/** Gets the name of a given player */
	public String getPlayerName(int id) {
		return _core.getPlayerName(id);
	}
}