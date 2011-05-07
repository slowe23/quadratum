//TODO:  Review for thread safety, check for dining philosophers problem

package net.quadratum.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

import net.quadratum.core.*;

public class MapPanel extends JPanel {
	private static final int DEFAULT_SCALE = 3;  //Default scale level
	private static final int MIN_SCALE = 2, MAX_SCALE = 8;
	
	private final GUIPlayer _guiPlayer;
	
	public final MinimapPanel _minimap;
	
	private int[][] _terrain;
	private Set<MapPoint> _placement;

	private Map<MapPoint, Unit> _units;
	private MapPoint _selectedLocation;
	private Map<MapPoint, net.quadratum.core.Action.ActionType> _selectedActions;
	
	private Set<MapPoint> _sight;
	
	private double _viewX, _viewY;  //Location in map squares currently displayed by the view
	private int _scaleLevel;  //Scale level
	
	public MapPanel(GUIPlayer player) {
		_guiPlayer = player;
		
		_scaleLevel = DEFAULT_SCALE;
		
		_minimap = new MinimapPanel(player, this);
		
		MouseInputListener mouseInputListener = new MapPanelMouseInputListener();
		addMouseListener(mouseInputListener);
		addMouseMotionListener(mouseInputListener);
		addMouseWheelListener(new MapPanelMouseWheelListener());
		addComponentListener(new MapPanelComponentListener());
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		//Create local references to everything, just in case something changes in the meantime
		int[][] terrain;
		Set<MapPoint> placement;
		Map<MapPoint, Unit> units;
		MapPoint selectedLocation;
		Map<MapPoint, net.quadratum.core.Action.ActionType> selectedActions;
		Set<MapPoint> sight;
		
		int scaleLevel = 0, scale = 0;
		double vX = 0, vY = 0, vW = 0, vH = 0;
		synchronized(this) {
			terrain = _terrain;
			placement = _placement;
			
			units = _units;
			selectedLocation = _selectedLocation;
			selectedActions = _selectedActions;
			sight = _sight;
			
			scaleLevel = _scaleLevel;
			scale = getScale();
			
			vX = getViewX();
			vY = getViewY();
			vW = getViewWidth();
			vH = getViewHeight();
		}
		
		
		if(terrain!=null) {
			//Draw visible map tiles and units
			int offx = StaticMethods.round(-1*vX*scale), offy = StaticMethods.round(-1*vY*scale);  //The pixel location of the top left map corner
			
			MapPoint here = new MapPoint(0, 0);
			for(here._x = Math.max(0, (int)vX); here._x<terrain.length && here._x<vX+vW; here._x++) {
				int tilex = offx+scale*here._x;
				
				for(here._y = Math.max(0, (int)vY); here._y<terrain[here._x].length && here._y<vY+vH; here._y++) {
					int tiley = offy+scale*here._y;
					
					Graphics tileGraph = g.create(tilex, tiley, scale, scale);
					
					_guiPlayer._drawingMethods.drawTerrainTile(tileGraph, terrain[here._x][here._y], scale, true);
					
					if(placement != null)
						_guiPlayer._drawingMethods.drawPlacementMask(tileGraph, _guiPlayer.getID(), placement, here, scale);
					if(sight!=null)
						_guiPlayer._drawingMethods.drawSightMask(tileGraph, sight.contains(here), scale);
					
					Unit unit = units.get(here);
					if(unit!=null) {
						_guiPlayer._drawingMethods.drawUnit(tileGraph, unit, scaleLevel, scale);
						
						if(selectedLocation!=null) {
							if(selectedLocation.equals(here)) {
								g.setColor(DrawingMethods.FOREGROUND_COLOR);
								g.drawRect(tilex, tiley, scale-1, scale-1);
							}
						}
					}
					
					if(selectedActions!=null && selectedActions.containsKey(here))
						_guiPlayer._drawingMethods.drawActionType(tileGraph, selectedActions.get(here), selectedLocation, here, scale);
				}
			}
		} else {
			//Draw waiting screen
			g.setColor(DrawingMethods.FOREGROUND_COLOR);
			FontMetrics fM = g.getFontMetrics();
			String waiting = "Waiting for map data...";
			g.drawString(waiting, (getWidth()-fM.stringWidth(waiting))/2, (getHeight()-(fM.getAscent()+fM.getDescent()))/2+fM.getAscent());
		}
	}
	
