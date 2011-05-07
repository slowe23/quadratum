package net.quadratum.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class NetworkGamePanel extends JPanel {

	private final String _title = "New network game";
	private JButton _hostGameBtn, _joinGameBtn, _returnMainBtn;
	
	public NetworkGamePanel (ActionListener al) {
		super(new BorderLayout());
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		JPanel holder = new JPanel();
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
		
		// Simple vertical layout to dispaly options
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//create buttons
		_hostGameBtn = new JButton("Host a game");
		_joinGameBtn = new JButton("Join a game");
		_returnMainBtn = new JButton("Return to main menu");
		
		//init buttons
		_hostGameBtn.setActionCommand(MainConstants.HOST);
		_hostGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_hostGameBtn.addActionListener(al);
		_joinGameBtn.setActionCommand(MainConstants.JOIN);
		_joinGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_joinGameBtn.addActionListener(al);
		_returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		_returnMainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_returnMainBtn.addActionListener(al);
		
		//sizing
		_hostGameBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		_joinGameBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		_returnMainBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		
		//add buttons
		JLabel title = new JLabel(_title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		holder.add(title);
		holder.add(Box.createVerticalGlue());
		holder.add(_hostGameBtn);
		holder.add(Box.createRigidArea(new Dimension(0,10)));
		holder.add(_joinGameBtn);
		holder.add(Box.createRigidArea(new Dimension(0,10)));
		holder.add(_returnMainBtn);
		holder.add(Box.createVerticalGlue());
		
		add(holder, BorderLayout.CENTER);
		
		// By default, do not go visible from within
		setEnabled(false);
		setVisible(false);
	}
	
}
