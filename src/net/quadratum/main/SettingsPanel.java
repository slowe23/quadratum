package net.quadratum.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import net.quadratum.core.WinCondition;
import net.quadratum.main.MainConstants.Defaults;


public class SettingsPanel extends JPanel implements Settings, ItemListener {

	private boolean _networkSettings; // Either network or quick game
	private final String _title = "Adjust settings";

	//Contents currently uncertain, will include settings for either
	//network or quick game
	private JLabel _titleLbl;
	
	
	/*
	 * Temporary debugging constructor; didn't want to comment other
	 * long one
	 */
	public SettingsPanel (ActionListener al) {
		super();
		JButton jb = new JButton("Test quickplay game");
		jb.setActionCommand(MainConstants.QUICKPLAY);
		jb.addActionListener(al);
		add(jb);
		JButton jb1 = new JButton("Return to main");
		jb1.setActionCommand(MainConstants.RETURN_MAIN);
		jb1.addActionListener(al);
		add(jb1);
	}
	
	
	public SettingsPanel (ActionListener al, int i) {
		super();
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		
		//three panels:
		// (1) Everpresent options
		// (2) Quickplay only opts
		// (3) Network only
		
		// (1) - Settings for both
		//     Players: How many starting resources and units each?
		//     Map: Grid dimensions, both width and height
		//     Map: use preset map Y/N ... Y-> select from drop-down list
		//     Map: Terrain constraints: water, bunkers, mtns > ON/OFF
		JPanel bothSettings = new JPanel();
		
		/*Preset map? Label- Checkbox | SelectionComponent (List, FileChooser)*/
		//JLabel presetLbl = new JLabel("Use preset map?");
		JCheckBox presetCB = new JCheckBox( "Use preset map?", 
										    Defaults.USE_PRESET_MAP );
		presetCB.setHorizontalTextPosition(SwingConstants.LEFT);
		//presetCB.setMnemonic(KeyEvent.VK_U);
		presetCB.addItemListener(this);
		Component mapSelector = generateMapSelector();
		
		/* Grid dimensions: Width: <select>     Height: <select> */
		JLabel gridDimLbl = new JLabel("Set grid dimensions:");
		SpinnerNumberModel widthModel = new SpinnerNumberModel(
				Defaults.MAP_TILE_WIDTH, Defaults.MIN_MAP_WIDTH, 
				Defaults.MAX_MAP_WIDTH, Defaults.GRID_SIZE_INCREMENT);
		JSpinner widthSpinner = new JSpinner(widthModel);
		JLabel widthLbl = new JLabel("Width: ");
		widthLbl.setLabelFor(widthSpinner);
		
		SpinnerNumberModel heightModel = new SpinnerNumberModel(
				Defaults.MAP_TILE_HEIGHT, Defaults.MIN_MAP_HEIGHT, 
				Defaults.MAX_MAP_HEIGHT, Defaults.GRID_SIZE_INCREMENT);
		JSpinner heightSpinner = new JSpinner(heightModel);
		JLabel heightLbl = new JLabel("height: ");
		heightLbl.setLabelFor(heightSpinner);
		
		/* Terrain: Enable/disable inclusion of oWater oBunkers oMountains */
		JCheckBox water = new JCheckBox("Water", Defaults.ALLOW_WATER);
		water.setHorizontalTextPosition(SwingConstants.LEFT);
		//water.setMnemonic(KeyEvent.VK_W);
		
		JCheckBox bunkers = new JCheckBox("Bunkers", Defaults.ALLOW_BUNKERS);
		bunkers.setHorizontalTextPosition(SwingConstants.LEFT);
		//bunkers.setMnemonic(KeyEvent.VK_W);
		
		JCheckBox mtns = new JCheckBox("Mountains", Defaults.ALLOW_MOUNTAINS);
		mtns.setHorizontalTextPosition(SwingConstants.LEFT);
		//mtns.setMnemonic(KeyEvent.VK_W);
		
		JPanel terrain = new JPanel();
		terrain.setLayout(new BoxLayout(terrain, BoxLayout.X_AXIS));
		terrain.add(water);
		terrain.add(Box.createRigidArea(new Dimension(8,0)));
		terrain.add(bunkers);
		terrain.add(Box.createRigidArea(new Dimension(8,0)));
		terrain.add(mtns);
		terrain.setBorder(BorderFactory.createTitledBorder("Include terrain elements?"));
		
		
		
		
		
		
		// layout
		//create buttons
		//init buttons
		//add buttons
		add(new JLabel(_title));
		//visibility
	}
	
	
	
	
	public void useNetworkSettings(boolean network) {
		_networkSettings = network;
		//enable and disable components accordingly
	}

	public boolean usingNetworkSettings() {
		return _networkSettings;
	}
	
	
	private Component generateMapSelector() {
		//STUBBED
		return new JLabel("Coming soon!");
	}
	

	public String generateMap() {
		// TODO Auto-generated method stub
		return "MAP";
	}



	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}




	public boolean altWinCondition() {
		// TODO Auto-generated method stub
		//return Defaults.USE_ALT_WINCON;
		return false;
	}




	public boolean areBunkersAllowed() {
		// TODO Auto-generated method stub
		return Defaults.ALLOW_BUNKERS;
	}




	public boolean areMountainsAllowed() {
		// TODO Auto-generated method stub
		return Defaults.ALLOW_MOUNTAINS;
	}




	public boolean areTurnsTimed() {
		// TODO Auto-generated method stub
		return true;
	}




	public int durationInTurns() {
		// TODO Auto-generated method stub
		return Defaults.PLAYER_TURNS_LIMIT;
	}




	public boolean durationSet() {
		// TODO Auto-generated method stub
		return Defaults.LIMIT_DURATION;
	}



	public int getAiDifficulty() {
		// TODO Auto-generated method stub
		return Defaults.AI_DIFFICULTY;
	}




	public WinCondition getAltWinCon() {
		// TODO Auto-generated method stub
		return null;
	}




	public int getMapHeight() {
		// TODO Auto-generated method stub
		return Defaults.MAP_TILE_HEIGHT;
	}




	public int getMapWidth() {
		// TODO Auto-generated method stub
		return Defaults.MAP_TILE_WIDTH;
	}




	public int getMaxUnits() {
		// TODO Auto-generated method stub
		return Defaults.INIT_NUM_UNITS;
	}




	public int getPort() {
		// TODO Auto-generated method stub
		return Defaults.PREFERRED_PORT;
	}




	public String getPresetMap() {
		// TODO Auto-generated method stub
		return null;
	}




	public int getStartingResources() {
		// TODO Auto-generated method stub
		return Defaults.INIT_NUM_RESOURCES;
	}




	public boolean isWaterAllowed() {
		// TODO Auto-generated method stub
		return Defaults.ALLOW_WATER;
	}




	public int numPlayers() {
		// TODO Auto-generated method stub
		return Defaults.NUM_PLAYERS;
	}




	public int turnTimeLimit() {
		// TODO Auto-generated method stub
		return Defaults.TURN_LIMIT_SECS;
	}




	public boolean usingPresetMap() {
		// TODO Auto-generated method stub
		return false;
	}


}
