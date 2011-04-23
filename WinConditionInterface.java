interface WinConditionInterface
{
	boolean hasPlayerWon(Map<Point, int> units, Player player); // Has this player won?
	boolean eliminatePlayer(Map<Point, int> units, Player player); // Has this player lost?
}