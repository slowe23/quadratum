package net.quadratum.mapeditor;

import net.quadratum.gui.DrawingMethods;
import net.quadratum.gui.StaticMethods;
import net.quadratum.core.*;

import java.io.*;

import java.util.*;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

public class MEDisplayPanel extends JPanel implements Scrollable {
	private MESelectionPanel _select;
	
	private DrawingMethods _draw;
	
	private final static int SCALE = 32;
	
	private int _offx, _offy;
	
	private int[][] _terrain;
	private Set<MapPoint>[] _places;
	
	public MEDisplayPanel(MESelectionPanel selection, int[][] terrain, Set<MapPoint>[] places) {
		_select = selection;
		
		_draw = new DrawingMethods();
		
		_terrain = terrain;
		_places = places;
		
		MouseInputListener mIL = new MEDisplayMouseInputListener();
		addMouseListener(mIL);
		addMouseMotionListener(mIL);
		
		setBackground(DrawingMethods.BACKGROUND_COLOR);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension((_terrain.length+2)*SCALE, (_terrain[0].length+2)*SCALE);
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}
	
	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 1; }
	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 1; }
	public boolean getScrollableTracksViewportHeight() { return false; }
	public boolean getScrollableTracksViewportWidth() { return false; }
	
	public void setNewMapData(int[][] terrain, Set<MapPoint>[] places) {
		_terrain = terrain;
		_places = places;
		repaint();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		_offx = (getWidth() - _terrain.length*SCALE)/2;
		_offy = (getHeight() - _terrain[0].length*SCALE)/2;
		
		for(int i = 0; i<_terrain.length; i++)
			for(int j = 0; j<_terrain[i].length; j++)
				_draw.drawTerrainTile(g.create(_offx+SCALE*i, _offy+SCALE*j, SCALE, SCALE), _terrain[i][j], SCALE, true);
		
		for(int i = 0; i<_places.length; i++)
			for(MapPoint m  : _places[i])
				_draw.drawPlacementMask(g.create(_offx+SCALE*m._x, _offy+SCALE*m._y, SCALE, SCALE), i, _places[i], m, SCALE);
	}
	
	private class MEDisplayMouseInputListener extends MouseInputAdapter {
		private boolean _press;
		
		public void mousePressed(MouseEvent e) {
			_press = true;
			fwee(e);
		}
		
		public void mouseDragged(MouseEvent e) {
			if(_press)
				fwee(e);
		}
		public void mouseReleased(MouseEvent e) {
			fwee(e);
			_press = false;
		}
		
		public void mouseClicked(MouseEvent e) {
		}
		
		private void fwee(MouseEvent e) {
			int xx = (e.getX()-_offx)/SCALE;
			int yy = (e.getY()-_offy)/SCALE;
			if(e.getX()>=_offx && xx<_terrain.length && e.getY()>=_offy && yy<_terrain[xx].length) {
				_terrain[xx][yy] = _select.getAppropriateTerrain(_terrain[xx][yy], e.isAltDown(), e.isShiftDown());
				
				int player = _select.getSelectedPlayer();
				MapPoint location = new MapPoint(xx, yy);
				if(player==-1 || (player!=-2 && e.isShiftDown() && e.isAltDown())) {
					for(int i = 0; i<_places.length; i++)
						_places[i].remove(location);
				} else if(player!=-2) {
					if(e.isShiftDown())
						_places[player].remove(location);
					else {	   
						for(int i = 0; i<_places.length; i++) {
							if(i==player)
								_places[i].add(location);
							else
								_places[i].remove(location);
						}
					}
				}
				
				repaint();
			}
		}
	}
	
	public void save(String filename) throws IOException {
		PrintWriter w = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
		try {
			w.println("width|"+_terrain.length);
			w.println("height|"+_terrain[0].length);
			
			int n = 0;
			for(int i = 0; i<_places.length; i++)
				if(_places[i].size()>0)
					n++;
			
			w.println("players|"+n);
			for(int i = 0; i<_places.length; i++) {
				if(_places[i].size()>0) {
					w.print("start");
					for(MapPoint m : _places[i])
						w.print("|"+m._x+","+m._y);
					w.println();
				}
			}
			
			HashSet<MapPoint> water = new HashSet<MapPoint>();
			HashSet<MapPoint> bunkers = new HashSet<MapPoint>();
			HashSet<MapPoint> mountains = new HashSet<MapPoint>();
			HashSet<MapPoint> resources = new HashSet<MapPoint>();
			HashSet<MapPoint> impassibles = new HashSet<MapPoint>();
			for(int xx = 0; xx<_terrain.length; xx++) {
				for(int yy = 0; yy<_terrain[xx].length; yy++) {
					int i = _terrain[xx][yy];
					MapPoint m = new MapPoint(xx, yy);
					if(TerrainConstants.isOfType(i, TerrainConstants.WATER))
						water.add(m);
					if(TerrainConstants.isOfType(i, TerrainConstants.BUNKER))
						bunkers.add(m);
					if(TerrainConstants.isOfType(i, TerrainConstants.MOUNTAIN))
						mountains.add(m);
					if(TerrainConstants.isOfType(i, TerrainConstants.RESOURCES))
						resources.add(m);
					if(TerrainConstants.isOfType(i, TerrainConstants.IMPASSABLE))
						impassibles.add(m);
				}
			}
			
			if(water.size()>0) {
				w.print("water");
				for(MapPoint m : water)
					w.print("|"+m._x+","+m._y);
				w.println();
			}
			if(bunkers.size()>0) {
				w.print("bunkers");
				for(MapPoint m : bunkers)
					w.print("|"+m._x+","+m._y);
				w.println();
			}
			if(mountains.size()>0) {
				w.print("mountains");
				for(MapPoint m : mountains)
					w.print("|"+m._x+","+m._y);
				w.println();
			}
			if(resources.size()>0) {
				w.print("resources");
				for(MapPoint m : resources)
					w.print("|"+m._x+","+m._y);
				w.println();
			}
			if(impassibles.size()>0) {
				w.print("impassibles");
				for(MapPoint m : impassibles)
					w.print("|"+m._x+","+m._y);
				w.println();
			}
			w.flush();
		} finally {
			w.close();
		}
	}
}