package net.quadratum.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;

import net.quadratum.core.WinCondition;
import net.quadratum.main.MainConstants.Defaults;
import net.quadratum.test.CheckWinnerTest;


public class SettingsPanel extends JPanel implements Settings, ItemListener, ActionListener {

	private boolean _useNetworkStgs; // Either network or quick game
	private final String _title = "Adjust settings";
	
	private JPanel _qpSettings, _netGameStgs, _otherNetStgs;

	private JButton _returnMainBtn, _useDefaultsBtn;
	private JLabel _titleLbl;
	private JCheckBox _presetCB, _water, _bunkers, _mtns,
					  _timeLimitCB, _turnLimitCB, _winconCB;
	private Component _mapSelector, _connectionCheck;
	private JComboBox _winconSelector;
	private JSpinner _widthSpinner, _heightSpinner,
					 _unitsSpinner, _resSpinner;
	private JSlider _aiLevel,    _timeLimit,    _turnLimit;
	private JList _numPlayers;
	private JTextField _portNumber, _serverAddr;
	private JButton _startGameBtn;
	
	private ArrayList<Component> _netComps;
	private JPanel _netComponents;
	
	
//	/*
//	 * Temporary debugging constructor; didn't want to comment other
//	 * long one
//	 */
//	public SettingsPanel (ActionListener al) {
//		super();
//		JButton jb = new JButton("Test quickplay game");
//		jb.setActionCommand(MainConstants.QUICKPLAY);
//		jb.addActionListener(al);
//		add(jb);
//		JButton jb1 = new JButton("Return to main");
//		jb1.setActionCommand(MainConstants.RETURN_MAIN);
//		jb1.addActionListener(al);
//		add(jb1);
//	}
	
