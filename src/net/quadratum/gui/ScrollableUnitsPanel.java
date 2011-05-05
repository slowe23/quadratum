package net.quadratum.gui;

import net.quadratum.core.Unit;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

//A panel to display and select a player's units
public class ScrollableUnitsPanel extends JPanel implements Scrollable {
	private final GUIPlayer _guiPlayer;
	
	private java.util.List<Unit> _units;
	private Integer _selID;
	
	private static final int BLOCK_SIZE = 4;
	private static final int MAX_SIZE = 12;
	private static final int N = 2;
	private static final int PAD = 4;
	private static final int WIDTH = N*MAX_SIZE*BLOCK_SIZE + 2*PAD;
	
	private final Font FONT;
	private final FontMetrics FMETR;
	
	private final int HEIGHT;
	
	public ScrollableUnitsPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		FONT = getFont();
		FMETR = getFontMetrics(FONT);
		
		HEIGHT = MAX_SIZE*BLOCK_SIZE+2*PAD+FMETR.getAscent()+FMETR.getDescent();
		
		addMouseListener(new ScrollableUnitsPanelMouseListener());
		
		setBackground(_guiPlayer._drawingMethods.BACKGROUND_COLOR);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		java.util.List<Unit> units;
		Integer selID;
		synchronized(this) {
			units = _units;
			selID = _selID;
		}
		
		if(units!=null) {
			g.setFont(FONT);
			
			int offx = (getWidth()-(units.size()*WIDTH))/2;
			int offy = (getHeight()-HEIGHT)/2;
			
			int maxUSize = (MAX_SIZE*BLOCK_SIZE);
			int uoffx = (WIDTH-maxUSize)/2, uoffy = offy+PAD;
			
			int which = 0;
			for(Unit u : units) {
				int xx = offx + which*WIDTH;
				
				if(selID!=null && u._id == selID) {
					g.setColor(_guiPlayer._drawingMethods.FOREGROUND_COLOR);
					g.drawRect(xx, offy, WIDTH-1, HEIGHT-1);
				}
				
				_guiPlayer._drawingMethods.drawUnit(g.create(xx+uoffx, uoffy, maxUSize, maxUSize), u, BLOCK_SIZE, maxUSize);
				
				g.setColor(_guiPlayer._drawingMethods.FOREGROUND_COLOR);
				String uname = u._name.substring(0, StaticMethods.findOnscreenLength(FMETR, u._name, WIDTH-2*PAD));
				int noffx = (WIDTH-(FMETR.stringWidth(uname)))/2;
				g.drawString(uname, xx+noffx, offy+HEIGHT-PAD-FMETR.getDescent());
				
				which++;
			}
		}
	}
	
	public synchronized void unitsUpdated() {
		synchronized(_guiPlayer._unitsData) {
			_units = _guiPlayer._unitsData.getPlayerUnits();
			_selID = _guiPlayer._unitsData.getSelectedID();
		}
	}
	
	public synchronized void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			_selID = _guiPlayer._unitsData.getSelectedID();
		}
	}
	
	public Dimension getMinimumSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	public synchronized Dimension getPreferredSize() {
		if(_units==null)
			return new Dimension(WIDTH, HEIGHT);
		else
			return new Dimension(Math.max(1, _units.size())*WIDTH, HEIGHT);
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 1;  //TODO: center on current unit or advance one unit if current unit is centered
	}
	
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 1;  //TODO: advance one unit
	}
	
	public boolean getScrollableTracksViewportHeight() {
		return true;  //No vertical scrolling
	}
	
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	private class ScrollableUnitsPanelMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			java.util.List<Unit> units;
			Integer selID;
			synchronized(ScrollableUnitsPanel.this) {
				units = _units;
				selID = _selID;
			}
			
			if(units!=null) {
				int offx = (getWidth()-(units.size()*WIDTH))/2;
				int offy = (getHeight()-HEIGHT)/2;
				int index = (e.getX()-offx)/WIDTH;
				if(e.getY()>=offy && e.getY()<offy+HEIGHT && e.getX()>=offx && index<units.size())
					_guiPlayer.selectUnit(units.get(index));
			}
		}
	}
}