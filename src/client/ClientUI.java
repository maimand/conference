package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet.ColorAttribute;

import config.Config;

public class ClientUI {
	String appName = "Conference v1.0.1";
	public static JFrame mainFrame = new JFrame();
	public static JPanel mainJPanel = new JPanel(new GridLayout(2, 1));
	public static JPanel chatJPanel = new JPanel();
	public static JPanel videoJPanel = new JPanel(new GridLayout(2, 2));
	public static JLabel selfVideo = new JLabel();
	public static JLabel user1 = new JLabel();
	public static JLabel user2 = new JLabel();
	public static JLabel user3 = new JLabel();

	ImageIcon img;
	JButton sender;

	JTextField messageBox;
	static JTextArea chatBox;
	JTextField usernameChooser;
	JTextField serverAddressTextField;
	JFrame preFrame;
	String username;
	String serverAddress;
	ChatThread chatThread;
	UpVideoThread upVideoThread;
	DownVideoThread downVideoThread;
	UpVoiceThread upVoiceThread;
	DownVoiceThread downVoiceThread;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) {
					e.printStackTrace();
				}
				ClientUI mainGUI;
				try {
					mainGUI = new ClientUI();
					mainGUI.preDisplay();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	public void preDisplay() {
		mainFrame.setVisible(false);
		preFrame = new JFrame(appName);
		usernameChooser = new JTextField(15);
		JLabel chooseUsernameLabel = new JLabel("     Your username:");
		serverAddressTextField = new JTextField(15);
		JLabel serverPanel = new JLabel("     Server address:");
		JButton enterServer = new JButton("Enter Chat Server");
		enterServer.addActionListener(new enterServerButtonListener());
		JPanel prePanel = new JPanel();
		GridLayout experimentLayout = new GridLayout(2, 2);
		prePanel.setLayout(experimentLayout);

		prePanel.add(chooseUsernameLabel);
		prePanel.add(usernameChooser);
		prePanel.add(serverPanel);
		prePanel.add(serverAddressTextField);
		preFrame.add(prePanel, BorderLayout.CENTER);
		preFrame.add(enterServer, BorderLayout.SOUTH);
		preFrame.setSize(300, 120);
		preFrame.setVisible(true);

	}

	public void display() {

		mainFrame.setSize(640, 960);
		mainFrame.setTitle("Client Show");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.setVisible(true);
		mainJPanel.add(videoJPanel);
		mainJPanel.add(chatJPanel);
		mainFrame.add(mainJPanel);

		videoJPanel.add(selfVideo);
		videoJPanel.add(user1);
		videoJPanel.add(user2);
		videoJPanel.add(user3);

		setBlack(3);

		sender = new JButton("Send Message");

		chatJPanel.setLayout(new BorderLayout());

		JPanel southPanel = new JPanel();
		southPanel.setBackground(Color.BLUE);
		southPanel.setLayout(new GridBagLayout());

		messageBox = new JTextField(30);
		messageBox.requestFocusInWindow();

		chatBox = new JTextArea();
		chatBox.setEditable(false);
		chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
		chatBox.setLineWrap(true);

		chatJPanel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

		GridBagConstraints left = new GridBagConstraints();
		left.anchor = GridBagConstraints.LINE_START;
		left.fill = GridBagConstraints.HORIZONTAL;
		left.weightx = 512.0D;
		left.weighty = 1.0D;

		GridBagConstraints right = new GridBagConstraints();
		right.insets = new Insets(0, 10, 0, 0);
		right.anchor = GridBagConstraints.LINE_END;
		right.fill = GridBagConstraints.NONE;
		right.weightx = 1.0D;
		right.weighty = 1.0D;

		southPanel.add(messageBox, left);
		southPanel.add(sender, right);

		chatJPanel.add(BorderLayout.SOUTH, southPanel);

		sender.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String sentence = messageBox.getText();
				if(sentence.isEmpty()) return;
				chatBox.append("" + username + ": " + sentence + "\n");
				chatBox.setCaretPosition(chatBox.getDocument().getLength());
				chatJPanel.revalidate();
				chatJPanel.repaint();
//                jp.revalidate();
//                jp.repaint();
				send(sentence);
				messageBox.setText(null);
			}
		});

		try {
			System.out.println(serverAddress);
			chatThread = new ChatThread(serverAddress);
			chatThread.start();
		} catch (Exception e) {
			System.err.println(e);
		}

		try {
			DatagramSocket videoDatagramSocket = new DatagramSocket();
			try {
				upVideoThread = new UpVideoThread(serverAddress, videoDatagramSocket);
			} catch (Exception e) {
			}
			try {
				downVideoThread = new DownVideoThread(videoDatagramSocket);
			} catch (Exception e) {
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		try {
			DatagramSocket voiceDatagramSocket = new DatagramSocket();
			try {
				upVoiceThread = new UpVoiceThread(serverAddress, voiceDatagramSocket);
			} catch (Exception e) {
			}
			try {
				downVoiceThread = new DownVoiceThread(voiceDatagramSocket);
			} catch (Exception e) {
			}

		} catch (Exception e) {
			System.err.println(e);
		}

		System.out.println("open");

		mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				try {
					upVideoThread.disconnect();
				} catch (Exception e) {
				}
				try {
					downVideoThread.disconnect();
				} catch (Exception e) {
				}
				try {
					upVoiceThread.disconnect();
				} catch (Exception e) {
				}
				try {
					downVideoThread.disconnect();
				} catch (Exception e) {
				}
				try {
					chatThread.disConnect(username);
				} catch (Exception e) {
				}
			}
		});

	}

	class enterServerButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			username = usernameChooser.getText();
			serverAddress = serverAddressTextField.getText();
			if (username.length() < 1 || serverAddress.length() < 1) {
				System.out.println("Invalid");
			} else {
				preFrame.setVisible(false);
				display();
			}
		}

	}

	public ClientUI() throws Exception {

	}

	public static void setBlack(int number) {
		if (number > 3) {
			user1.setIcon(null);
			user1.setForeground(Color.lightGray);
			user1.setText("...");
			user1.setFont(new Font("Serif", Font.BOLD, 30));
			user1.setHorizontalAlignment(JLabel.CENTER);
			user1.setVerticalAlignment(JLabel.CENTER);
		}

		if (number > 2) {
			user2.setIcon(null);
			user2.setForeground(Color.lightGray);
			user2.setText("...");
			user2.setFont(new Font("Serif", Font.BOLD, 30));
			user2.setHorizontalAlignment(JLabel.CENTER);
			user2.setVerticalAlignment(JLabel.CENTER);
		}

		if (number > 1) {
			user3.setIcon(null);
			user3.setForeground(Color.lightGray);
			user3.setText("...");
			user3.setFont(new Font("Serif", Font.BOLD, 30));
			user3.setHorizontalAlignment(JLabel.CENTER);
			user3.setVerticalAlignment(JLabel.CENTER);
		}

	}

	public void send(String message) {
		try {
			chatThread.send(username, message);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public static void addSelfVideo(BufferedImage bufferedImage) {
		ImageIcon imc = new ImageIcon(bufferedImage);
		selfVideo.setIcon(imc);
	}

	public static void receive(String username, String message) {
		chatBox.append("" + username + ": " + message + "\n");
		chatBox.setCaretPosition(chatBox.getDocument().getLength());
		chatJPanel.revalidate();
		chatJPanel.repaint();
	}

	public static void receiveVideo(byte[] rcvbyte, int index) {
		ImageIcon imc;
		ByteArrayInputStream bais = new ByteArrayInputStream(rcvbyte);

		BufferedImage bf;
		try {
			bf = ImageIO.read(bais);
		} catch (IOException e) {
			bf = null;
			e.printStackTrace();
		}

		if (bf != null) {
			// jf.setVisible(true);
			imc = new ImageIcon(bf);
			switch (index) {
			case 0:
				user1.setIcon(imc);
				break;
			case 1:
				user2.setIcon(imc);
				break;
			case 2:
				user3.setIcon(imc);
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + index);
			}
		}
		mainFrame.revalidate();
		mainFrame.repaint();

	}

}
