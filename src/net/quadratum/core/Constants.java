package net.quadratum.core;

public class Constants
{
	/** Maximum players a game can have. */
	public static final int MAX_PLAYERS = 8;
	/** Internal size of a unit. */
	public static final int UNIT_SIZE = 8;
	/** Resources gained per turn per unit on a resource square. */
	public static final int RESOURCES_PER_TURN = 10;
	/** Initial move radius of units. */
	public static final int INITIAL_MOVE = 2;
	/** Initial sight radius of units. */
	public static final int INITIAL_SIGHT = 3;
	/** Initial number of "attack lines" */
	public static final int INITIAL_ATTACK_LINES = 3;
	/** Health of heart blocks. */
	public static final int HEART_HEALTH = 100;
	
	/** Debug flag. When set to true, core logs to log.txt, otherwise it logs to System.out. */
	public static final boolean DEBUG_TO_FILE = true;
	/**
	 * Level of debugging messages that should be shown.
	 * -1 = no messages
	 * 0 = all messages
	 * 1 = warnings and errors
	 * 2 = errors
	 */
	public static final int DEBUG_LEVEL = 0; 
}