package net.quadratum.main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class NetworkGamePanel extends JPanel {

	private final String _title = "New network game";
	private JButton _hostGameBtn, _joinGameBtn, _returnMainBtn;
	
	public NetworkGamePanel (ActionListener al) {
		super();
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		
		// Simple vertical layout to dispaly options
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create buttons
		_hostGameBtn = new JButton("Host a game");
		_joinGameBtn = new JButton("Join a game");
		_returnMainBtn = new JButton("Return to main menu");
		
		//init buttons
		_hostGameBtn.setActionCommand(MainConstants.HOST);
		_hostGameBtn.addActionListener(al);
		_joinGameBtn.setActionCommand(MainConstants.JOIN);
		_joinGameBtn.addActionListener(al);
		_returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		_returnMainBtn.addActionListener(al);
		
		//add buttons
		add(new JLabel(_title));
		add(_hostGameBtn);
		add(_joinGameBtn);
		add(_returnMainBtn);
		
		// By default, do not go visible from within
		setEnabled(false);
		setVisible(false);
	}
	
}
