package client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;


public class DownVoiceThread extends Thread{
    private DatagramSocket connection;
    private AudioPlayer audioPlayer;
    byte[] receiveData = new byte[62000];
    public DownVoiceThread() throws SocketException {
        this.connection = new DatagramSocket();
        this.audioPlayer = new AudioPlayer();
    }
    public void run() {
        while (true) {
            try {
                // Packet for receiving data
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Receive data
                connection.receive(receivePacket);

                // Run ProcessReceived worker to handle new data
                audioPlayer.playAudio(receiveData);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }
}