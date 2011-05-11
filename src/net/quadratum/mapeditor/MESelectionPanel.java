package net.quadratum.mapeditor;

import net.quadratum.core.TerrainConstants;
import net.quadratum.core.MapPoint;
import net.quadratum.gui.DrawingMethods;

import java.io.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MESelectionPanel extends JPanel {
	public static final boolean HORIZONTAL = true, VERTICAL = false;
	
	private static final int SIZE = 32, PAD = 6;
	
	private final DrawingMethods _draw;
	
	private final boolean _horiz;
	
	private int _selection;
	private static final int ERASE = -1;
	private static final int[] TCONST = {TerrainConstants.WATER, TerrainConstants.BUNKER, TerrainConstants.MOUNTAIN, TerrainConstants.RESOURCES, TerrainConstants.IMPASSABLE};
	private static final int NPLAY = net.quadratum.core.Constants.MAX_PLAYERS;
	
	public MESelectionPanel(boolean isHorizontal) {
		_draw = new DrawingMethods();
		
		_horiz = isHorizontal;
		_selection = ERASE;
		
		addMouseListener(new MESelectionPanelMouseListener());
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	public int getAppropriateTerrain(int curTerrain, boolean option, boolean shift) {
		if(_selection==ERASE)
			return 0;
		
		if(_selection>=0 && _selection<TCONST.length) {
			if(option && shift)
				return 0;
			else if(option)
				return TCONST[_selection];
			else if(shift)
				return curTerrain & ~(TCONST[_selection]);
			else
				return curTerrain | TCONST[_selection];
		}
		
		return curTerrain;
	}
	
	public int getSelectedPlayer() {
		if(_selection<ERASE)
			return ERASE-1-_selection;
		else if(_selection==ERASE)
			return -1;
		else
			return -2;
	}
	
	public Dimension getPreferredSize() {
		if(_horiz)
			return new Dimension((1*(SIZE+PAD)+PAD)+1+(TCONST.length*(SIZE+PAD)+PAD)+1+(NPLAY*(SIZE+PAD)+PAD), PAD+SIZE+PAD);
		else
			return new Dimension(PAD+SIZE+PAD, (1*(SIZE+PAD)+PAD)+1+(TCONST.length*(SIZE+PAD)+PAD)+1+(NPLAY*(SIZE+PAD)+PAD));
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int x = PAD, y = PAD;
		
		int dx, dy;
		if(_horiz) {
			dx = 1;
			dy = 0;
		} else {
			dx = 0;
			dy = 1;
		}
		
		_draw.drawTerrainTile(g.create(x, y, SIZE, SIZE), 0, SIZE, false);
		if(_selection==ERASE)
			drawSelection(g, x, y);
		
		x += (SIZE+PAD)*dx;
		y += (SIZE+PAD)*dy;
		
		x += dx;
		y += dy;
		drawDivider(g, x, y);
		x += PAD*dx;
		y += PAD*dy;
		
		for(int i = 0; i<TCONST.length; i++) {
			_draw.drawTerrainFeatures(g.create(x, y, SIZE, SIZE), TCONST[i], SIZE);
			if(i==_selection)
				drawSelection(g, x, y);
			
			x += (SIZE+PAD)*dx;
			y += (SIZE+PAD)*dy;
		}
		
		x += dx;
		y += dy;
		drawDivider(g, x, y);
		x += PAD*dx;
		y += PAD*dy;
		
		Set<MapPoint> set = new HashSet<MapPoint>();
		MapPoint mP = new MapPoint(0, 0);
		set.add(mP);
		
		
		for(int i = 0; i<NPLAY; i++) {
			_draw.drawPlacementMask(g.create(x, y, SIZE, SIZE), i, set, mP, SIZE);

			if(_selection==ERASE-1-i)
				drawSelection(g, x, y);
			
			x += (SIZE+PAD)*dx;
			y += (SIZE+PAD)*dy;
		}
	}
	
	private void drawDivider(Graphics g, int x, int y) {
		g.setColor(DrawingMethods.NEUTRAL_COLOR);
		if(_horiz)
			g.drawLine(x, y, x, y+SIZE-1);
		else
			g.drawLine(x, y, x+SIZE-1, y);
	}
	
	private void drawSelection(Graphics g, int x, int y) {
		g.setColor(DrawingMethods.FOREGROUND_COLOR);
		g.drawRect(x-1, y-1, SIZE+2-1, SIZE+2-1);
	}
	
	private class MESelectionPanelMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int x = PAD, y = PAD;
			
			int dx, dy;
			if(_horiz) {
				dx = 1;
				dy = 0;
			} else {
				dx = 0;
				dy = 1;
			}
			
			if(isIn(e, x, y)) {
				if(_selection!=ERASE) {
					_selection = ERASE;
					repaint();
				}
				return;
			}
			
			x += (SIZE+PAD)*dx;
			y += (SIZE+PAD)*dy;
			
			x += dx;
			y += dy;
			x += PAD*dx;
			y += PAD*dy;
			
			for(int i = 0; i<TCONST.length; i++) {
				if(isIn(e, x, y)) {
					if(_selection!=i) {
						_selection = i;
						repaint();
					}
					return;
				}
				
				x += (SIZE+PAD)*dx;
				y += (SIZE+PAD)*dy;
			}
			
			x += dx;
			y += dy;
			x += PAD*dx;
			y += PAD*dy;
			
			for(int i = 0; i<NPLAY; i++) {
				if(isIn(e, x, y)) {
					if(_selection!=ERASE-1-i) {
						_selection = ERASE-1-i;
						repaint();
					}
					return;
				}
				   
				x += (SIZE+PAD)*dx;
				y += (SIZE+PAD)*dy;
			}
		}
		
		private boolean isIn(MouseEvent e, int x, int y) {
			return e.getX()>=x && e.getY()>=y && e.getX()<x+SIZE && e.getY()<y+SIZE;
		}
	}
}