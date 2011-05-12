package net.quadratum.network;

/**
 * An interface implemented by classes that will send heartbeat
 * messages across a socket until a certain condition is not met.
 * @author Zircean
 *
 */
public interface Pingable {

	/**
	 * Pings the connected client over the network.
	 */
	public void ping();
	
	/**
	 * Returns whether or not this Pingable object is still listening.
	 * @return true if listeing, false otherwise.
	 */
	public boolean keepListening();
}
