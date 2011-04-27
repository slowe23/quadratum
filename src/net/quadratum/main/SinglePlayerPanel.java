package net.quadratum.main;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class SinglePlayerPanel extends JPanel {
	
	private final String _title = "New game (single-player)";
	private JButton _campaignGameBtn, _quickPlayBtn, _returnMainBtn;
	
	public SinglePlayerPanel (ActionListener al) {
		super();
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		// Vertical layout for button options
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// Create buttons
		_campaignGameBtn = new JButton("Continue campaign");
		_quickPlayBtn = new JButton("Quick play game");
		_returnMainBtn = new JButton("Return to main menu");
		
		//init buttons
		_campaignGameBtn.setActionCommand(MainConstants.CAMPAIGN);
		_campaignGameBtn.addActionListener(al);
		_quickPlayBtn.setActionCommand(MainConstants.QUICKPLAY);
		_quickPlayBtn.addActionListener(al);
		_returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		_returnMainBtn.addActionListener(al);
		
		//add buttons
		add(new JLabel(_title));
		add(_campaignGameBtn);
		add(_quickPlayBtn);
		add(_returnMainBtn);
		
		// By default, do not go visible from within
		setEnabled(false);
		setVisible(false);
	}

}
