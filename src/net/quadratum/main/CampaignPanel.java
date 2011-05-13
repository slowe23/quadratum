package net.quadratum.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.quadratum.gamedata.Level;
import net.quadratum.gamedata.Level1;
import net.quadratum.gamedata.Level2;
import net.quadratum.gamedata.Level3;
import net.quadratum.gamedata.Level4;
import net.quadratum.gamedata.Level5;
import net.quadratum.gamedata.Tutorial;

public class CampaignPanel extends JPanel implements ActionListener, MouseListener{

	private MainGui _main;
	
	private JButton _playBtn, _returnMainBtn;
	private JLabel _levelName, _levelStatus;
	
	
	private Image _mapImg, _battleImg;
	private static Point[] _levelLocs =
		{ new Point(48, 550), new Point(100, 510), new Point(115, 455),
		  new Point(190,455), new Point(250, 475), new Point(310, 435)}; // Where level buttons appear on map
	private static Level[] _levels;
	private static LvlStatus[] _wins;
	private static int[] _invalidLevels;
	private static final String _dataFile = "src/net/quadratum/gamedata/campaign.dat";
	
	private int _levelSelected, _xOffMap, _yOffMap, _xOffIcon, _yOffIcon;
	private static int _maxLevel;
	//private final String _title = "Campaign";
	
	// net.quadratum.gamedata should have info (like constants)
	//  about campaign levels, perhaps in a "LevelInfo" class
	
	
	public CampaignPanel(ActionListener al) {
		
		super(new BorderLayout());
		setSize(MainConstants.DEFAULT_WINDOW_W, MainConstants.DEFAULT_WINDOW_H);
		
		_main = (MainGui)al;
		//_main.setSize(800, 640);
		setPreferredSize(new Dimension(800, 600));
		
		loadLevelInfo();
		
		// Panel at right displays information about selected level,
		//  Map with levels in center. Navigational buttons at southeast.
		
		try {
			_mapImg = Toolkit.getDefaultToolkit().createImage("imgs/campaignMap.gif");
			_battleImg = Toolkit.getDefaultToolkit().createImage("imgs/campaignBattle.gif");
		} catch (Exception e) {
			System.err.println("Could not load campaign icons.");
			_mapImg = new BufferedImage(600, 600, BufferedImage.TYPE_INT_RGB);
			_battleImg = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
		}
		_xOffIcon = 20;//_battleImg.getWidth(null)/2;
		_yOffIcon = 20;//_battleImg.getHeight(null)/2;
		JPanel mapPane = new JPanel() {//implements ImageObserver {
			public void paint(Graphics g)
		    {
		        g.drawImage(_mapImg, 0, 0, this);
		        for(Point p : _levelLocs) {
		        	g.drawImage(_battleImg, p.x-_xOffIcon, p.y-_yOffIcon, this);
		        }
		    }
		};
		mapPane.setPreferredSize(new Dimension(600, 600));
		mapPane.setBorder(BorderFactory.createCompoundBorder(
							BorderFactory.createRaisedBevelBorder (),
							BorderFactory.createLoweredBevelBorder() ) );
		
		
		// Sidebar features: Play button, level name, level status; return at bottom
		JPanel sidebar = new JPanel();
		sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
		sidebar.setPreferredSize(new Dimension(200, 600));
		sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
		
		_playBtn = new JButton("Do Battle!");
		_playBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_playBtn.setActionCommand("play");
		_playBtn.addActionListener(this);
		_playBtn.setEnabled(false);
		
		_levelName = new JLabel("No level selected");
		_levelName.setAlignmentX(Component.CENTER_ALIGNMENT);
		_levelStatus = new JLabel("-");
		_levelStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		
		sidebar.add(_playBtn);
		sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebar.add(_levelName);
		sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
		sidebar.add(_levelStatus);
		sidebar.add(Box.createVerticalGlue());
		
		
		_returnMainBtn = new JButton("Return to main menu");
		_returnMainBtn.setActionCommand(MainConstants.RETURN_MAIN);
		_returnMainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		_returnMainBtn.addActionListener(al);
		
		sidebar.add(_returnMainBtn);
		
		add(mapPane);
		add(sidebar, BorderLayout.EAST);
		
		Point mapLoc = mapPane.getLocation();
		_xOffMap = mapLoc.x;
		_yOffMap = mapLoc.y;
		
		this.addMouseListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String s = e.getActionCommand();
		
		if(s.equals("play")) {
			if(canPlay(_levelSelected)) {
				if(_wins[_levelSelected].val < 1 &&
				   _wins[_levelSelected-1].val < 1) {
					_levelStatus.setText("<html>You haven't advanced far<br>" +
										 "enough play this level.</html>");
					return;
				}
				_main.startCampaignGame(getLevel(_levelSelected));
			}
			
			return;
		}
		
		if(s.equals("level")) {
			Integer in = ((Integer) e.getSource());
			_levelSelected = in.intValue();
			displayLevelInfo(in.intValue());
			
			return;
		}
	}
	
	private boolean canPlay(int level) {
		return (level <= _maxLevel && level >= 0);
	}
	
	private boolean allowedLevel(int level) {
		for(int i : _invalidLevels) {
			if(level==i)
				return false;
		}
		return true;
	}

