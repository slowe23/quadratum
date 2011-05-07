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

public class SinglePlayerPanel extends JPanel {
	
	private final String _title = "New game (single-player)";
	private JButton _campaignGameBtn, _quickPlayBtn, _returnMainBtn;
	
	public SinglePlayerPanel (ActionListener al) {
		super(new BorderLayout());
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		JPanel holder = new JPanel();
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
		
		// Create buttons
		_campaignGameBtn = new JButton("Continue campaign");
		_quickPlayBtn = new JButton("Quick play game");
		_returnMainBtn = new JButton("Return to main menu");
		
		//init buttons
		_campaignGameBtn.setActionCommand(MainConstants.CAMPAIGN);
		_campaignGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_campaignGameBtn.addActionListener(al);
		_quickPlayBtn.setActionCommand(MainConstants.QUICKPLAY);
		_quickPlayBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_quickPlayBtn.addActionListener(al);
		_returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		_returnMainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_returnMainBtn.addActionListener(al);
		
		//sizing
		_campaignGameBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		_quickPlayBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		_returnMainBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		
		//add buttons
		holder.add(Box.createVerticalGlue());
		JLabel title = new JLabel(_title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		holder.add(title);
		holder.add(Box.createRigidArea(new Dimension(0,20)));
		holder.add(_campaignGameBtn);
		holder.add(Box.createRigidArea(new Dimension(0,10)));
		holder.add(_quickPlayBtn);
		holder.add(Box.createRigidArea(new Dimension(0,10)));
		holder.add(_returnMainBtn);

		holder.add(Box.createVerticalGlue());
		holder.add(Box.createRigidArea(new Dimension(0,50)));
		
		add(holder, BorderLayout.CENTER);
		
		// By default, do not go visible from within
		setEnabled(false);
		setVisible(false);
	}

}
