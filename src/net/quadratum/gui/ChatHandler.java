package net.quadratum.gui;

public interface ChatHandler {
	public void setChatPanel(ChatPanel chien);
	public void sendMessage(String message);
	public void getMessage(int id, String message);
	public String getPlayerName(int id);
}