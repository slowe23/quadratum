package net.quadratum.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class MainMenuPanel extends JPanel{

	private final String _title = "Main Menu";
	private JButton _singlePlayerBtn, _multiPlayerBtn, _helpBtn, _quitBtn;
	
	
	public MainMenuPanel(ActionListener al) {
		super(new BorderLayout());
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		JPanel holder = new JPanel();
		
		// Use vertical layout for buttons
		holder.setLayout(new BoxLayout(holder, BoxLayout.Y_AXIS));
		
		//Title
		JLabel title = new JLabel(_title);
		title.setAlignmentX(Component.CENTER_ALIGNMENT);
		holder.add(title);
		
		// Create buttons
		JButton[] buttons = new JButton[3];
		
		_singlePlayerBtn = new JButton("New game - single player");
		buttons[0] = _singlePlayerBtn;
		_multiPlayerBtn = new JButton("New network game - multiplayer");
		buttons[1] = _multiPlayerBtn;
		_helpBtn = new JButton("Help");
		buttons[2] = _helpBtn;
		_quitBtn = new JButton("Quit");
		//buttons[3] = _quitBtn;
		//_helpBtn = new JButton("Help");
		
		// Initialize buttons
		_singlePlayerBtn.setActionCommand(MainConstants.SINGLE);
		_singlePlayerBtn.addActionListener(al);
		_singlePlayerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_multiPlayerBtn.setActionCommand(MainConstants.NETWORK);
		_multiPlayerBtn.addActionListener(al);
		_multiPlayerBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
//		_helpBtn.setActionCommand(MainConstants.HELP);
//		_helpBtn.addActionListener(al);
//		_helpBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_quitBtn.setActionCommand(MainConstants.QUIT);
		_quitBtn.addActionListener(al);
		_quitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		//_helpBtn.setActionCommand("help");
		//_helpBtn.addActionListener(al);
		
		// sizing prefs
		_singlePlayerBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		_multiPlayerBtn.setPreferredSize(MainConstants.BUTTON_DIM);
//		_helpBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		_quitBtn.setPreferredSize(MainConstants.BUTTON_DIM);
		
		// Add buttons
		holder.add(Box.createVerticalGlue());
		holder.add(_singlePlayerBtn);
		holder.add(Box.createRigidArea(new Dimension(0,10)));
		holder.add(_multiPlayerBtn);
//		holder.add(Box.createRigidArea(new Dimension(0,10)));
//		holder.add(_helpBtn);
		holder.add(Box.createRigidArea(new Dimension(0,10)));
		//add(_helpBtn);
		//holder.add(Box.createRigidArea(new Dimension(0,10)));
		holder.add(_quitBtn);
		holder.add(Box.createVerticalGlue());
		holder.add(Box.createRigidArea(new Dimension(0,50)));
	
		add(holder, BorderLayout.CENTER);
		//holder.setAlignmentY(CENTER_ALIGNMENT);
		
//		holder.validate();
//		int maxW = 0;
//		for(JButton b : buttons)
//			maxW = (b.getSize().width > maxW ? b.getSize().width : maxW);
//		System.out.println(maxW);
//		for(JButton b : buttons)
//			//b.setPreferredSize(new Dimension(maxW, MainConstants.BUTTON_DIM.height));
//			b.setMinimumSize(new Dimension(maxW, MainConstants.BUTTON_DIM.height));
		
//		// By default, do not go visible from within
//		setEnabled(false);
//		setVisible(false);
	}
	

}
