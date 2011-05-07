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
import javax.swing.SwingUtilities;

import net.quadratum.ai.test.TestAI_MTC;
import net.quadratum.core.Block;
import net.quadratum.core.GameCore;
import net.quadratum.core.MapPoint;
import net.quadratum.core.Piece;
import net.quadratum.core.Player;
import net.quadratum.core.WinCondition;
import net.quadratum.gui.GUIPlayer;
import net.quadratum.network.VirtualPlayer;
import net.quadratum.test.CheckWinnerTest;

public class MainGui extends JFrame 
					 implements Main, ActionListener, ComponentListener {

	// JPanel with actual contents
	private JPanel _mainMenuPanel, _singlePlayerPanel,
				   _networkGamePanel, _networkLobbyPanel, _settingsPanel;
	private GameCore _gc;
	
	
	public static void main(String[] args) {
		
//		SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                MainGui gm = new MainGui();
//            }
//        });
		
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
		getContentPane().setVisible(false);
		getContentPane().setEnabled(false);
		setVisible(false);
		//setEnabled(false);
		//wait(); //? Makes it truly dormant, but requires threads and notify()
		
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
		gc.addPlayer(human, "human", maxU, 0);
		gc.addPlayer(ai, "ai", maxU, 0);
		
		
		hideMe();
		
		_gc = gc;
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				_gc.startGame();
			} else SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					//handOff();
					_gc.startGame();
				}
			});
		} catch (Exception e) {
			System.out.println("Crap.");
		}		
		
//		hideMe();
	}
	
	private void createNetworkGame() {
		//query _settingsPanel for map
		Settings set = (Settings)_settingsPanel;
		
		String map;
		WinCondition wc;
		ArrayList<Piece> pieces;
		
		if(set.usingPresetMap()) {
			map = set.getPresetMap();
		}
		else map = set.getMap();
		
		//query  for WinCon
		if(set.altWinCondition()) {
			wc = set.getAltWinCon();
		}
		else wc = new CheckWinnerTest();
		//generate pieces
		pieces = getStdPieces();
		
		//using these, create core
		GameCore gc = new GameCore(this, map, wc, pieces);
		
		//create GuiPlayer and virtual players
		Player human = new GUIPlayer();
		
		//add players to core
		int maxU = set.getMaxUnits();
		gc.addPlayer(human, "human", maxU, 0);
		
		for(int i = 1; i < maxU; i++) {
			gc.addPlayer(new VirtualPlayer(), "virtual player"+i, maxU, i);
		}
		hideMe();

		_gc = gc;
		try {
			if (SwingUtilities.isEventDispatchThread()) {
				_gc.startGame();
			} else SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					//handOff();
					_gc.startGame();
				}
			});
		} catch (Exception e) {
			System.out.println("Crap.");
		}		
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
			((SettingsPanel) _settingsPanel).useNetworkSettings(true);
			changePanel(_networkGamePanel);
			return;
		}
		
		if(MainConstants.QUICKPLAY.equals(e.getActionCommand())) {
			((SettingsPanel) _settingsPanel).useNetworkSettings(false);
			changePanel(_settingsPanel);
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
	
	
	
	private ArrayList<Piece> getStdPieces() {
		// TODO do more than quick&dirty
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		Block attackBlock = new Block(30);
		attackBlock._bonuses.put(Block.BonusType.ATTACK, 10);
		Piece lPiece = new Piece(10, -1, "L Block", "Provides +40 attack");
		lPiece._blocks.put(new MapPoint(0, 0), new Block(attackBlock));
		lPiece._blocks.put(new MapPoint(0, 1), new Block(attackBlock));
		lPiece._blocks.put(new MapPoint(0, 2), new Block(attackBlock));
		lPiece._blocks.put(new MapPoint(1, 2), new Block(attackBlock));
		pieces.add(lPiece);
		return pieces;
	}
	
	
	// Other required but not implemented listener methods

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
