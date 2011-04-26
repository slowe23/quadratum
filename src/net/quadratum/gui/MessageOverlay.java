package net.quadratum.gui;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class MessageOverlay extends JPanel implements MessageDisplay {
	private final Font FONT;
	private final FontMetrics FMETR;
	
	private static final int MSG_TIME = 7500;  //Max time in ms to display messages
	private static final int MSG_NUM = 5;  //Max number of messages to display
	private static final int MSG_PAD = 5;  //Number of pixels from the edge to pad messages
	
	private boolean _showMessages;
	private Deque<String[]> _msgs; //Messages  //TODO: Make this a fixed-size array with a parallel array of timers
	
	public MessageOverlay() {
		setOpaque(false);
		
		FONT = getFont();
		FMETR = getFontMetrics(FONT);
		
		_showMessages = true;
		_msgs = new LinkedList<String[]>();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setFont(FONT);
		
		int y = getHeight()-FMETR.getDescent()-MSG_PAD;
		int w = Math.max(getWidth()/2-2*MSG_PAD, 1);
		
		if(_showMessages) {
			Deque<String[]> msgs = _msgs;  //Get a local reference in case the global one changes
			synchronized(msgs) {
				int n = 0;
				for(String[] ss:msgs) {
					if(n>=MSG_NUM || y<FMETR.getAscent()+MSG_PAD)
						break;
					
					n++;
					
					for(int s = ss.length-1; s>=0 && y>=FMETR.getAscent()+MSG_PAD; s--) {
						String[] sss = getWrap(ss[s], w);
						
						int lines = 0;
						int max = 0;
						
						g.setColor(new Color(255, 255, 255, 255));
						
						for(int ssss = sss.length-1; ssss>=0 && y>=FMETR.getAscent()+MSG_PAD; ssss--) {
							max = Math.max(max, FMETR.stringWidth(sss[ssss]));
							lines++;
							
							g.drawString(sss[ssss], MSG_PAD, y);
							y -= FMETR.getHeight();
						}
						g.setColor(new Color(255, 255, 255, 255));  //TODO: adjust this based on who is sending the message
						g.drawRect(-1, y+FMETR.getHeight()-FMETR.getAscent(), 2*MSG_PAD+max, lines*FMETR.getHeight()-1);
						g.setColor(new Color(255, 255, 255, 63));
						g.fillRect(0, y+FMETR.getHeight()-FMETR.getAscent(), 2*MSG_PAD+max, lines*FMETR.getHeight());
						
						y -= MSG_PAD;
					}
				}
			}
		}
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
	
	public void newMessage(String str) {
		newMessage(str.split("\n"));
	}
	
	private void newMessage(String[] strs) {
		final Deque<String[]> DQ = _msgs;
		
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
			DQ.addFirst(strs);
			t.start();
		}
		
		repaint();
	}
	
	public void clearMessages() {
		_msgs = new LinkedList<String[]>();
		
		repaint();
	}
	
	private String[] getWrap(String s, int maxw) {
		ArrayList<String> list = new ArrayList<String>();
		String[] words = s.split(" ");
		String current = "";
		for(int i = 0; i<words.length; i++) {
			String word = words[i];
			
			String pot;
			if(current.length()>0)
				pot = current+" "+word;
			else
				pot = word;
			
			if(FMETR.stringWidth(pot)<=maxw)
				current = pot;
			else {
				list.add(current);
				current = "";
			}
			
			if(current=="") {
				while(FMETR.stringWidth(word)>maxw) {
					int brk = findOnscreenLength(word, maxw);
					list.add(word.substring(0, brk));
					word = word.substring(brk);
				}
				current = word;
			}
		}
		
		if(FMETR.stringWidth(current)<=maxw)
			list.add(current);
		
		return list.toArray(new String[list.size()]);
	}
	
	private int findOnscreenLength(String s, int maxw) {
		int sw = FMETR.stringWidth(s);
		
		if(sw<=maxw)
			return s.length();
		
		int minlength = 0;  //minlength is the longest substring of s known to fit
		int maxlength = s.length()-1;  //maxlength+1 is the shortest substring of s known not to fit
		
		int trylength = (maxw*s.length())/sw;
		if(trylength<=minlength)
			trylength = minlength+1;
		if(trylength>maxlength)
			trylength = maxlength;
		
		while(minlength < maxlength) {
			sw = FMETR.stringWidth(s.substring(0, trylength));
			if(sw<=maxw) {
				minlength = trylength;
				trylength = minlength+1;
			} else {
				maxlength = trylength-1;
				trylength = (maxw*trylength)/sw;
				if(trylength<=minlength)
					trylength = minlength+1;
				if(trylength>maxlength)
					trylength = maxlength;
			}
		}
		return maxlength;
	}
}