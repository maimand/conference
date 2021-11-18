package server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

class VideoThread {

	DatagramSocket soc;
	IpAddress ipAddress;

	byte[] outbuff = new byte[Server.BYTES_LENGTH];

	public VideoThread(DatagramSocket soc, IpAddress address) throws Exception {
		this.soc = soc;
		this.ipAddress = address;
//		this.start();
		while (true) {
			try {
				DatagramPacket reP = new DatagramPacket(outbuff, outbuff.length);
				soc.receive(reP);
				
				for(ServerConnection connection : Server.serverConnections) {
					DatagramPacket dp = new DatagramPacket(outbuff, outbuff.length, connection.videoThreadSocket.ipAddress.address,
							connection.videoThreadSocket.ipAddress.port);
					soc.send(dp);
				}
				Thread.sleep(15);
			} catch (Exception e) {

			}
		}
	}

}
