package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonsPanel extends Container {
	private GUIPlayer _guiPlayer;
	private JButton _start, _endTurn;
	private JLabel _text;
	
	public ButtonsPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		setLayout(new BorderLayout());
		
		ActionListener aL = new ButtonsPanelActionListener();
		_start = new JButton("Start Game");
		_start.addActionListener(aL);
		_start.setEnabled(false);
		_start.setOpaque(true);
		add(_start, BorderLayout.EAST);
		
		_endTurn = new JButton("End Turn");
		_endTurn.addActionListener(aL);
		_endTurn.setOpaque(true);
		_endTurn.setEnabled(false);
		
		_text = new JLabel("");
		_text.setOpaque(true);
		_text.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
		add(_text, BorderLayout.WEST);
	}
	
	public void updateResources(int r) {
		_text.setText("Resources: "+r);
		validate();
		repaint();
	}
	
	public void updateToPlace(int p) {
		_text.setText("Units to place: "+p);
		if(p==0)
			_start.setEnabled(true);
		validate();
		repaint();
	}
	
	public void start(int p) {
		updateToPlace(p);
	}
	
	public void gameStart(int r) {
		remove(_start);
		add(_endTurn, BorderLayout.EAST);
		updateResources(r);
	}
	
	public void turn(boolean yours) {
		_endTurn.setEnabled(yours);
	}
	
	private class ButtonsPanelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==_start)
				_guiPlayer.placementDone();
			else
				_guiPlayer.turnDone();
		}
	}
}