package server.controller;

import server.model.*;
import server.view.*;
import java.io.*;
import java.util.*;
import java.net.Socket;
import java.text.*;
import sharedClasses.*;

/**
 * This class handles all the logic between the serverclass serverUI and some other helping classes.
 */

public class ServerController {
    private Clients clients = new Clients();
    private UnsentMessages unsentMessages = new UnsentMessages();
    private Server server;
    private ServerUI serverUi;

    /**
     * This function acts as a get-method which is called by the Server class.
     * @param server
     */
    public void addServer(Server server) {
        this.server = server;
    }

    /**
     * This function acts as a get-method which is called by the ServerUI class.
     * @param serverUi
     */
    public void addServerUi(ServerUI serverUi) {
        this.serverUi = serverUi;
    }

    /**
     * This function is when a new client connects to the server. It enters the user into the hashmap and updates the user
     * list. It also checks if there are any unsent messages for this user by calling the unsentMessages function.
     * @param user
     * @param oos
     * @param socket
     */
    public void newClient(User user, ObjectOutputStream oos, Socket socket) {
        clients.put(user, new ClientAddress(oos, socket));
        updateUserList();
        checkUnsentMessages(user);
    }

    /**
     * This function updates the userlist/online userlist and is called by functions in this class.
     */
    public void updateUserList() {
        ArrayList<User> temp = getConnectedUsers();
        for (int i = 0; i < temp.size(); i++) {
            ClientAddress tempAddress = clients.getClient(temp.get(i));
            server.newOnlineUserSender(temp, tempAddress);
        }
    }

    /**
     * This function acts as a get-method which is called by a function in this class,
     * it retrieves the users from the hash-map.
     * @return
     */
    public ArrayList<User> getConnectedUsers() {
        return clients.getKeySet();
    }

    /**
     * This function removes a user from the hashmap and updates the other lists when the user disconnects.
     * @param user
     */
    public void removeClient(User user) {
        clients.remove(user);
        updateUserList();
    }

    /**
     * This function checks if there are any unsent messages for the user, sends them and then removes the unsent
     * message from the list.
     * @param user
     */
    public void checkUnsentMessages(User user) {
        if (unsentMessages.contains(user)) {
            ArrayList<Message> temp = unsentMessages.getUnsentMessageList(user);
            for (int i = 0; i < temp.size(); i++) {
                sendMessage(temp.get(i), user);
            }
            unsentMessages.remove(user);
        }
    }

    /**
     * This function checks which of the sent messages receivers are online and then stores the offline users
     * and unsent message in the unsent messages list.
     * @param message
     */
    public void newMessage(Message message) {
        message.setTimeToServer(new Date());
        ArrayList<User> receivers = message.getReceiverList();
        for (int i = 0; i < receivers.size(); i++) {
            if (clients.contains(receivers.get(i))) {
                sendMessage(message, receivers.get(i));
            } else {
                unsentMessages.put(receivers.get(i), message);
            }
        }
    }

    /**
     * This function calls upon the server to create a new message with the information to be sent and
     * which user to send it to.
     * @param message
     * @param user
     */
    public void sendMessage(Message message, User user) {
        server.newMessageSender(message, clients.getClient(user));
    }

    /**
     * This function reads and retrieves the logged data from the hard drive.
     * It is returned to the calling function as an arraylist.
     * @return
     */
    private ArrayList<String> readTrafficFromFile() {
        ArrayList<String> res = new ArrayList<String>();
        String str = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("files/traffic.log")))) {
            str = br.readLine();
            while (str != null) {
                res.add(str);
                str = br.readLine();
            }
        } catch (IOException e) {
        }
        return res;
    }

    /**
     * This function takes the arraylist from the function above and sorts it so that only the logged data within
     * the specified time frame is saved. It then saves it in a new arraylist.
     * @param fromDate
     * @param toDate
     * @throws ParseException
     */
    public void getTraffic(String fromDate, String toDate) throws ParseException {
        ArrayList<String> temp = readTrafficFromFile();
        ArrayList<String> res = new ArrayList<String>();
        Date start = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(fromDate);
        Date end = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(toDate);
        Date tempDate;

        for (int i = 0; i < temp.size(); i++) {
            tempDate = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(temp.get(i).substring(0, 17));
            if ((!tempDate.before(start)) && (!tempDate.after(end))) {
                res.add(temp.get(i));
            }
        }
        setTrafficToUI(res);
    }

    /**
     * This function takes the new sorted arraylist from the function and sends it to the UI accordingly.
     * @param traffic
     */
    private void setTrafficToUI(ArrayList<String> traffic) {
        if (traffic.size() > 0) {
            serverUi.clearTextArea();
            for (int i = 0; i < traffic.size(); i++) {
                serverUi.updateTextArea(traffic.get(i));
            }
        } else {
            serverUi.noTraffic();
        }
    }

    /**
     * This function writes the logged traffic to the hard drive so it can later be retrieved.
     * @param str
     */
    public synchronized void logToFile(String str) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream("files/traffic.log", true)))) {
            bw.write(str);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}