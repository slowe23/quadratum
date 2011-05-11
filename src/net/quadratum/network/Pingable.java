package net.quadratum.network;

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
