package client.model;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import client.controller.ClientController;
import sharedClasses.*;

/**
 * This class handles all the communication with the server. Everything from connecting the client, disconnecting
 * adding new messages to the output and receiving messages.
 */

public class Client {
	private Socket socket;
	private ClientController controller;
	private String host;
	private int port;
	private boolean connected;
	private ClientOutput clientOutput;
	private MessageListener messageListener;

	/**
	 * Constructor.
	 * @param host
	 * @param port
	 * @param controller
	 */
	public Client(String host, int port, ClientController controller) {
		this.host = host;
		this.port = port;
		this.controller = controller;
		controller.addClient(this);
	}

	/**
	 * This function handles the connection of the client towards the server. It creates a new socket for each
	 * each client instance and a new messageListener which handles the receiving of messages for this client.
	 */
	public void connect() {
		try {
			if (!connected) {
				socket = new Socket(host, port);
				clientOutput = new ClientOutput();
				messageListener = new MessageListener();
				messageListener.start();
				connected = true;
			}
		} catch (IOException e) {
		}
	}

	/**
	 * This function disconnects the client from the server, closes the socket and the messageListener.
	 */
	public void disconnect() {
		if (connected) {
			try {
				socket.close();
				messageListener.interrupt();
				clientOutput.interrupt();
			} catch (IOException e) {
				System.err.println(e);
			}
			connected = false;
		}
	}

	/**
	 * This function calls the inner class clientOutput so it can store the new message in the messageBuffer.
	 * @param message
	 */
	public void addNewMessage(Message message) {
		clientOutput.newMessage(message);
	}

	/**
	 * This inner class handles the output to the server, like the client data and messages to be sent.
	 * It creates a messageBuffer for each user so that the messages for that user can be stored (to be sent).
	 */
	private class ClientOutput extends Thread {
		private MessageBuffer<Message> messageBuffer;

		public ClientOutput() {
			messageBuffer = new MessageBuffer<>();
			start();
		}

		public void run() {
			try (ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
				oos.writeObject(controller.getUser());
				while (true) {
					oos.writeObject(messageBuffer.get());
				}
			} catch (IOException | InterruptedException e) {
				System.err.println(e);
			}
		}

		public void newMessage(Message message) {
			messageBuffer.put(message);
		}
	}

	/**
	 * This inner class handles the input from the server, like the client online list and messages to be received.
	 * it calls the controller to update the online list if a new one is received.
	 */
	private class MessageListener extends Thread {
		public void run() {
			try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream())) {
				while (!Thread.interrupted()) {
					try {
						Object obj = ois.readObject();
						if (obj instanceof Message) {
							controller.incomingMessage((Message) obj);
						} else {
							controller.updateOnlineUsersList((ArrayList<User>) obj);
						}

					} catch (ClassNotFoundException | IOException e) {
						ois.close();
					}
				}
			} catch (IOException e2) {
			}
		}
	}
}
