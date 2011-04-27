package net.quadratum.main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class NetworkLobbyPanel extends JPanel {

	private final String _title = "Network Lobby";
	private final String NONE_AVAILABLE = "No games currently available.";
	
	private JLabel _connectionStatus;
	//private Component _chatBox;
	private Component _availableGameDisplay;
	private JButton _joinSelectedBtn, _returnNetworkBtn, _returnMainBtn;
	
	
	public NetworkLobbyPanel (ActionListener al) {
		super();
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		// layout
		//create buttons
		//init buttons
		//add buttons
		add(new JLabel(_title));
		
		// By default, do not go visible from within
		setEnabled(false);
		setVisible(false);
	}
	
	private void generateGameDisplay() {
		Component disp = new JLabel("Lobby display is not yet functional",
									SwingConstants.CENTER);
		disp.setSize(350, 300);
		_availableGameDisplay = disp;
	}
}
