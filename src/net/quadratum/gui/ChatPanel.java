package net.quadratum.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatPanel extends JPanel {
	private ChatHandler _chatHandler;
	private MessageDisplay _messageDisplay;
	
	private JTextArea _area;
	private JTextField _field;
	private JButton _fieldButton;
	private JCheckBox _show;
	
	public ChatPanel(ChatHandler chatHandler, MessageDisplay messageDisplay) {
		_chatHandler = chatHandler;
		_messageDisplay = messageDisplay;
		
		ActionListener actionListener = new ChatPanelActionListener();
		ItemListener itemListener = new ChatPanelItemListener();
		
		setLayout(new BorderLayout());
		
		//Set up main area of panel
		JPanel ctr = new JPanel();
		ctr.setLayout(new BorderLayout());
		
		//Set up main text area
		StaticMethods.STD std = StaticMethods.createScrollingTextDisplay(5);
		_area = std._jta;
		ctr.add(std._jsp, BorderLayout.CENTER);
		
		///Set up text entry area with text field and button
		JPanel textEntryPanel = new JPanel();
		textEntryPanel.setLayout(new BorderLayout());
		
		//Set up text field
		_field = new JTextField();
		_field.addActionListener(actionListener);
		textEntryPanel.add(_field, BorderLayout.CENTER);
		
		//Set up text entry button
		_fieldButton = new JButton("Send");
		_fieldButton.addActionListener(actionListener);
		textEntryPanel.add(_fieldButton, BorderLayout.EAST);
		
		ctr.add(textEntryPanel, BorderLayout.SOUTH);
		
		add(ctr, BorderLayout.CENTER);
		
		///Add labeled show/hide checkbox
		_show = new JCheckBox("Show recent messages over map", _messageDisplay.getShowMessages());
		_show.addItemListener(itemListener);
		add(_show, BorderLayout.SOUTH);
		
		///Disable input until start method is called
		_field.setEnabled(false);
		_fieldButton.setEnabled(false);
		_show.setEnabled(false);
		
		_chatHandler.setChatPanel(this);
	}
	
	public void start() {
		_field.setEnabled(true);
		_fieldButton.setEnabled(true);
		_show.setEnabled(true);
	}
	
	public void addMessage(int from, String message) {
		String msg = _chatHandler.getPlayerName(from) + ": " + message;
		synchronized(_area) {
			if(_area.getText().length()>0)
				_area.append("\n");
			_area.append(msg);
			_area.setCaretPosition(_area.getDocument().getLength());
			_area.validate();
			_messageDisplay.newMessage(from, msg);
		}
	}
	
	private class ChatPanelActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(_field.getText().length()>0) {
				_chatHandler.outgoingMessage(_field.getText());
				_field.setText("");
			}
		}
	}
	
	private class ChatPanelItemListener implements ItemListener {
		public void itemStateChanged(ItemEvent e) {
			_messageDisplay.setShowMessages(e.getStateChange()==ItemEvent.SELECTED);
		}
	}
}