	public void repaintBoth() {
		repaint();
		_minimap.repaint();
	}
	
	private synchronized boolean isOnscreen(double x, double y) {
		return x>=_viewX && y>=_viewY && x<_viewX+getViewWidth() && y<_viewY+getViewHeight();
	}
	
	private synchronized boolean isOnscreen(MapPoint point) {
		return point._x>=_viewX && point._y>=_viewY && point._x+1<=_viewX+getViewWidth() && point._y+1<=_viewY+getViewHeight();
	}
	
	public synchronized void scrollTo(net.quadratum.core.Action a, boolean center) {
		if(a!=null) {
			if(a._dest==null)
				scrollTo(a._source, center);
			else if(a._source==null)
				scrollTo(a._dest, center);
			else if((Math.abs(a._source._x - a._dest._x)+1<=getViewWidth()) && (Math.abs(a._source._y-a._dest._y)+1<=getViewHeight()))
				scrollTo(a._dest, center);
			else
				scrollTo((a._source._x + a._dest._x)/2.0+0.5, (a._source._y + a._dest._y)/2.0+0.5, center);
		}
	}
	
	public synchronized void scrollTo(MapPoint point, boolean center) {
		if(point!=null) {
			if(center || !(isOnscreen(point)))
				center(point._x+0.5, point._y+0.5);  //The +0.5 is to get to the center of the target tile
		}
	}
	
	public synchronized void scrollTo(double x, double y, boolean center) {
		if(center || !(isOnscreen(x, y)))
			center(x, y);
	}
	
	private synchronized void center(double cx, double cy) {
		setViewPos(cx-getViewWidth()/2.0, cy-getViewHeight()/2.0);
	}
	
	public synchronized void centerAtPlacementArea() {
		if(_placement!=null) {
			int size = _placement.size();
			if(size==0)
				center(_terrain.length/2.0, _terrain[0].length/2.0);
			else {
				int tpx = 0, tpy = 0;
				for(MapPoint point : _placement) {
					tpx += point._x;
					tpy += point._y;
				}
				center(tpx/(double)size + 0.5, tpy/(double)size + 0.5);
			}
		}
	}
	
	public void placementUpdated() {
		synchronized(_guiPlayer._mapData) {
			synchronized(this) {
				if(_guiPlayer._mapData._placementArea==null)
					_placement = null;
				else
					_placement = new HashSet<MapPoint>(_guiPlayer._mapData._placementArea);
			}
		}
		
		_minimap.placementUpdated();
		repaintBoth();
	}
	
	public void mapUpdated() {
		synchronized(_guiPlayer._mapData) {
			synchronized(this) {
				_terrain = StaticMethods.copy(_guiPlayer._mapData._terrain);
				
				if(_guiPlayer._mapData._placementArea==null)
					_placement = null;
				else
					_placement = new HashSet<MapPoint>(_guiPlayer._mapData._placementArea);
			}
		}
		
		_minimap.mapUpdated();
		repaintBoth();
	}
	
	public void selectionUpdated() {
		synchronized(_guiPlayer._unitsData) {
			synchronized(this) {
				_selectedLocation = _guiPlayer._unitsData.getSelectedLocation();
				_selectedActions = _guiPlayer._unitsData.getSelectedActions();
			}
		}
		
		_minimap.selectionUpdated();
		repaintBoth();
	}
	
	public void unitsUpdated() {
		synchronized(_guiPlayer._unitsData) {
			synchronized(this) {
				_units = _guiPlayer._unitsData.getAllUnits();
				_sight = _guiPlayer._unitsData.getSight();

				_selectedLocation = _guiPlayer._unitsData.getSelectedLocation();
				_selectedActions = _guiPlayer._unitsData.getSelectedActions();
			}
		}
		
		_minimap.unitsUpdated();
		repaintBoth();
	}
	
