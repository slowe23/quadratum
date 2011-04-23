interface PlayerInterface
{
	void start(int id, MapData mapData, List<String> otherPlayers, List<Piece> pieces); // Notifies the player that there is a new game
	void end(int[] stats); // Notifies the player that the game is over and provides some gate stats
	void lost(); // Notifies the player that they have lost
	void turnStart(); // Notifies the player that their turn has started
	void updateMap(Map<Point, int> units, int resources, Action lastAction); // Updates the positions of units on the map
	void chatMessage(int from, String message); // Notifies the player of a new chat message
}