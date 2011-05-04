package net.quadratum.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

import javax.swing.JPanel;

public class MessageOverlay extends JPanel implements MessageDisplay {
	private GUIPlayer _guiPlayer;
	
	private final Font FONT;
	private final FontMetrics FMETR;
	
	private static final int MSG_TIME = 7500;  //Max time in ms to display messages
	private static final int MSG_NUM = 10;  //Max number of messages to display
	private static final int MSG_PAD = 5;  //Number of pixels from the edge to pad messages
	
	private boolean _showMessages;
	
	private Deque<Message> _msgs; //Messages to display
	
	public MessageOverlay(GUIPlayer player) {
		_guiPlayer = player;
		
		FONT = getFont();
		FMETR = getFontMetrics(FONT);
		
		_showMessages = true;
		_msgs = new LinkedList<Message>();

		setOpaque(false);
	}
	
	public void setShowMessages(boolean b) {
		_showMessages = b;
		repaint();
	}
	
	public void showMessages() {
		setShowMessages(true);
	}
	
	public void hideMessages() {
		setShowMessages(false);
	}
	
	public boolean getShowMessages() {
		return _showMessages;
	}
	
	public void newMessage(int id, String message) {
		newMessage(new Message(message.split("\n"), _guiPlayer._drawingMethods.getPlayerColor(id)));
	}
	
	public void newMessage(String message) {
		newMessage(new Message(message.split("\n"), _guiPlayer._drawingMethods.FOREGROUND_COLOR));
	}
	
	private void newMessage(Message mess) {
		final Deque<Message> DQ = _msgs;
		
		javax.swing.Timer t = new javax.swing.Timer(MSG_TIME, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized(DQ) {
					DQ.removeLast();
				}
				repaint();
			}
		});
		t.setRepeats(false);
		
		synchronized(DQ) {
			DQ.addFirst(mess);
			t.start();
		}
		
		repaint();
	}
	
	public void clearMessages() {
		_msgs = new LinkedList<Message>();
		
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(FONT);
		
		int y = getHeight()-FMETR.getDescent()-MSG_PAD;
		int w = Math.max(getWidth()/2-2*MSG_PAD, 1);
		
		if(_showMessages) {
			Deque<Message> msgs = _msgs;  //Get a local reference in case the global one changes
			synchronized(msgs) {
				int n = 0;
				for(Message m:msgs) {
					String[] ss = m._lines;
					if(n>=MSG_NUM || y<FMETR.getAscent()+MSG_PAD)
						break;
					
					n++;
					
					for(int s = ss.length-1; s>=0 && y>=FMETR.getAscent()+MSG_PAD; s--) {
						String[] sss = StaticMethods.getWrap(FMETR, ss[s], w);
						
						int lines = 0;
						int max = 0;
						int ny = y;
						
						for(int ssss = sss.length-1; ssss>=0 && ny>=FMETR.getAscent()+MSG_PAD; ssss--) {
							max = Math.max(max, FMETR.stringWidth(sss[ssss]));
							lines++;
							
							ny -= FMETR.getHeight();
						}
						
						g.setColor(_guiPlayer._drawingMethods.BACKGROUND_COLOR);
						g.fillRect(0, ny+FMETR.getHeight()-FMETR.getAscent(), 2*MSG_PAD+max, lines*FMETR.getHeight());
						
						
						g.setColor(_guiPlayer._drawingMethods.FOREGROUND_COLOR);
						for(int ssss = sss.length-1; ssss>=0 && ny>=FMETR.getAscent()+MSG_PAD; ssss--) {
							g.drawString(sss[ssss], MSG_PAD, y);
							y -= FMETR.getHeight();
						}
						
						g.setColor(m._color);
						g.drawRect(-1, ny+FMETR.getHeight()-FMETR.getAscent(), 2*MSG_PAD+max, lines*FMETR.getHeight()-1);
						
						y -= MSG_PAD;
					}
				}
			}
		}
	}
	
	private static class Message {
		public final String[] _lines;
		public final Color _color;
		
		public Message(String[] lines, Color color) {
			_lines = lines;
			_color = StaticMethods.applyAlpha(color, 255);  //Opaque version of the given color
		}
	}
}