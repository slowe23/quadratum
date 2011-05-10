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

public class CampaignPanel extends JPanel implements ActionListener, MouseListener {

	
	private MainGui _main;
	
	private JButton _playBtn, _returnMainBtn;
	private JLabel _levelName, _levelStatus;
	
	
	private Image _mapImg, _battleImg;
	private static Point[] _levelLocs =
		{ new Point(48, 550), new Point(100, 510), new Point(115, 455),
		  new Point(190,455), new Point(250, 475), new Point(310, 435)}; // Where level buttons appear on map
	private static Level[] _levels;
	private static LvlStatus[] _wins;
	
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
		JPanel mapPane = new JPanel() {
			public void paint(Graphics g)
		    {
		        g.drawImage(_mapImg, 0, 0, null);
		        for(Point p : _levelLocs) {
		        	g.drawImage(_battleImg, p.x-_xOffIcon, p.y-_yOffIcon, null);
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
		// TODO Auto-generated method stub
		// Campaign level selected, game started, maybe mouseover?
		
		String s = e.getActionCommand();
		
		if(s.equals("play")) {
			if(canPlay(_levelSelected)) {
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
		return (level <= _maxLevel && level > -1);
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
	
	private static void loadLevelInfo() {
		// TODO hard-code level info
		// - load info about number of levels
		
		// Currently hardcoded for demonstration
		int numLevels = 5;
		
		_levels = new Level[numLevels + 1];
		//_levels[0] = new Tutorial();
		_levels[1] = new Level1();
		_levels[2] = new Level2();
		_levels[3] = new Level3();
		_levels[4] = new Level4();
		_levels[5] = new Level5();
		
		//load associated win/loss info from a data file
		_wins = new LvlStatus[numLevels + 1];
		
		_wins[0] = LvlStatus.BEATEN;
		_wins[1] = LvlStatus.BEATEN;
		_wins[2] = LvlStatus.LOST;
		_wins[3] = LvlStatus.UNPLAYED;
		_wins[4] = LvlStatus.UNPLAYED;
		_wins[5] = LvlStatus.UNPLAYED;
		
		_maxLevel = 5;
	}
	
	private Level getLevel(int num) {
		
		// temporary safeguard:
		if(num>3 || num <1)
			return null;
		
		
		if(canPlay(num)) {
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
