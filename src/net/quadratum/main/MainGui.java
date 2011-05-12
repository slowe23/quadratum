package net.quadratum.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.Block;
import net.quadratum.core.GameCore;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.WinCondition;
import net.quadratum.gamedata.DefaultPieces;
import net.quadratum.gamedata.Level;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.network.NetworkPlayer;
import net.quadratum.network.VirtualPlayer;
import net.quadratum.test.CheckWinnerTest;

public class MainGui extends JFrame 
					 implements Main, ActionListener, ComponentListener {

	// JPanel with actual contents
	private JPanel _mainMenuPanel, _singlePlayerPanel, _campaignPanel,
				   _networkGamePanel, _networkLobbyPanel, _settingsPanel;
	private GameCore _gc;
	private boolean _lastGameCampaign; //maybe use Panel _returnDest instead
	private boolean _resize = false;
	
	
	public static void main(String[] args) {
		
//		SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                MainGui gm = new MainGui();
//            }
//        });
		
		MainGui gm = new MainGui();
		gm.setVisible(true);

	}
	
	
	private MainGui() {
		
		super("Quadratum");
		
		_lastGameCampaign = false;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		setPreferredSize(getSize());
		setMinimumSize( new Dimension(MainConstants.MIN_WINDOW_W,
				        MainConstants.MIN_WINDOW_H) );
		setResizable(false);
		
		
		
		addComponentListener(this);
		
		_resize = true;
		
		
		
		// TODO: add prettiness (e.g. background, icons)
		
		initPanels();
		setContentPane(_mainMenuPanel);
		changePanel(_mainMenuPanel);
		
		//center in middle of screen
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation( (dim.width - this.getWidth())/2,
				     (dim.height - this.getHeight())/2 );
		
		//setVisible(true);
	}
	
	private void initPanels() {
		
		_mainMenuPanel = new MainMenuPanel(this);
		_singlePlayerPanel = new SinglePlayerPanel(this);
		_campaignPanel = new CampaignPanel(this);
		_networkGamePanel = new NetworkGamePanel(this);
		_networkLobbyPanel = new NetworkLobbyPanel(this);
		_settingsPanel = new SettingsPanel(this);
	}
	
	
	private void hideMe() {
		//System.err.println("Main: Passing off control, hiding.");
		getContentPane().setVisible(false);
		getContentPane().setEnabled(false);
		setVisible(false);
		//setEnabled(false);
		
//		synchronized (this) {
//			try {
//				wait();
//			} catch (InterruptedException e) {
//				System.out.println("Oh noes! interrupted");
//			} catch (IllegalMonitorStateException imse) {
//				System.out.println("Oh noes! illegal monitor state");
//			}
//		}
	}
	
	public void returnControl() {
		if(!_lastGameCampaign)
			changePanel(_mainMenuPanel);
		else changePanel(_campaignPanel);
		
		_gc = null;
		
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
		
		if(_resize)
		{
			Dimension d = panel.getPreferredSize();
			Dimension m = panel.getMinimumSize();
			d.width += 20;	d.height += 20;
			d.width = (m.width > d.width ? m.width : d.width);
			d.height = (m.height > d.height ? m.height : d.height);
			setSize(d.width, d.height);
		}
		
		update(this.getGraphics());
	}
	

	private void createQuickGame() {
		
		Settings set = (Settings)_settingsPanel;
		
		String map;
		WinCondition wc;
		ArrayList<Piece> pieces;
		
		if(set.usingPresetMap()) {
			map = set.getPresetMap();
		}
		else map = set.getMap();
		
		wc = new CheckWinnerTest();
		
		//pieces = new ArrayList<Piece>();
		pieces = getStdPieces();
		
		GameCore gc = new GameCore(this, map, wc, pieces);
		//then create GuiPlayer and AIPlayer
		Player human = new GUIPlayer();
		// TODO make this not a test AI later
		Player ai = new TestAI_MTC();
		
		int maxU = set.getMaxUnits();
		int res = set.getStartingResources();
		gc.addPlayer(human, "human", maxU, res);
		gc.addPlayer(ai, "ai", maxU, res);
		
		
		hideMe();
		
		_gc = gc;
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				_gc.startGame();
			} else SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					_gc.startGame();
				}
			});
		} catch (Exception e) {
			System.err.println("Threading error.");
		}
		
