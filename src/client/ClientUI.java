package client;

import java.awt.BorderLayout;
import java.awt.Button;
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
import java.net.Socket;
import java.net.UnknownHostException;

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


public class ClientUI {
	String appName = "Colt Chat v0.1";
	public static JFrame mainFrame = new JFrame();
    public static JPanel mainJPanel = new JPanel(new GridLayout(2,1));
    public static JPanel chatJPanel = new JPanel();
    public static JPanel videoJPanel = new JPanel(new GridLayout(2,2));
    public static JLabel selfVideo = new JLabel();
    public static JLabel user1 = new JLabel();
    public static JLabel user2 = new JLabel();
    public static JLabel user3 = new JLabel();
    
	ImageIcon img;
    public static JTextArea ta,tb;
    JButton sender;
    ChatThread chatThread;
    
    JTextField  messageBox;
    JTextArea   chatBox;
    JTextField  usernameChooser;
    JFrame      preFrame;
    String  username;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager
                            .getSystemLookAndFeelClassName());
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
        JLabel chooseUsernameLabel = new JLabel("Pick a username:");
        JButton enterServer = new JButton("Enter Chat Server");
        enterServer.addActionListener(new enterServerButtonListener());
        JPanel prePanel = new JPanel(new GridBagLayout());

        GridBagConstraints preRight = new GridBagConstraints();
        preRight.insets = new Insets(0, 0, 0, 10);
        preRight.anchor = GridBagConstraints.EAST;
        GridBagConstraints preLeft = new GridBagConstraints();
        preLeft.anchor = GridBagConstraints.WEST;
        preLeft.insets = new Insets(0, 10, 0, 10);
        // preRight.weightx = 2.0;
        preRight.fill = GridBagConstraints.HORIZONTAL;
        preRight.gridwidth = GridBagConstraints.REMAINDER;

        prePanel.add(chooseUsernameLabel, preLeft);
        prePanel.add(usernameChooser, preRight);
        preFrame.add(prePanel, BorderLayout.CENTER);
        preFrame.add(enterServer, BorderLayout.SOUTH);
        preFrame.setSize(300, 300);
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
        
        sender.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String sentence = tb.getText();
                ta.append("From myself: "+sentence+"\n");
                ta.setCaretPosition(ta.getDocument().getLength());
                chatJPanel.revalidate();
                chatJPanel.repaint();
//                jp.revalidate();
//                jp.repaint();
                send(sentence);
                tb.setText(null);
            }
        });
        
        
    	chatJPanel.setLayout(new BorderLayout());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLUE);
        southPanel.setLayout(new GridBagLayout());

        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();

        sender = new JButton("Send Message");
        sender.addActionListener(new sendMessageButtonListener());

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

		try {
			Socket clientSocket = new Socket("localhost", 9789);
			chatThread = new ChatThread(clientSocket);
			UpVideoThread upVideoThread = new UpVideoThread();
			upVideoThread.start();
			DownVideoThread downVideoThread = new DownVideoThread();
			downVideoThread.start();
			System.out.println("open");
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
    }

    class sendMessageButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            if (messageBox.getText().length() < 1) {
                // do nothing
            } else if (messageBox.getText().equals(".clear")) {
                chatBox.setText("Cleared all messages\n");
                messageBox.setText("");
            } else {
                chatBox.append("<" + username + ">:  " + messageBox.getText()
                        + "\n");
                send(messageBox.getText());
                messageBox.setText("");
            }
            messageBox.requestFocusInWindow();
        }
    }



    class enterServerButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            username = usernameChooser.getText();
            if (username.length() < 1) {
                System.out.println("No!");
            } else {
                preFrame.setVisible(false);
                display();
            }
        }

    }
    
    public ClientUI() throws Exception {
    	

	}
    
    public void send(String message) {
    	try {
    		chatThread.send(message);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	
	}
    
    public static void addSelfVideo(BufferedImage bufferedImage) {
    	ImageIcon imc = new ImageIcon(bufferedImage);
    	selfVideo.setIcon(imc);
	}
    
    public static void receive(String message) {
    	ta.append(message+"\n");
        ta.setCaretPosition(ta.getDocument().getLength());
        chatJPanel.revalidate();
        chatJPanel.repaint();
	}
    
    public static void receiveVideo(byte[] rcvbyte, int userId) {
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
            //jf.setVisible(true);
            imc = new ImageIcon(bf);
            switch (userId) {
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
				throw new IllegalArgumentException("Unexpected value: " + userId);
			}
            if(userId == 0) user1.setIcon(imc);
            if(userId == 0) user1.setIcon(imc);
            if(userId == 0) user1.setIcon(imc);
        }
        mainFrame.revalidate();
        mainFrame.repaint();

	}
    
}




