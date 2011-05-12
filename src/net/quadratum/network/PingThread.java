package net.quadratum.network;

import net.quadratum.main.MainConstants;

/**
 * A thread used by Pingable classes to ping every PING_TIME milliseconds
 * (defined in MainConstants).
 * @author Zircean
 *
 */
public class PingThread extends Thread {
	
	Pingable _host;

	public PingThread(Pingable p) {
		_host = p;
	}
	
	@Override
	public void run() {
		while (_host.keepListening()) {
			try {
				Thread.sleep(MainConstants.PING_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			_host.ping();
		}
	}
}
