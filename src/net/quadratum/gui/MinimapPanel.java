package net.quadratum.gui;

import net.quadratum.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;
import java.util.Set;

public class MinimapPanel extends JPanel {
	private static final int SCALE = 3;  //Pixels per map square
	private static final int BORDER = 5;
	
	private MapView _view;  //Contains information about currently visible map region
	private UnitHandler _unitHandler;  //Contains unit information
	private GraphicsCoordinator _graphicsCoordinator;  //Contains game-level graphics information
	private BufferedImage _terrain;  //An image of the map terrain
	private BufferedImage _placementMask;  //An image of available placement squares
	
	public MinimapPanel(GUIPlayer player, MapView m) {
		_unitHandler = player._unitHandler;
		_graphicsCoordinator = player._graphicsCoordinator;
		
		_view = m;
		
		setBackground(_graphicsCoordinator.getBackgroundColor());
	}
	
	public void setTerrain(int[][] terrain) {
		_terrain = new BufferedImage(terrain.length, terrain[0].length, BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i<_terrain.getWidth(); i++)
			for(int j = 0; j<_terrain.getHeight(); j++)
				_terrain.setRGB(i, j, _graphicsCoordinator.getTerrainColor(terrain[i][j]).getRGB());
	}
	
	public void setPlacementArea(Set<MapPoint> placement) {
		_placementMask = new BufferedImage(_terrain.getWidth(), _terrain.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int i = 0; i<_placementMask.getWidth(); i++)
			for(int j = 0; j<_placementMask.getHeight(); j++)
				_placementMask.setRGB(i, j, _graphicsCoordinator.getTerrainMaskColor(placement.contains(new MapPoint(i, j))).getRGB());
	}
	
	public void clearPlacementArea() {
		_placementMask = null;
	}
	
	//Gets the amount in pixels to offset the upper left corner of the terrain image from the upper left corner of the minimap panel
	private int[] calcOffset() {
		int offx, offy;
		
		int w_width = getWidth()-2*BORDER, w_height = getHeight()-2*BORDER;
		int t_width = SCALE*(_terrain.getWidth()+2), t_height = SCALE*(_terrain.getHeight()+2);
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
		
		offx += SCALE;  //For the one-tile padding around map edge
		offy += SCALE;
		
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
			if(_placementMask!=null)
				g.drawImage(_placementMask, offx, offy, SCALE*_terrain.getWidth(), SCALE*_terrain.getHeight(), this);
			g.setColor(_graphicsCoordinator.getNeutralColor());
			g.drawRect(offx-1, offy-1, SCALE*_terrain.getWidth()+2-1, SCALE*_terrain.getHeight()+2-1);
			
			///Draw dots for the units
			Map<MapPoint, Unit> units = _unitHandler.getUnits();
			if(units!=null) {
				Unit selected = _unitHandler.getSelectedUnit();
				
				for(MapPoint p : units.keySet()) {
					Unit unit = units.get(p);
					g.setColor(_graphicsCoordinator.getPlayerColor(unit._owner));
					int x = offx + SCALE*p._x;
					int y = offy + SCALE*p._y;
					g.fillRect(x, y, SCALE, SCALE);
					
					if(unit==selected) {
						g.setColor(_graphicsCoordinator.getForegroundColor());
						g.drawRect(x-1, y-1, SCALE+2-1, SCALE+2-1);
					}
				}
			}
			
			//Draw the border
			g.setColor(CM.applyAlpha(_graphicsCoordinator.getBackgroundColor(), 127));
			g.fillRect(0,0,getWidth(), BORDER);
			g.fillRect(0,getHeight()-BORDER,getWidth(), BORDER);
			
			g.fillRect(0,BORDER,BORDER,getHeight()-2*BORDER);
			g.fillRect(getWidth()-BORDER, BORDER, BORDER, getHeight()-2*BORDER);
			
			//Draw a box around the currently visible area
			g.setColor(_graphicsCoordinator.getForegroundColor());
			g.drawRect(offx+CM.round(SCALE*_view.getViewX())-1, offy+CM.round(SCALE*_view.getViewY())-1, CM.round(SCALE*_view.getViewWidth())+2-1, CM.round(SCALE*_view.getViewHeight())+2-1);
		}
	}
}