package client.controller;

import javax.swing.*;
import java.io.*;
import java.util.*;
import client.model.*;
import client.view.*;
import sharedClasses.*;

/**
 * This class handles all the logic between the client classes and UI classes.
 */

public class ClientController {
	private User user;
	private Client client;
	private ListOfUsersUI listOfUsersUI;
	private ArrayList<User> onlineUsersList;
	private ArrayList<User> contacts = new ArrayList<>();
	private MainUI mainUI;

	public ClientController()
	{
		mainUI = new MainUI(this);
	}


	public void addListOfUsersUI(ListOfUsersUI ui) { // KANSKE TA BORT?
		this.listOfUsersUI = ui;
	}

	/**
	 * This function adds the client instance to the clientController, it acts as a get method.
	 * @param client
	 */
	public void addClient(Client client) {
		this.client = client;
	}

	/**
	 * This function receives lists of online users and contacts through arraylists, it then sorts them
	 * and removes any doubles. After sorting it creates a MessageUI instance and calls the function
	 * responsible for writing the message. This function is called when pressing the send message button.
	 * @param onlineIndexes
	 * @param contactIndexes
	 */
	public void writeMessagePanel(ArrayList<Integer> onlineIndexes, ArrayList<Integer> contactIndexes) {
		ArrayList<User> receiverList = new ArrayList<User>();
		for (int i = 0; i < onlineIndexes.size(); i++) {
			int index = onlineIndexes.get(i);
			receiverList.add(onlineUsersList.get(index));
		}
		for (int i = 0; i < contactIndexes.size(); i++) {
			if (!receiverList.contains(contacts.get(contactIndexes.get(i)))) {
				receiverList.add(contacts.get(contactIndexes.get(i)));
			}
		}
		MessageUI ui = new MessageUI(this, receiverList);
		mainUI.writeMessage(ui);
	}

	/**
	 * These 3 functions compile a message to be sent when the user presses the send message button when in
	 * the messagewindow. One of the 3 variations is used when the user wants to send a message with
	 * only text, text and an image or just an image.
	 * @param recieverList
	 * @param text
	 * @param image
	 */
	public void outGoingMessage(ArrayList<User> recieverList, String text, ImageIcon image) {
		client.addNewMessage(new Message(user, recieverList, text, image));
	}

	public void outGoingMessage(ArrayList<User> recieverList, String text) {
		client.addNewMessage(new Message(user, recieverList, text));
	}

	public void outGoingMessage(ArrayList<User> recieverList, ImageIcon image) {
		client.addNewMessage(new Message(user, recieverList, image));
	}


	/**
	 * This function creates a User-object with the parameters (UserName, ProfilePic), initiates the clients
	 * connection to the server and creates a new instance of the ListOfUsersUI which is the programs main chat
	 * window. If the username matches one that is already in the system, it also calls the function
	 * that reads the users contact data and loads it.
	 * @param userName
	 * @param image
	 */
	public void newUser(String userName, ImageIcon image) {
		user = new User(userName.toLowerCase(), image);
		client.connect();
		listOfUsersUI = new ListOfUsersUI(this);
		mainUI.showTerminalInFrame(listOfUsersUI);
		readContacts();
	}

	/**
	 * This function is a get method for the User instance.
	 * @return
	 */
	public User getUser() {
		return user;
	}

	/**
	 * This function creates a new IncomingMessageUI instance when a message is received that contains
	 * either text, an image and text or just an image. It then calls showMessageInFrame containing the
	 * message information.
	 * @param message
	 */
	public void incomingMessage(Message message) {
		IncomingMessageUI imUI;
		message.setTimeToReceiver(new Date());
		if (message.getImage() == null) {
			imUI = new IncomingMessageUI(message.getSender().getUserName(), message.getSender().getImage(),
					message.getText());
		} else if (message.getText() == null) {
			imUI = new IncomingMessageUI(message.getSender().getUserName(), message.getSender().getImage(),
					message.getImage());
		} else {
			imUI = new IncomingMessageUI(message.getSender().getUserName(), message.getSender().getImage(),
					message.getText(), message.getImage());
		}
		mainUI.showMessageInFrame(imUI);
	}

	/**
	 * This function updates the online list in the main window with the latest version of the onlinelist each time
	 * a user connects or disconnects.
	 * @param onlineUsersList
	 */
	public void updateOnlineUsersList(ArrayList<User> onlineUsersList) {
		this.onlineUsersList = onlineUsersList;
		listOfUsersUI.addOnlineUserLabels(onlineUsersList);
	}

	/**
	 * This function reads the contacts list from the hard drive for a user using an
	 * objectInputStream. It is called by a function in this class.
	 */
	public void readContacts() {
		int size;
		ArrayList<User> temp = new ArrayList<User>();
		File contactFile = new File("files/contacts" + user.getUserName() + ".dat");
		if (contactFile.exists()) {
			try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(contactFile))) {
				size = ois.readInt();
				for (int i = 0; i < size; i++) {
					temp.add((User) ois.readObject());
				}
				contacts.addAll(temp);
				listOfUsersUI.addContactListLabels(contacts);
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This function writes the contacts list to the hard drive for a user using an
	 * objectOutputStream. It is called by a function in this class.
	 */
	public void saveContacts() {
		try (ObjectOutputStream oos = new ObjectOutputStream(
				new FileOutputStream("files/contacts" + user.getUserName() + ".dat"))) {
			oos.writeInt(contacts.size());
			for (int i = 0; i < contacts.size(); i++) {
				oos.writeObject(contacts.get(i));
			}
			System.out.println("Contacts Saved");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This function adds a user-object to the contacts list when the user presses the add contact button
	 * and someone is checked in the online list. It then calls the function responsible for updating
	 * the list in the UI.
	 * @param user
	 */
	public void addContact(User user) {
		contacts.add(user);
		listOfUsersUI.addContactListLabels(contacts);
	}

	/**
	 * This function removes a user-object to the contacts list when the user presses the remove contact button
	 * and someone is checked in the contacts list. It then calls the function responsible for updating
	 * the list in the UI.
	 * @param index
	 */
	public void removeContact(int index) {
		contacts.remove(index);
		listOfUsersUI.addContactListLabels(contacts);
	}

	/**
	 * This function is responsible for forwarding the disconnect enquiry to the client. It then also calls
	 * the function responsible for saving the contacts list for that user.
	 */
	public void disconnect() {
		client.disconnect();
		saveContacts();
	}
}
