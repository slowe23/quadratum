package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import java.util.*;

import javax.swing.JPanel;

import net.quadratum.core.*;

public class MinimapPanel extends JPanel {
	private static final int SCALE = 3;  //Pixels per map square
	private static final int BORDER = 5;  //Pixels of border
	
	private GUIPlayer _guiPlayer;
	
	private MapPanel _mapPanel;
	
	private BufferedImage _terrainImg;  //A predrawn image of the map terrain
	private BufferedImage _placementImg;  //A predrawn image mask of the placement area
	
	private Map<MapPoint, Unit> _units;
	private MapPoint _selectedLocation;
	
	private BufferedImage _sightImg;  //An image mask of the sight area
	
	private int _offx, _offy;  //Pixel offset of the map image
	
	public MinimapPanel(GUIPlayer player, MapPanel mapPanel) {
		_guiPlayer = player;
		
		_mapPanel = mapPanel;
		
		addMouseListener(new MinimapPanelMouseListener());
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	public void placementUpdated() {
		synchronized(_guiPlayer._mapData) {
			synchronized(this) {
				if(_guiPlayer._mapData._placementArea==null)
					_placementImg = null;
				else
					_placementImg = generatePlacementImage(_terrainImg.getWidth(), _terrainImg.getHeight(), _guiPlayer._mapData._placementArea);
			}
		}
	}
	
	public void mapUpdated() {
		synchronized(_guiPlayer._mapData) {
			synchronized(this) {
				if(_guiPlayer._mapData._terrain==null) {
					_terrainImg = null;
					_placementImg = null;
				} else {
					_terrainImg = new BufferedImage(_guiPlayer._mapData._terrain.length, _guiPlayer._mapData._terrain[0].length, BufferedImage.TYPE_INT_ARGB);
					for(int xx = 0; xx<_terrainImg.getWidth(); xx++)
						for(int yy = 0; yy<_terrainImg.getHeight(); yy++)
							_terrainImg.setRGB(xx, yy, _guiPlayer._drawingMethods.getTerrainTileColor(_guiPlayer._mapData._terrain[xx][yy]).getRGB());
				
					if(_guiPlayer._mapData._placementArea==null)
						_placementImg = null;
					else
						_placementImg = generatePlacementImage(_terrainImg.getWidth(), _terrainImg.getHeight(), _guiPlayer._mapData._placementArea);
				}
			}
		}
	}
	
	private BufferedImage generatePlacementImage(int w, int h, Set<MapPoint> placementArea) {
		BufferedImage placementImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		MapPoint here = new MapPoint(0, 0);
		for(here._x = 0; here._x<w; here._x++)
			for(here._y = 0; here._y<h; here._y++)
				placementImg.setRGB(here._x, here._y, _guiPlayer._drawingMethods.getPlacementMaskColor(_guiPlayer.getID(), placementArea.contains(here)).getRGB());
		
		return placementImg;
	}
	
	public void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			synchronized(this) {
				_selectedLocation = _guiPlayer._unitsData.getSelectedLocation();
			}
		}
	}
	
	public void unitsUpdated() {
		synchronized(_guiPlayer._unitsData) {
			synchronized(this) {
				_units = _guiPlayer._unitsData.getAllUnits();
				_sightImg = generateSightImage(_terrainImg.getWidth(), _terrainImg.getHeight(), _guiPlayer._unitsData.getSight());
				_selectedLocation = _guiPlayer._unitsData.getSelectedLocation();
			}
		}
	}
	
	private BufferedImage generateSightImage(int w, int h, Set<MapPoint> sight) {
		if(sight==null)
			return null;
		
		BufferedImage sightImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		
		MapPoint here = new MapPoint(0, 0);
		for(here._x = 0; here._x<w; here._x++)
			for(here._y = 0; here._y<h; here._y++)
				sightImg.setRGB(here._x, here._y, _guiPlayer._drawingMethods.getSightMaskColor(sight.contains(here)).getRGB());
		
		return sightImg;
	}
	
	private int[] calcOffsets(double vx, double vy, double vw, double vh) {
		int offx, offy;
		
		int w_width = getWidth()-2*BORDER, w_height = getHeight()-2*BORDER;
		int t_width = SCALE*(_terrainImg.getWidth()+2), t_height = SCALE*(_terrainImg.getHeight()+2);
		double v_x = SCALE*vx, v_y = SCALE*vy;
		double v_width = SCALE*vw, v_height = SCALE*vh;
		
		
		if(t_width<= w_width) {
			offx = StaticMethods.round((w_width-t_width)/2.0);
		} else {
			offx = StaticMethods.round((w_width-v_width)/2.0-v_x);
			if(offx>0)
				offx = 0;
			if(offx+t_width<w_width)
				offx = w_width-t_width;
		}
		
		if(t_height <= w_height) {
			offy = StaticMethods.round((w_height-t_height)/2.0);
		} else {
			offy = StaticMethods.round((w_height-v_height)/2.0-v_y);
			if(offy>0)
				offy = 0;
			if(offy+t_height<w_height)
				offy = w_height-t_height;
		}
		
		offx += SCALE;  //For the one-tile padding around map edge
		offy += SCALE;
		
		offx += BORDER;  //For the border around the panel edge
		offy += BORDER;
		
		return new int[] {offx, offy};
	}
	
	// Paints the minimap
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		double viewx, viewy, vieww, viewh;
		synchronized(_mapPanel) {
			viewx = _mapPanel.getViewX();
			viewy = _mapPanel.getViewY();
			vieww = _mapPanel.getViewWidth();
			viewh = _mapPanel.getViewHeight();
		}
		
		BufferedImage terrainImg = null, placementImg = null;
		int offx = 0, offy = 0;
		Map<MapPoint, Unit> units = null;
		BufferedImage sightImg = null;
		MapPoint selectedLocation = null;
		synchronized(this) {
			if(_terrainImg!=null) {
				int[] offs = calcOffsets(viewx, viewy, vieww, viewh);
				offx = _offx = offs[0];
				offy = _offy = offs[1];
				
				terrainImg = _terrainImg;
				placementImg = _placementImg;
				
				units = _units;
				selectedLocation = _selectedLocation;
				sightImg = _sightImg;
			}
		}
		
		if(terrainImg!=null) {
			//Draw the map
			g.drawImage(terrainImg, offx, offy, SCALE*terrainImg.getWidth(), SCALE*terrainImg.getHeight(), null);

			//Draw the placement overlay
			if(placementImg!=null)
				g.drawImage(placementImg, offx, offy, SCALE*placementImg.getWidth(), SCALE*placementImg.getHeight(), null);
			
			//Draw a border around the map
			g.setColor(DrawingMethods.NEUTRAL_COLOR);
			g.drawRect(offx-1, offy-1, SCALE*terrainImg.getWidth()+2-1, SCALE*terrainImg.getHeight()+2-1);
			
			///Draw in the units
			if(units!=null) {
				for(Map.Entry<MapPoint, Unit> entry : units.entrySet()) {
					MapPoint p = entry.getKey();
					
					int xx = offx + SCALE*p._x;
					int yy = offy + SCALE*p._y;
					
					g.setColor(DrawingMethods.FOREGROUND_COLOR);
					g.fillRect(xx, yy, SCALE, SCALE);
					g.setColor(_guiPlayer._drawingMethods.getPlayerColor(entry.getValue()._owner));
					g.drawRect(xx, yy, SCALE-1, SCALE-1);
				}
			}
			
			//Emphasize the selected unit
			if(selectedLocation!=null) {
				g.setColor(DrawingMethods.FOREGROUND_COLOR);
				g.drawRect(offx+SCALE*selectedLocation._x-1, offy+SCALE*selectedLocation._y-1, SCALE+2-1, SCALE+2-1);
			}
			
			if(sightImg!=null)
				g.drawImage(sightImg, offx, offy, SCALE*sightImg.getWidth(), SCALE*sightImg.getHeight(), null);
			
			//Draw the panel border
			g.setColor(StaticMethods.applyAlpha(DrawingMethods.BACKGROUND_COLOR, 127));
			g.fillRect(0,0,getWidth(), BORDER);
			g.fillRect(0,getHeight()-BORDER,getWidth(), BORDER);
			
			g.fillRect(0,BORDER,BORDER,getHeight()-2*BORDER);
			g.fillRect(getWidth()-BORDER, BORDER, BORDER, getHeight()-2*BORDER);
			
			//Draw a box around the currently visible area
			g.setColor(DrawingMethods.FOREGROUND_COLOR);
			int startx = StaticMethods.round(SCALE*viewx), starty = StaticMethods.round(SCALE*viewy), endx = StaticMethods.round(SCALE*(viewx+vieww)), endy = StaticMethods.round(SCALE*(viewy+viewh));
			g.drawRect(offx+startx-1, offy+starty-1, endx-startx+2-1, endy-starty+2-1);
		}
	}
	
	private class MinimapPanelMouseListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			boolean ok = false;
			double sx = 0, sy = 0;
			synchronized(this) {
				if(_terrainImg!=null) {
					ok = true;
					sx = (e.getX()-_offx)/((double)SCALE);
					sy = (e.getY()-_offy)/((double)SCALE);
				}
			}
			
			if(ok) {
				_mapPanel.scrollTo(sx, sy, true);
				_mapPanel.repaintBoth();
			}
		}
	}
}