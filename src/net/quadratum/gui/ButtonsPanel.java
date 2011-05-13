package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** A panel for displaying some text and buttons at the top of the window */
public class ButtonsPanel extends Container {
	private GUIPlayer _guiPlayer;
	private JButton _start, _endTurn, _forfeit;
	private JPanel _right1, _right2;
	private JLabel _resources, _units;
	
	public ButtonsPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		setLayout(new BorderLayout());
		
		ActionListener aL = new ButtonsPanelActionListener();
		
		_right1 = new JPanel();
		_right1.setLayout(new BorderLayout());
		
		_units = new JLabel("Units to place:");
		_units.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		_right1.add(_units, BorderLayout.WEST);
		
		_start = new JButton("Start Game");
		_start.addActionListener(aL);
		_start.setEnabled(false);
		_right1.add(_start, BorderLayout.EAST);
		
		add(_right1, BorderLayout.EAST);
		
		_right2 = new JPanel();
		_right2.setLayout(new BorderLayout());
		
		_endTurn = new JButton("End Turn");
		_endTurn.addActionListener(aL);
		_endTurn.setEnabled(false);
		_right2.add(_endTurn, BorderLayout.WEST);
		
		_forfeit = new JButton("Forfeit");
		_forfeit.addActionListener(aL);
		_forfeit.setEnabled(false);
		_right2.add(_forfeit, BorderLayout.EAST);
		
		_resources = new JLabel("Resources:");
		_resources.setOpaque(true);
		_resources.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
		add(_resources, BorderLayout.WEST);
	}
	
	/** Updates the number of resources displayed */
	public void updateResources(int r) {
		_resources.setText("Resources: "+r);
		validate();
		repaint();
	}
	
	/** Updates the number of units left to place displayed */
	public void updateToPlace(int p) {
		_units.setText("Units to place: "+p);
		validate();
		repaint();
	}
	
	/** Starts the game, setting an initial number of units to place */
	public void start(int p) {
		_start.setEnabled(true);
		updateToPlace(p);
	}
	
	/** Ends the placement phase and begins the main phase of gameplay */
	public void gameStart() {
		remove(_right1);
		add(_right2, BorderLayout.EAST);
		_forfeit.setEnabled(true);
		validate();
		repaint();
	}
	
	/** Enables or disables buttons according to whether it is the player's turn */
	public void turn(boolean yours) {
		_endTurn.setEnabled(yours);
	}
	
	/** Removes all components in response to the player's loss. */
	public void lost() {
		removeAll();
		validate();
		repaint();
	}
	
	/** Responds to button actions by sending messages to the GUIPlayer */
	private class ButtonsPanelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==_start)
				_guiPlayer.placementDone();
			else if(e.getSource()==_endTurn)
				_guiPlayer.turnDone();
			else if(e.getSource()==_forfeit) {
				_forfeit.setEnabled(false);
				_guiPlayer.forfeit();
			}
		}
	}
}