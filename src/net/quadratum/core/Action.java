package net.quadratum.core;

import java.io.Serializable;

public class Action implements Serializable {

	/**
	 * Serialization ID
	 */
	private static final long serialVersionUID = -5076267909701737339L;
	
	/** The type of action that was performed. */
	public ActionType _action;
	/** The source point this action was performed from. */
	public MapPoint _source;
	/** The destination point this action was performed at. */
	public MapPoint _dest;
	
	// TODO should this get moved to a separate class?
	public enum ActionType {
		MOVE,
		ATTACK,
		UNIT_DIED,
		GAME_START,
		UNIT_UPDATED,
		GATHER_RESOURCES;
	}
	
	/**
	 * Copy constructor for Action.
	 * @param action the Action to copy
	 */
	public Action(Action action)
	{
		this(action._action, action._source, action._dest);
	}
	
	/**
	 * Constructor for Action.
	 * @param action the type of action
	 * @param source the source of the action
	 * @param dest the destination of the action
	 */
	public Action(ActionType action, MapPoint source, MapPoint dest)
	{
		_action = action;
		if(source == null)
		{
			_source = null;
		}
		else
		{
			_source = new MapPoint(source);
		}
		if(dest == null)
		{
			_dest = null;
		}
		else
		{
			_dest = new MapPoint(dest);
		}
	}
}