	private synchronized int getScale() {
		return 16*_scaleLevel;
	}
	
	public synchronized void setScaleLevel(int scaleLevel) {
		_scaleLevel = Math.max(MIN_SCALE, Math.min(scaleLevel, MAX_SCALE));
	}
	
	public synchronized double getViewX() {
		return _viewX;
	}
	public synchronized double getViewY() {
		return _viewY;
	}
	public synchronized double getViewWidth() {
		return getWidth()/((double)(getScale()));
	}
	public synchronized double getViewHeight() {
		return getHeight()/((double)(getScale()));
	}
	
	public synchronized void setViewPos(double x, double y) {
		_viewX = x;
		_viewY = y;
	
		fixViewPos();
	}
	
	private synchronized void fixViewPos() {
		if(_terrain!=null) {
			double vW = getViewWidth(), vH = getViewHeight();
			int tW = _terrain.length, tH = _terrain[0].length;
				
			if(vW<tW) {
				if(_viewX<-1)
					_viewX = -1;
				else if(_viewX+vW>tW+1)
					_viewX = tW+1-vW;
			} else
				_viewX = (tW-vW)/2.0;
			
			if(vH<tH) {
				if(_viewY<-1)
					_viewY = -1;
				else if(_viewY+vH>tH+1)
					_viewY = tH+1-vH;
			} else
				_viewY = (tH-vH)/2.0;
		}
	}
	
	private class MapPanelMouseInputListener extends MouseInputAdapter {
		private Point _press;  //The location where the mouse was pressed (null when the mouse is not currently pressed)
		private double _pvx, _pvy;  //The location of the view where the mouse was last pressed
		
		public void mousePressed(MouseEvent e) {
			synchronized(MapPanel.this) {
				if(_terrain!=null) {
					_press = new Point(e.getX(), e.getY());
					_pvx = getViewX();
					_pvy = getViewY();
				}
			}
		}
		
		public synchronized void mouseDragged(MouseEvent e) {
			synchronized(MapPanel.this) {
				if(_press!=null)
					setViewPos(_pvx - (e.getX()-_press.x)/((double)(getScale())), _pvy - (e.getY()-_press.y)/((double)(getScale())));
				
				repaintBoth();
			}
		}
		
		public void mouseReleased(MouseEvent e) {
			synchronized(MapPanel.this) {
				_press = null;
			}
		}
		
		public void mouseMoved(MouseEvent e) {
			//TODO: implement scroll when mouse is near edge of map
		}
		
		public void mouseClicked(MouseEvent e) {
			synchronized(MapPanel.this) {
				if(_terrain!=null) {
					int scale = getScale();
					double vX = getViewX(), vY = getViewY();
					int offx = StaticMethods.round(-1*vX*scale), offy = StaticMethods.round(-1*vY*scale);  //The pixel location of the top left map corner
					
					MapPoint click = new MapPoint((e.getX()-offx)/scale, (e.getY()-offy)/scale);
					
					if(click!=null) {
						if(click._x>=0 && click._x<_terrain.length && click._y>=0 && click._y<_terrain[click._x].length)
							_guiPlayer.click(click);
						else
							_guiPlayer.clickOut();
					}
				}
			}
		}
	}
	
	private class MapPanelMouseWheelListener implements MouseWheelListener {
		public void mouseWheelMoved(MouseWheelEvent e) {
			synchronized(MapPanel.this) {
				if(_terrain!=null) {
					double centerX = _viewX + getViewWidth()/2.0, centerY = _viewY + getViewHeight()/2.0;
					setScaleLevel(_scaleLevel - e.getWheelRotation());			
					center(centerX, centerY);  //Recenter
					repaintBoth();
				}
			}
		}
	}
	
	private class MapPanelComponentListener extends ComponentAdapter {
		public void componentShown(ComponentEvent e) {
			fixViewPos();
			repaintBoth();
		}
		public void componentResized(ComponentEvent e) {
			fixViewPos();
			repaintBoth();
		}
	}
}