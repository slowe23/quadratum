package net.quadratum.gui;

public interface MessageDisplay {
	public void newMessage(String str);
	public void clearMessages();
	
	public void showMessages();
	public void hideMessages();
	public void setShowMessages(boolean b);
	public boolean getShowMessages();
}