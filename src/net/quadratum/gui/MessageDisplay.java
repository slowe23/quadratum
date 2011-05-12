package net.quadratum.gui;

/** An interface for something that displays chat messages */
public interface MessageDisplay {
	/** Creates a new message from the given player */
	public void newMessage(int id, String message);
	
	/** Clears all currently displayed messages */
	public void clearMessages();
	
	/** Turns on the message display */
	public void showMessages();
	
	/** Turns off the message display */
	public void hideMessages();
	
	/** Sets the visibility of the message display */
	public void setShowMessages(boolean b);
	
	/** Gets the visibility of the message display */
	public boolean getShowMessages();
}