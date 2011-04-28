package net.quadratum.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.quadratum.ai.InstantiableAI;
import net.quadratum.core.GameCore;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.WinCondition;
import net.quadratum.gui.GUIPlayer;

public class MainGui extends JFrame 
					 implements Main, ActionListener, ComponentListener {

	// JPanel with actual contents
	private JPanel _mainMenuPanel, _singlePlayerPanel,
				   _networkGamePanel, _networkLobbyPanel, _settingsPanel;
	
	
	public static void main(String[] args) {
		MainGui gm = new MainGui();
	}
	
	
	private MainGui() {
		
		super("Quadratum");
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		setPreferredSize(getSize());
		setMinimumSize( new Dimension(MainConstants.MIN_WINDOW_W,
				        MainConstants.MIN_WINDOW_H) );
		
		//center in middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( (dim.width - this.getWidth())/2,
				     (dim.height - this.getHeight())/2 );
		
		
		
		addComponentListener(this);
		
		
		
		
		
		//todo: add prettiness (e.g. background, icons)
		
		initPanels();
		setContentPane(_mainMenuPanel);
		
		setVisible(true);
	}
	
	private void initPanels() {
		
		_mainMenuPanel = new MainMenuPanel(this);
		_singlePlayerPanel = new SinglePlayerPanel(this);
		_networkGamePanel = new NetworkGamePanel(this);
		_networkLobbyPanel = new NetworkLobbyPanel(this);
		_settingsPanel = new SettingsPanel(this);
	}
	
	
	private void hideMe() {
		//System.err.println("Main: Passing off control, hiding.");
		setEnabled(false);
		setVisible(false);
		//wait(); //? Makes it truly dormant, but requires threads and notify()
	}
	
	public void returnControl() {
		setContentPane(_mainMenuPanel);
		
		setEnabled(true);
		setVisible(true);
	}
	
	
	
	private void changePanel(JPanel panel) {
		if(panel == null) {
			//error msg
			System.err.print("UNEXPECTED - call to changePanel with");
			System.err.println(" current panel as argument.");
			return;
		}
		
		Container former = getContentPane();
		former.setEnabled(false);
		former.setVisible(false);
		setContentPane(panel);
		Container pane = getContentPane();
		pane.setEnabled(true);
		pane.setVisible(true);
		
		update(this.getGraphics());
	}
	

	private void createQuickGame() {
		//query _settingsPanel for map
		Settings set = (Settings)_settingsPanel;
		
		String map;
		WinCondition wc;
		ArrayList<Piece> pieces;
		
		if(set.usingPresetMap()) {
			map = set.getPresetMap();
		}
		else map = set.generateMap();
		
		//query for wincon
		if(set.altWinCondition()) {
			wc = set.getAltWinCon();
		}
		else wc = new CheckWinner();
		
		pieces = new ArrayList<Piece>();
		
		GameCore gc = new GameCore(this, map, wc, pieces);
		//then create GuiPlayer and AIPlayer
		Player human = new GUIPlayer();
		Player ai = new InstantiableAI();
		
		int maxU = set.getMaxUnits();
		gc.addPlayer(human, "human", maxU);
		gc.addPlayer(ai, "ai", maxU);
		
		gc.start();
		hideMe();
	}
	
	private void createNetworkGame() {
		//query _settingsPanel for map
		Settings set = (Settings)_settingsPanel;
		
		String map;
		WinCondition wc;
		ArrayList<Piece> pieces;
		
		if(set.usingPresetMap()) {
			
		}
		//query  for WinCon
		//generate pieces
		
		//using these, create core
		
		//create GuiPlayer and virtual players
		
		//add players to core
		//tell core to start
		//go to sleep
	}
	
	
	private void startCampaignGame(int level) {
		//level can be some other unique campaign level identifier
		// TODO STUBBED
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		
		// changePanel actions
		if(MainConstants.SINGLE.equals(e.getActionCommand())) {
			changePanel(_singlePlayerPanel);
			return;
		}
		
		if(MainConstants.NETWORK.equals(e.getActionCommand())) {
			changePanel(_networkGamePanel);
			return;
		}
		
		if(MainConstants.QUICKPLAY.equals(e.getActionCommand())) {
			//tell settings qp view, not network?
			createQuickGame();
			//changePanel(_settingsPanel);
			return;
		}
		
		if(MainConstants.RETURN_MAIN.equals(e.getActionCommand())) {
			changePanel(_mainMenuPanel);
			return;
		}
		
		// different window?
		if(MainConstants.CAMPAIGN.equals(e.getActionCommand())) {
			// Start campaign map viewer
		}
		
		
		// Misc. actions
		if(MainConstants.LOAD.equals(e.getActionCommand())) {
			// perform necessary actions to load game
		}
		
		if(MainConstants.QUIT.equals(e.getActionCommand())) {
			exit();
		}
	}
	
	
	
	public void exit() {
		// In some cases, we may want to prompt first:
//		if(cond) {
//			if (!verifyPopUp()) return;
//		}
		
		System.err.println("Exiting Quadratum.");
		//clean up a bit?
		setEnabled(false);
		setVisible(false);
		System.exit(0);
	}
	
	
	// Other implemented listener methods

	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void componentResized(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
		Dimension currentD = getSize();
		Dimension minD = getMinimumSize();
		if(currentD.width < minD.width)
			setSize(minD.width, currentD.height);
		if(currentD.height < minD.height)
			setSize(currentD.width, minD.height);
	}

	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
