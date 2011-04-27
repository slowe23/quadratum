package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameWindow extends JFrame implements WindowListener {
	public GameWindow(GUIPlayer player) {
		setTitle("Quadratum");
		setSize(1024, 768);
		setResizable(false);
		
		addWindowListener(this);
		
		GameplayHandler gameplayHandler = player._gameplayHandler;
		
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		//Add a layered pain containing the map
		JLayeredPane mapPane = new JLayeredPane();
		mapPane.setLayout(new FillLayout());
		
		//Add map display panel
		MapPanel map = new MapPanel(player);
		gameplayHandler.setMapPanel(map);
		mapPane.add(map, new Integer(0));
		
		//Add message display
		MessageOverlay msg = new MessageOverlay(player);
		mapPane.add(msg, new Integer(1));
		
		content.add(mapPane, BorderLayout.CENTER);
		
		//Create control panel
		JPanel controls = new JPanel();
		controls.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		LineConstraints constraints;
		
		//Add minimap panel
		MinimapPanel minimap = map.getMinimapPanel();
		constraints = new LineConstraints(0.2);
		controls.add(minimap, constraints);
		
		//Add selected unit panel
		JPanel sUPanel = CM.createTitledPanel("Selected Unit");
		sUPanel.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		//Add unit info panel
		JPanel uInfo = CM.createTitledPanel("Unit Info");
		uInfo.setLayout(new FillLayout());
		JScrollPane uInfoScroll = CM.createScrollingTextDisplay();
		gameplayHandler.setUnitInfoArea((JTextArea)(uInfoScroll.getViewport().getView()));
		uInfo.add(uInfoScroll);
		sUPanel.add(uInfo);
		
		//Add unit image panel
		UnitImagePanel uImg = new UnitImagePanel(player);
		sUPanel.add(uImg);
		gameplayHandler.setUnitImagePanel(uImg);
		constraints = new LineConstraints(0.4);
		controls.add(sUPanel, constraints);
		
		//Add tabbed info panel
		MyTabbedPanel infoArea = new MyTabbedPanel();
		
		//Add unit tab
		UnitPanel units = new UnitPanel();
		gameplayHandler.setUnitPanel(units);
		infoArea.addTab("Units", units);
		
		//Add build tab
		BuildPanel build = new BuildPanel();

		infoArea.addTab("Build", build);
		
		//Add objectives tab
		JPanel objectives = CM.createTitledPanel("Game Objectives");
		objectives.setLayout(new FillLayout());
		
		objectives.add(CM.createScrollingTextDisplay());
		
		infoArea.addTab("Objectives", objectives, true);
		
		//Add chat tab
		ChatPanel chat = new ChatPanel(player, msg);
		
		infoArea.addTab("Chat", chat);
		
		constraints = new LineConstraints(0.4);
		controls.add(infoArea, constraints);
		
		content.add(controls, BorderLayout.SOUTH);
	}
	
	public void windowDeactivated(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
	
	public void windowClosing(WindowEvent e) {
		//TODO:  Flail around
		System.exit(0);
	}
}