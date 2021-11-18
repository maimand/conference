package server;

import java.net.InetAddress;

public class IpAddress {
	public final InetAddress address;
	public final int port;
	
	public IpAddress(InetAddress address, int port) {
		this.address = address;
		this.port = port;
	}
}
