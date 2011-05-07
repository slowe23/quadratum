package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.quadratum.core.*;

public class GameWindow extends JFrame {
	private GUIPlayer _guiPlayer;
	private Core _core;
	
	public GameWindow(GUIPlayer player, ChatHandler chatHandler) {
		setTitle("Quadratum");
		setSize(800, 600);
		setResizable(false);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new GameWindowWindowListener());
		
		_guiPlayer = player;
		
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		//Add a layered pane containing the map and other stuff
		JLayeredPane mapPane = new JLayeredPane();
		mapPane.setLayout(new FillLayout());
		
		//Add map display panel
		MapPanel map = new MapPanel(_guiPlayer);
		mapPane.add(map, new Integer(0));
		
		//Add message display
		MessageOverlay msg = new MessageOverlay(_guiPlayer);
		mapPane.add(msg, new Integer(1));
		
		//Add a strip at the top with buttons and stuff
		ButtonsPanel buttons = new ButtonsPanel(_guiPlayer);
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setOpaque(false);
		panel.add(buttons, BorderLayout.NORTH);
		mapPane.add(panel, new Integer(2));
		
		content.add(mapPane, BorderLayout.CENTER);
		
		//Create control panel
		JPanel controls = new JPanel();
		controls.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		LineConstraints constraints;
		
		//Add minimap panel
		constraints = new LineConstraints(0.2);
		controls.add(map._minimap, constraints);
		
		//Add selected unit panel
		JPanel sUPanel = new JPanel();
		sUPanel.setBorder(StaticMethods.getTitleBorder("Selected Unit"));
		sUPanel.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		//Add unit info panel
		UnitInfoPanel uInfo = new UnitInfoPanel(_guiPlayer);
		sUPanel.add(uInfo);
		
		//Add unit image panel
		UnitImagePanel uImg = new UnitImagePanel(_guiPlayer);
		sUPanel.add(uImg);
		
		constraints = new LineConstraints(0.4);
		controls.add(sUPanel, constraints);
		
		//Add tabbed info panel
		MyTabbedPanel infoArea = new MyTabbedPanel();
		
		//Add unit tab
		UnitsPanel units = new UnitsPanel(_guiPlayer);
		infoArea.addTab("Units", units);
		
		//Add pieces tab
		PiecesPanel pieces = new PiecesPanel(_guiPlayer, uImg);
		infoArea.addTab("Pieces", pieces);
		
		//Add objectives tab
		ObjectivesPanel objectives = new ObjectivesPanel();
		infoArea.addTab("Objectives", objectives, true);
		
		//Add chat tab
		ChatPanel chat = new ChatPanel(chatHandler, msg);
		infoArea.addTab("Chat", chat);
		
		constraints = new LineConstraints(0.4);
		controls.add(infoArea, constraints);
		
		content.add(controls, BorderLayout.SOUTH);
		
		_guiPlayer.setStuff(map, uInfo, uImg, units, pieces, buttons, objectives);
	}
	
	public void start(Core core) {
		_core = core;
	}
	
	public void end(GameStats stats) {
		JOptionPane.showMessageDialog(this, stats.toString(), "Game over.", JOptionPane.INFORMATION_MESSAGE);
		quit();
	}
	
	public void quit() {
		setVisible(false);
	}
	
	private class GameWindowWindowListener extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
			_guiPlayer.closing();
			quit();
		}
	}
}