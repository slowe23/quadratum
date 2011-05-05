package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ButtonsPanel extends Container {
	private GUIPlayer _guiPlayer;
	private JButton _start, _endTurn;
	private JPanel _right;
	private JLabel _resources, _units;
	
	public ButtonsPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		setLayout(new BorderLayout());
		
		_right = new JPanel();
		_right.setLayout(new BorderLayout());
		
		ActionListener aL = new ButtonsPanelActionListener();
		_start = new JButton("Start Game");
		_start.addActionListener(aL);
		_start.setEnabled(false);
		_right.add(_start, BorderLayout.EAST);
		
		_units = new JLabel("Units to place:");
		_units.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));
		_right.add(_units, BorderLayout.WEST);
		
		add(_right, BorderLayout.EAST);
		
		_endTurn = new JButton("End Turn");
		_endTurn.addActionListener(aL);
		_endTurn.setOpaque(true);
		_endTurn.setEnabled(false);
		
		_resources = new JLabel("Resources:");
		_resources.setOpaque(true);
		_resources.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));
		add(_resources, BorderLayout.WEST);
	}
	
	public void updateResources(int r) {
		_resources.setText("Resources: "+r);
		validate();
		repaint();
	}
	
	public void updateToPlace(int p) {
		_units.setText("Units to place: "+p);
		validate();
		repaint();
	}
	
	public void start(int p) {
		_start.setEnabled(true);
		updateToPlace(p);
	}
	
	public void gameStart() {
		System.out.println("Game start.");
		remove(_right);
		add(_endTurn, BorderLayout.EAST);
		validate();
		repaint();
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