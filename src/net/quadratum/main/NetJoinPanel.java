package net.quadratum.main;

import javax.swing.JPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import net.quadratum.main.MainConstants.Defaults;

public class NetJoinPanel extends JPanel implements ActionListener {

	private JTextField _addrField, _portField;
	private JLabel _joinStatus, _message, _countdown;
	private Socket _sock;
	JButton _connectBtn, _disconnBtn;
	
	
	public NetJoinPanel(ActionListener al) {

		super();
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		JPanel topPane = new JPanel();
		topPane.setLayout(new BoxLayout(topPane, BoxLayout.X_AXIS));
		
		JLabel addrLbl = new JLabel("Host's address:", JLabel.CENTER);
		_addrField = new JTextField(20);
		_addrField.setMaximumSize(new Dimension(80, 25));
		addrLbl.setLabelFor(_addrField);
		
		JLabel portLbl = new JLabel("Host's port:", JLabel.CENTER);
		_portField = new JTextField(5);
		_portField.setText("" + Defaults.PREFERRED_PORT);
		_portField.setMaximumSize(new Dimension(80, 25));
		portLbl.setLabelFor(_portField);
		
		topPane.add(addrLbl);
		topPane.add(Box.createRigidArea(new Dimension(5, 0)));
		topPane.add(_addrField);
		topPane.add(Box.createRigidArea(new Dimension(15, 0)));
		topPane.add(portLbl);
		topPane.add(Box.createRigidArea(new Dimension(5, 0)));
		topPane.add(_portField);
		topPane.setAlignmentX(SwingConstants.CENTER);
		
		
		JPanel btnPane = new JPanel();
		btnPane.setLayout(new BoxLayout(btnPane, BoxLayout.X_AXIS));
		
		_connectBtn = new JButton("Connect to host");
		_connectBtn.setActionCommand("connectHost");
		_connectBtn.addActionListener(this);
		
//		JButton joinBtn = new JButton("Join");
//		joinBtn.setActionCommand("join");
//		joinBtn.addActionListener(this);
		
		_disconnBtn = new JButton("Disconnect");
		_disconnBtn.setActionCommand("disconnect");
		_disconnBtn.addActionListener(this);
		_disconnBtn.setEnabled(false);
		
		JButton returnMainBtn = new JButton("Return to main menu");
		returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		returnMainBtn.addActionListener(al);
		
		btnPane.add(returnMainBtn);
		btnPane.add(Box.createRigidArea(new Dimension(8,0)));
		btnPane.add(_connectBtn);
//		btnPane.add(Box.createRigidArea(new Dimension(8,0)));
//		btnPane.add(joinBtn);
		btnPane.add(Box.createRigidArea(new Dimension(8,0)));
		btnPane.add(_disconnBtn);
		btnPane.setAlignmentX(SwingConstants.CENTER);
		
		
		JPanel infoPane = new JPanel();
		infoPane.setLayout(new BoxLayout(infoPane, BoxLayout.Y_AXIS));
		
//		_joinStatus = new JLabel("");
//		_joinStatus.setAlignmentX(SwingConstants.CENTER);
		_message = new JLabel("Not yet connected");		// When connection succeeds or fails, post message here
		_message.setAlignmentX(SwingConstants.CENTER);
		
//		infoPane.add(_joinStatus);
//		infoPane.add(Box.createRigidArea(new Dimension(0, 8)));
		infoPane.add(_message);
		
		JPanel inPane = new JPanel();
		inPane.setLayout(new BoxLayout(inPane, BoxLayout.X_AXIS));
		
		JPanel left = new JPanel();
		left.add(new JLabel("Hosted game settings will go here"));
		JPanel right = new JPanel();
		
		
		inPane.add(left);
		inPane.add(Box.createRigidArea(new Dimension(1, 0)));
		inPane.add(right);
		
		infoPane.add(inPane);
		
		
		add(topPane);
		add(Box.createRigidArea(new Dimension(0, 15)));
		add(btnPane);
		add(Box.createRigidArea(new Dimension(0, 20)));
		add(infoPane);
		
		setVisible(false);
		setEnabled(true);
	}


	@Override
	public void actionPerformed(ActionEvent ev) {
		// TODO Auto-generated method stub
		String s = ev.getActionCommand();
		
		if(s.equals("connectHost")) {
			//Make a socket
			// Parse input
			String addr = _addrField.getText();
			int port = ConnectionCheck.validPortNumber(_portField.getText());
			if(port < 0) {
				_message.setText("Invalid port number");
				return;
			}
			
			try {
				_sock = new Socket(addr, port);
			} catch (UnknownHostException uhe) {
				_message.setText("Cannot identify that host");
				return;
			} catch (IOException ioe) {
				_message.setText("Error connecting. Check that address and port are correct.");
				return;
			}
			
			_message.setText("Successfully connected!");
			
			_connectBtn.setEnabled(false);
			_disconnBtn.setEnabled(true);
		}
		
//		if(s.equals("join")) {
//			//act accordingly
//		}
		
		if(s.equals("disconnect")) {
			if(_sock != null) {
				_connectBtn.setEnabled(true);
				_disconnBtn.setEnabled(false);
				try {
					_sock.close();
				} catch (Exception e) {}
				
			}
		}
	}
	
	public synchronized boolean isConnected() {
		return (_sock != null && _sock.isConnected());
	}
}