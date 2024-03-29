package net.quadratum.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.event.*;

/** A tabbed panel which isn't as ugly as the Swing one */
public class MyTabbedPanel extends JPanel {
	private MyTabBar _tabs;
	private JPanel _view;
	private int _selected;
	
	public MyTabbedPanel() {
		setLayout(new BorderLayout());
		
		_selected = -1;
		
		_tabs = new MyTabBar();
		add(_tabs, BorderLayout.NORTH);
		
		_view = new JPanel();
		_view.setLayout(new FillLayout(true));
		_view.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Color.black), BorderFactory.createLineBorder(Color.white, 5)));
		add(_view, BorderLayout.CENTER);
	}
	
	/**
	 * Adds a new tab to this panel
	 *
	 * @param s The title of the tab
	 * @param c The associated component
	 */
	public void addTab(String s, Component c) {
		addTab(s, c, false);
	}
	
	/**
	 * Adds a new tab to this panel
	 *
	 * @param s The title of the tab
	 * @param c The associated component
	 * @param sel Whether to select the newly-created tab
	 */
	public void addTab(String s, Component c, boolean sel) {
		if(s==null || c==null)
			throw new NullPointerException();
		
		_tabs.addTab(s);
		_view.add(c);
		
		if(_view.getComponentCount()==1)
			selectFirst();
		else if(sel)
			setSelected(_view.getComponentCount()-1);
		else
			c.setVisible(false);
	}
	
	/** Selects the first tab */
	private void selectFirst() {
		_selected = 0;
		Component selected = _view.getComponent(_selected);
		selected.setVisible(true);
	}
	
	/** Selects the tab with the given index */
	public void setSelected(int s) {
		int osel = _selected;
		
		if(s>=0 && s<_view.getComponentCount()) {
			_selected = s;
		
			if(osel!=_selected) {
				Component oC = _view.getComponent(osel);
				Component nC = _view.getComponent(_selected);
				
				if(oC!=nC) {
					oC.setVisible(false);
					nC.setVisible(true);
					_view.validate();
				}
				
				repaint();
			}
		}
	}
	
	/** A panel for displaying and interacting with the tab bar */
	private class MyTabBar extends JPanel {
		private ArrayList<String> _tabs;
		
		private final Font FONT;
		private final FontMetrics FMETRICS;
		
		private static final int XPAD = 5, YPAD = 3;
		
		public MyTabBar() {
			setBackground(Color.LIGHT_GRAY);
			
			_tabs = new ArrayList<String>();
			
			FONT = getFont();
			FMETRICS = getFontMetrics(FONT);
			
			setPreferredSize(new Dimension(0, FMETRICS.getAscent()+FMETRICS.getDescent()+YPAD));
			
			MouseInputListener mouseIn = new MyTabBarMouseInputListener();
			
			addMouseListener(mouseIn);
			addMouseMotionListener(mouseIn);
		}
		
		/** Adds a new tab to the tab bar */
		public void addTab(String s) {
			_tabs.add(s);
		}
		
		/** Displays the tab bar */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			g.setColor(Color.black);
			g.drawLine(0, getHeight()-1, getWidth()-1, getHeight()-1);
			
			g.setFont(FONT);
			int y = FMETRICS.getAscent();
			int x = getWidth()-1;
			for(int i = _tabs.size()-1; i>=0; i--) {
				String str = _tabs.get(i);
				int width = FMETRICS.stringWidth(str)+2*XPAD;

				x -= width;
				
				g.setColor(Color.WHITE);
				g.fillRoundRect(x, YPAD, width, getHeight(), XPAD, XPAD);
				
				g.setColor(Color.BLACK);
				g.drawRoundRect(x, YPAD, width, getHeight()-1, XPAD, XPAD);
			
				if(i==_selected) {
					g.drawString(str, x+XPAD, y+YPAD);
				} else {
					g.setColor(Color.BLACK);
					g.drawLine(x, getHeight()-1, x+width-1, getHeight()-1);
					g.setColor(Color.GRAY);	
					g.drawString(str, x+XPAD, y+YPAD);
				}
			}
		}
		
		/** A class for handling mouse input */
		private class MyTabBarMouseInputListener extends MouseInputAdapter {
			public void mousePressed(MouseEvent e) {
				reactToEvent(e);
			}
			
			public void mouseDragged(MouseEvent e) {
				reactToEvent(e);
			}
			
			public void mouseReleased(MouseEvent e) {
				reactToEvent(e);
			}
			
			private void reactToEvent(MouseEvent e) {
				int sel = getTab(e.getX(), e.getY());
				if(sel!=_selected)
					setSelected(sel);
			}
			
			private int getTab(int x, int y) {
				if(x<0 || x>=getWidth() || y<0 || y>=getHeight())
					return _selected;
				
				int xx = getWidth();
				for(int i = _tabs.size()-1; i>=0; i--) {
					int width = FMETRICS.stringWidth(_tabs.get(i))+2*XPAD;
					xx -= width;
					if(x>=xx && x<xx+width)
						return i;
				}
				
				return -1;
			}
		}
	}
}