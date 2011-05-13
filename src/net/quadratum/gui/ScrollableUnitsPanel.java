package net.quadratum.gui;

import net.quadratum.core.Unit;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

/** A panel to display and select a player's units */
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
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	/** Paints the units in a row along with their names */
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
					g.setColor(DrawingMethods.FOREGROUND_COLOR);
					g.drawRect(xx, offy, WIDTH-1, HEIGHT-1);
				}
				
				_guiPlayer._drawingMethods.drawUnit(g.create(xx+uoffx, uoffy, maxUSize, maxUSize), u, BLOCK_SIZE, maxUSize);
				
				g.setColor(DrawingMethods.FOREGROUND_COLOR);
				String uname = u._name.substring(0, StaticMethods.findOnscreenLength(FMETR, u._name, WIDTH-2*PAD));
				int noffx = (WIDTH-(FMETR.stringWidth(uname)))/2;
				g.drawString(uname, xx+noffx, offy+HEIGHT-PAD-FMETR.getDescent());
				
				which++;
			}
		}
	}
	
	/** Notifies the component that the units have been updated */
	public synchronized void unitsUpdated() {
		synchronized(_guiPlayer._unitsData) {
			_units = _guiPlayer._unitsData.getPlayerUnits();
			_selID = _guiPlayer._unitsData.getSelectedID();
		}
	}
	
	/** Notifies the component taht the selected unit has changed */
	public synchronized void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			_selID = _guiPlayer._unitsData.getSelectedID();
		}
	}
	
	/** Gets the minimum size of this component */
	public Dimension getMinimumSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
	
	/** Gets the preferred size of this component */
	public synchronized Dimension getPreferredSize() {
		if(_units==null)
			return new Dimension(WIDTH, HEIGHT);
		else
			return new Dimension(Math.max(1, _units.size())*WIDTH, HEIGHT);
	}
	
	/** Gets the preferred size of the viewport displaying a portion of this component */
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	/** Gets the number of pixels corresponding to one "unit" of scrolling */
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 1;
	}
	
	/** Gets the number of pixels corresponding to one "block" of scrolling */
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		return 1;
	}
	
	/** Returns whether the component should be resized to match viewport height */
	public boolean getScrollableTracksViewportHeight() {
		return true;  //No vertical scrolling
	}
	
	/** Returns whether the component should be resized to match viewport width */
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}
	
	/** A mouse listener that responds to clicks by attempting to select the clicked-on unit */
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