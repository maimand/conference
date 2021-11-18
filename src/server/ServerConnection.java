package server;

public class ServerConnection {
	ChatThread chatThreadSocket;
	VideoThread videoThreadSocket;
//	DatagramSocket voiceThreadSocket;
	
	public ServerConnection(
			ChatThread chatThreadSocket,
			VideoThread videoThreadSocket
//			DatagramSocket voiceThreadSocket
			) {
		this.chatThreadSocket = chatThreadSocket;
		this.videoThreadSocket = videoThreadSocket;
//		this.voiceThreadSocket = videoThreadSocket;
	}
}