	private void displayLevelInfo(int level) {
		// Put appropriate level information in sidebar
		// -"Level X"; -levelname; -Unplayed/Beaten/Lost
		// Eventually, more information may be added - storyline, rewards, etc.
		
		if(canPlay(level)) {
			_playBtn.setEnabled(true);
			if(level == 0)
				_levelName.setText("Tutorial");
			else
				_levelName.setText("Level "+level);
			_levelStatus.setText(_wins[level].toString());
		} else {
			_playBtn.setEnabled(false);
			_levelName.setText("No level selected");
			_levelStatus.setText("-");
		}
	}
	
	private static void beatLevel(int level) {
		if(_wins[level] == LvlStatus.BEATEN)
		{
			//Give message congratulating you for beating this level again
			return;
		}
		
		BufferedReader fin = null;
		PrintWriter fout = null;
		try {
			fin = new BufferedReader(new FileReader(_dataFile));
			String[] lines = new String[5];
			for(int i = 0; i<5; i++)
				lines[i] = fin.readLine();
			fin.close();

			// Increment first unplayed level; set next to unplayed
			lines[2] = "" + (Integer.parseInt(lines[2].trim()) + 1);
			lines[3] = "0";
			
			fout = new PrintWriter(new FileWriter(_dataFile));
			for(int i = 0; i<5; i++)
				fout.println(lines[i]);
			fout.close();
		} catch (Exception e) {
			//Give message about possible failure of saving win info
		} finally {
			try {
				fin.close();
				fout.close();
			} catch (Exception e) {}
		}
	}
	
	private static void loseLevel(int level) {
		//see above
	}
	
	private static void loadLevelInfo() {
		// - load info about number of levels
		BufferedReader fin = null;
		int firstUnbeaten;
		try {
			// Campaign data file has the following information:
			// number of levels, not including tutorial
			// locations of level battles on map, separated as so: x,y;x,y ...
			// first unbeaten level
			// whether that level is lost or unplayed (0/anything else)
			// levels that are disabled, separated by commas
			fin = new BufferedReader(new FileReader(_dataFile));
			_maxLevel = Integer.parseInt(fin.readLine());
			String[] locsS = fin.readLine().split(";");
			Point[] locsP = new Point[locsS.length];
			boolean locsGood = true;
			for(int i = 0; i < locsS.length; i++) {
				String next = locsS[i].trim();
				int comma = next.indexOf(',');
				if(comma < 0) {
					locsGood = false;
					break;
				}
				String x = next.substring(0, comma);
				String y = next.substring(comma + 1);
				try {
					locsP[i] = new Point(Integer.parseInt(x), Integer.parseInt(y));
				} catch (Exception e) {
					locsGood = false;
					break;
				}
				
			}
			if(locsGood)	_levelLocs = locsP;
			
			
			_wins = new LvlStatus[_maxLevel + 1];
			firstUnbeaten = Integer.parseInt(fin.readLine());
			for(int i = 0; i < firstUnbeaten; i++) {
				_wins[i] = LvlStatus.BEATEN;
			}
			if(Integer.parseInt(fin.readLine()) == 0)
				_wins[firstUnbeaten] = LvlStatus.LOST;
			for(int i = firstUnbeaten; i < _maxLevel; ) {
				i++;
				_wins[i] = LvlStatus.UNPLAYED;
			}
			
			String[] invLvls = (fin.readLine().split(","));
			_invalidLevels = new int[invLvls.length];
			for(int i = 0; i < _invalidLevels.length; i++) {
				_invalidLevels[i] = Integer.parseInt(invLvls[i].trim());
			}
		} catch (Exception e) {
			_maxLevel = 5;
			
			_wins = new LvlStatus[_maxLevel + 1];
			
			_wins[0] = LvlStatus.BEATEN;
			_wins[1] = LvlStatus.BEATEN;
			_wins[2] = LvlStatus.LOST;
			_wins[3] = LvlStatus.UNPLAYED;
			_wins[4] = LvlStatus.UNPLAYED;
			_wins[5] = LvlStatus.UNPLAYED;
			
			_invalidLevels = new int[] {0, 4, 5};
		} finally {

			// Currently hardcoded		
			_levels = new Level[_maxLevel + 1];
			_levels[0] = new Tutorial();
			_levels[1] = new Level1();
			_levels[2] = new Level2();
			_levels[3] = new Level3();
			_levels[4] = new Level4();
			_levels[5] = new Level5();
			
			
			_invalidLevels = new int[0];

			try {if(fin != null) fin.close();} catch (Exception e) {}
		}
	}
	
	private Level getLevel(int num) {
		
		if(canPlay(num)) {
			// Safeguard if some levels aren't implemented
			if(!allowedLevel(num))
				return null;
			
			return _levels[num];
		}
		
		return null;
	}
	
	
	private enum LvlStatus {
		UNPLAYED (0, "Unplayed"),
		BEATEN (1, "Beaten"),
		LOST (-1, "Lost");
		
		public final int val;
	    public final String str;
	    LvlStatus(int val, String s) {
	        this.str = s;
	        this.val = val;
	    }

	    public String toString() {return str;}
	}

	private boolean closeEnough(Point p1, Point p2) {
		if(Math.abs(p1.x - p2.x) > _xOffIcon)
			return false;
		if(Math.abs(p1.y - p2.y) > _yOffIcon)
			return false;
		
		return true;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		Point loc = new Point(p.x-_xOffMap, p.y-_yOffMap);
		for(int i = 0; i<_levels.length; i++) {
			if(closeEnough(loc, _levelLocs[i])) {
				actionPerformed(new ActionEvent(new Integer(i), 42, "level"));
				return;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
