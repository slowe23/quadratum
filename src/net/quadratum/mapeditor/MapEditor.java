package net.quadratum.mapeditor;

import net.quadratum.core.Constants;
import net.quadratum.core.MapPoint;

import java.util.*;

import java.io.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class MapEditor extends JFrame {
	
	private MEDisplayPanel _display;
	
	@SuppressWarnings({"unchecked"})
	public static void main(String[] args) {
		int[][] terrain;
		Set<MapPoint>[] places;
		if(args.length==1 || args.length==2) {
			TP tp;
			try {
				tp = load(args[0]);
			} catch(IOException e) {
				System.out.println("Could not load file.");
				e.printStackTrace();
				return;
			}
			terrain = tp._terrain;
			places = tp._places;
		} else if(args.length==3) {
			try {
				terrain = new int[Integer.parseInt(args[0])][Integer.parseInt(args[1])];
			} catch(Exception e) {
				System.out.println("Invalid args.");
				return;
			}
			places = (Set<MapPoint>[])(new Set[Constants.MAX_PLAYERS]);
			for(int i = 0; i<places.length; i++) {
				places[i] = new HashSet<MapPoint>();
			}
		} else if (args.length == 0) {
			// make a default size terrain full of land
			terrain = new int[MEConstants.DEFAULT_SIZE_X][MEConstants.DEFAULT_SIZE_Y];
			// make empty placement areas
			places = (Set<MapPoint>[])(new Set[Constants.MAX_PLAYERS]);
			for(int i = 0; i<places.length; i++) {
				places[i] = new HashSet<MapPoint>();
			}
		} else {
			System.out.println("Invalid args.");
			return;
		}
		
		new MapEditor(terrain, places).setVisible(true);
	}
	
	public MapEditor(int[][] terrain, Set<MapPoint>[] places) {
		
		// Set frame vitals
		setSize(800, 600);
		setTitle("Quadratum Map Editor");
		setLayout(new BorderLayout());
		
		// Set selection panel
		MESelectionPanel select = new MESelectionPanel(MESelectionPanel.VERTICAL);
		add(select, BorderLayout.WEST);
		
		// Set display panel
		_display = new MEDisplayPanel(select, terrain, places);
		JScrollPane jsp = new JScrollPane(_display, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(jsp, BorderLayout.CENTER);
		
		// Set menu bar
		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		// File menu
		final JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');
		menuBar.add(fileMenu);
		
		// New item
		final JMenuItem newMenuItem = new JMenuItem("New");
		newMenuItem.setMnemonic('N');
		newMenuItem.addActionListener(new ActionListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO add modal dialog to allow changing of size
				int[][] terrain = new int[MEConstants.DEFAULT_SIZE_X][MEConstants.DEFAULT_SIZE_Y];
				// make empty placement areas
				Set<MapPoint>[] places = (Set<MapPoint>[])(new Set[Constants.MAX_PLAYERS]);
				for(int i = 0; i<places.length; i++) {
					places[i] = new HashSet<MapPoint>();
				}
				_display.setNewMapData(terrain, places);
				validate();
			}
		});
		fileMenu.add(newMenuItem);
		
		// Open item
		final JMenuItem openMenuItem = new JMenuItem("Open");
		openMenuItem.setMnemonic('O');
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create file chooser
				final JFileChooser chooser = new JFileChooser();
				// chooser.setFileFilter(new QMapFileFilter());
				
				// Get a location
				int success = chooser.showOpenDialog(MapEditor.this);
				if (success == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					try {
						TP tp = load(f.getAbsolutePath());
						_display.setNewMapData(tp._terrain, tp._places);
						validate();
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MapEditor.this,"Error opening file.");
					}
				}
			}
		});
		fileMenu.add(openMenuItem);
		
		// Save item
		final JMenuItem saveMenuItem = new JMenuItem("Save");
		saveMenuItem.setMnemonic('S');
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// Create file chooser
				final JFileChooser chooser = new JFileChooser();
				
				// Get a location
				int success = chooser.showSaveDialog(MapEditor.this);
				if (success == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					try {
						_display.save(f.getAbsolutePath());
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(MapEditor.this,"Error saving file.");
					}
				}
			}
		});
		fileMenu.add(saveMenuItem);
		
		fileMenu.addSeparator();
		
		// Exit item
		final JMenuItem exitMenuItem = new JMenuItem("Exit");
		exitMenuItem.setMnemonic('E');
		exitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		fileMenu.add(exitMenuItem);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@SuppressWarnings({"unchecked"})
	private static TP load(String filename) throws IOException {
		BufferedReader r = new BufferedReader(new FileReader(filename));
		try {
			Map<MapPoint, Integer> mapPoints = new HashMap<MapPoint, Integer>();
			
			int width = 0, height = 0, players = 0;
			boolean wset = false, hset = false, pset = false;
			
			Map<MapPoint, Integer> placePts = new HashMap<MapPoint, Integer>();
			int nextPlayer = 0;
			
			String[] strs = {"water", "bunkers", "mountains", "resources", "impassibles"};
			int[] ts = {1, 2, 4, 8, 16};
			boolean[] tsets = new boolean[ts.length];
			
			String line;
			while((line = r.readLine())!=null) {
				int place = -1;
				int ter = -1;
				
				if(line.startsWith("height|")) {
					if(hset)
						throw new IOException("Multiple height values provided.");
						
					try {
						height = Integer.parseInt(line.substring("height|".length()));
					} catch(NumberFormatException e) {
						throw new IOException("Error while parsing height", e);
					}
					
					hset = true;
				} else if(line.startsWith("width|")) {
					if(wset)
						throw new IOException("Multiple width values provided.");
					
					try {
						width = Integer.parseInt(line.substring("width|".length()));
					} catch(NumberFormatException e) {
						throw new IOException("Error while parsing width", e);
					}
					
					wset = true;
				} else if(line.startsWith("players|")) {
					if(pset)
						throw new IOException("Multiple players values provided.");
					
					try {
						players = Integer.parseInt(line.substring("players|".length()));
					} catch(NumberFormatException e) {
						throw new IOException("Error while parsing players", e);
					}
					
					pset = true;
				} else if(line.startsWith("start|")) {
					place = nextPlayer++;
				} else {
					boolean ok = false;
					for(int i = 0; !ok && i<strs.length; i++) {
						if(line.startsWith(strs[i]+"|")) {
							if(tsets[i])
								throw new IOException("Multiple "+strs[i]+" values provided.");
							
							ter = i;
							tsets[i] = true;
							ok = true;
						}
					}
					
					if(!ok)
						throw new IOException("Could not parse line.");
				}
				
				if(place!=-1) {
					Set<MapPoint> set = getPoints(line.substring("start|".length()));
					for(MapPoint m : set) {
						if(placePts.containsKey(m))
							throw new IOException("Placement areas overlapped.");
						else
							placePts.put(m, place);
					}
				} else if(ter!=-1) {
					Set<MapPoint> set = getPoints(line.substring(strs[ter].length()+1));
					for(MapPoint m : set) {
						if(mapPoints.containsKey(m)) {
							int i = mapPoints.get(m);
							
							if((i & ts[ter])!=0)
								throw new IOException("Duplicate "+strs[ter]+" location.");
							
							mapPoints.put(m, i | ts[ter]);
						} else
							mapPoints.put(m, ts[ter]);
					}
				}
			}
			
			if(!wset)
				throw new IOException("Width not set.");
			if(!hset)
				throw new IOException("Height not set.");
			if(!pset)
				throw new IOException("Players not set.");
			if(width<=0)
				throw new IOException("Invalid width value: "+width);
			if(height<=0)
				throw new IOException("Invalid height value: "+height);
			if(players<2 || players>Constants.MAX_PLAYERS)
				throw new IOException("Invalid number of players: "+players);
			if(nextPlayer!=players)
				throw new IOException("Number of placement areas given ("+nextPlayer+") did not match number of players given ("+players+").");
			
			Set<MapPoint>[] places = (Set<MapPoint>[])(new Set[Constants.MAX_PLAYERS]);
			for(int i = 0; i<places.length; i++)
				places[i] = new HashSet<MapPoint>();
			for(Map.Entry<MapPoint, Integer> entry : placePts.entrySet())
				places[entry.getValue()].add(entry.getKey());
			
			int[][] terrain = new int[width][height];
			for(Map.Entry<MapPoint, Integer> entry : mapPoints.entrySet()) {
				MapPoint m = entry.getKey();
				if(m._x<0 || m._x>=width || m._y<0 || m._y>=height)
					throw new IOException("Invalid map point: "+m);
				terrain[m._x][m._y] = entry.getValue();
			}
			return new TP(terrain, places);
		} finally {
			r.close();
		}
	}
	
	private static Set<MapPoint> getPoints(String line) throws IOException {
		Set<MapPoint> pts = new HashSet<MapPoint>();
		MapPoint res = new MapPoint(0, 0);
		int index = getPoint(line, 0, res);
		pts.add(new MapPoint(res));
		while(index<line.length()) {
			index = getPoint(line, index, res);
			if(pts.contains(res))
				throw new IOException("Duplicate point: "+res);
			pts.add(new MapPoint(res));
		}
		return pts;
	}
	
	private static int getPoint(String line, int start, MapPoint result) throws IOException {
		try {
			int comma = line.indexOf(",", start);
			int bar = line.indexOf("|", comma);
			if(bar<0)
				bar = line.length();
			result._x = Integer.parseInt(line.substring(start, comma));
			result._y = Integer.parseInt(line.substring(comma+1, bar));
			return bar+1;
		} catch(Exception e) {
			throw new IOException("Could not parse line.", e);
		}
	}
	
	private static class TP {
		public int[][] _terrain;
		public Set<MapPoint>[] _places;
		
		public TP() { }
		
		public TP(int[][] t, Set<MapPoint>[] p) {
			_terrain = t;
			_places = p;
		}
	}
	
	private class QMapFileFilter implements FileFilter {
		@Override
		public boolean accept(File pathname) {
			String name = pathname.getName();
			return pathname.isDirectory() || name.endsWith(".qmap");
		}
		
	}
}