package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameWindow extends JFrame implements WindowListener {
	private final Center _center;
	
	public GameWindow(GUIPlayer player, Center center, ChatHandler chatHandler, DrawingMethods drawingMethods, MapData mapData, UnitsInfo unitsInfo) {
		setTitle("Quadratum");
		setSize(1024, 768);
		setResizable(false);
		
		_center = center;
		
		addWindowListener(this);
		
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		//Add a layered pane containing the map
		JLayeredPane mapPane = new JLayeredPane();
		mapPane.setLayout(new FillLayout());
		
		//Add map display panel
		MapPanel map = new MapPanel(center, unitsInfo, mapData, drawingMethods);
		mapPane.add(map, new Integer(0));
		
		//Add message display
		MessageOverlay msg = new MessageOverlay(drawingMethods);
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
		JPanel sUPanel = new JPanel();
		sUPanel.setBorder(StaticMethods.getTitleBorder("Selected Unit"));
		sUPanel.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		//Add unit info panel
		UnitInfoPanel uInfo = new UnitInfoPanel(unitsInfo);
		sUPanel.add(uInfo);
		
		//Add unit image panel
		UnitImagePanel uImg = new UnitImagePanel(unitsInfo, drawingMethods);
		sUPanel.add(uImg);
		
		constraints = new LineConstraints(0.4);
		controls.add(sUPanel, constraints);
		
		//Add tabbed info panel
		MyTabbedPanel infoArea = new MyTabbedPanel();
		
		//Add unit tab
		UnitPanel units = new UnitPanel();
		infoArea.addTab("Units", units);
		
		//Add build tab
		BuildPanel build = new BuildPanel();
		infoArea.addTab("Build", build);
		
		//Add objectives tab
		JPanel objectives = new JPanel();
		objectives.setBorder(StaticMethods.getTitleBorder("Game Objectives"));
		objectives.setLayout(new FillLayout());
		objectives.add(StaticMethods.createScrollingTextDisplay());
		infoArea.addTab("Objectives", objectives, true);
		
		//Add chat tab
		ChatPanel chat = new ChatPanel(chatHandler, msg);
		infoArea.addTab("Chat", chat);
		
		constraints = new LineConstraints(0.4);
		controls.add(infoArea, constraints);
		
		content.add(controls, BorderLayout.SOUTH);
		
		center.setComponents(map, uInfo, uImg, units, build);
	}
	
	public void windowDeactivated(WindowEvent e) { }
	public void windowActivated(WindowEvent e) { }
	public void windowDeiconified(WindowEvent e) { }
	public void windowIconified(WindowEvent e) { }
	public void windowClosed(WindowEvent e) { }
	public void windowOpened(WindowEvent e) { }
	
	public void windowClosing(WindowEvent e) {
		_center.closing();
		setVisible(false);
	}
}