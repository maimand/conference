package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import config.Config;

class ChatThread extends Thread {

	public static ArrayList<ChatClient> clients;

	ServerSocket chatServerSocket;

	public ChatThread() {
		try {
			this.chatServerSocket = new ServerSocket(Config.portTCPMessage);
			clients = new ArrayList<ChatClient>();
			this.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket soc = chatServerSocket.accept();

				addNewClient(soc);

				Thread.sleep(100);
			} catch (Exception e) {

			}

		}
	}

	public void addNewClient(Socket socket) {
		if (clients.size() >= 4)
			return;
		// Look through client list
		boolean found = false;
		for (int i = 0; i < ChatThread.clients.size(); i++) {
			ChatClient client = ChatThread.clients.get(i);
			if (client.soc.getInetAddress().equals(socket.getInetAddress())
					&& client.soc.getPort() == socket.getPort()) {
				found = true;
			}
		}
		// Add to list if it doesn't exist
		if (!found) {
			System.out.println("New chat client connected" + socket.getInetAddress().getHostAddress() + " Port: "
					+ socket.getPort());
			clients.add(new ChatClient(socket));
		}
	}

	class ChatClient extends Thread {
		Socket soc;
		DataInputStream dis;
		DataOutputStream dos;

		public ChatClient(Socket soc) {
			this.soc = soc;
			try {
				this.dis = new DataInputStream(soc.getInputStream());
				this.dos = new DataOutputStream(soc.getOutputStream());
			} catch (Exception e) {

			}
			this.start();
		}

		public void run() {
			while (true) {
				try {
					String username = dis.readUTF();
					String msg = dis.readUTF();
					for (ChatClient client : ChatThread.clients) {
//						if (!client.soc.getInetAddress().getAddress().equals(this.soc.getInetAddress().getAddress())
//								&& client.soc.getPort() != this.soc.getPort()) 
						try {
							client.dos.writeUTF(username);
							client.dos.writeUTF(msg);
							if (msg.equals("disconnect")) client.dos.writeUTF(soc.getInetAddress().getHostAddress());
						} catch (Exception e1) {
							// ChatServer.clients.remove(c);???
						}

					}
					if (msg.equals("disconnect")) {
						clients.removeIf(element -> soc.getInetAddress().equals(element.soc.getInetAddress()));
						VideoThread.clients.removeIf(element -> soc.getInetAddress().equals(element.address));
						VoiceThread.clients.removeIf(element -> soc.getInetAddress().equals(element.address));
						soc.close();
						dis.close();
						dos.close();
						return;
					}
				} catch (Exception e) {

				}
			}
		}
	}
}
