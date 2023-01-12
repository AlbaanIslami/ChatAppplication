package client.view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class configures and displays the UI for incoming messages.
 */
public class IncomingMessageUI extends JPanel implements ActionListener {
	private JLabel sender = new JLabel(" ", JLabel.CENTER);
	private JTextArea txtMessage = new JTextArea();
	private JLabel imageLbl = new JLabel(" ", JLabel.CENTER);
	private JButton exitBtn = new JButton("Cancel");
	private JLabel senderPic = new JLabel(" ", JLabel.CENTER);
	private JPanel pnlNorth = new JPanel(new GridLayout(1, 2));
	private JPanel pnlCenter = new JPanel(new GridLayout(1, 2));
	private JPanel pnlSouth = new JPanel(new GridLayout(1, 2));
	private Font font = new Font("MONOSPACE", Font.PLAIN, 16);
	private JFrame frame;

	/**
	 * Constructor for messages with both text and a picture.
	 * @param username
	 * @param profilePic
	 * @param message
	 * @param imageIcon
	 */
	public IncomingMessageUI(String username, ImageIcon profilePic, String message, ImageIcon imageIcon) {
		setLayout(new BorderLayout());
		sender.setText("New Message From: " + username);
		sender.setFont(font);
		senderPic.setIcon(profilePic);
		
		pnlNorth.setBorder(new LineBorder(Color.BLACK, 5));
		pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		txtMessage.setText(message);
		txtMessage.setFont(font);
		txtMessage.setLineWrap(true);
		txtMessage.setEditable(false);
		txtMessage.setBackground(null);
		
		Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		imageLbl.setIcon(imageIcon);
		
		pnlNorth.add(sender);
		pnlNorth.add(senderPic);
		pnlCenter.add(txtMessage);
		pnlCenter.add(imageLbl);
		pnlSouth.add(exitBtn);
		
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlCenter);
		add(pnlSouth, BorderLayout.SOUTH);
		
		exitBtn.addActionListener(this);
	}

	/**
	 * Constructor for messages with just text.
	 * @param username
	 * @param profilePic
	 * @param message
	 */
	public IncomingMessageUI(String username, ImageIcon profilePic, String message) {
		setLayout(new BorderLayout());
		sender.setText("New Message From: " + username);
		sender.setFont(font);
		senderPic.setIcon(profilePic);
		
		txtMessage.setText(message);
		txtMessage.setFont(font);
		txtMessage.setLineWrap(true);
		txtMessage.setEditable(false);
		txtMessage.setBackground(null);
		
		pnlNorth.setBorder(new LineBorder(Color.BLACK, 5));
		pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		pnlNorth.add(sender);
		pnlNorth.add(senderPic);		
		pnlCenter.add(txtMessage);
		pnlSouth.add(exitBtn);
		
		add(pnlCenter);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
		
		exitBtn.addActionListener(this);
	}

	/**
	 * Constructor for messages with just an image.
	 * @param username
	 * @param profilePic
	 * @param imageIcon
	 */
	public IncomingMessageUI(String username, ImageIcon profilePic, ImageIcon imageIcon) {
		setLayout(new BorderLayout());
		sender.setText("New Message From: " + username);
		sender.setFont(font);
		senderPic.setIcon(profilePic);
		
		pnlNorth.setBorder(new LineBorder(Color.BLACK, 5));
		pnlCenter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(image);
		imageLbl.setIcon(imageIcon);
		
		pnlNorth.add(sender);
		pnlNorth.add(senderPic);
		pnlSouth.add(exitBtn);
		pnlCenter.add(imageLbl);
		
		add(pnlCenter);
		add(pnlNorth, BorderLayout.NORTH);
		add(pnlSouth, BorderLayout.SOUTH);
		
		exitBtn.addActionListener(this);
	}

	/**
	 * This function closes the window if the exit button is clicked.
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
			frame.dispose();
	}

	/**
	 * This function sets the JFrame for this window.
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