	public static void main(String[] args) {
		JFrame window = new JFrame();
		window.setSize(640, 640);
		
		SettingsPanel sp = new SettingsPanel(null);
		sp.setVisible(true);
		window.add(sp, BorderLayout.CENTER);
		window.setVisible(true);
	}
	
	
	public SettingsPanel (ActionListener al) {
		super();
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		_netComps = new ArrayList<Component>();
		
		// Settings components are split left-right
		JPanel settingsPane = new JPanel();
		settingsPane.setLayout(new BoxLayout(settingsPane, BoxLayout.X_AXIS));
		// Buttons below
		JPanel btnPane = new JPanel();
		btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.X_AXIS));
		
		
		//utility dimensions - between lines in a menu; between menus
		Dimension innerSpacing = new Dimension(0, 10);
		Dimension outerSpacing = new Dimension(0, 25);
		
		
		//three panels:
		// (1) Universal settings
		// (2) Quickplay only settings
		// (3) Network only settings
		
		// (1) - Universal settings
		//     Players: How many starting resources and units each?
		//     Map: Grid dimensions, both width and height
		//     Map: use preset map Y/N ... Y-> select from drop-down list
		//     Map: Terrain constraints: water, bunkers, mtns > ON/OFF
		JPanel uniSettings = new JPanel();
		uniSettings.setLayout(new BoxLayout(uniSettings, BoxLayout.Y_AXIS));
		
		JPanel mapOpts = new JPanel();
		JPanel playerOpts = new JPanel();
		mapOpts.setLayout(new BoxLayout(mapOpts, BoxLayout.Y_AXIS));
		playerOpts.setLayout(new BoxLayout(playerOpts, BoxLayout.Y_AXIS));
		
		/*Preset map? Label- Checkbox | SelectionComponent (List, FileChooser)*/
		_presetCB = new JCheckBox( "Use preset map?", 
										    Defaults.USE_PRESET_MAP );
		_presetCB.setHorizontalTextPosition(SwingConstants.LEFT);
		//presetCB.setMnemonic(KeyEvent.VK_P);
		_presetCB.addItemListener(this);
		_mapSelector = generateMapSelector();
		_mapSelector.setEnabled(false);
		
		JPanel mapLine1 = new JPanel();
		mapLine1.setLayout(new BoxLayout(mapLine1, BoxLayout.X_AXIS));
		mapLine1.add(_presetCB);
		mapLine1.add(Box.createHorizontalGlue());
		mapLine1.add(_mapSelector);
		//first line done
		
		/* Grid dimensions: Width: <select>     Height: <select> */
		Dimension spinSize = new Dimension(40, 25);
		
		JLabel gridDimLbl = new JLabel("Set grid dimensions:");
		gridDimLbl.setAlignmentX(SwingConstants.LEFT);
		gridDimLbl.setPreferredSize(new Dimension(200, 25));
		
		SpinnerNumberModel widthModel = new SpinnerNumberModel(
				Defaults.MAP_TILE_WIDTH, Defaults.MIN_MAP_WIDTH, 
				Defaults.MAX_MAP_WIDTH, Defaults.GRID_SIZE_INCREMENT);
		_widthSpinner = new JSpinner(widthModel);
		_widthSpinner.setPreferredSize(spinSize);
		_widthSpinner.setMaximumSize(spinSize);
		JLabel widthLbl = new JLabel("Width: ");
		widthLbl.setLabelFor(_widthSpinner);
		
		SpinnerNumberModel heightModel = new SpinnerNumberModel(
				Defaults.MAP_TILE_HEIGHT, Defaults.MIN_MAP_HEIGHT, 
				Defaults.MAX_MAP_HEIGHT, Defaults.GRID_SIZE_INCREMENT);
		_heightSpinner = new JSpinner(heightModel);
		_heightSpinner.setPreferredSize(spinSize);
		_heightSpinner.setMaximumSize(spinSize);
		JLabel heightLbl = new JLabel("Height: ");
		heightLbl.setLabelFor(_heightSpinner);
		
		JPanel mapLine2 = new JPanel();
		mapLine2.setLayout(new BoxLayout(mapLine2, BoxLayout.Y_AXIS));
		mapLine2.add(gridDimLbl);//, BorderLayout.NORTH
		
		
		JPanel dimsPane = new JPanel();
		dimsPane.setLayout(new BoxLayout(dimsPane, BoxLayout.X_AXIS));
		dimsPane.add(widthLbl);    dimsPane.add(_widthSpinner);
		dimsPane.add(Box.createRigidArea(new Dimension(10, 0)));
		dimsPane.add(heightLbl);    dimsPane.add(_heightSpinner);
		
		mapLine2.add(Box.createRigidArea(new Dimension(0, 5)));
		mapLine2.add(dimsPane);//, BorderLayout.CENTER
		//second line done
		
		/* Terrain: Enable/disable inclusion of oWater oBunkers oMountains */
		_water = new JCheckBox("Water", Defaults.ALLOW_WATER);
		_water.setHorizontalTextPosition(SwingConstants.LEFT);
		//water.setMnemonic(KeyEvent.VK_W);
		
		_bunkers = new JCheckBox("Bunkers", Defaults.ALLOW_BUNKERS);
		_bunkers.setHorizontalTextPosition(SwingConstants.LEFT);
		//bunkers.setMnemonic(KeyEvent.VK_B);
		
		_mtns = new JCheckBox("Mountains", Defaults.ALLOW_MOUNTAINS);
		_mtns.setHorizontalTextPosition(SwingConstants.LEFT);
		//mtns.setMnemonic(KeyEvent.VK_M);
		
		JPanel terrain = new JPanel();
		terrain.setLayout(new BoxLayout(terrain, BoxLayout.X_AXIS));
		terrain.add(_water);
		terrain.add(Box.createRigidArea(new Dimension(8,0)));
		terrain.add(_bunkers);
		terrain.add(Box.createRigidArea(new Dimension(8,0)));
		terrain.add(_mtns);
		terrain.setBorder(BorderFactory.createTitledBorder("Include terrain elements?"));
		
		
		mapOpts.add(mapLine1);
		mapOpts.add(Box.createRigidArea(innerSpacing));
		mapOpts.add(mapLine2);
		mapOpts.add(Box.createRigidArea(innerSpacing));
		mapOpts.add(terrain);
		mapOpts.add(Box.createRigidArea(outerSpacing));
		mapOpts.setBorder(BorderFactory.createTitledBorder("Map options:"));
		
		
		/* Player options: #Units/player; Starting resources */
		
		spinSize = new Dimension(80, 25);
		
		JPanel plLine1 = new JPanel();
		plLine1.setLayout(new BoxLayout(plLine1, BoxLayout.X_AXIS));
		SpinnerNumberModel unitsModel = new SpinnerNumberModel(
				Defaults.INIT_NUM_UNITS, Defaults.MIN_UNITS, 
				Defaults.MAX_UNITS, 1);
		_unitsSpinner = new JSpinner(unitsModel);
		_unitsSpinner.setPreferredSize(spinSize);
		_unitsSpinner.setMaximumSize(spinSize);
		JLabel unitsLbl = new JLabel("Units per player: ");
		unitsLbl.setLabelFor(_unitsSpinner);
		
		plLine1.add(unitsLbl);
		plLine1.add(Box.createHorizontalGlue());
		plLine1.add(_unitsSpinner);
		
		
		JPanel plLine2 = new JPanel();
		plLine2.setLayout(new BoxLayout(plLine2, BoxLayout.X_AXIS));
		SpinnerNumberModel resModel = new SpinnerNumberModel(
				Defaults.INIT_NUM_RESOURCES, Defaults.MIN_RESOURCES, 
				Defaults.MAX_RESOURCES, Defaults.RESOURCES_INCREMENT);
		_resSpinner = new JSpinner(resModel);
		_resSpinner.setPreferredSize(spinSize);
		_resSpinner.setMaximumSize(spinSize);
		JLabel resLbl = new JLabel("Starting resources: ");
		resLbl.setLabelFor(_resSpinner);
		
		plLine2.add(resLbl);
		plLine2.add(Box.createHorizontalGlue());
		plLine2.add(_resSpinner);
		// all of player contents
		playerOpts.add(plLine1);
		playerOpts.add(Box.createRigidArea(innerSpacing));
		playerOpts.add(plLine2);
		playerOpts.add(Box.createRigidArea(innerSpacing));
		playerOpts.setBorder(BorderFactory.createTitledBorder("Player options:"));
		
		
		uniSettings.add(mapOpts, BorderLayout.CENTER);
		uniSettings.add(Box.createRigidArea(outerSpacing));
		uniSettings.add(playerOpts, BorderLayout.PAGE_END);
		
		
		/* Quickplay options: AI difficulty */
		_qpSettings = new JPanel();
		_qpSettings.setLayout(new BoxLayout(_qpSettings, BoxLayout.X_AXIS));
		_qpSettings.add(new JLabel("AI difficulty:"));
		
		_aiLevel = new JSlider(
						JSlider.HORIZONTAL, Defaults.MIN_AI_DIFFICULTY,
						Defaults.MAX_AI_DIFFICULTY, Defaults.AI_DIFFICULTY );
		_aiLevel.setMajorTickSpacing(9);
		_aiLevel.setMinorTickSpacing(1);
		_aiLevel.setPaintTicks(true);
		_aiLevel.setPaintLabels(true);
		_aiLevel.setSnapToTicks(true);
		
		_qpSettings.add(Box.createHorizontalGlue());
		_qpSettings.add(_aiLevel);
		_qpSettings.setBorder(BorderFactory.createTitledBorder("Quick play only"));
		
		
		/* Right side: Network game settings, followed by other network settings */
		
		_netGameStgs = new JPanel();
		_netComps.add(_netGameStgs);
		
		//line 1: # players
		JPanel ngLine1 = new JPanel();
		ngLine1.setLayout(new BoxLayout(ngLine1, BoxLayout.X_AXIS));
		ngLine1.add(new JLabel("Number of players:"));
		// select number of players
		int minPl = Defaults.MIN_PLAYERS;
		Integer[] numPlRng = new Integer
						[ 1 + Defaults.MAX_PLAYERS - minPl ];
		for(int k = 0; k + minPl <= Defaults.MAX_PLAYERS; k++) {
			numPlRng[k] = minPl + k;
		}
		_numPlayers = new JList(numPlRng);
		_numPlayers.setVisibleRowCount(1);
		_numPlayers.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		_numPlayers.setSelectedIndex(0); //2 player default
		
		ngLine1.add(Box.createRigidArea(new Dimension(8, 0)));
		ngLine1.add(_numPlayers);
		
		//line 2: time limit per turn
		JPanel ngLine2 = new JPanel();
		ngLine2.setLayout(new BoxLayout(ngLine2, BoxLayout.X_AXIS));
		_timeLimitCB = new JCheckBox("Turn time limit?", false);
		_timeLimitCB.setHorizontalTextPosition(SwingConstants.LEFT);
		//timeLimitCB.setMnemonic(KeyEvent.VK_T);
		_timeLimitCB.addItemListener(this);
		ngLine2.add(_timeLimitCB);
		ngLine2.add(Box.createHorizontalGlue());
		
		_timeLimit = new JSlider(
				JSlider.HORIZONTAL, Defaults.MIN_TURN_LIMIT_SECS,
				Defaults.MAX_TURN_LIMIT_SECS, Defaults.TURN_LIMIT_SECS );
		//more slider options
		//currently too long
		_timeLimit.setEnabled(false);
		ngLine2.add(_timeLimit);
		
		//line 3: total game turn limit
		JPanel ngLine3 = new JPanel();
		ngLine3.setLayout(new BoxLayout(ngLine3, BoxLayout.X_AXIS));
		
		_turnLimitCB = new JCheckBox( "Limit game length?", 
			    					  Defaults.LIMIT_DURATION );
		_turnLimitCB.setHorizontalTextPosition(SwingConstants.LEFT);
		//turnLimitCB.setMnemonic(KeyEvent.VK_L);
		_turnLimitCB.addItemListener(this);
		
		_turnLimit = new JSlider(
				JSlider.HORIZONTAL, Defaults.MIN_TURNS_LIMIT,
				Defaults.MAX_TURNS_LIMIT, Defaults.PLAYER_TURNS_LIMIT );
		//more slider options; possibly too long
		// specifically, list times in min:sec form, and only list a given few
		_turnLimit.setEnabled(false);
		
		ngLine3.add(_turnLimitCB);
		ngLine3.add(Box.createHorizontalGlue());
		ngLine3.add(_turnLimit);
		
		
		//line 4: alt wincon
		JPanel ngLine4 = new JPanel();
		ngLine4.setLayout(new BoxLayout(ngLine4, BoxLayout.X_AXIS));

		_winconCB = new JCheckBox( "<html>Use an alternative<br>" +
								   "win condition?</html>", false );
		_winconCB.setHorizontalTextPosition(SwingConstants.LEFT);
		//winconCB.setMnemonic(KeyEvent.VK_C);
		_winconCB.addItemListener(this);
		_winconSelector = (JComboBox)generateWinconSelector();
		_winconSelector.setEnabled(false);

		ngLine4.add(_winconCB);
		ngLine4.add(Box.createHorizontalGlue());
		ngLine4.add(_winconSelector);
		
		
		
		_netGameStgs.setLayout(new BoxLayout(_netGameStgs, BoxLayout.Y_AXIS));
		_netGameStgs.add(ngLine1);
		_netGameStgs.add(Box.createRigidArea(innerSpacing));
		_netGameStgs.add(ngLine2);
		_netGameStgs.add(Box.createRigidArea(innerSpacing));
		_netGameStgs.add(ngLine3);
		_netGameStgs.add(Box.createRigidArea(innerSpacing));
		_netGameStgs.add(ngLine4);
		_netGameStgs.add(Box.createRigidArea(innerSpacing));
		
		_netGameStgs.setBorder(BorderFactory.createTitledBorder(
												"Network game settings"));
		
		
		// Other network settings:
		// - Select port	- Test connection (with result label)
		// should also allow server address
		_otherNetStgs = new JPanel();
		_netComps.add(_otherNetStgs);
		_otherNetStgs.setLayout(new BoxLayout(_otherNetStgs,BoxLayout.Y_AXIS));
		
		JPanel portInfo = new JPanel();
		portInfo.setLayout(new BoxLayout(portInfo,BoxLayout.X_AXIS));
		
		portInfo.add(new JLabel("Use port # :"));
		_portNumber = new JTextField(5); //fucking huge
		//_portNumber.setMaximumSize(new Dimension(80, 20));
		_portNumber.setText( ""+Defaults.PREFERRED_PORT );
		portInfo.add(_portNumber);
		
		_serverAddr = new JTextField(20);
		
		_connectionCheck = new ConnectionCheck();
		((ConnectionCheck) _connectionCheck).addActionListener(this);
		
		
		
		_otherNetStgs.add(portInfo);
		_otherNetStgs.add(Box.createRigidArea(innerSpacing));
		_otherNetStgs.add(_connectionCheck);
		_otherNetStgs.setBorder(BorderFactory.createTitledBorder(
											  "Other network settings"));
		_otherNetStgs.setEnabled(false);
		
		
		
		JPanel leftSide = new JPanel();
		//leftSide.setLayout(new BorderLayout());
		//leftSide.add(uniSettings, BorderLayout.CENTER);
		//leftSide.add(qpSettings, BorderLayout.PAGE_END);
		leftSide.setLayout(new BoxLayout(leftSide, BoxLayout.Y_AXIS));
		leftSide.add(uniSettings);
		leftSide.add(Box.createRigidArea(outerSpacing));
		leftSide.add(_qpSettings);
		leftSide.add(Box.createVerticalGlue());
		leftSide.add(Box.createRigidArea(new Dimension(0, 1)));
		
		
		JPanel rightSide = new JPanel();
		_netComponents = rightSide;
		rightSide.setLayout(new BoxLayout(rightSide, BoxLayout.Y_AXIS));
		//rightSide.add(Box.createRigidArea(new Dimension(10, 0)));
		//rightSide.add(Box.createRigidArea(new Dimension(250, 500)));
		rightSide.add(_netGameStgs);
		rightSide.add(Box.createRigidArea(outerSpacing));
		rightSide.add(_otherNetStgs);
		rightSide.add(Box.createVerticalGlue());
		rightSide.add(Box.createRigidArea(new Dimension(0, 1)));
		
		settingsPane.add(Box.createRigidArea(new Dimension(1, 1)));
		settingsPane.add(Box.createHorizontalGlue());
		settingsPane.add(leftSide);
		settingsPane.add(Box.createHorizontalGlue());
		settingsPane.add(rightSide);
		settingsPane.add(Box.createHorizontalGlue());
		settingsPane.add(Box.createRigidArea(new Dimension(1, 1)));
		
		
		
		// Outer buttons
		_startGameBtn = new JButton("Start game");
		_returnMainBtn = new JButton("Return to main menu");
		_useDefaultsBtn = new JButton("Apply defaults");
		//init btns
		_startGameBtn.setActionCommand(MainConstants.START_GAME);
		_startGameBtn.addActionListener(al);
		_returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		_returnMainBtn.addActionListener(al);
		_useDefaultsBtn.setActionCommand("DEFAULTS");
		_useDefaultsBtn.addActionListener(this);
		
		//align right
		btnPane.add(Box.createHorizontalGlue());
		//add btns
		btnPane.add(_startGameBtn);
		btnPane.add(Box.createRigidArea(new Dimension(10, 0)));
		btnPane.add(_returnMainBtn);
		btnPane.add(Box.createRigidArea(new Dimension(10, 0)));
		btnPane.add(_useDefaultsBtn);
		
		setLayout(new BorderLayout());
		add(new JLabel(_title, SwingConstants.CENTER), BorderLayout.NORTH);
		
		add(settingsPane, BorderLayout.CENTER);
		
		add(btnPane, BorderLayout.PAGE_END);
		/*setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(new JLabel(_title, SwingConstants.CENTER));
		add(settingsPane);
		add(Box.createVerticalGlue());
		add(btnPane);*/
		
