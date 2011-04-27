package net.quadratum.core;

public class Action {

	/** The type of action that was performed. */
	ActionType _action;
	/** The source point this action was performed from. */
	MapPoint _source;
	/** The destination point this action was performed at. */
	MapPoint _dest;
	
	// TODO should this get moved to a separate class?
	public enum ActionType {
		MOVE,
		ATTACK,
		GATHER_RESOURCES;
	}
	
	/**
	 * Copy constructor for Action.
	 * @param action the Action to copy
	 */
	public Action(Action action)
	{
		_action = action._action;
		_source = new MapPoint(action._source);
		_dest = new MapPoint(action._dest);
	}
}