//		hideMe();
	}
	
	private void createNetworkGame() {
		Settings set = (Settings)_settingsPanel;
		
		String map;
		WinCondition wc;
		ArrayList<Piece> pieces;
		
		if(set.usingPresetMap()) {
			map = set.getPresetMap();
		}
		else map = set.getMap();
		
		if(set.altWinCondition()) {
			wc = set.getAltWinCon();
		}
		else wc = new CheckWinnerTest();
		pieces = getStdPieces();
		
		GameCore gc = new GameCore(this, map, wc, pieces);
		
		ServerThread server = new ServerThread(set.getPort());
		server.run();
		try {wait(2000);} catch (Exception e) {}
		List<NetworkPlayer> others = server.stopListening();
		
		Player human = new GUIPlayer();
		
		//add players to core
		int maxU = set.getMaxUnits();
		int res = set.getStartingResources();
		gc.addPlayer(human, "human", maxU, res);
		
		for(int i = 1; i < maxU; i++) {
			gc.addPlayer(new VirtualPlayer(), "virtual player"+i, maxU, res);
		}
		hideMe();

		_gc = gc;
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				_gc.startGame();
			} else SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					_gc.startGame();
				}
			});
		} catch (Exception e) {
			System.out.println("Crap.");
		}		
	}
	
	
	public void startCampaignGame(Level level) {
		//level can be some other unique campaign level identifier
		// TODO STUBBED
		
		_lastGameCampaign = true;
		
		if(level == null) {
			//make dialog saying level isn't implemented yet
			JOptionPane.showMessageDialog(this, "That campaign level hasn't been made yet.", 
				    "Sorry", JOptionPane.WARNING_MESSAGE);
			returnControl();
			return;
		}
		
		GameCore gc = new GameCore(this, level.getMap(), level.getWinCondition(), level.getPieces());
		
		Player human = new GUIPlayer();
		Player ai = level.getAI();
		
		int maxU = level.getMaxUnits();
		gc.addPlayer(human, "human", maxU, level.getStartingResources());
		gc.addPlayer(ai, "ai", Integer.MAX_VALUE, Integer.MAX_VALUE);
		
		
		hideMe();
		
		_gc = gc;
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				_gc.startGame();
			} else SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					_gc.startGame();
				}
			});
		} catch (Exception e) {
			System.err.println("Threading error.");
		}
	}
	
	
	public void actionPerformed(ActionEvent e) {
		
		
		// changePanel actions
		if(MainConstants.SINGLE.equals(e.getActionCommand())) {
			changePanel(_singlePlayerPanel);
			return;
		}
		
		if(MainConstants.NETWORK.equals(e.getActionCommand())) {
			((SettingsPanel) _settingsPanel).useNetworkSettings(true);
			changePanel(_networkGamePanel);
			return;
		}
		
		if(MainConstants.QUICKPLAY.equals(e.getActionCommand())) {
			((SettingsPanel) _settingsPanel).useNetworkSettings(false);
			changePanel(_settingsPanel);
			return;
		}
		
		if(MainConstants.CAMPAIGN.equals(e.getActionCommand())) {
			changePanel(_campaignPanel);
			return;
		}
		
		if(MainConstants.RETURN_MAIN.equals(e.getActionCommand())) {
			changePanel(_mainMenuPanel);
			return;
		}
		
		if(MainConstants.HOST.equals(e.getActionCommand())) {
			changePanel(_settingsPanel);
			return;
		}
		
		// new game - pass to core
		if(MainConstants.START_GAME.equals(e.getActionCommand())) {
			if(((SettingsPanel) _settingsPanel).usingNetworkSettings()) {
				createNetworkGame();
			}
			else {
				createQuickGame();
			}
		}
		
		// different window?
		if(MainConstants.CAMPAIGN.equals(e.getActionCommand())) {
			// Start campaign map viewer
		}
		
		
		// Misc. actions
		if(MainConstants.LOAD.equals(e.getActionCommand())) {
			// perform necessary actions to load game
			// Currently not part of functionality
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
		
		//System.err.println("Exiting Quadratum.");
		//clean up a bit?
		setEnabled(false);
		setVisible(false);
		System.exit(0);
	}
	
	
	
	private ArrayList<Piece> getStdPieces() {
		return DefaultPieces.getPieces();
	}
	
	
	// Other required but not implemented listener methods

	public void componentHidden(ComponentEvent arg0) {
		// No effect
		
	}

	public void componentMoved(ComponentEvent arg0) {
		// No effect
		
	}

	public void componentResized(ComponentEvent arg0) {
		// Keep above minimum size
		Dimension currentD = getSize();
		Dimension minD = getMinimumSize();
		if(currentD.width < minD.width)
			setSize(minD.width, currentD.height);
		if(currentD.height < minD.height)
			setSize(currentD.width, minD.height);
	}

	public void componentShown(ComponentEvent arg0) {
		// No effect
		
	}

}
