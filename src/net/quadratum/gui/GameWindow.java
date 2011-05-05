package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import net.quadratum.core.*;

public class GameWindow extends JFrame implements WindowListener {
	private GUIPlayer _guiPlayer;
	
	public GameWindow(GUIPlayer player, ChatHandler chatHandler) {
		setTitle("Quadratum");
		setSize(800, 600);
		setResizable(false);
		
		addWindowListener(this);
		
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
		JPanel objectives = new JPanel();
		objectives.setBorder(StaticMethods.getTitleBorder("Game Objectives"));
		objectives.setLayout(new FillLayout());
		StaticMethods.STD objSTD = StaticMethods.createScrollingTextDisplay(5);
		objSTD._jta.setText("-Win the game.\n-Don't lose.");
		objectives.add(objSTD._jsp);
		infoArea.addTab("Objectives", objectives, true);
		
		//Add chat tab
		ChatPanel chat = new ChatPanel(chatHandler, msg);
		infoArea.addTab("Chat", chat);
		
		constraints = new LineConstraints(0.4);
		controls.add(infoArea, constraints);
		
		content.add(controls, BorderLayout.SOUTH);
		
		_guiPlayer.setStuff(map, uInfo, uImg, units, pieces, buttons);
	}
	
	public void windowDeactivated(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
	
	public void windowClosing(WindowEvent e) {
		//TODO: some kind of confirmation?
		_guiPlayer.closing();
		setVisible(false);
	}
}