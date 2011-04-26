package net.quadratum.gui;

import java.awt.Color;

public interface MessageDisplay {
	public void newMessage(String str, Color col);
	public void clearMessages();
	
	public void showMessages();
	public void hideMessages();
	public void setShowMessages(boolean b);
	public boolean getShowMessages();
}