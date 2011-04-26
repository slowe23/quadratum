package net.quadratum.gui;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

public class ChatPanel extends JPanel implements ActionListener, ItemListener {
	private ChatHandler _chatHandler;
	private MessageDisplay _message;
	private JTextField _field;
	private JButton _fieldButton;
	private JTextArea _area;
	private JCheckBox _show;
	
	public ChatPanel(GUIPlayer player, MessageDisplay message) {
		_chatHandler = player.getChatHandler();
		_message = message;
		
		setLayout(new BorderLayout());
		
		///Add labeled show/hide checkbox
		_show = new JCheckBox("Show recent messages over map", _message.getShowMessages());
		_show.addItemListener(this);
		
		add(_show, BorderLayout.NORTH);
		
		JScrollPane jsp = CM.createScrollingTextDisplay();
		_area = (JTextArea)(jsp.getViewport().getView());
		_area.setRows(5);
		
		add(jsp, BorderLayout.CENTER);
		
		///Add text entry area with text field and button
		JPanel textEntryPanel = new JPanel();
		textEntryPanel.setLayout(new BorderLayout());
		
		_field = new JTextField();
		_field.addActionListener(this);
		
		textEntryPanel.add(_field, BorderLayout.CENTER);
		
		_fieldButton = new JButton("Send");
		_fieldButton.addActionListener(this);
		
		textEntryPanel.add(_fieldButton, BorderLayout.EAST);
		
		add(textEntryPanel, BorderLayout.SOUTH);
		
		_chatHandler.setChatPanel(this);
	}
	
	public void addMessage(int from, String message) {
		String msg = _chatHandler.getPlayerName(from) + ": " + message;
		
		synchronized(_area) {
			if(_area.getText().length()>0)
				_area.append("\n");
			_area.append(msg);
			
			_message.newMessage(from, msg);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		if(_field.getText().length()>0) {
			_chatHandler.sendMessage(_field.getText());
			_field.setText("");
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		_message.setShowMessages(e.getStateChange()==ItemEvent.SELECTED);
	}
}