package client.view;

import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import client.controller.*;

/**
 * This class configures and displays the UI for the login window. The user can insert their desired username
 * and choose a profile picture.
 */
public class LoginUI extends JPanel {
	private JTextField userNameTextField = new JTextField("");
	private JLabel inputUsernameLabel = new JLabel("Enter Username:");
	private JFileChooser fileChooser = new JFileChooser();
	private JButton loginButton = new JButton("Log in");
	private JButton pictureButton = new JButton("Choose Pic");
	private JLabel userPictureLabel = new JLabel(
			"<html><body><div style='text-align: center;'>Your User Pic</div></body></html>",
			(int) CENTER_ALIGNMENT);
	private JLabel bgImageLabel = new JLabel();
	private JLabel bgLoginLabel = new JLabel();
	private ImageIcon bgImage;
	private ImageIcon bgLogin =  new ImageIcon("images/loginBg2.jpg");
	private ImageIcon userImage = new ImageIcon("images/hasbullaBig.jpg");
	private ImageIcon checkBoxImage = new ImageIcon("images/hasbulla.png");
	private Font inputFont = new Font("SansSerif", Font.BOLD, 14);
	private Font smallFont = new Font("SansSerif", Font.PLAIN, 14);
	private ClientController clientController;
	private JFrame frame;

	/**
	 * Constructor
	 * @param controller
	 */
	public LoginUI(ClientController controller) {
		this.clientController = controller;
		setVisible(true);
		setPreferredSize(new Dimension(400, 500));
		setOpaque(true);
		setLayout(null);

		try {
			userNameTextField.getDocument().addDocumentListener(new DocumentListener() {

				@Override
				public void removeUpdate(DocumentEvent e) {
					enableLogin();
				}

				@Override
				public void insertUpdate(DocumentEvent e) {
					enableLogin();
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					enableLogin();
				}
			});

		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		userNameTextField.setBounds(100, 190, 200, 40);
		userNameTextField.setForeground(Color.GRAY);

		inputUsernameLabel.setBounds(100, 155, 200, 40);
		inputUsernameLabel.setFont(inputFont);
		inputUsernameLabel.setForeground(Color.WHITE);

		userPictureLabel.setBounds(150, 245, 100, 100);
		userPictureLabel.setFont(smallFont);
		userPictureLabel.setIcon(userImage);

		bgLoginLabel.setBounds(10,10,380,150);
		bgLoginLabel.setFont(smallFont);
		bgLoginLabel.setIcon(bgLogin);

		add(bgLoginLabel);
		add(userNameTextField);
		add(inputUsernameLabel);
		add(pictureButton);
		add(userPictureLabel);
		add(loginButton);
		loadBG();

		PictureLoader pictureLoader = new PictureLoader();
		pictureButton.addActionListener(pictureLoader);
		pictureButton.setBounds(125, 360, 150, 40);

		Login loginListener = new Login();
		loginButton.addActionListener(loginListener);
		loginButton.setBounds(100, 420, 200, 60);
		loginButton.setBackground(new Color(84, 173, 117));
		loginButton.setForeground(Color.BLACK);
		loginButton.setEnabled(false);

		JFrame frame = new JFrame("Log in!");
		frame.add(this);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setLocation(400, 120);
		frame.setResizable(false);
		this.frame = frame;
	}

	/**
	 * This function loads the background picture for the login window.
	 */
	public void loadBG() {
		bgImage = new ImageIcon(
				new ImageIcon("images/Chatt.png").getImage().getScaledInstance(400, 500, Image.SCALE_DEFAULT));
		bgImageLabel.setIcon(bgImage);
		bgImageLabel.setBounds(0, 0, 400, 500);
		add(bgImageLabel);
	}

	/**
	 * This functions enables or disables the login functionality based on if the user has input a username or not.
	 * Or if the desired username is too long. It also disables the login if there is no profile picture set.
	 * By default the profile picture is set to cute hasbulla king.
	 */
	public void enableLogin() {
		if (userNameTextField.getText().length() != 0 && userNameTextField.getText().length() <= 30) {
			if (userImage.getImage() == null) {
				loginButton.setEnabled(false);
			}
			loginButton.setEnabled(true);
		} else {
			loginButton.setEnabled(false);
		}
	}

	/**
	 * This inner class is used to load the users profile picture using a fileChooser. It also resizes the picture
	 * to fit the profile pic window and then calls the login enabler function.
	 */
	private class PictureLoader implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			fileChooser.addChoosableFileFilter(
					new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
			fileChooser.setAcceptAllFileFilterUsed(false);
			if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				String filename = fileChooser.getSelectedFile().getPath();
				System.out.println(filename);
				userImage = new ImageIcon(
						new ImageIcon(filename).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
				checkBoxImage = new ImageIcon(
						new ImageIcon(filename).getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
				userPictureLabel.setIcon(userImage);
				userPictureLabel.setText("");
				enableLogin();
			}
		}
	}

	/**
	 * This function is the buttonListener that sends the users entered info (username and profile pic) through
	 * the method below.
	 */
	private class Login implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sendUserInfo();
			frame.dispose();
		}
	}

	/**
	 * This function calls upon the clientController to create a new user with the info entered into the login screen.
	 */
	public void sendUserInfo() {
		clientController.newUser(userNameTextField.getText(), checkBoxImage);
	}

}
