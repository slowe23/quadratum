package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameWindow extends JFrame implements WindowListener {
	private ChatPanel _chat;
	
	public GameWindow(GUIPlayer player) {
		setTitle("Quadratum");
		setSize(1024, 768);
		setResizable(false);
		
		addWindowListener(this);
		
		Container content = getContentPane();
		content.setLayout(new BorderLayout());
		
		//Add a layered pain containing the map
		JLayeredPane mapPane = new JLayeredPane();
		mapPane.setLayout(new FillLayout());
		mapPane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 5));
		
		//Add map display panel
		MapPanel map = new MapPanel(player);
		mapPane.add(map, new Integer(0));
		
		//Add message display
		MessageOverlay msg = new MessageOverlay();
		mapPane.add(msg, new Integer(1));
		
		content.add(mapPane, BorderLayout.CENTER);
		
		//Create control panel
		JPanel controls = new JPanel();
		controls.setLayout(new LineLayout(LineLayout.LEFT_TO_RIGHT));
		
		LineConstraints constraints;
		
		//Add minimap panel
		MinimapPanel minimap = map.getMinimap();
		constraints = new LineConstraints(0.2);
		controls.add(minimap, constraints);
		
		//Add unit info panel
		JPanel uInfo = CM.createTitledPanel("Unit Info");
		uInfo.setLayout(new FillLayout());
		
		uInfo.add(CM.createScrollingTextDisplay());
		
		constraints = new LineConstraints(0.2);
		controls.add(uInfo, constraints);
		
		//Add unit image panel
		UnitImagePanel uImg = new UnitImagePanel(player);
		constraints = new LineConstraints(0.2);
		controls.add(uImg, constraints);
		
		//Add tabbed info panel
		MyTabbedPanel infoArea = new MyTabbedPanel();
		
		//Add unit tab
		UnitPanel units = new UnitPanel();

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
		_chat = new ChatPanel(player, msg);
		
		infoArea.addTab("Chat", _chat);
		
		constraints = new LineConstraints(0.4);
		controls.add(infoArea, constraints);
		
		content.add(controls, BorderLayout.SOUTH);
		
		//TODO: have some way to give this information to the various components once the game starts
		/*int[][] terrain = new int[40][30];
		Random r = new Random();
		for(int i = 0; i<terrain.length; i++) {
			for(int j = 0; j<terrain[i].length; j++) {
				int rand = r.nextInt(5);
				if(rand==3)
					terrain[i][j] = 1;
				else if(rand==4)
					terrain[i][j] = 2;
			}
		}*/
		//map.setTerrain(player.getTerrain());
	}
	
	public ChatPanel getChatPanel() {
		return _chat;
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