//		Dimension d = settingsPane.getSize();
//		System.out.println(d.width+" x "+d.height);
//		leftSide.setMaximumSize(new Dimension(d.width/2, d.height));
//		rightSide.setMaximumSize(new Dimension(d.width/2, d.height));
		
		// By default, do not go visible from within
		setEnabled(false);
		setVisible(false);
		
		useNetworkSettings(true);
	}
	
	
	
	
	public void useNetworkSettings(boolean network) {
		_useNetworkStgs = network;
		
		//enable and disable components accordingly
		//_netGameStgs.setEnabled( network );
		_netComponents.setEnabled( network );
		_qpSettings.setEnabled( !network );
	}

	public boolean usingNetworkSettings() {
		return _useNetworkStgs;
	}
	
	
	public void applyDefaults() {
		// TODO - apply defaults of all components
	}
	
	
	private Component generateMapSelector() {
		//STUBBED
		return new JLabel("Coming soon!   ");
	}
	
	private Component generateWinconSelector() {
		// TODO provide default list
		WinCondition[] wincons = new WinCondition[1];
		wincons[0] = new CheckWinnerTest();
		
		_winconSelector = new JComboBox(wincons);
		_winconSelector.setSelectedIndex(0);
		
		return _winconSelector;
	}

	
	public String getMap() {
		// TODO
		// settings: +w/h, +terrain options
		return "maps/test.qmap";
	}


	public void itemStateChanged(ItemEvent e) {
		Object source = e.getSource();
		
		if(source == _presetCB) {
			_mapSelector.setEnabled(((JCheckBox)source).isSelected());
			return;
		}
		
		if(source == _timeLimitCB) {
			_timeLimit.setEnabled(((JCheckBox)source).isSelected());
			return;
		}
		
		
		if(source == _turnLimitCB) {
			_turnLimit.setEnabled(((JCheckBox)source).isSelected());
			return;
		}
		
		if(source == _winconCB) {
			_winconSelector.setEnabled(((JCheckBox)source).isSelected());
			return;
		}
	}




	public boolean altWinCondition() {
		return _winconCB.isSelected();
	}




	public boolean areBunkersAllowed() {
		return _bunkers.isSelected();
	}




	public boolean areMountainsAllowed() {
		return _mtns.isSelected();
	}




	public boolean areTurnsTimed() {
		return _timeLimitCB.isSelected();
	}




	public int durationInTurns() {
		if(durationSet()) {
			//should not be adjusting when called
			return _turnLimit.getValue();
		}
		
		return -1; // no limit on duration
	}




	public boolean durationSet() {
		return _turnLimitCB.isSelected();
	}



	public int getAiDifficulty() {
		return _aiLevel.getValue();
	}




	public WinCondition getAltWinCon() {
		if(altWinCondition()) {
			Object o = ((JComboBox) _winconSelector).getSelectedItem();
			if (o instanceof WinCondition) {
				return ((WinCondition) o);
			}
		}
		return null;
	}




	public int getMapHeight() {
		return ((Integer)(_heightSpinner.getValue())).intValue();
	}




	public int getMapWidth() {
		return ((Integer)(_widthSpinner.getValue())).intValue();
	}




	public int getMaxUnits() {
		return ((Integer)(_unitsSpinner.getValue())).intValue();
	}




	public int getPort() {
		int port = ((ConnectionCheck) _connectionCheck)
								.validPortNumber(_portNumber.getText());
		
		if(port > 0)	return port;
		
		return Defaults.PREFERRED_PORT;
	}




	public String getPresetMap() {
		// TODO Auto-generated method stub
		if(_presetCB.isSelected()) {
			//User has chosen to use a preset map
			if(_mapSelector instanceof ItemSelectable) {
				return (String)(((ItemSelectable)_mapSelector).getSelectedObjects()[0]);
			}
			else {
				System.err.println("Preset maps not yet selectable.");
			}
		}
		return null;
	}




	public int getStartingResources() {
		return ((Integer)(_resSpinner.getValue())).intValue();
	}




	public boolean isWaterAllowed() {
		return _water.isSelected();
	}




	public int numPlayers() {
		return ((Integer)(_numPlayers.getSelectedValue())).intValue();
	}




	public int turnTimeLimit() {
		if(areTurnsTimed()) {
			return _timeLimit.getValue();
		}
		
		return -1;
	}




	public boolean usingPresetMap() {
		if(_mapSelector instanceof ItemSelectable) {
			return _presetCB.isSelected();
		}
		
		//System.err.println("Preset maps not yet selectable.");
		return false;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("connectioncheck_testbutton")) {
			// test it with the appropriate input
			
			return;
		}
		
		if(e.getActionCommand().equals("DEFAULT")) {
			applyDefaults();
		}
	}


}
