interface CoreInterface
{
	void Core(String map, WinCondition winCondition); // Constructs a new game core
	void addPlayer(Player player); // Adds a player
	void start(); // Starts the game
	void ready(int id); // Callback so players can let the game core know that they are ready to start the game (i.e. all of their units have been placed)
	void endTurn(int id); // Callback so players can let the game core know that their turn has ended
	boolean unitAction(int id, int unitId, Point coords); // Callback for unit actions (returns false for invalid actions) - if coords is an empty square, the unit moves, if coords contains a unit, the unit attacks
	Map<Point, ActionEnum> getValidActions(int id, int unitId);	 // Gets the valid actions for a unit, returns null if no possible actions
	void quit(int id);	// Notifies the core that a player has quit - if the player is the main player, it notifies the main thread to end the game and to display itself again
	void sendChatMessage(int id, String message); // Callback for sending a chat message
	boolean placeUnit(int id, Point coords); // Callback for placing a unit
	boolean updateUnit(int id, int unitId, Piece newPiece); // Callback for updating a unit
}