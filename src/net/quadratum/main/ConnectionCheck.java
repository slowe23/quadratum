package net.quadratum.main;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * GUI component with button to test connection with whatever port is set,
 * as well as a label to display status messages.
 */

public class ConnectionCheck extends JPanel {

	public static final int MIN_PORT_NUM = 1024;
	public static final int MAX_REGISTERED = 49151;
	public static final int MIN_PRIVATE = 49152;
	public static final int MAX_PORT_NUM = 65535;
	
	private JButton _test;
	private JLabel  _status;
	
	
	public ConnectionCheck() {
		super();
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		_test = new JButton("Test connection");
		_test.setActionCommand(MainConstants.CHECK_CONN);
		add(_test);
		
		_status = new JLabel("             ", SwingConstants.CENTER);
		add(_status);
	}
	
	
	public boolean testConnection(String host, int port) {
		// TODO -stubbed
		Socket echo = null;
		
		try {
			echo = new Socket(host, 1025);
			echo.close();
		} catch (Exception e) {
			// TODO set status message accordingly
			
			_status.setText("Connection problem");
			return false;
		}
		finally {
			try {echo.close();}
			catch (Exception e) {}
		}
		
		_status.setText("Success!");
		return true;
	}
	
	public boolean testConnection(InetAddress addr, int port) {
		return testConnection(addr.toString(), port);
	}
	
	public boolean validPortNumber(int port) {
		return port >= MIN_PORT_NUM && port <= MAX_PORT_NUM;
	}
	
	/*
	 * @return - The int represented in port if it's a valid port,
	 * 		  or -1 if the int is invalid,
	 * 		  or -2 if the string can't be parsed to an int.
	 */
	public int validPortNumber(String port) {
		int p = -1;
		try {
			p = Integer.parseInt(port.trim());
		} catch (Exception e) {
			return -2;
		}
		return (validPortNumber(p) ? p : -1);
	}
	
	public void addActionListener(ActionListener al) {
		_test.addActionListener(al);
	}


//	@Override
//	public void actionPerformed(ActionEvent ev) {
//		Object source = ev.getSource();
//		
//		if(source == _test) {
//			
//		}
//		
//	}
}
