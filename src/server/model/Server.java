package server.model;

import java.io.*;
import java.net.*;
import java.util.*;
import server.controller.*;
import java.text.SimpleDateFormat;
import sharedClasses.*;

/**
 * This class takes care of all the communication between connected clients.
 */
public class Server {
    private ServerController controller;

    /**
     * This is the constructor.
     * @param port
     * @param controller
     */
    public Server(int port, ServerController controller) {
        this.controller = controller;
        controller.addServer(this);
        new ConnectionListener(port);
    }

    /**
     * This inner class listens after new incoming connections. If a new client wants to connect, a new
     * ClientHandler-object is created.
     */
    private class ConnectionListener extends Thread {
        private int port;
        private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        private Date date;

        public ConnectionListener(int port) {
            this.port = port;
            start();
        }

        public void run() {
            Socket socket = null;
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (true) {
                    try {
                        socket = serverSocket.accept();
                        new ClientHandler(socket);
                        date = new Date();
                        controller.logToFile(simpleDate.format(date) + ": " + socket.getInetAddress() + " has connected!");
                    } catch (IOException e) {
                        if (socket != null) {
                            socket.close();
                        }
                        System.err.println(e);
                    }
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * This inner class listens after input from the respective connected client.
     */
    private class ClientHandler extends Thread {
        private Socket socket;
        private User user;
        private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        private Date date;

        /**
         * Constructor.
         * @param socket
         * @throws IOException
         */
        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            start();
        }

        public void run() {
            System.out.println("ClientHandler Starting " + Thread.currentThread().getName() + socket.getInetAddress());
            try (ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                 ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream())) {
                oos.flush();
                user = (User) ois.readObject();
                date = new Date();
                controller.logToFile(simpleDate.format(date) + ": " + socket.getInetAddress() + ": User Transferred");
                controller.newClient(user, oos, socket);
                while (true) {
                    controller.newMessage((Message) ois.readObject());
                    date = new Date();
                    controller.logToFile(simpleDate.format(date) + ": New Message from: " + socket.getInetAddress());
                }
            } catch (ClassNotFoundException | IOException e) {
            }
            try {
                System.out.println("ClientHandler Shutting Down " + Thread.currentThread().getName());
                date = new Date();
                controller.logToFile(simpleDate.format(date) + ": " + socket.getInetAddress() + ": has disconnected!");
                socket.close();
                controller.removeClient(user);
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
    }

    /**
     * This function creates a new messageSender object.
     * @param message
     * @param client
     */
    public void newMessageSender(Message message, ClientAddress client) {
        new MessageSender(message, client.getOos(), client.getSocket()).start();
    }

    /**
     * This function creates a new OnlineUserSender object.
     * @param userList
     * @param client
     */
    public void newOnlineUserSender(ArrayList<User> userList, ClientAddress client) {
        new OnlineUserSender(userList, client.getOos(), client.getSocket()).start();
    }

    /**
     * This inner class handles the message sending process.
     */
    private class MessageSender extends Thread {
        private Message message;
        private ObjectOutputStream oos;
        private Socket socket;
        private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        private Date date;

        /**
         * Constructor.
         * @param message
         * @param oos
         * @param socket
         */
        public MessageSender(Message message, ObjectOutputStream oos, Socket socket) {
            this.message = message;
            this.oos = oos;
            this.socket = socket;
        }

        public void run() {
            try {
                oos.writeObject(message);
                date = new Date();
                controller.logToFile(simpleDate.format(date) + ": Message sent to: " + socket.getInetAddress());
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    /**
     * This inner class sends a new list of connected users to a client.
     */
    private class OnlineUserSender extends Thread {
        private ArrayList<User> userList;
        private ObjectOutputStream oos;
        private SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        private Date date;
        private Socket socket;

        /**
         * Constructor
         * @param userList
         * @param oos
         * @param socket
         */
        public OnlineUserSender(ArrayList<User> userList, ObjectOutputStream oos, Socket socket) {
            this.userList = userList;
            this.oos = oos;
            this.socket = socket;
        }

        public void run() {
            try {
                oos.writeObject(userList);
                date = new Date();
                controller
                        .logToFile(simpleDate.format(date) + ": OnlineList sent to: " + socket.getInetAddress());
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }
}