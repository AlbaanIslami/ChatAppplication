package client.view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import client.controller.*;
import sharedClasses.*;

/**
 * This class configures and displays the UI for the message sending window.
 */
public class MessageUI extends JPanel implements ActionListener {
	private JFileChooser fileChooser = new JFileChooser("Choose Picture");
	private JButton btnAddImage = new JButton("Add Picture");
	private JTextArea txtMessage = new JTextArea();
	private JButton sendMessage = new JButton("Send Message");
	private JButton exit = new JButton("Cancel");
	private ImageIcon image;
	private ClientController controller;
	private ArrayList<User> receiverList;
	private Font font = new Font("MONOSPACE", Font.PLAIN, 16);
	private JPanel pnlSouth = new JPanel();
	private JPanel pnlNorth = new JPanel(new GridLayout(2, 1));
	private JLabel receiversLabel = new JLabel("Receiver: ");
	private JFrame frame;

	/**
	 * Constructor.
	 * @param controller
	 * @param receiverList
	 */
	public MessageUI(ClientController controller, ArrayList<User> receiverList) {
		this.receiverList = receiverList;
		setPreferredSize(new Dimension(500, 500));
		setLayout(new BorderLayout());
		txtMessage.setLineWrap(true);
		txtMessage.setWrapStyleWord(true);
		fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
		fileChooser.setAcceptAllFileFilterUsed(false);
		txtMessage.setFont(font);
		txtMessage.setBorder(new LineBorder(Color.BLACK, 5));
		pnlNorth.setBorder(new EmptyBorder(5, 5, 5, 5));
		pnlNorth.add(receiversLabel);
		pnlNorth.add(btnAddImage);
		add(pnlNorth, BorderLayout.NORTH);
		add(txtMessage, BorderLayout.CENTER);
		pnlSouth.add(sendMessage);
		pnlSouth.add(exit);
		add(pnlSouth, BorderLayout.SOUTH);
		btnAddImage.addActionListener((ActionListener) this);
		sendMessage.addActionListener((ActionListener) this);
		exit.addActionListener((ActionListener) this);
		setReceiversLabel(receiverList);
		this.controller = controller;
	}

	/**
	 * This function is the buttonListener for this window.
	 * @param e
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnAddImage) {
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();
				System.out.println(filename);
				image = new ImageIcon(
						new ImageIcon(filename).getImage().getScaledInstance(300, 300, Image.SCALE_DEFAULT));
				btnAddImage.setText("Pic added: " + filename);
			}
		}

		if (e.getSource() == sendMessage) {
			if (image == null && txtMessage != null) {
				controller.outGoingMessage(receiverList, txtMessage.getText());
			} else if (image != null && txtMessage == null) {
				controller.outGoingMessage(receiverList, image);
			} else if (image != null && txtMessage != null) {
				controller.outGoingMessage(receiverList, txtMessage.getText(), image);
			} else {
				JOptionPane.showMessageDialog(null, "Text and/or pic has to be attached!");
			}
		}

		if (e.getSource() == exit) {
			frame.dispose();
		}

	}

	/*
	 * Uppdaterar UI med namn på valda mottagare
	 */

	/**
	 * This function updates the small UI window in the messageWindow with the chosen receivers for the message
	 * to be sent.
	 * @param receiverList
	 */
	public void setReceiversLabel(ArrayList<User> receiverList) {
		String temp = "Receivers: ";
		for (int i = 0; i < receiverList.size(); i++) {
			if (i < receiverList.size() - 1) {
				temp += receiverList.get(i).getUserName() + ", ";
			} else {
				temp += receiverList.get(i).getUserName();
			}
		}
		receiversLabel.setText(temp);
	}

	/**
	 * This function sets the JFrame for this window.
	 * @param frame
	 */
	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}
