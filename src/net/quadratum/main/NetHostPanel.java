package net.quadratum.main;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import net.quadratum.core.Player;
import net.quadratum.main.MainConstants.Defaults;
import net.quadratum.network.NetworkPlayer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;


public class NetHostPanel extends JPanel implements ActionListener {

	
	private JList _pList;
	private DefaultListModel _listModel;
	private JTextArea _messageBox;
	private JButton _bootBtn, _messagePBtn, _messageAllBtn, _beginBtn;
	
	private MainGui _main;
	private ActionServerThread _serverThd;
	//private Pair _lastPair;
	
	private boolean _listEmpty = true;
	private List<Integer> _playerIds;
	private final int _minPlayers = Defaults.MIN_PLAYERS;
	private int _chosenNumPlayers;
	private int _maxPlayers = Defaults.MAX_PLAYERS;
	private int _port;
	
	private static final int WARNING = 0;
	private static final int LIST_CHANGE = 1;
	
	public NetHostPanel(MainGui mg, Settings set) {
		
		super();
		this.setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		_main = mg;
		_chosenNumPlayers = set.numPlayers();
		_port = set.getPort();
		
		// Title; List of connected players; Player-action buttons;
		//  Other buttons
		JLabel title = new JLabel("Hosting new game");
		title.setAlignmentX(SwingConstants.CENTER);
		
		JPanel centerPane = new JPanel();
		centerPane.setLayout(new BoxLayout(centerPane, BoxLayout.X_AXIS));
		JPanel leftHalf = new JPanel();
		JPanel rightHalf = new JPanel();
		leftHalf.setLayout(new BoxLayout(leftHalf, BoxLayout.Y_AXIS));
		rightHalf.setLayout(new BoxLayout(rightHalf, BoxLayout.Y_AXIS));
		
		_listEmpty = true;
		_listModel = new DefaultListModel();
		_listModel.addElement("No players connected yet.");
		_pList = new JList(_listModel);
		_pList.setVisibleRowCount(Defaults.MAX_PLAYERS);
		_pList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_pList.setLayoutOrientation(JList.VERTICAL);
		_pList.setBorder(BorderFactory.createTitledBorder("Connected players"));
		
		leftHalf.add(new JLabel("Connected players:"));
		leftHalf.add(_pList);
		
		_bootBtn = new JButton("Boot player");
		_bootBtn.setActionCommand("BOOT");
		_bootBtn.addActionListener(this);
		_bootBtn.setEnabled(false);
		
		_beginBtn = new JButton("Begin");
		_beginBtn.setActionCommand("BEGIN");
		_beginBtn.addActionListener(this);
		
		rightHalf.add(_bootBtn);
		rightHalf.add(Box.createRigidArea(new Dimension(0, 15)));
		rightHalf.add(_beginBtn);
		
		centerPane.add(leftHalf);
		centerPane.add(Box.createRigidArea(new Dimension(3, 0)));
		centerPane.add(rightHalf);
		centerPane.setAlignmentX(SwingConstants.CENTER);
		
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.X_AXIS));
		
		JButton returnMainBtn = new JButton("Return to main.");
		returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		returnMainBtn.addActionListener(this);
		
		_messageBox = new JTextArea(5, 10);
		_messageBox.append("Message log --- ");
		_messageBox.setEditable(false);
		_messageBox.setLineWrap(true);
		_messageBox.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane( _messageBox,
							  ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
							  ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );

		bottomPane.add(_messageBox);
		bottomPane.add(Box.createHorizontalGlue());
		bottomPane.add(returnMainBtn);
		

		add(Box.createVerticalGlue());
		add(title, BorderLayout.NORTH);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(centerPane, BorderLayout.CENTER);
		add(Box.createRigidArea(new Dimension(0, 40)));
		add(bottomPane, BorderLayout.SOUTH);
		add(Box.createVerticalGlue());
		
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setEnabled(false);
		setVisible(false);
	}
	
	public void start() {
		try {
			_serverThd = new ActionServerThread(_port, this);
			_playerIds = new LinkedList<Integer>();
		} catch (Exception e) {
			logMessage("Could not open server connection on port " + _port, WARNING);
			
			try {
				_serverThd.stopListening();
			} catch (Exception ee) {}
		}
	}
	
	public synchronized void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		if(s.equals(MainConstants.RETURN_MAIN)) {
			//clean up
			_serverThd.stopListening();
			_messageBox.setText("");
			_main.actionPerformed(new ActionEvent(this, 42, s));
		}
		
		
		if(s.equals("BOOT")) {
			//Check that player is selected
			int listInd = _pList.getSelectedIndex();
			if(listInd < 0) {
				return;
			}
			//Remove from both lists, inform ServerThread
			Player p = ((Player) _listModel.getElementAt(listInd));
			int playerId = _playerIds.get(listInd);
			_listModel.remove(listInd);
			updateList(listInd);
			
			_serverThd.kick(p);
			logMessage("Player " + playerId + " booted.", LIST_CHANGE);
			return;
		}
		
		if(s.equals("BEGIN")) {
			// Freeze and get list for verification
			_serverThd.freeze(true);
			//Player[]
			List<NetworkPlayer> currList = _serverThd.getCurrentPlayers();//.toArray(new Player[] {});
			
			int len = currList.size();
			if(len == _pList.getModel().getSize()) {
				for(int i = 0; i<len; i++) {
					if(currList.get(i) != _pList.getModel().getElementAt(i)) {
						logMessage("Warning - player list changed. Verify you" +
								" have the players you want, then try again.", WARNING);
						_serverThd.freeze(false);
						return;
					}
				}
				// We have the right list
				if(len < _minPlayers) {
					logMessage("Not enough players to start. Wait and try again.", WARNING);
					_serverThd.freeze(false);
					return;
				}
				if(len > _minPlayers) {
					logMessage("Too many players - boot some and try again.", WARNING);
					return;
				}
				if(len != _chosenNumPlayers) {
					//prompt - are you sure?
				}
				logMessage("Starting game.");
				_serverThd.stopListening();
				_main.createNetworkGame(currList);
				
				//clear stuff
				return;
			}
			
			else {
				logMessage("Warning - player list changed. Verify you" +
						" have the players you want, then try again.", WARNING);
				return;
			}
		}
		
		
		if(s.equals("PLAYER_ADDED")) {
			Player p = _serverThd.getNewestPlayer();
			int loc = _listModel.size();
			_playerIds.add(e.getID(), loc);
			_listModel.add(loc, p);
			logMessage("Player " + e.getID() + " connected.", LIST_CHANGE);
			updateList(loc);
		}
		
		
		if(s.equals("PLAYER_DISCONNECTED")) {
			int ind = e.getID();
			if(ind >= _listModel.size()) {
				//uh-oh
			}
			int listInd = _playerIds.indexOf(ind);
			_playerIds.remove(ind);
			_listModel.remove(listInd);
			logMessage("Player " + ind + " disconnected.", LIST_CHANGE);
			updateList(listInd);
		}
	}
	
	private synchronized void logMessage(String msg) {
		_messageBox.append("\n");
		_messageBox.append(msg);
		_messageBox.setCaretPosition(_messageBox.getDocument().getLength());
		_messageBox.validate();
	}
	
	private synchronized void logMessage(String msg, int source) {
		//should use an enum with values for different types of status messages
		// as well as, eventually, players 1 to maxConnections
		switch (source) {
		case WARNING : _messageBox.setForeground(Color.RED); break;
		case LIST_CHANGE : _messageBox.setForeground(Color.BLUE); break;
		default : _messageBox.setForeground(Color.BLACK);
		}
		logMessage(msg);
	}
	
	private void updateList(int i) {
		if(_listEmpty) {
			if(_listModel.size() > 0) {
				_listModel.remove(0);
				_listEmpty = false;
				_bootBtn.setEnabled(true);
			}
		}
		else {
			if(i == 0) {
				_listModel.addElement("No players connected yet.");
				_listEmpty = true;
				_bootBtn.setEnabled(false);
			}
		}
	}
}
