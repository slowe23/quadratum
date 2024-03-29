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

	// Used in main menu
	public static final String SINGLE = "spMenu";
	public static final String NETWORK = "ntwkMenu";
	public static final String LOAD = "load";
	public static final String HELP = "help";
	public static final String QUIT = "quit";
	// Used in 1P menu
	public static final String QUICKPLAY = "quickplay";
	public static final String RETURN_MAIN = "returnMain";
	public static final String CAMPAIGN = "campaign";
	// Used in network menu
	public static final String HOST = "host";
	public static final String JOIN = "join";
	// Used in settings menu
	public static final String START_GAME = "startGame";
	public static final String DEFAULTS = "useDefaults";
	public static final String CHECK_CONN = "connectioncheck_testbutton";
	
	
	public static final Dimension BUTTON_DIM = 
					new Dimension(DEFAULT_BUTTON_W, DEFAULT_BUTTON_H);
	
	public static final String MAP_DIRECTORY = "maps/";
	
	public static final int SOCKET_TIMEOUT = 45000;
	public static final int PING_TIME = 15000;
	
	public static class Defaults {
		// Map-related default settings
		public static final boolean USE_PRESET_MAP = false;
		public static final boolean ALLOW_WATER =     true;
		public static final boolean ALLOW_BUNKERS =   true;
		public static final boolean ALLOW_MOUNTAINS = true;
		public static final boolean ALLOW_RESOURCES = true;
		public static final int MAP_TILE_WIDTH =  25;
		public static final int MAP_TILE_HEIGHT = 25;
		
		public static final int MIN_MAP_WIDTH =   8;
		public static final int MIN_MAP_HEIGHT =  8;
		public static final int MAX_MAP_WIDTH =  80;
		public static final int MAX_MAP_HEIGHT = 80;
		public static final int GRID_SIZE_INCREMENT = 5;
		
		// Player-related defaults
		public static final int INIT_NUM_RESOURCES = 1000;
		public static final int INIT_NUM_UNITS = 5;
		public static final int NUM_PLAYERS = 2;
		
		public static final int MIN_RESOURCES = 500;
		public static final int MAX_RESOURCES = 9999;
		public static final int RESOURCES_INCREMENT = 50;
		public static final int MIN_UNITS = 1;
		public static final int MAX_UNITS = 18;
		public static final int MIN_PLAYERS = 2;
		public static final int MAX_PLAYERS = 8;
		
		// Network-only setting defaults
		public static final int TURN_LIMIT_SECS =   120;
		//public static final int TOTAL_LIMIT_SECS =  54000; //1.5h
		public static final int PLAYER_TURNS_LIMIT = 25;
		public static final boolean LIMIT_DURATION = false;
		//public static final boolean USE_ALT_WINCON = false;
		public static final int PREFERRED_PORT = 9292;
		
		public static final int MIN_TURN_LIMIT_SECS =  45;
		public static final int MAX_TURN_LIMIT_SECS = 300;
		    // ^note that games can have untimed turns as well
		public static final int MIN_TURNS_LIMIT = 8;
		public static final int MAX_TURNS_LIMIT = 36;
		public static final int MIN_PORT = 1025;
		
		
		// Quick play only
		public static final int     AI_DIFFICULTY =  5;
		public static final int MIN_AI_DIFFICULTY =  1;
		public static final int MAX_AI_DIFFICULTY = 10;
		
		
		// blocks -- (dis)allowing certain types not important
	}
}
