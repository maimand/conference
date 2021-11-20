package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class ChatThread extends Thread {

	public static ArrayList<ChatClient> clients;

	ServerSocket chatServerSocket;

	public ChatThread(ServerSocket chatServerSocket) {
		this.chatServerSocket = chatServerSocket;
		clients = new ArrayList<ChatClient>();
		this.start();
	}

	@Override
	public void run() {
		while (true) {
			try {
				Socket soc = chatServerSocket.accept();

				addNewClient(soc);

				Thread.sleep(15);
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
			System.out.println(
					"New chat client connected" + socket.getInetAddress().getAddress() + " Port: " + socket.getPort());
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
						if (!client.soc.getInetAddress().getAddress().equals(this.soc.getInetAddress().getAddress())
								&& client.soc.getPort() != this.soc.getPort()) {
							try {
								client.dos.writeUTF(username);
								client.dos.writeUTF(msg);
							} catch (Exception e1) {
								// ChatServer.clients.remove(c);???
							}
						}

					}
				} catch (Exception e) {

				}
			}
		}
	}
}
