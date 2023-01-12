package sharedClasses;

import javax.swing.*;
import java.io.Serializable;
import java.util.*;

/**
 * This class represents a message which can consist of a String, a String + image or just an image.
 * It also contains the corresponding user. Each scenario has its own constructor whether its just a String, String + image
 * or just an image.
 */
public class Message implements Serializable {
	private User sender;
	private ArrayList<User> recieverList;
	private String text;
	private ImageIcon image;
	private Date timeToServer;
	private Date timeToReceiver;

	public Message(User sender, ArrayList<User> recieverList, String text, ImageIcon image) {
		this.sender = sender;
		this.recieverList = recieverList;
		this.text = text;
		this.image = image;
	}

	public Message(User sender, ArrayList<User> recieverList, String text) {
		this.sender = sender;
		this.recieverList = recieverList;
		this.text = text;
	}

	public Message(User sender, ArrayList<User> recieverList, ImageIcon image) {
		this.sender = sender;
		this.recieverList = recieverList;
		this.image = image;
	}

	public Date getTimeToServer() {
		return timeToServer;
	}

	public void setTimeToServer(Date timeToServer) {
		this.timeToServer = timeToServer;
	}

	public User getSender() {
		return sender;
	}

	public ArrayList<User> getReceiverList() {
		return recieverList;
	}

	public String getText() {
		return text;
	}

	public ImageIcon getImage() {
		return image;
	}

	public Date getTimeToReceiver() {
		return timeToReceiver;
	}

	public void setTimeToReceiver(Date timeToReceiver) {
		this.timeToReceiver = timeToReceiver;
	}
}
