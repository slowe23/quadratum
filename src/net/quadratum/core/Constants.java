package net.quadratum.core;

// Whole bunch of constants
public class Constants
{
	/** Maximum players a game can have. */
	public static final int MAX_PLAYERS = 8;
	/** Internal size of a unit. */
	public static final int UNIT_SIZE = 8;
	/** Resources gained per turn per unit on a resource square. */
	public static final int RESOURCES_PER_TURN = 10;
	/** Initial move radius of units. */
	public static final int INITIAL_MOVE = 0;
	/** Initial sight radius of units. */
	public static final int INITIAL_SIGHT = 3;
	/** Initial attack width of units. */
	public static final int INITIAL_ATTACK_WIDTH = 3;
	/** Range bonus if the unit is on a mountain. */
	public static final int MOUNTAIN_RANGE_BONUS = 3;
	/** Defense bonus on a bunker. */
	public static final int BUNKER_DEFENSE_BONUS = 75;
	/** Defense modifier. As this goes up, actual defensive power goes up. */
	public static final double DEFENSE_MODIFIER_POW = 1.2;
	/** Defense modifier. As this goes up, actual defensive power goes down. */
	public static final double DEFENSE_MODIFIER_DIV = 5000.0;
	/** Health of heart blocks. */
	public static final int HEART_HEALTH = 100;
	/** Movement modifier. */
	public static final int MOVEMENT_MODIFIER = 120;
	/** Attack range modifier. */
	public static final int ATTACK_RANGE_MODIFIER = 120;
	/** Sight modifier. */
	public static final int SIGHT_MODIFIER = 120;
	/** Attack width modifier. */
	public static final int ATTACK_WIDTH_MODIFIER = 120;
	
	/** Debug flag. When set to true, core logs to log.txt, otherwise it logs to System.out. */
	public static final boolean DEBUG_TO_FILE = true;
	/**
	 * Level of debugging messages that should be shown.
	 * -1 = no messages
	 * 0 = all messages
	 * 1 = warnings and errors
	 * 2 = errors
	 */
	public static final int DEBUG_LEVEL = -1; 
}