package net.quadratum.main;

import java.awt.Dimension;

/**
 * Contains information about current settings.
 * May also contain constants.
 */

public class MainConstants {
	
	public static final int DEFAULT_WINDOW_W = 640;
	public static final int DEFAULT_WINDOW_H = 640;
	public static final int MIN_WINDOW_W = 400;
	public static final int MIN_WINDOW_H = 400;
	
	
	public static final int DEFAULT_BUTTON_W = 80;
	public static final int DEFAULT_BUTTON_H = 30;

	public static final String SINGLE = "spMenu";
	public static final String NETWORK = "ntwkMenu";
	public static final String LOAD = "load";
	public static final String QUIT = "quit";
	public static final String QUICKPLAY = "quickplay";
	public static final String RETURN_MAIN = "returnMain";
	public static final String CAMPAIGN = "campaign";
	public static final String HOST = "host";
	public static final String JOIN = "join";
	
	
	public static final Dimension BUTTON_DIM = 
					new Dimension(DEFAULT_BUTTON_W, DEFAULT_BUTTON_H);
	
	
	public static class Defaults {
		// Map-related default settings
		public static final boolean USE_PRESET_MAP = false;
		public static final boolean ALLOW_WATER =     true;
		public static final boolean ALLOW_BUNKERS =   true;
		public static final boolean ALLOW_MOUNTAINS = true;
		public static final int MAP_TILE_WIDTH =  60;
		public static final int MAP_TILE_HEIGHT = 40;
		
		public static final int MIN_MAP_WIDTH =  20;
		public static final int MIN_MAP_HEIGHT = 20;
		public static final int MAX_MAP_WIDTH = 120;
		public static final int MAX_MAP_HEIGHT = 80;
		public static final int GRID_SIZE_INCREMENT = 5;
		
		// Player-related defaults
		public static final int INIT_NUM_RESOURCES = 5000;
		public static final int INIT_NUM_UNITS = 8;
		public static final int NUM_PLAYERS = 2;
		
		public static final int MIN_RESOURCES = 1000;
		public static final int MAX_RESOURCES = 9999;
		public static final int MIN_UNITS = 1;
		public static final int MAX_UNITS = 18;
		public static final int MIN_PLAYERS = 2;
		public static final int MAX_PLAYERS = 8;
		
		// Network-only setting defaults
		public static final int TURN_LIMIT_SECS =   120;
		//public static final int TOTAL_LIMIT_SECS =  54000; //1.5h
		public static final int PLAYER_TURNS_LIMIT = 35;
		public static final boolean LIMIT_DURATION = false;
		//public static final boolean USE_ALT_WINCON = false;
		public static final int PREFERRED_PORT = 9292;
		
		public static final int MIN_TURN_LIMIT_SECS =  45;
		public static final int MAX_TURN_LIMIT_SECS = 300;
		    // ^note that games can have untimed turns as well
		public static final int MIN_PORT = 1025;
		
		
		// Quick play only
		public static final int     AI_DIFFICULTY =  5;
		public static final int MIN_AI_DIFFICULTY =  1;
		public static final int MAX_AI_DIFFICULTY = 10;
		
		
		// blocks -- (dis)allowing certain types not important
	}
}
