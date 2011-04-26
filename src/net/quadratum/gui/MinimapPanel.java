package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class MinimapPanel extends JPanel {
	private static final Color[] T_COLS = {new Color(0, 127, 0), new Color(100, 75, 20), new Color(0,0,127)};  //Probably temporary
	private static final Color[] P_COLS = {Color.red, Color.blue};
	
	private static final int SCALE = 3;  //Pixels per map square
	private static final int BORDER = 5;
	
	private MapView _view;  //Contains information about currently visible map region
	private UnitHolder _units;  //Contains unit information
	private BufferedImage _terrain;  //An image of the map terrain
	
	public MinimapPanel(MapView m, UnitHolder u) {
		_view = m;
		_units = u;
		
		setBackground(Color.BLACK);

		/*
		addMouseListener(this);
		addMouseMotionListener(this);
		 */
	}
	
	public void setTerrain(int[][] terrain) {
		_terrain = new BufferedImage(terrain.length, terrain[0].length, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i<_terrain.getWidth(); i++)
			for(int j = 0; j<_terrain.getHeight(); j++)
				_terrain.setRGB(i, j, getTerrainColor(terrain[i][j]).getRGB());
	}
	
	//Gets the color for the given terrain type
	private Color getTerrainColor(int i) {
		if(i>=0 && i<T_COLS.length)
			return T_COLS[i];
		
		return Color.BLACK;
	}
	
	private Color getUnitColor(Unit u) {
		int i = u._owner;
		if(i>=0 && i<P_COLS.length)
			return P_COLS[i];
		
		return Color.WHITE;
	}
	
	private int[] calcOffset() {
		int offx, offy;
		
		int w_width = getWidth()-2*BORDER, w_height = getHeight()-2*BORDER;
		int t_width = SCALE*_terrain.getWidth()+2, t_height = SCALE*_terrain.getHeight()+2;
		double v_width = SCALE*_view.getViewWidth(), v_height = SCALE*_view.getViewHeight();
		double v_x = SCALE*_view.getViewX(), v_y = SCALE*_view.getViewY();
		
		if(t_width<= w_width) {
			offx = CM.round((w_width-t_width)/2.0);
		} else {
			offx = CM.round((w_width-v_width)/2.0-v_x);
			if(offx>0)
				offx = 0;
			if(offx+t_width<w_width)
				offx = w_width-t_width;
		}
		
		if(t_height <= w_height) {
			offy = CM.round((w_height-t_height)/2.0);
		} else {
			offy = CM.round((w_height-v_height)/2.0-v_y);
			if(offy>0)
				offy = 0;
			if(offy+t_height<w_height)
				offy = w_height-t_height;
		}
		
		offx += 1;  //For the one-pixel border around map edge
		offy += 1;
		
		offx += BORDER;  //For the border around the panel edge
		offy += BORDER;
		
		return new int[]{offx, offy};
	}
	
	// Paints the minimap
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(_terrain!=null) {
			int[] off = calcOffset();
			
			int offx = off[0], offy = off[1];
			
			g.drawImage(_terrain, offx, offy, SCALE*_terrain.getWidth(), SCALE*_terrain.getHeight(), this);
			g.setColor(Color.gray);
			g.drawRect(offx-1, offy-1, SCALE*_terrain.getWidth()+2-1, SCALE*_terrain.getHeight()+2-1);
			
			Unit selected = _units.getSelectedUnit();
			
			///Draw dots for the units
			Map<MapPoint, Unit> units = _units.getUnits();
			for(MapPoint p : units.keySet()) {
				Unit unit = units.get(p);
				g.setColor(getUnitColor(unit));
				int x = offx + SCALE*p._x;
				int y = offy + SCALE*p._y;
				g.fillRect(x, y, SCALE, SCALE);
				
				if(unit==selected) {
					g.setColor(Color.white);
					g.drawRect(x-1, y-1, SCALE+2-1, SCALE+2-1);
				}
			}
			
			//Draw the border
			g.setColor(new Color(0,0,0,127));
			g.fillRect(0,0,getWidth(), BORDER);
			g.fillRect(0,getHeight()-BORDER,getWidth(), BORDER);
			
			g.fillRect(0,BORDER,BORDER,getHeight()-2*BORDER);
			g.fillRect(getWidth()-BORDER, BORDER, BORDER, getHeight()-2*BORDER);
			
			//Draw a box around the currently visible area
			g.setColor(Color.white);
			g.drawRect(offx+CM.round(SCALE*_view.getViewX())-1, offy+CM.round(SCALE*_view.getViewY())-1, CM.round(SCALE*_view.getViewWidth())+2-1, CM.round(SCALE*_view.getViewHeight())+2-1);
		}
	}
}