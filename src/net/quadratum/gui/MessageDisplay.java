package net.quadratum.gui;

import java.awt.Color;

public interface MessageDisplay {
	public void newMessage(int id, String message);
	public void newMessage(String message);
	public void clearMessages();
	
	public void showMessages();
	public void hideMessages();
	public void setShowMessages(boolean b);
	public boolean getShowMessages();
}